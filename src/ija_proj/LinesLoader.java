/*
 * Authors: 
 * Matej Cimmerman (xcimme00)
 * Daniel Paulovi� (xpaulo04)
 */
package ija_proj;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Trieda pre načítanie riadkov z obidvoch vstupných súborov
 * @author Matej Cimmerman (xcimme00), Daniel Paulovič (xpaulo04)
 */
public abstract class LinesLoader {


    /**
     * Načíta linky zo súboru.
     * @param streetMap StreetMap to be referenced.
     * @return Výsledný zoznam liniek
     * @throws Súbor musí existovať pri spustení programu - inak Exception.
     */
    public static List<Line> load(StreetMap streetMap) throws Exception {
        List<Line> lines = new ArrayList<>();

        JSONArray jsonLines = (JSONArray) JSONLoader.load("data/linky.json").get("linky");

        for (JSONObject jsonLine : (Iterable<JSONObject>) jsonLines) {

            String lineId = (String) jsonLine.get("lineId");
            String lineName = (String) jsonLine.get("lineName");

            Line line = new Line(lineId, lineName);
            lines.add(line);

            for (JSONObject jsonRoute : (Iterable<JSONObject>) jsonLine.get("path")) {

                String streetName = (String) jsonRoute.get("street");
                Street street = streetMap.getStreet(streetName);
                if (street == null) {
                    throw new Exception("Ulica \"" + streetName + "\" v súbore \"linky.json\" nie je dostupná v mape \"map.json\"!");
                }

                String stopName = (String) jsonRoute.get("stop");
                Stop stop= street.getStop(stopName);
                if (stop != null) {
                    line.addStop(stop);
                }
                else {
                    line.addStreet(street);
                }
                if(!street.getLines().contains(line))
                {
                    street.addLine(line);
                }

            }

            for (JSONObject jsonTrip : (Iterable<JSONObject>) jsonLine.get("trips")) {
                String tripId = (String) jsonTrip.get("tripId");
                List<LocalTime> times = new ArrayList<>();

                for (String jsonTripTime : (Iterable<String>) jsonTrip.get("times")) {
                    times.add(LocalTime.parse(jsonTripTime));
                }
                Path path = new Path(tripId,times);
                path.setLine(line);
                if(line.getRealStopsCount() != path.getTimetable().size())
                {
                    throw new Exception("Počet spojov \""+line.getId()+"\" a \""+path.getId()+"\" musí byť rovnaký!");
                }
                line.addPath(path);
            }
            line.backUpOld();



        }

        return lines;

    }
}