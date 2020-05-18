/*
 * Authors: 
 * Matej Cimmerman (xcimme00)
 * Daniel Paulovi� (xpaulo04)
 */
package ija_proj;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Matej Cimmerman (xcimme00), Daniel Paulovič (xpaulo04)
 */
public abstract class TripSimulation {
    /**
     * Vypočítava aktuálnu polohu vozidla podľa času
     * @param nowTime Čas (teraz)
     * @param startTime Čas začiatočnej stopky
     * @param endTime čas konečnej stopky
     * @param startStop zač. stopka
     * @param endStop kon. stopka
     * @param line čiara, po ktorej sa vozidlo musí pohybovať
     * @return Vráti aktuálnu polohu pre vykreslenie
     */
    public static Coordinate dotPosition(LocalTime nowTime, LocalTime startTime, LocalTime endTime, Stop startStop, Stop endStop, Line line) {


        Coordinate returnC = null;
        try {
            returnC = (Coordinate) startStop.getCoordinate().clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        Coordinate follow;
        List<Street> Streets = new ArrayList<>();

        LocalTime pathTime = endTime.minusHours(startTime.getHour())
                .minusMinutes(startTime.getMinute())
                .minusSeconds(startTime.getSecond());

        LocalTime pathTimeNow = nowTime.minusHours(startTime.getHour())
                .minusMinutes(startTime.getMinute())
                .minusSeconds(startTime.getSecond());

        int secsNow = (pathTimeNow.getHour() * 60 * 60) + (pathTimeNow.getMinute() * 60) + (pathTimeNow.getSecond());
        int secs = (pathTime.getHour() * 60 * 60) + (pathTime.getMinute() * 60) + (pathTime.getSecond());

        float actualPercent = (secsNow * 100.0f) / secs;

        double lineLenght = line.getStopsLength(startStop, endStop);

        double driven = (actualPercent / 100.0) * lineLenght;

        Streets = line.getStreetsBetween(startStop, endStop);


        for (int i = 0; i < Streets.size(); i++) {
            if (driven == 0) {
                break;
            }
            if (i == Streets.size()-1){
                follow = endStop.getCoordinate();
            }
            else
            {
                follow = line.followPoint(Streets.get(i), Streets.get(i + 1));
            }

            if(line.changeX(Streets.get(i))) {
                if((Math.abs(follow.getX() - returnC.getX())) <= driven) {
                    driven -= ((Math.abs(follow.getX() - returnC.getX())));
                    if(line.plusX(returnC, follow)) {
                        returnC.setX(returnC.getX() + (Math.abs(follow.getX() - returnC.getX())));
                    } else{
                        returnC.setX(returnC.getX() - (Math.abs(follow.getX() - returnC.getX())));
                    }
                    driven -= ((Math.abs(follow.getX() - returnC.getX())));
                } else{
                    if(line.plusX(returnC, follow)) { //kontrola smeru po X ose
                        returnC.setX(returnC.getX() + driven);
                    } else{
                        returnC.setX(returnC.getX() - driven);
                    }
                    driven = 0;
                }
            } else{
                if((Math.abs(follow.getY() - returnC.getY())) <= driven){ //kontrola či sa bod nachádza na aktuálnej ulici
                    driven -= ((Math.abs(follow.getY() - returnC.getY())));
                    if (line.plusY(returnC, follow)){ //kontrola smeru po Y ose
                        returnC.setY(returnC.getY() + (Math.abs(follow.getY() - returnC.getY())));
                    } else {
                        returnC.setY(returnC.getY() - (Math.abs(follow.getY() - returnC.getY())));
                    }
                    driven -= ((Math.abs(follow.getY() - returnC.getY())));
                } else {
                    if(line.plusY(returnC, follow)) { //kontrola smeru po Y ose
                        returnC.setY(returnC.getY() + driven);
                    } else{
                        returnC.setY(returnC.getY() - driven);
                    }
                    driven = 0;
                }
            }
        }
        return returnC;

    }
}