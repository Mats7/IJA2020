/*
 * Authors: 
 * Matej Cimmerman (xcimme00)
 * Daniel Paulovi� (xpaulo04)
 */
package ija_proj;
/**
 * Trieda pre objekt v okne s aktívnymi vozidlami
 * @author Matej Cimmerman (xcimme00), Daniel Paulovič (xpaulo04)
 */
public class ActiveItem {
    /**
     * Názov linky.
     */
    private String lineName;
    /**
     * Názov cesty.
     */
    private String pathName;

    /**
     * Konštruktor pre aktívny objekt.
     * @param lineName l
     * @param pathName p
     */
    public ActiveItem(String lineName, String pathName)
    {
        this.lineName=lineName;
        this.pathName=pathName;
    }

    /**
     * @return
     */
    public String getLineName() {
        return lineName;
    }

    /**
     * @return
     */
    public String getPathName() {
        return pathName;
    }
}