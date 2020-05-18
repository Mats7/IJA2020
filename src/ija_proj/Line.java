/*
 * Authors: 
 * Matej Cimmerman (xcimme00)
 * Daniel Paulovi� (xpaulo04)
 */
package ija_proj;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

/**
 * Trieda pre linky.
 * @author Matej Cimmerman (xcimme00), Daniel Paulovič (xpaulo04)
 */
public class Line implements Cloneable{
    private List<Stop> stops;
    private List<Street> streets;
    private String id;
    private String name;
    private List<Path> paths = new ArrayList<>();
    private List<List<Street>> conflictStreets = new ArrayList<>();
    private List<Stop> stopsOld = new ArrayList<>();
    private List<Street> streetsOld = new ArrayList<>();
    private List<Path> tripsOld = new ArrayList<>();

    /**
     * Získa všetky cesty vozidiel na tejto linke
     * @return Objekt s cestami
     */
    public List<Path> getLineConnections() {
        return paths;
    }

    /**
     * Vráti identifikátor linky
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * Vráti názov linky
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Vráti zoznam všetkých stopiek na linke
     * @return stops
     */
    public List<Stop> getStops() {
        return stops;
    }

    /**
     * Konštruktor pre linku
     * @param id
     * @param name
     */
    public Line(String id, String name) {
        this.id = id;
        this.name = name;
        this.stops = new ArrayList<>();
        this.streets = new ArrayList<>();
    }


    /**
     * Vráti vzdialenosť medzi stopkami
     * @param stop1 stopka 
     * @param stop2 stopka
     * @return Vzdialenosť
     */
    public double getDistBetweenStops(Stop stop1, Stop stop2) {
        double lenght = 0;
        int first = 0;
        int last = 0;
        List<Street> lineStreets = new ArrayList<>();
        first = this.streets.indexOf(stop1.getStreet());
        last = this.streets.indexOf(stop2.getStreet());
        if (stop1.getStreet().equals(stop2.getStreet())) {
            if (changeX(stop1.getStreet())) {
                return Math.abs(stop1.getCoordinate().getX() - stop2.getCoordinate().getX());
            } else {
                return Math.abs(stop1.getCoordinate().getY() - stop2.getCoordinate().getY());
            }
        }
        for (; first <= last; first++) {
            lineStreets.add(this.streets.get(first));
        }
        for (int i = 0; i < lineStreets.size(); i++) {
            if (i == 0) {
                Coordinate endCoord = followPoint(lineStreets.get(i), lineStreets.get(i + 1));
                Coordinate stopCoord = stop1.getCoordinate();
                if (changeX(lineStreets.get(i))) {
                    lenght += Math.abs(stopCoord.getX() - endCoord.getX());
                } else {
                    lenght += Math.abs(stopCoord.getY() - endCoord.getY());
                }
            } else if (i == lineStreets.size() - 1) {
                Coordinate endCoord2 = followPoint(lineStreets.get(i - 1), lineStreets.get(i));
                Coordinate stopCoord2 = stop2.getCoordinate();

                if (changeX(lineStreets.get(i))) {
                    lenght += Math.abs(stopCoord2.getX() - endCoord2.getX());
                } else {
                    lenght += Math.abs(stopCoord2.getY() - endCoord2.getY());
                }
            } else {
                Coordinate start11 = lineStreets.get(i).getCoordinates().get(0);
                Coordinate end11 = lineStreets.get(i).getCoordinates().get(1);

                if (changeX(lineStreets.get(i))) {
                    lenght += Math.abs(start11.getX() - end11.getX());
                } else {
                    lenght += Math.abs(start11.getY() - end11.getY());
                }
            }


        }
        return lenght;
    }

    /**
     * Pomocná funkcia - Vráti dĺžku ulice
     * @param street ulica
     * @return dĺžka ulice
     */
    public double getLenghtOfStreet(Street street) {
        if (Math.abs(street.getCoordinates().get(0).getX() - street.getCoordinates().get(1).getX()) > 0) {
            return (Math.abs(street.getCoordinates().get(0).getX() - street.getCoordinates().get(1).getX()));
        } else {
            return (Math.abs(street.getCoordinates().get(0).getY() - street.getCoordinates().get(1).getY()));
        }
    }

