/*
 * Authors: 
 * Matej Cimmerman (xcimme00)
 * Daniel Paulovi� (xpaulo04)
 */
package ija_proj;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.shape.Circle;

/**
 * Trieda pre pohyb spoju podľa určených časov
 * @author Matej Cimmerman (xcimme00), Daniel Paulovič (xpaulo04)
 */
class Path {
    private String id;
    private List<LocalTime> plannedTimetable = new ArrayList<>();
    private List<LocalTime> actualTimetable = new ArrayList<>();
    private List<LocalTime> backUpTimetable = new ArrayList<>();
    private Line line;
    private Circle circle;

    /**
     * Získa názov cesty
     * @return ID of trip as string
     */
    public String getId() {
        return id;
    }

    /**
     * Nastaví názov cesty
     * @param id ID that we want to set for this trip
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Konštruktor pre cestu
     * @param id ID of trip
     * @param timetable timetable of trip
     */
    public Path(String id, List<LocalTime> timetable) {
        this.id = id;
        this.plannedTimetable=timetable;
        this.actualTimetable.addAll(timetable);
        this.backUpTimetable.addAll(timetable);
    }

    /**
     * Obnovenie aktuálneho časového plánu cesty
     */
    public void resetTimetable()
    {
        this.actualTimetable=new ArrayList<>();
        this.actualTimetable.addAll(this.plannedTimetable);
    }

    /**
     * Obnovenie pôvodného časového plánu cesty
     */
    public void restoreBackUp()
    {
        this.plannedTimetable.clear();
        this.actualTimetable.clear();
        this.plannedTimetable.addAll(this.backUpTimetable);
        this.actualTimetable.addAll(this.backUpTimetable);
    }

    /**
     * Načítanie časového plánu cesty
     */
    public void loadBackUpTimetable()
    {
        this.actualTimetable=new ArrayList<>(this.backUpTimetable);
        this.plannedTimetable=new ArrayList<>(this.backUpTimetable);
    }

    /**
     * Vrátenie pôvodného plánu cesty
     * @return pôvodný časový plán
     */
    public List<LocalTime> getBackUpTimetable() {
        return backUpTimetable;
    }

    /**
     * @return Planned timetable počíta s uzavretými ulicami
     */
    public List<LocalTime> getPlannedTimetable() {
        return plannedTimetable;
    }

    /**
     * @return Actual timetable počíta s uzavretými ulicami a aj nastavenou trafikou.
     */
    public List<LocalTime> getActualTimetable() {
        return actualTimetable;
    }

    /**
     * Konštruktor pre prípad zadania mena
     * @param id názov cesty
     */
    public Path(String id) {
        this.id = id;
    }

    /**
     *
     * @return viz. getActualTimeTable
     */
    public List<LocalTime> getTimetable() {
        return actualTimetable;
    }

    /**
     * Pridá časovú položku do plánu
     * @param time String time
     */
    public void addTimetableItem(String time)
    {
        LocalTime localTime = LocalTime.parse(time);
        plannedTimetable.add(localTime);
    }

    /**
     * GUI funckia pre nastavenie Circle
     * @param circle Kruh.
     */
    public void setCircle(Circle circle) {
        this.circle = circle;
    }


    /**
     * GUI funkcia pre vrátenie Circle
     * @return Kruh.
     */
    public Circle getCircle() {
        return circle;
    }

    /**
     * GUI funkcia pre nastavenie Line
     * @param line Čiara.
     */
    public void setLine(Line line) {
        this.line = line;
    }

    /**
     * GUI funkcia pre vrátenie Line
     * @return Čiara.
     */
    public Line getLine() {
        return line;
    }
}
