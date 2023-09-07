import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class SmartCamera extends Device{
    private String name;
    private double megabytesConsumedPerRecord;
    private String initialStatus;
    public static List<SmartCamera> smartCameras = new ArrayList<SmartCamera>();
    private LocalDateTime onTime = LocalDateTime.of(1,1,1,1,1,1); //to initialize
    private LocalDateTime offTime = LocalDateTime.of(1,1,1,1,1,1);
    private double totalMegabytesConsumed = 0;
    /**
     * Class constructor specifying name and megabytes consumed per record.
     */
    public SmartCamera(String name, double megabytesConsumedPerRecord) {
        this.name = name;
        this.megabytesConsumedPerRecord = megabytesConsumedPerRecord;
        initialStatus = "Off";
    }
    /**
     * Class constructor specifying name, megabytes consumed per record and initial status.
     */
    public SmartCamera(String name, double megabytesConsumedPerRecord,String initialStatus) {
        this.name = name;
        this.megabytesConsumedPerRecord = megabytesConsumedPerRecord;
        this.initialStatus = initialStatus;
    }
    /**
     * @return Returns time difference between to local date times in minutes.
     */
    public long getPeriod() {
        if (!onTime.equals(LocalDateTime.of(1, 1, 1, 1, 1, 1)) && !onTime.equals(LocalDateTime.of(1, 1, 1, 1, 1, 1))) {
            return onTime.until(offTime, ChronoUnit.MINUTES);
        }else {
            return 0;
        }
    }
    /**
     * Calculates the total megabytes consumed according to period.
     */
    public void calculateConsumedMegabytes(){
        totalMegabytesConsumed +=megabytesConsumedPerRecord *getPeriod();
        totalMegabytesConsumed = totalMegabytesConsumed*100; // to round to two decimal places
        totalMegabytesConsumed = Math.round(totalMegabytesConsumed);
        totalMegabytesConsumed = totalMegabytesConsumed /100;
    }
    /**
     * @return  Returns switch off time as LocalDateTime.
     */
    public LocalDateTime getOffTime(){
        return offTime;
    }
    /**
     * @return  Returns switch on time as LocalDateTime.
     */
    public LocalDateTime getOnTime(){
        return onTime;
    }
    public double getTotalMegabytesConsumed(){
        return totalMegabytesConsumed;
    }
    /**
     * @return  Returns name as String.
     */
    public String getName(){
        return name;
    }
    /**
     * @return  Returns status as String.
     */
    public String getInitialStatus(){
        return initialStatus;
    }

    /**
     * @return Returns switch on time as a string.
     */
    public String getStringOnTime(){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");
        return onTime.format(dateTimeFormatter);
    }

    /**
     * @return Returns switch off time as a string.
     */
    public String getStringOffTime(){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");
        return offTime.format(dateTimeFormatter);
    }

    /**
     * @return Returns the later time between the switch on time and the switch off time to use in ZReport.
     *         Returns null if they are equal.
     */
    public String getLaterSwitchTime(){
        if (onTime.isAfter(offTime)){
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");
            return onTime.format(dateTimeFormatter);
        }else if (offTime.isAfter(onTime)){
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");
            return offTime.format(dateTimeFormatter);
        }else{
            return "null";
        }
    }
    /**
     * Sets the name.
     * @param name  name of the device
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * Sets the status.
     * @param initialStatus  status of the device
     */
    public void setInitialStatus(String initialStatus) {
        this.initialStatus = initialStatus;
    }
    /**
     * Sets the switch on time of the device.
     * @param onTime  switch on time of the device
     */
    public void setOnTime(LocalDateTime onTime){
        this.onTime = onTime;
    }
    /**
     * Sets the switch off time of the device.
     * @param offTime  switch off time of the device
     */
    public void setOffTime(LocalDateTime offTime){
        this.offTime = offTime;
    }
}