    /**
     * Obnoví časový rozpis do povodného stavu
     */
    public void resetTimetable() {
        for (Path path : this.paths) {
            path.resetTimetable();
        }
    }

    /**
     * Prida obmedzenie do zoznamu
     *
     * @param street ulica
     */
    public void addConflictStreet(Street street) {
        List<Street> streetTMP = new ArrayList<>();
        streetTMP.add(street);
        this.conflictStreets.add(streetTMP);
    }

    /**
     * Obnoví zoznam problémových ulíc (ktoré sú v konflikte s inými)
     */
    private void clearConflicts() {
        this.conflictStreets.clear();
    }

 
    /**
     * Vrati pocet obmedzeni
     *
     * @return velkost
     */
    public int getConflictsCount() {
        return this.conflictStreets.size();
    }

    /**
     * Je tu obmedzenie?
     *
     * @return bool
     */
    public boolean isConflict() {
        return !this.conflictStreets.isEmpty();
    }

    /**
     * Vrati obmedzenia
     *
     * @return zoznam
     */
    public List<List<Street>> getConflicts() {
        return this.conflictStreets;
    }


    /**
     * Vypocita dlzku medzi 2 zastavkami
     * @param stop1 start
     * @param stop2 koniec
     * @return dlzka
     */
    public double getStopsLength(Stop stop1, Stop stop2) {
        double lenght = 0;
        List<Street> lineStreets = new ArrayList<>();
        int first = 0;
        int last = 0;
        if (this.stops.contains(stop1) && this.stops.contains(stop2)) {
            if (this.streets.contains(stop1.getStreet()) && this.streets.contains(stop2.getStreet())) {
                first = this.streets.indexOf(stop1.getStreet());
                last = this.streets.indexOf(stop2.getStreet());
                if (stop1.getStreet().equals(stop2.getStreet())) {
                    if (changeX(stop1.getStreet())) {
                        return Math.abs(stop1.getCoordinate().getX() - stop2.getCoordinate().getX());
                    } else {
                        return Math.abs(stop1.getCoordinate().getY() - stop2.getCoordinate().getY());
                    }
                }
                for (; first <= last; first++) {
                    lineStreets.add(this.streets.get(first));
                }
                for (int i = 0; i < lineStreets.size(); i++) {
                    if (i == 0) {
                        Coordinate endCoord = followPoint(lineStreets.get(i), lineStreets.get(i + 1));
                        Coordinate stopCoord = stop1.getCoordinate();
                        if (changeX(lineStreets.get(i))) {
                            lenght += Math.abs(stopCoord.getX() - endCoord.getX());
                        } else {
                            lenght += Math.abs(stopCoord.getY() - endCoord.getY());
                        }
                    } else if (i == lineStreets.size() - 1) {
                        Coordinate endCoord2 = followPoint(lineStreets.get(i - 1), lineStreets.get(i));
                        Coordinate stopCoord2 = stop2.getCoordinate();

                        if (changeX(lineStreets.get(i))) {
                            lenght += Math.abs(stopCoord2.getX() - endCoord2.getX());
                        } else {
                            lenght += Math.abs(stopCoord2.getY() - endCoord2.getY());
                        }
                    } else {
                        Coordinate start11 = lineStreets.get(i).getCoordinates().get(0);
                        Coordinate end11 = lineStreets.get(i).getCoordinates().get(1);

                        if (changeX(lineStreets.get(i))) {
                            lenght += Math.abs(start11.getX() - end11.getX());
                        } else {
                            lenght += Math.abs(start11.getY() - end11.getY());
                        }
                    }


                }
            }
        }
        return lenght;
    }

    /**
     * Get stop by real index, ignore streets with no stop
     *
     * @param index what index I want
     * @return Stop on index.
     */
    public Stop getStopByIndex(int index) {
        int counter = 0;
        for (Stop stop : this.stops) {
            if (stop != null) {
                if (counter == index) {
                    return stop;
                }
                counter++;
            }
        }
        return null;
    }

