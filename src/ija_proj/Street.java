/*
 * Authors:
 * Matej Cimmerman (xcimme00)
 * Daniel Paulovič (xpaulo04)
 */
package ija_proj;
//import com.fasterxml.jackson.annotation.JsonIdentityInfo;
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import javafx.application.Platform;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

/**
 *
 * @author Matej Cimmerman (xcimme00), Daniel Paulovič (xpaulo04)
 */
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "name")
public class Street /*implements Drawable*/{
    //Coordinate c1;
    //Coordinate c2;
    private String name;
    private Coordinate startC;
    private Coordinate stopC;
    private List<Coordinate> coordinates;
    private List<Stop> stops;
    private int trafficCoefficient = 1;
    private javafx.scene.shape.Line lineGui;
    private boolean closed = false;
    private List<Line> lines = new ArrayList<>();
    
    /**
     * Konstruktor pre ulicu.
     * @param name Názov ulice.
     * @param coordinates ccs
     */
    
    public Street(String name, Coordinate[] coordinates) {
        this.name = name;
        this.coordinates = new ArrayList<>();
        this.coordinates.addAll(Arrays.asList(coordinates));
        this.stops = new ArrayList<>();
    }
    
     public Street(String name, List<Coordinate> coordinates) {
        this.name = name;
        this.coordinates = coordinates;
        this.stops = new ArrayList<>();
    }
    
    /*public Street(String name, Coordinate startC, Coordinate stopC)
    {
        this.name = name;
        this.startC = startC;
        this.stopC = stopC;
    }*/
     
     
     public void addLine(Line line) {
        lines.add(line);
    }

    public List<Line> getLines() {
        return lines;
    }

    public boolean isClosed() {
        return closed;
    }

    public boolean isOpen() {
        return !closed;
    }
    
    public void setClosed(boolean closed) {
        this.closed = closed;
        Platform.runLater(() -> {
            if (closed) {
                this.lineGui.setStyle("-fx-stroke-width: 2; -fx-stroke: red;");
            } else {
                this.lineGui.setStyle("-fx-stroke-width: 2; -fx-stroke: black;");
            }
        });
    }
    
    public void setTrafficCoefficient(int trafficCoefficient) throws Exception {
        if (trafficCoefficient < 1 || trafficCoefficient > 10) {
            throw new Exception("Traffic coefficient have to be in range 1..10! Value will remain unchanged.");
        }
        this.trafficCoefficient = trafficCoefficient;
    }

    /**
     * Get the trafic coeficient of actual street
     * @return Trafic coeficient of street
     */
    public int getTrafficCoefficient() {
        return trafficCoefficient;
    }

    public List<Coordinate> getCoordinates() {
        return this.coordinates;
    }

    public List<Stop> getStops() {
        return this.stops;
    }
    
    public boolean addStop(Stop stop) {
       int len = this.coordinates.size();
        Coordinate c3 = stop.getCoordinate();
        for(int i = 1; i < len; ++i)
        {
            Coordinate c1 = this.coordinates.get(i - 1);
            Coordinate c2 = this.coordinates.get(i);
            double diffx = c1.getX() - c2.getX();
            double diffy = c1.getY() - c2.getY();
            if(diffx == 0)
            {
                if(c3.getX() == c1.getX() && ((c3.getY() >= c1.getY() && c3.getY() <= c2.getY()) || (c3.getY() <= c1.getY() && c3.getY() >= c2.getY()) ))
                {
                    stop.setStreet(this);
                    this.stops.add(stop);
                    return true;
                }
            }
            else if(diffy == 0)
            {
                if(c3.getY() == c1.getY() && ((c3.getX() >= c1.getX() && c3.getX() <= c2.getX()) || (c3.getX() <= c1.getX() && c3.getX() >= c2.getX()) ))
                {
                    stop.setStreet(this);
                    this.stops.add(stop);
                    return true;
                }
            }

        }
        return false;
    }
    
    public Coordinate begin() {
        return this.coordinates.get(0);
    }

    /**
     * Get last coord of street
     * @return last coordinate of street
     */
    public Coordinate end() {
        return this.coordinates.get(this.coordinates.size() - 1);
    }

    public boolean follows(Street s) {
        if (this.begin().equals(s.begin()) || this.begin().equals(s.end()) || this.end().equals(s.begin()) || this.end().equals(s.end())) {
            return true;
        }
        return false;
    }

