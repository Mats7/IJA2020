/*
 * Authors: 
 * Matej Cimmerman (xcimme00)
 * Daniel Paulovi� (xpaulo04)
 */
package ija_proj;

import java.time.LocalTime;
/**
 * Pomocná trieda pre niektoré funkcie manipulácie s časom
 * @author Matej Cimmerman (xcimme00), Daniel Paulovič (xpaulo04)
 */
public abstract class TimeExtender {
    /**
     * Odčítanie dvoch časových objektov
     * @param diff1 Time 1
     * @param diff2 Time 2
     * @return Count of seconds Time 1 - Time 2
     */
    public static int minusLocalTime(LocalTime diff1, LocalTime diff2) {
        LocalTime diff = diff1.minusHours(diff2.getHour())
                .minusMinutes(diff2.getMinute())
                .minusSeconds(diff2.getSecond());

        return (diff.getHour() * 60 * 60) + (diff.getMinute() * 60) + (diff.getSecond());
    }

    /**
     * Pričítanie času k časovému objektu
     * @param diff1 Povodný čas
     * @param diff2 Pričítaný čas v sekundách
     * @return Result time.
     */
    public static LocalTime plusLocalTime(LocalTime diff1, long diff2) {
        LocalTime diff = diff1.plusSeconds(diff2);

        return diff;
    }

    /**
     * Odčítanie času od povodného času
     * @param diff1 Povodný čas
     * @param diff2 Pričítaný Čas v sekundách
     * @return Result time.
     */
    public static LocalTime minusLocalTime(LocalTime diff1, long diff2) {
        LocalTime diff = diff1.minusSeconds(diff2);

        return diff;
    }
}