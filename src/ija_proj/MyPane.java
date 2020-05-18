/*
 * Authors: 
 * Matej Cimmerman (xcimme00)
 * Daniel Paulovi� (xpaulo04)
 */
package ija_proj;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Scale;


public class MyPane extends Pane {
    /**
     * Obsah
     */
    Node content;
    
    
    
    
    /**
     * Koeficient priblizenia
     */  
    private DoubleProperty zoom = new SimpleDoubleProperty(1);

    
    
    
    
    /**
     * Konštruktor pre približovatelny Pane
     * @param content Node vo vnutri tohto Pane
     */
    public MyPane(Node content) {
        this.content = content;
        getChildren().add(content);
        Scale scale = new Scale(1, 1);
        content.getTransforms().add(scale);

        zoom.addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                scale.setX(newValue.doubleValue());
                scale.setY(newValue.doubleValue());
                requestLayout();
            }
        });
    }

    /**
     * Funkcia pre aktualizovanie Node vo vnútri Pane
     */
    protected void layoutChild() {
        Pos pos = Pos.TOP_LEFT;
        double width = getWidth();
        double height = getHeight();
        double top = getInsets().getTop();
        double right = getInsets().getRight();
        double left = getInsets().getLeft();
        double bottom = getInsets().getBottom();
        double contentWidth = (width - left - right)/zoom.get();
        double contentHeight = (height - top - bottom)/zoom.get();
        layoutInArea(content, left, top,
                contentWidth, contentHeight,
                0, null,
                pos.getHpos(),
                pos.getVpos());
    }

    /**
     * Vráti koeficient priblíženia
     * @return Hodnota zoomu
     */
    public final Double getZoomC() {
        return zoom.get();
    }

    /**
     * Nastaví koeficient priblíženia
     * @param zoomFactor  zoomu
     */
    public final void setZoomC(Double zoomFactor) {
        this.zoom.set(zoomFactor);
    }

    /**
     * Vráti celý objekt zoom
     * @return Celá premenna zoom
     */
    public final DoubleProperty zoomC() {
        return zoom;
    }
}