    public Stop getStop(String id) {
        for (Stop stop : this.stops) {
            if (stop.getId().equals(id))
                return stop;
        }
        return null;
    }

    public void setGui(javafx.scene.shape.Line gui) {
        this.lineGui = gui;
    }

    public javafx.scene.shape.Line getGui() {
        return lineGui;
    }
    
    
    public String getName(){
        return name;
    }
    
    static Street create(String id, List<Coordinate> coordinates) {
        if (coordinates.size() <= 1)
            return null;

        for (int i = 0; i < coordinates.size()-2; i++) {
            double diffX1 = coordinates.get(i).diffX(coordinates.get(i + 1));
            double diffY1 = coordinates.get(i).diffY(coordinates.get(i + 1));
            double diffX2 = coordinates.get(i + 1).diffX(coordinates.get(i + 2));
            double diffY2 = coordinates.get(i + 1).diffY(coordinates.get(i + 2));

            double res = diffX1 * diffX2 + diffY1 * diffY2;

            if (res != 0) {
                return null;
            }
        }
        return new Street(id, coordinates);
    }
    
    static Street defaultStreet(String id, Coordinate... coordinates) {
        if (coordinates.length <= 1)
            return null;

        for (int i = 0; i < coordinates.length-2; i++) {
            double diffX1 = coordinates[i].diffX(coordinates[i+1]);
            double diffY1 = coordinates[i].diffY(coordinates[i+1]);
            double diffX2 = coordinates[i+1].diffX(coordinates[i+2]);
            double diffY2 = coordinates[i+1].diffY(coordinates[i+2]);

            double res = diffX1 * diffX2 + diffY1 * diffY2;

            if (res != 0) {
                return null;
            }
        }
        return new Street(id, coordinates);
    }

    /**
     * Vykreslí ulicu do scény.
     * @return Text+Line
     */
    /*@JsonIgnore
    @Override
    public List<Shape> getGUI(){
        return Arrays.asList(
                new Text(startC.getX()+Math.abs(startC.getX() - stopC.getX())/2,startC.getY()+Math.abs(startC.getY() - stopC.getY())/2,name),
                new Line(startC.getX(), startC.getY(), stopC.getX(), stopC.getY())
        );
    }*/
    
/*
    @Override
    public boolean addStop(Stop stop) {
       int len = this.lc.size();
        Coordinate c3 = stop.getCoordinate();
        for(int i = 1; i < len; ++i)
        {
            Coordinate c1 = this.lc.get(i - 1);
            Coordinate c2 = this.lc.get(i);
            double diffx = c1.getX() - c2.getX();
            double diffy = c1.getY() - c2.getY();
            if(diffx == 0)
            {
                if(c3.getX() == c1.getX() && ((c3.getY() >= c1.getY() && c3.getY() <= c2.getY()) || (c3.getY() <= c1.getY() && c3.getY() >= c2.getY()) ))
                {
                    stop.setStreet(this);
                    this.ls.add(stop);
                    return true;
                }
            }
            else if(diffy == 0)
            {
                if(c3.getY() == c1.getY() && ((c3.getX() >= c1.getX() && c3.getX() <= c2.getX()) || (c3.getX() <= c1.getX() && c3.getX() >= c2.getX()) ))
                {
                    stop.setStreet(this);
                    this.ls.add(stop);
                    return true;
                }
            }

        }
        return false;
    }
    
    //NETREBA POUZIT
    @Override
    public boolean equals(Object o)
    {
        if (o == this)
        { 
            return true;
        } 
        if (o == null || o.getClass() != this.getClass())
        { 
            return false;
        }
        Street street = (Street) o;
        return streetid.equals(street.streetid);
    }
    
    @Override
    public Coordinate begin(){
        return getCoordinates().get(0);
    }
    
    @Override
    public Coordinate end(){
        return getCoordinates().get(1);
    }
    
    @Override
    public boolean follows(Street s){
        if(this.begin().equals(s.end()) || this.begin().equals(s.begin()) || this.end().equals(s.begin()) || this.end().equals(s.end())){
            return true;
        }
        else{
            return false;
        }
    }
    
    @Override
    public Street clone()
    {
        Street s;
        s = new Street(this.getName(), coordinates.toArray(new Coordinate[coordinates.size()]).clone());
        int len = this.stops.size();
        for(int i = 0; i < len; ++i)
        {
            s.addStop(this.stops.get(i).clone());
        }

        return s;
        
    }*/

    
    
}