    /**
     * Get real stops count.
     *
     * @return count of stops on line
     */
    public int getRealStopsCount() {
        int counter = 0;
        for (Stop stop : this.getStops()) {
            if (stop != null) {
                counter++;
            }
        }
        return counter;
    }

    /**
     * Get real stops
     * @return Gest list of stops with no null values
     */
    public List<Stop> getRealStops() {
        List<Stop> stops = new ArrayList<>();
        for (Stop stop : this.getStops()) {
            if (stop != null) {
                stops.add(stop);
            }
        }
        return stops;
    }

    /**
     * Return streets on line.
     * @return Streets on line
     */
    public List<Street> getStreets() {
        return streets;
    }

    /**
     * Get streets between 2 bus stops.
     * @param stop1 starting stop
     * @param stop2 ending stop
     * @return List of streets between stops.
     */
    public List<Street> getStreetsBetween(Stop stop1, Stop stop2) {
        List<Street> lineStreets = new ArrayList<>();
        int first = 0;
        int last = 0;

        first = this.streets.indexOf(stop1.getStreet());
        last = this.streets.indexOf(stop2.getStreet());
        if (stop1.getStreet().equals(stop2.getStreet())) {
            lineStreets.add(stop1.getStreet());
            return lineStreets;
        }
        for (; first <= last; first++) {
            lineStreets.add(this.streets.get(first));
        }
        return lineStreets;
    }

    /**
     * Return a Coordinate that joins 2 streets.
     * @param street1 first street
     * @param street2 second street
     * @return Coordinate that is identical for both streets.
     */
    public Coordinate followPoint(Street street1, Street street2) {
        Coordinate start = street1.getCoordinates().get(0);
        Coordinate end = street1.getCoordinates().get(1);

        Coordinate start2 = street2.getCoordinates().get(0);
        Coordinate end2 = street2.getCoordinates().get(1);

        if (start.equals(start2)) {
            return start;
        }
        if (start.equals(end2)) {
            return start;
        }
        if (end.equals(start2)) {
            return end;
        }
        if (end.equals(end2)) {
            return end;
        }
        return null;
    }

