import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class Device {
    private String name;
    private String initialStatus;
    private LocalDateTime onTime = LocalDateTime.of(1,1,1,1,1,1);
    private LocalDateTime offTime = LocalDateTime.of(1,1,1,1,1,1);

    /**
     * Class constructor.
     */
    public Device(){
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
    public void setName(String name){
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
