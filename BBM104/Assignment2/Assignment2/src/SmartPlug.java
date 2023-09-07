import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SmartPlug extends Device{
    private String name;
    private String initialStatus;
    private double ampere;
    private double totalEnergy = 0;
    public static List<SmartPlug> pluggedIns = new ArrayList<SmartPlug>();
    public static List<SmartPlug> smartPlugs = new ArrayList<SmartPlug>();

    private LocalDateTime onTime = LocalDateTime.of(1,1,1,1,1,1);
    private LocalDateTime offTime = LocalDateTime.of(1,1,1,1,1,1);
    private LocalDateTime plugInTime = LocalDateTime.of(1,1,1,1,1,1);
    private LocalDateTime plugOffTime = LocalDateTime.of(1,1,1,1,1,1);
    private boolean isPluggedIn;
    /**
     * Class constructor specifying name.
     */
    public SmartPlug(String name) {
        this.name = name;
        initialStatus = "Off";
        isPluggedIn = false;
    }
    /**
     * Class constructor specifying name and the initial status.
     */
    public SmartPlug(String name, String initialStatus) {
        this.name = name;
        this.initialStatus = initialStatus;
        isPluggedIn = false;
    }
    /**
     * Class constructor specifying name, initial status and the ampere value.
     */
    public SmartPlug(String name,String initialStatus, double ampere) {
        this.name = name;
        this.initialStatus = initialStatus;
        this.ampere = ampere;
        plugInTime = setTime.currentTime;
        isPluggedIn = true;
    }
    /**
     * @return Returns time difference between to local date times in minutes.
     */
    public long getPeriod() {
        List<LocalDateTime> dateTimeList = Arrays.asList(onTime,offTime,plugInTime,plugOffTime);
        Collections.sort(dateTimeList);
        LocalDateTime start = dateTimeList.get(1);
        LocalDateTime end = dateTimeList.get(dateTimeList.size()-2);
        return start.until(end, ChronoUnit.MINUTES);
    }
    /**
     * Calculates the total energy according to ampere and period.
     */
    public void calculateEnergy(){
        totalEnergy += 220 * ampere * getPeriod()/60;
        totalEnergy = totalEnergy*100; // to round to two decimal places
        totalEnergy = Math.round(totalEnergy);
        totalEnergy = totalEnergy /100;
    }
    /**
     * @return  Returns total energy calculated.
     */
    public double getTotalEnergy(){
        return totalEnergy;
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
    /**
     * @return  Returns true if there is an ampere plugged in, otherwise returns false.
     */
    public boolean getIsPluggedIn(){
        return isPluggedIn;
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
     * Sets the ampere value.
     * @param ampere  plugged in ampere value
     */
    public void setAmpere(double ampere) {
        this.ampere = ampere;
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
    /**
     * Sets the time of plugging in an ampere.
     * @param plugInTime  the plugging in time of ampere
     */
    public void setPlugInTime(LocalDateTime plugInTime){
        this.plugInTime = plugInTime;
    }
    /**
     * Sets the time of plugging out an ampere.
     * @param plugOffTime  the plugging in time of ampere
     */
    public void setPlugOffTime(LocalDateTime plugOffTime){
        this.plugOffTime = plugOffTime;
    }
    /**
     * Sets if there is an ampere plugged in or not.
     * @param isPluggedIn  boolean of the action of pluggign in
     */
    public void setIsPluggedIn(boolean isPluggedIn){
        this.isPluggedIn = isPluggedIn;
    }
}