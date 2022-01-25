package sample;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.function.Consumer;

/**
 * Stores valuable time related functions and variables.
 */
public abstract class DifferentTimes {

    // Find difference between UTC and Local
    private static LocalDateTime differenceUtcLocal = getUtcTime().minusHours(getLocalTime().getHour());


    public static LocalDateTime getUtcTime(){
        return LocalDateTime.now(Clock.systemUTC());
    }

    public static LocalDateTime getEstTime(){
        return LocalDateTime.now(Clock.systemUTC()).minusHours(4);
    }
    public static LocalDateTime getLocalTime(){
        return LocalDateTime.now();
    }

    /**
     * Converts local time to UTC
     * @param input
     * @return UTC time
     */
    public static LocalDateTime toUtc(LocalDateTime input){
        input = input.plusHours(differenceUtcLocal.getHour());

        return input;
    }

    /**
     * Converts UTC time to local time
     * @param input
     * @return local time
     */
    public static LocalDateTime toLocal(LocalDateTime input){
        input = input.minusHours(differenceUtcLocal.getHour());

        return input;
    }

    /**
     * Converts local time to EST time.
     * Used when checking against business hours.
     * @param input
     * @return EST time
     */
    public static LocalTime localToEst(LocalTime input){

        // Add the difference to the input time
        input = input.plusHours(differenceUtcLocal.getHour());

        // Subtract 4 hours to get to EST time
        input = input.minusHours(4);

        return input;
    }


    /**
     * Get EST business hours opening time.
     * @return EST business opening time
     */
    public static LocalTime getEstCompanyOpen(){
        return LocalTime.parse("08:00:00");
    }

    /**
     * Get EST business hours closing time.
     * @return EST business closing time
     */
    public static LocalTime getEstCompanyClose(){
        return LocalTime.parse("22:00:00");
    }



}
