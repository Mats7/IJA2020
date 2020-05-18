/*
 * Authors: 
 * Matej Cimmerman (xcimme00)
 * Daniel Paulovi� (xpaulo04)
 */
package ija_proj;

import javafx.scene.control.Label;

/**
 *
 * @author Matej Cimmerman (xcimme00), Daniel Paulovič (xpaulo04)
 */
public class Stop {
    private String id;
    private Coordinate coordinate;
    private Street street = null;
    public Label label;

    public Stop(String id)
    {
        this.id = id;
    }

    public Stop(String id, Coordinate coordinate)
    {
        this.id = id;
        this.coordinate = coordinate;
    }

    /**
     * Vráti názov zastávky
     *
     * @return Identifikátor zastávky.
     */
    public String getId()
    {
        return this.id;
    }

    /**
     * Získa súradnice stopky
     *
     * @return Poloha vo forme Coordinate
     */
    public Coordinate getCoordinate()
    {
        return this.coordinate;
    }

    /**
     * Nastaví stopku na danú ulicu
     *
     * @param s Ulica
     */
    public void setStreet(Street s)
    {
        this.street = s;
    }

    /**
     * Vráti ulicu danej stopky
     *
     * @return Ulica danej stopky
     */
    public Street getStreet()
    {
        return this.street;
    }

    /**
     * Nastaví label s názvom stopky
     *
     * @param label label that we want draw
     */
    public void setLabel(Label label) {
        this.label=label;
    }

    /**
     * Ukáže label s názvom stopky.
     *
     * @return Name of stop label
     */
    public Label getLabel() {
        return this.label;
    }
    
    static Stop defaultStop(String id, Coordinate c) {
        return new Stop(id,c);
    }


    /**
     * Compute if stops are the same
     * @param obj stop that we want to compare to actual stop
     * @return true if they are the same
     */
    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        if (this.getClass() != obj.getClass())
        {
            return false;
        }
        Stop stop = (Stop) obj;
        if (this.id.equals(stop.getId()))
        {
            return true;
        }
        return false;
    }


    /**
     * Override string function
     * @return String of stop
     */
    @Override
    public String toString() {
        return "stop(" + this.getId() + ")";
    }
}
