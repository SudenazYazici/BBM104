import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class controlDevices {
    public static boolean contains;
    public static boolean isKelvinInRange;
    public static boolean isBrightnessInRange;
    public static boolean isKelvinNumeric;
    public static boolean isBrightnessNumeric;
    public static boolean initialStatusBool;
    public static boolean isHexInRange;
    public static boolean isHexNum;

    public static List<Device> checkList = new ArrayList<>();
    public static List<Device> devicesWithTimeList = new ArrayList<>();
    public static List<Device> devicesWithNullTimeList = new ArrayList<>();
    /**
     * Checks the all devices' names to decide if the device already exists.
     * (The names of the devices' are unique.)
     *
     * @param name Name of the device that is going to be checked.
     */
    public void checkNames(String name){
        checkList.addAll(SmartPlug.smartPlugs);
        checkList.addAll(SmartCamera.smartCameras);
        checkList.addAll(SmartLamp.smartLamps);
        checkList.addAll(SmartColorLamp.smartColorLamps);
        for (Device element:checkList){
            if (element.getName().equals(name)){
                FileOutput.writeToFile(Main.writePath,"ERROR: There is already a smart device with same name!",true,true);
                contains = true;
                break;
            }else {
                contains = false;
            }
        }
    }
    /**
     * Checks the kelvin value of the given device to decide if it is numeric or in range.
     *
     * @param kelvinValue Kelvin value of the device that is going to be checked.
     */
    public void checkKelvin(String kelvinValue){
        try{
            int num = Integer.parseInt(kelvinValue);
            isKelvinNumeric = true;
            if (1999 < num && num < 6501){
                isKelvinInRange = true;
            }else {
                FileOutput.writeToFile(Main.writePath,"ERROR: Kelvin value must be in range of 2000K-6500K!",true,true);
                isKelvinInRange = false;
            }
        }catch (NumberFormatException nfe){
            FileOutput.writeToFile(Main.writePath,"ERROR: Erroneous command!",true,true);
            isKelvinNumeric = false;
        }
    }

    /**
     * Checks the brightness of the given device to decide if it is numeric or in range.
     *
     * @param brightness  Brightness of the device that is going to be checked.
     */
    public void checkBrightness(String brightness){
        try{
            int num = Integer.parseInt(brightness);
            isBrightnessNumeric = true;
            if (-1 < num && num < 101){
                isBrightnessInRange = true;
            }else {
                FileOutput.writeToFile(Main.writePath,"ERROR: Brightness must be in range of 0%-100%!",true,true);
                isBrightnessInRange= false;
            }
        }catch (NumberFormatException nfe){
            FileOutput.writeToFile(Main.writePath,"ERROR: Erroneous command!",true,true);
            isBrightnessNumeric = false;
        }
    }
    /**
     * Checks the status of the given device to see if it is "On" or "Off".
     * If it is not one of them, the boolean value becomes false.
     *
     * @param initialStatus  Status of the device that is going to be checked.
     */
    public void checkInitialStatus(String initialStatus){
        if (initialStatus.equals("On") || initialStatus.equals("Off")){
            initialStatusBool = true;
        }else{
            FileOutput.writeToFile(Main.writePath,"ERROR: Erroneous command!",true,true);
            initialStatusBool = false;
        }
    }
    /**
     * Checks the hexadecimal value of the device to decide if it is hexadecimal number or in range.
     *
     * @param str  Hexadecimal value of the device that is going to be checked.
     */
    public void checkHexadecimal(String str){
        str = str.substring(2);
        while (str.charAt(0) == '0'){
            str = str.substring(1);
            if (str.equals("")){
                isHexNum = false;
                isHexInRange = false;
                break;
            }
        }
        char[] characters =str.toCharArray();
        for (char c:characters){
            if (c != '0'&& c !='1'&& c !='2'&& c !='3'&& c !='4'&& c !='5'&& c !='6'&& c !='7'&& c !='8'&& c !='9'&&
                    c !='A'&& c !='B'&& c !='C'&& c !='D'&& c !='E'&& c !='F'){
                isHexNum = false;
                break;
            }else {
                if (!(str.length() <=6)){
                    isHexNum = true;
                    isHexInRange = false;
                    break;
                }else {
                    isHexNum = true;
                    isHexInRange = true;
                }
            }

        }
    }
    /**
     * Checks and changes the status' of the devices according to their switch times if necessary.
     */
    public void changeInitialStatus(){
        for(SmartPlug plug:SmartPlug.smartPlugs){
            if (plug.getLaterSwitchTime().equals(plug.getStringOnTime()) && !plug.getOnTime().isAfter(setTime.currentTime)){
                plug.setInitialStatus("On");
            }else if (plug.getLaterSwitchTime().equals(plug.getStringOffTime()) && !plug.getOffTime().isAfter(setTime.currentTime)){
                plug.setInitialStatus("Off");
            }
        }
        for(SmartCamera camera:SmartCamera.smartCameras){
            if (camera.getLaterSwitchTime().equals(camera.getStringOnTime()) && !camera.getOnTime().isAfter(setTime.currentTime)){
                camera.setInitialStatus("On");
            }else if (camera.getLaterSwitchTime().equals(camera.getStringOffTime()) && !camera.getOffTime().isAfter(setTime.currentTime)){
                camera.setInitialStatus("Off");
            }
        }
        for(SmartLamp lamp:SmartLamp.smartLamps){
            if (lamp.getLaterSwitchTime().equals(lamp.getStringOnTime()) && !lamp.getOnTime().isAfter(setTime.currentTime)){
                lamp.setInitialStatus("On");
            }else if (lamp.getLaterSwitchTime().equals(lamp.getStringOffTime()) && !lamp.getOffTime().isAfter(setTime.currentTime)){
                lamp.setInitialStatus("Off");
            }
        }
        for(SmartColorLamp colorLamp:SmartColorLamp.smartColorLamps){
            if (colorLamp.getLaterSwitchTime().equals(colorLamp.getStringOnTime()) && !colorLamp.getOnTime().isAfter(setTime.currentTime)){
                colorLamp.setInitialStatus("On");
            }else if (colorLamp.getLaterSwitchTime().equals(colorLamp.getStringOffTime()) && !colorLamp.getOffTime().isAfter(setTime.currentTime)){
                colorLamp.setInitialStatus("Off");
            }
        }
    }
    /**
     * Puts all the ZReport information together and prints it to the output file.
     */
    public void printZReport(){
        devicesWithNullTimeList.clear();
        devicesWithTimeList.clear();
        for (SmartPlug plug:SmartPlug.smartPlugs){
            if (plug.getLaterSwitchTime().equals("null")){
                devicesWithNullTimeList.add(plug);
            } else {
                devicesWithTimeList.add(plug);
            }
        }
        for (SmartCamera camera:SmartCamera.smartCameras){
            if (camera.getLaterSwitchTime().equals("null")){
                devicesWithNullTimeList.add(camera);
            } else {
                devicesWithTimeList.add(camera);
            }
        }
        for (SmartLamp lamp:SmartLamp.smartLamps){
            if (lamp.getLaterSwitchTime().equals("null")){
                devicesWithNullTimeList.add(lamp);
            } else {
                devicesWithTimeList.add(lamp);
            }
        }
        for (SmartColorLamp colorLamp:SmartColorLamp.smartColorLamps){
            if (colorLamp.getLaterSwitchTime().equals("null")){
                devicesWithNullTimeList.add(colorLamp);
            } else {
                devicesWithTimeList.add(colorLamp);
            }
        }
        devicesWithTimeList.sort(Comparator.comparing(Device::getLaterSwitchTime));
        for (Device device:devicesWithTimeList){
            for (SmartPlug plug:SmartPlug.smartPlugs){
                if (device.getName().equals(plug.getName())){
                    String[] tempStr = plug.getLaterSwitchTime().split("_|-|:");
                    LocalDateTime tempTime = LocalDateTime.of(Integer.parseInt(tempStr[0]),Integer.parseInt(tempStr[1]),
                            Integer.parseInt(tempStr[2]),Integer.parseInt(tempStr[3]),Integer.parseInt(tempStr[4]),Integer.parseInt(tempStr[5]));
                    if (!tempTime.isAfter(setTime.currentTime)) {
                        if (plug.getOnTime().isAfter(plug.getOffTime())) {
                            if (plug.getOnTime().isBefore(setTime.currentTime) | plug.getOnTime().equals(setTime.currentTime)) {
                                plug.setInitialStatus("On");
                            }
                        } else if (plug.getOffTime().isAfter(plug.getOnTime())) {
                            if (plug.getOffTime().isBefore(setTime.currentTime) | plug.getOffTime().equals(setTime.currentTime)) {
                                plug.setInitialStatus("Off");
                            }
                        }
                        FileOutput.writeToFile(Main.writePath,"Smart Plug " +plug.getName()+" is "+plug.getInitialStatus().toLowerCase()+
                                " and consumed "+plug.getTotalEnergy()+"W so far (excluding current device), and its time to switch its status is null.",true, true);
                        continue;

                    }
                    FileOutput.writeToFile(Main.writePath,"Smart Plug " +plug.getName()+" is "+plug.getInitialStatus().toLowerCase()+
                            " and consumed "+plug.getTotalEnergy()+"W so far (excluding current device), and its time to switch its status is "
                            +plug.getLaterSwitchTime() +".",true, true);

                }
            }
            for (SmartCamera camera:SmartCamera.smartCameras){
                if (device.getName().equals(camera.getName())){
                    String[] tempStr = camera.getLaterSwitchTime().split("_|-|:");
                    LocalDateTime tempTime = LocalDateTime.of(Integer.parseInt(tempStr[0]),Integer.parseInt(tempStr[1]),
                            Integer.parseInt(tempStr[2]),Integer.parseInt(tempStr[3]),Integer.parseInt(tempStr[4]),Integer.parseInt(tempStr[5]));
                    if (!tempTime.isAfter(setTime.currentTime)){
                        if (camera.getOnTime().isAfter(camera.getOffTime())) {
                            if (camera.getOnTime().isBefore(setTime.currentTime) | camera.getOnTime().equals(setTime.currentTime)) {
                                camera.setInitialStatus("On");
                            }
                        } else if (camera.getOffTime().isAfter(camera.getOnTime())) {
                            if (camera.getOffTime().isBefore(setTime.currentTime) | camera.getOffTime().equals(setTime.currentTime)){
                                camera.setInitialStatus("Off");
                            }
                        }

                        FileOutput.writeToFile(Main.writePath,"Smart Camera " +camera.getName()+" is "+
                                camera.getInitialStatus().toLowerCase()+" and used "+camera.getTotalMegabytesConsumed()+
                                " MB of storage so far (excluding current status), and its time to switch its status is null.",true, true);
                        continue;
                    }
                    FileOutput.writeToFile(Main.writePath,"Smart Camera " +camera.getName()+" is "+
                            camera.getInitialStatus().toLowerCase()+" and used "+camera.getTotalMegabytesConsumed()+
                            " MB of storage so far (excluding current status), and its time to switch its status is "+
                            camera.getLaterSwitchTime() +".",true, true);

                }
            }
            for (SmartLamp lamp:SmartLamp.smartLamps){
                if (device.getName().equals(lamp.getName())){
                    String[] tempStr = lamp.getLaterSwitchTime().split("_|-|:");
                    LocalDateTime tempTime = LocalDateTime.of(Integer.parseInt(tempStr[0]),Integer.parseInt(tempStr[1]),
                            Integer.parseInt(tempStr[2]),Integer.parseInt(tempStr[3]),Integer.parseInt(tempStr[4]),Integer.parseInt(tempStr[5]));
                    if (!tempTime.isAfter(setTime.currentTime)) {
                        if (lamp.getOnTime().isAfter(lamp.getOffTime())) {
                            if (lamp.getOnTime().isBefore(setTime.currentTime) | lamp.getOnTime().equals(setTime.currentTime)) {
                                lamp.setInitialStatus("On");
                            }
                        } else if (lamp.getOffTime().isAfter(lamp.getOnTime())) {
                            if (lamp.getOffTime().isBefore(setTime.currentTime) | lamp.getOffTime().equals(setTime.currentTime)) {
                                lamp.setInitialStatus("Off");
                            }
                        }
                        FileOutput.writeToFile(Main.writePath,"Smart Lamp "+lamp.getName()+" is "+
                                lamp.getInitialStatus().toLowerCase()+" and its kelvin value is "+lamp.getKelvinValue()+
                                "K with "+lamp.getBrightness()+"% brightness, and its time to switch its status is null.",true, true);
                        continue;

                    }
                    FileOutput.writeToFile(Main.writePath,"Smart Lamp "+lamp.getName()+" is "+
                            lamp.getInitialStatus().toLowerCase()+" and its kelvin value is "+lamp.getKelvinValue()+"K with "+
                            lamp.getBrightness()+"% brightness, and its time to switch its status is "+lamp.getLaterSwitchTime()+".",true, true);
                }
            }
            for (SmartColorLamp colorLamp:SmartColorLamp.smartColorLamps){
                if (device.getName().equals(colorLamp.getName())){
                    String[] tempStr = colorLamp.getLaterSwitchTime().split("_|-|:");
                    LocalDateTime tempTime = LocalDateTime.of(Integer.parseInt(tempStr[0]),Integer.parseInt(tempStr[1]),
                            Integer.parseInt(tempStr[2]),Integer.parseInt(tempStr[3]),Integer.parseInt(tempStr[4]),Integer.parseInt(tempStr[5]));
                    if (!tempTime.isAfter(setTime.currentTime)) {
                        if (colorLamp.getOnTime().isAfter(colorLamp.getOffTime())) {
                            if (colorLamp.getOnTime().isBefore(setTime.currentTime) | colorLamp.getOnTime().equals(setTime.currentTime)) {
                                colorLamp.setInitialStatus("On");
                            }
                        } else if (colorLamp.getOffTime().isAfter(colorLamp.getOnTime())) {
                            if (colorLamp.getOffTime().isBefore(setTime.currentTime) | colorLamp.getOffTime().equals(setTime.currentTime)) {
                                colorLamp.setInitialStatus("Off");
                            }
                        }
                        if (colorLamp.getIsColorModeOn()){
                            FileOutput.writeToFile(Main.writePath,"Smart Color Lamp "+colorLamp.getName()+" is "+
                                    colorLamp.getInitialStatus().toLowerCase()+" and its color value is "+colorLamp.getColorCode()+
                                    " with "+colorLamp.getBrightness()+"% brightness, and its time to switch its status is null.",true, true);
                            continue;
                        }else{
                            FileOutput.writeToFile(Main.writePath,"Smart Color Lamp "+colorLamp.getName()+" is "+
                                    colorLamp.getInitialStatus().toLowerCase()+" and its color value is "+colorLamp.getKelvinValue()+
                                    "K with "+colorLamp.getBrightness()+"% brightness, and its time to switch its status is null.",true, true);
                            continue;
                        }

                    }
                    if (colorLamp.getIsColorModeOn()){
                        FileOutput.writeToFile(Main.writePath,"Smart Color Lamp "+colorLamp.getName()+" is "+
                                colorLamp.getInitialStatus().toLowerCase()+" and its color value is "+colorLamp.getColorCode()+
                                " with "+colorLamp.getBrightness()+"% brightness, and its time to switch its status is "
                                +colorLamp.getLaterSwitchTime()+".",true, true);
                    }else{
                        FileOutput.writeToFile(Main.writePath,"Smart Color Lamp "+colorLamp.getName()+" is "+
                                colorLamp.getInitialStatus().toLowerCase()+" and its color value is "+colorLamp.getKelvinValue()+
                                "K with "+colorLamp.getBrightness()+"% brightness, and its time to switch its status is "
                                +colorLamp.getLaterSwitchTime()+".",true, true);
                    }

                }
            }

        }
        for (Device device:devicesWithNullTimeList){
            for (SmartPlug plug:SmartPlug.smartPlugs){
                if (device.getName().equals(plug.getName())){
                    FileOutput.writeToFile(Main.writePath,"Smart Plug " +plug.getName()+" is "+
                            plug.getInitialStatus().toLowerCase()+" and consumed "+plug.getTotalEnergy()+
                            "W so far (excluding current device), and its time to switch its status is null.",true, true);
                }
            }
            for (SmartCamera camera:SmartCamera.smartCameras){
                if (device.getName().equals(camera.getName())){
                    FileOutput.writeToFile(Main.writePath,"Smart Camera " +camera.getName()+" is "+
                            camera.getInitialStatus().toLowerCase()+" and used "+camera.getTotalMegabytesConsumed()+
                            " MB of storage so far (excluding current status), and its time to switch its status is null.",true, true);

                }
            }
            for (SmartLamp lamp:SmartLamp.smartLamps){
                if (device.getName().equals(lamp.getName())){
                    FileOutput.writeToFile(Main.writePath,"Smart Lamp "+lamp.getName()+" is "+
                            lamp.getInitialStatus().toLowerCase()+" and its kelvin value is "+lamp.getKelvinValue()+
                            "K with "+lamp.getBrightness()+"% brightness, and its time to switch its status is null.",true, true);
                }
            }
            for (SmartColorLamp colorLamp:SmartColorLamp.smartColorLamps){
                if (device.getName().equals(colorLamp.getName())){
                    if (colorLamp.getIsColorModeOn()){
                        FileOutput.writeToFile(Main.writePath,"Smart Color Lamp "+colorLamp.getName()+" is "+
                                colorLamp.getInitialStatus().toLowerCase()+" and its color value is "+colorLamp.getColorCode()+
                                " with "+colorLamp.getBrightness()+"% brightness, and its time to switch its status is null.",true, true);
                    }else{
                        FileOutput.writeToFile(Main.writePath,"Smart Color Lamp "+colorLamp.getName()+" is "+
                                colorLamp.getInitialStatus().toLowerCase()+" and its color value is "+colorLamp.getKelvinValue()
                                +"K with "+colorLamp.getBrightness()+"% brightness, and its time to switch its status is null.",true, true);
                    }
                }
            }
        }
    }

}