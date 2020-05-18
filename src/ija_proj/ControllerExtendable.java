/*
 * Authors: 
 * Matej Cimmerman (xcimme00)
 * Daniel Paulovi� (xpaulo04)
 */
package ija_proj;

/**
 * Extendovateľný kontroler, ktorý vykresľuje alebo uzaviera scénu
 * @author Matej Cimmerman (xcimme00), Daniel Paulovič (xpaulo04)
 */
public abstract class ControllerExtendable {

    /**
     * Inicializácia scény.
     */
    public abstract void startUp();

    /**
     * Uzavretie scény.
     */
    public abstract void close();


}