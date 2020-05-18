/*
 * Authors: 
 * Matej Cimmerman (xcimme00)
 * Daniel Paulovi� (xpaulo04)
 */
package ija_proj;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Matej Cimmerman (xcimme00), Daniel Paulovič (xpaulo04)
 */
public class StreetMap {
     private List<Street> streets;

    public StreetMap() {
        streets = new ArrayList<>();
    }

    /**
     * Přidá ulici do mapy.
     *
     * @param s Objekt reprezentující ulici.
     */
    public void addStreet(Street s) {
        if (s == null)
            return;

        this.streets.add(s);
    }

    /**
     * Vrátí objekt reprezentující ulici se zadaným id.
     *
     * @param id Identifikátor ulice.
     * @return Nalezenou ulici. Pokud ulice s daným identifikátorem není součástí mapy, vrací null.
     */
    public Street getStreet(String id) {
        for (Street street : this.streets) {
            if (street.getName().equals(id))
                return street;
        }
        return null;
    }

    /**
     * Check if street maps are same
     * @param o street map that we want to compare to actual map
     * @return true if they are same
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StreetMap that = (StreetMap) o;
        return streets.equals(that.streets);
    }

    /**
     * Get streets on actual map.
     * @return List of streets of map.
     */
    public List<Street> getStreets() {
        return streets;
    }
}
