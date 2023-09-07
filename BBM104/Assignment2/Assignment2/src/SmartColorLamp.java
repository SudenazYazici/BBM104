import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SmartColorLamp extends Device{
    private String name;
    private String initialStatus;
    private int kelvinValue;
    private String colorCode;
    private int brightness;
    public static List<SmartColorLamp> smartColorLamps = new ArrayList<SmartColorLamp>();
    private LocalDateTime onTime = LocalDateTime.of(1,1,1,1,1,1);
    private LocalDateTime offTime = LocalDateTime.of(1,1,1,1,1,1);
    private boolean isColorModeOn;
    /**
     * Class constructor specifying name.
     */
    public SmartColorLamp(String name) {
        this.name = name;
        initialStatus = "Off";
        kelvinValue = 4000;
        brightness = 100;
        isColorModeOn = false;
    }

    /**
     * Class constructor specifying name and initial status.
     */
    public SmartColorLamp(String name,String initialStatus) {
        this.name = name;
        this.initialStatus = initialStatus;
        kelvinValue = 4000;
        brightness = 100;
        isColorModeOn = false;
    }

    /**
     * Class constructor specifying name, initial status, kelvin and brightness.
     */
    public SmartColorLamp(String name,String initialStatus, int kelvinValue, int brightness) {
        this.name = name;
        this.initialStatus = initialStatus;
        this.kelvinValue = kelvinValue;
        this.brightness = brightness;
        isColorModeOn = false;
    }

    /**
     * Class constructor specifying name, initial status, color code and brightness.
     */
    public SmartColorLamp(String name,String initialStatus, String colorCode, int brightness) {
        this.name = name;
        this.initialStatus = initialStatus;
        this.colorCode = colorCode;
        this.brightness = brightness;
        kelvinValue =4000;
        isColorModeOn = true;
    }

    /**
     * @return  Returns the later time between the switch on time and the switch off time to use in ZReport.
     *          Returns null if they are equal.
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
     * @return  Returns kelvin value of the device as integer. Must be in range of 2000-6500.
     */
    public int getKelvinValue(){
        return kelvinValue;
    }
    /**
     * @return  Returns color code of the device as String. After converting to hexadecimal, must be in range of 0x000000-0xFFFFFF.
     */
    public String getColorCode(){
        return colorCode;
    }
    /**
     * @return  Returns brightness of the device as integer. Must be in range of 0-100.
     */
    public int getBrightness(){
        return brightness;
    }
    /**
     * @return  Returns if the color mode is on or not as boolean.
     */
    public boolean getIsColorModeOn(){
        return isColorModeOn;
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
     * @return  Returns switch on time as a string.
     */
    public String getStringOnTime(){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");
        return onTime.format(dateTimeFormatter);
    }

    /**
     * @return  Returns switch off time as a string.
     */
    public String getStringOffTime(){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");
        return offTime.format(dateTimeFormatter);
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
     * Sets the kelvin value.
     * @param kelvinValue  kelvin value of the device. Must be in range of 2000-6500.
     */
    public void setKelvinValue(int kelvinValue) {
        this.kelvinValue = kelvinValue;
    }
    /**
     * Sets the color code.
     * @param colorCode  color code of the device. After converting to hexadecimal, must be in range of 0x000000-0xFFFFFF.
     */
    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }
    /**
     * Sets the brightness.
     * @param brightness  brightness of the device. Must be in range of 0-100.
     */
    public void setBrightness(int brightness) {
        this.brightness = brightness;
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
     * Sets if the color mode is on or not.
     * @param isColorModeOn  boolean of the action of changing into color mode
     */
    public void setColorModeOn(boolean isColorModeOn){
        this.isColorModeOn = isColorModeOn;
    }
}
