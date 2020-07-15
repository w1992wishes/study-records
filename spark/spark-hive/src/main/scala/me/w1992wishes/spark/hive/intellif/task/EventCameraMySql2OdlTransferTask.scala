package me.w1992wishes.spark.hive.intellif.task

import me.w1992wishes.common.util.PropertiesTool
import me.w1992wishes.spark.hive.intellif.param.EventCameraMySql2OdlTransferCLParam
import org.apache.spark.SparkConf
import org.apache.spark.sql.{SaveMode, SparkSession}

/**
  * 摄像头提取任务，摄像头是小数量数据
  *
  * @author w1992wishes 2020/3/9 14:42.
  */
object EventCameraMySql2OdlTransferTask {

  def main(args: Array[String]): Unit = {
    args.foreach(println(_))

    val clParam = new EventCameraMySql2OdlTransferCLParam(args)
    val propsTool = PropertiesTool(clParam.confName)
    val bizCode = clParam.bizCode

    // 全量覆盖
    // spark 参数设置
    val conf = new SparkConf()
      .setIfMissing("spark.master", "local")
      .set("spark.sql.adaptive.enabled", "true")
      .set("spark.sql.adaptive.shuffle.targetPostShuffleInputSize", "67108864b")
      .set("spark.sql.adaptive.join.enabled", "true")
      .set("spark.sql.autoBroadcastJoinThreshold", "20971520")
      .set("spark.sql.broadcastTimeout", "600000ms")
      .set("spark.hadoopRDD.ignoreEmptySplits", "true")
      .set("spark.sql.warehouse.dir","hdfs://nameservice1/user/hive/warehouse")
      .set("spark.sql.catalogImplementation", "hive")

    val spark = SparkSession.builder()
      .appName(getClass.getSimpleName)
      .enableHiveSupport() // 启用 hive
      .config(conf)
      .getOrCreate()

    spark.sparkContext.setLogLevel("WARN")

    val cameraDF = spark.read
      .format("jdbc")
      .option("driver", propsTool.getString("source.driver"))
      .option("url", propsTool.getString("source.url"))
      .option("dbtable", propsTool.getString("source.table"))
      .option("user", propsTool.getString("source.user"))
      .option("password", propsTool.getString("source.pass"))
      .option("fetchsize", propsTool.getInt("source.fetchsize"))
      .load()

    cameraDF.show()
    cameraDF.createOrReplaceTempView("eventCamera")

    import spark.sql
    sql(s"CREATE DATABASE IF NOT EXISTS ${bizCode}_odl")
    sql(s"USE ${bizCode}_odl")
    sql(
      s"""
         | CREATE TABLE IF NOT EXISTS odl_${bizCode}_event_camera (
         |   biz_code string,
         |   id string,
         |   name string,
         |   ip string,
         |   geo_string string,
         |   props string)
         | ROW FORMAT DELIMITED
         | NULL DEFINED AS ''
         | STORED AS TEXTFILE
         """.stripMargin)

    sql(s"SELECT '$bizCode' AS biz_code, id, name, ip, geo_string, '' AS props FROM eventCamera")
      .write
      .format("hive")
      .mode(SaveMode.Overwrite)
      .saveAsTable(s"${bizCode}_odl.odl_${bizCode}_event_camera")

    sql(s"SELECT count(id) FROM ${bizCode}_odl.odl_${bizCode}_event_camera").show()

    spark.stop()
  }

}



