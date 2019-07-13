package me.w1992wishes.hbase.inaction.model;

/**
 * @author w1992wishes
 */
public abstract class Relation {

    public String relation;
    public String from;
    public String to;

    @Override
    public String toString() {
        return String.format(
                "<Relation: %s %s %s>",
                from,
                relation,
                to);
    }
}