    /**
     * Function that will find if streets bind to each other
     *
     * @param street first street
     * @param street2 second street
     * @return boolean value if second street follow first street
     */
    public boolean isFollowing(Street street, Street street2) {
        if ((street.getCoordinates().get(0).equals(street2.getCoordinates().get(0))) || (street.getCoordinates().get(0).equals(street2.getCoordinates().get(1))) || (street.getCoordinates().get(1).equals(street2.getCoordinates().get(0))) || (street.getCoordinates().get(1).equals(street2.getCoordinates().get(1)))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Finding what coord point is more left on the map.
     * @param coord1 first coordinate
     * @param coord2 second coordinate
     * @return Boolean value if first coord is more left or no.
     */
    public boolean plusX(Coordinate coord1, Coordinate coord2) {
        boolean plus;
        if (coord1.getX() - coord2.getX() < 0) {
            plus = true;
        } else {
            plus = false;
        }
        return plus;
    }

    /**
     * Finding what coord point is more higher on the map.
     * @param coord1 first coord
     * @param coord2 second coord
     * @return Boolean value if first coord is more higher or no.
     */
    public boolean plusY(Coordinate coord1, Coordinate coord2) {
        boolean plus;
        if (coord1.getY() - coord2.getY() < 0) {
            plus = true;
        } else {
            plus = false;
        }
        return plus;
    }


    /**
     * Compute if street is horizontal or vertical.
     * @param street computed street
     * @return return true if street if horizontal and false if vertical.
     */
    public boolean changeX(Street street) {
        boolean zmenaX;
        Coordinate first = street.getCoordinates().get(0);
        Coordinate second = street.getCoordinates().get(1);

        if (first.getX() != second.getX()) {
            zmenaX = true;
        } else {
            zmenaX = false;
        }
        return zmenaX;
    }

    /**
     * prida stopku do ulice
     * @param stop stopka
     * @return true 
     */
    public boolean addStop(Stop stop) {
        if (this.stops.isEmpty()) {
            this.stops.add(stop);
            if (this.streets.isEmpty()) {
                this.streets.add(stop.getStreet());
            } else {
                if (!this.streets.get(this.streets.size() - 1).getName().equals(stop.getId())) {
                    this.streets.add(stop.getStreet());
                }
            }
            return true;
        }

        if (this.streets.get(this.streets.size() - 1).follows(stop.getStreet())) {
            this.stops.add(stop);
            if (!this.streets.get(this.streets.size() - 1).getName().equals(stop.getStreet().getName())) {
                this.streets.add(stop.getStreet());
            }
            return true;
        }
        return false;

    }

    /**
     * prida ulicu do linky
     * @param street ulica
     * @return true
     */
    public boolean addStreet(Street street) {
        if (this.stops.isEmpty()) {
            return false;
        }
        if (this.streets.get(this.streets.size() - 1).follows(street)) {
            //this.stops.add(null);
            this.streets.add(street);
            return true;
        }
        return false;
    }

    /**
     * vrati zoznam ulic a stopiek na linke
     * @return list 
     */
    public List<AbstractMap.SimpleImmutableEntry<Street, Stop>> getRoute() {
        List<AbstractMap.SimpleImmutableEntry<Street, Stop>> result = new ArrayList<>();
        for (int i = 0; i < this.streets.size(); i++) {
            result.add(new AbstractMap.SimpleImmutableEntry<Street, Stop>(this.streets.get(i), this.stops.get(i)));
        }
        return result;
    }

    /**
     * Pridá spoj do linky
     * @param path
     */
    public void addPath(Path path) {
        paths.add(path);
    }

    /**
     * Získa spoje na danej linke
     * @return paths zoznam
     */
    public List<Path> getPaths() {
        return paths;
    }

    /**
     * Klonovanie linky
     * @return clone
     * @throws CloneNotSupportedException
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * Vytvorí zálohu všetkých údajov zo súborov pre možnosť manipulácie bez ich straty
     */
    public void backUpOld() {
        this.streetsOld.addAll(this.streets);
        this.tripsOld.addAll(this.paths);
        this.stopsOld.addAll(this.stops);
    }

    /**
     * Vráti zoznamy ulic, stopiek a ciest do povodneho stavu
     */
    public void restoreOld() {
        this.streets.clear();
        this.stops.clear();
        this.paths.clear();
        this.streets.addAll(this.streetsOld);
        this.paths.addAll(this.tripsOld);
        this.stops.addAll(this.stopsOld);

        for (Path path : this.paths) {
            path.restoreBackUp();
        }
    }

    /**
     * Aktualizuje obmedzenia pri pridaní ďaľšieho
     */
    private void updateConflicts()
    {
        clearConflicts();
        for (Street street : getStreets()) {
            if (street.isClosed()) {
                addConflictStreet(street);
            }
        }
    }

    /**
     * Získa a spracuje vŠetky konflikty na mape
     */
    public void getAllConflicts() {
        updateConflicts();
        if (getConflictsCount() >0) {
            List<Street> closedStreets = getConflicts().get(0);
            if (getStreets().get(0).equals(closedStreets.get(0))) {
                for (Street street : closedStreets) {
                    for (Stop stop : street.getStops()) {
                        if (getStops().contains(stop)) {
                            getStops().remove(stop);
                            for (Path path : getPaths()) {
                                path.getActualTimetable().remove(0);
                                path.getPlannedTimetable().remove(0);
                            }
                        }
                    }
                    getStreets().remove(0);
                }
            }
        }
        updateConflicts();
        if (getConflictsCount() > 0) {
            List<Street> closedStreets = getConflicts().get(getConflicts().size() - 1);
            if (getStreets().get(getStreets().size() - 1).equals(closedStreets.get(closedStreets.size() - 1))) {
                for (Street street : closedStreets) {
                    for (Stop stop : street.getStops()) {
                        if (getStops().contains(stop)) {
                            getStops().remove(stop);
                            for (Path path : getPaths()) {
                                path.getActualTimetable().remove(path.getActualTimetable().size() - 1);
                                path.getPlannedTimetable().remove(path.getPlannedTimetable().size() - 1);
                            }
                        }
                    }
                    getStreets().remove(getStreets().size() - 1);
                }
            }
            updateConflicts();
        }
    }
}
