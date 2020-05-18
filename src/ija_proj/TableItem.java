/*
 * Authors: 
 * Matej Cimmerman (xcimme00)
 * Daniel Paulovi� (xpaulo04)
 */
package ija_proj;
/**
 *
 * @author Matej Cimmerman (xcimme00), Daniel Paulovič (xpaulo04)
 */
public class TableItem {
    /**
     * identifikator stopky
     */
    private String stopId;
    /**
     * povodny cas 
     */
    private String plannedTime;
    /**
     * skutocny cas 
     */
    private String actualTime;


    /**
     * Konstruktor
     * @param stopId id stopky
     * @param plannedTime p
     * @param actualTime a
     */
    public TableItem(String stopId,String plannedTime, String actualTime)
    {
        this.stopId=stopId;
        this.plannedTime=plannedTime;
        this.actualTime=actualTime;
    }

    /**
     *
     * @return {@link TableItem#stopId}
     */
    public String getActualTime() {
        return actualTime;
    }

    /**
     *
     * @return {@link TableItem#plannedTime}
     */
    public String getPlannedTime() {
        return plannedTime;
    }

    /**
     *
     * @return {@link TableItem#actualTime}
     */
    public String getStopId() {
        return stopId;
    }
}