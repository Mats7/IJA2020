/*
 * Authors:
 * Matej Cimmerman (xcimme00)
 * Daniel Paulovič (xpaulo04)
 */
package ija_proj;
//import com.fasterxml.jackson.annotation.JsonIdentityInfo;
//import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import ija_proj.Coordinate;
import ija_proj.Street;
import java.util.Objects;

/**
 * Trieda pre objekt súradnice
 * @author Matej Cimmerman (xcimme00), Daniel Paulovič (xpaulo04)
 */
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "name")
public class Coordinate implements Cloneable{
    //private String name;
    double x;
    double y;
    
    /**
     * Konštruktor pre súradnice
     * @param x double X
     * @param y double Y
     */
    public Coordinate(double x, double y)
    {
        this.x = x;
        this.y = y;
    }
            
    /**
     * Vráti X rozmer súradnice
     * @return X
     */
    public double getX() {
        return x;
    }

    /**
     * Vráti Y rozmer súradnice
     * @return Y
     */
    public double getY() {
        return y;
    }
    
    /**
     * Nastaví X rozmer súradnice.
     * @param x X pre nastavenie.
     */
    public void setX(double x){
        this.x = x;
    }
    
    /**
     * Nastaví Y rozmer súradnice.
     * @param y Y pre nastavenie.
     */
    public void setY(double y){
        this.y = y;
    }
    
    /*public String getName(){
        return name;
    }*/

    /**
     * Vytvorí novú súradnicu 
     * @param x Vzdialenosť na osi X.
     * @param y Vzdialenosť na osi Y.
     * @return Vráti novú súradnicu, pokiaľ má Y aj X rozmery vacsie ako 0
     */
    public static Coordinate create(double x, double y)
    {
        if(x<0 || y<0) {
            return null;
        } 
        else {
            return new Coordinate(x,y);
        }
    }
    
    public double diffX(Coordinate c){
        return this.x - c.x;
    }
    
    /**
     * Rozdiel dvoch Y súradníc
     * @param c Koordinát
     * @return Vráti rozdiel
     */
    public double diffY(Coordinate c){
        return this.y - c.y;
    }
    

    @Override
    public boolean equals(Object o)
    {
        if(o == this)
        {
            return true;
        }

        if(o == null || o.getClass() != this.getClass())
        {
            return false;
        }

        Coordinate ccc = (Coordinate) o;
        return (x == ccc.x) && (y == ccc.y);
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public int hashCode() {
        /*int hash = 3;
        hash = 83 * hash + this.x;
        hash = 83 * hash + this.y;*/
        return Objects.hash(x,y);
    }
}