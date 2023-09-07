import java.time.LocalDateTime;

public class setTime {
    public static LocalDateTime currentTime;
    /**
     * Sets initial time to entered local date time. Also checks if the time format is correct,
     *
     * @param year              Year of the entered local date time.
     * @param month             Month of the entered local date time. Must be in range of 1 to 12(included).
     * @param day               Day of the entered local date time. Days are identified according to months.
     * @param hour              Hour of the entered local date time. Must be in range of 0 to 23(included).
     * @param minute            Minute of the entered local date time. Must be in range of 0 to 59(included).
     * @param second            Second of the entered local date time. Must be in range of 0 to 59(included).
     */
    public setTime(String year, String month, String day, String hour, String minute, String second){
        try{
            currentTime = LocalDateTime.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day), Integer.parseInt(hour), Integer.parseInt(minute), Integer.parseInt(second));
        }catch (Exception e){
            FileOutput.writeToFile(Main.writePath,"ERROR: Time format is not correct!",true,true);
        }

    }
}
