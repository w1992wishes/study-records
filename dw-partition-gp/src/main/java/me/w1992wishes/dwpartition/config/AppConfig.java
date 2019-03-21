package me.w1992wishes.dwpartition.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author w1992wishes 2019/3/21 14:58
 */
@Configuration
public class AppConfig {

    @Value("${app.odl.event-face-table}")
    private String eventFaceTable;

    @Value("${app.odl.event-face-table-refreshed}")
    private String eventFaceTableRefreshed;

    @Value("${app.dwd.event-face-table-preprocessed}")
    private String eventFaceTablePreprocessed;

    public String getEventFaceTable() {
        return eventFaceTable;
    }

    public void setEventFaceTable(String eventFaceTable) {
        this.eventFaceTable = eventFaceTable;
    }

    public String getEventFaceTableRefreshed() {
        return eventFaceTableRefreshed;
    }

    public void setEventFaceTableRefreshed(String eventFaceTableRefreshed) {
        this.eventFaceTableRefreshed = eventFaceTableRefreshed;
    }

    public String getEventFaceTablePreprocessed() {
        return eventFaceTablePreprocessed;
    }

    public void setEventFaceTablePreprocessed(String eventFaceTablePreprocessed) {
        this.eventFaceTablePreprocessed = eventFaceTablePreprocessed;
    }
}
