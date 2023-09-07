import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
    public static String writePath;
    public static LocalDateTime initialTime;
    public static boolean isDeviceInList = false;
    public static boolean isPlugInList = false;
    public static boolean isLampInList = false;
    public static boolean isColorLampInList = false;
    private static List<LocalDateTime> timeList = new ArrayList<LocalDateTime>();
    public static void main(String[] args) {
        writePath = args[1];
        FileOutput.writeToFile(writePath,"",false,false); // to initialize empty output file
        FileInput.readFile(args[0], true, true);
        if(!FileInput.lines.get(0).startsWith("SetInitialTime\t")){
            FileOutput.writeToFile(writePath,"COMMAND: "+FileInput.lines.get(0),true, true);
            FileOutput.writeToFile(writePath,"ERROR: First command must be set initial time! Program is going to terminate!",true,true);
            System.exit(0);
        }else{
            try{
                String[] temp = FileInput.lines.get(0).split("\t|_|-|:");
                initialTime = LocalDateTime.of(Integer.parseInt(temp[1]), Integer.parseInt(temp[2]), Integer.parseInt(temp[3]),
                        Integer.parseInt(temp[4]), Integer.parseInt(temp[5]), Integer.parseInt(temp[6]));
                setTime.currentTime = initialTime;
                FileOutput.writeToFile(writePath,"COMMAND: "+FileInput.lines.get(0),true, true);
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");
                FileOutput.writeToFile(writePath,"SUCCESS: Time has been set to "+initialTime.format(dateTimeFormatter)
                        +"!",true,true);
            }catch (Exception e){
                FileOutput.writeToFile(writePath,"COMMAND: "+FileInput.lines.get(0),true, true);
                FileOutput.writeToFile(writePath,"ERROR: First command must be set initial time! Program is going to terminate!",true,true);
                System.exit(0);
            }
        }
        for (int i=1;i<FileInput.lines.size();i++){ // reading every line of input file

            for (SmartPlug plug:SmartPlug.smartPlugs){
                if (plug.getIsPluggedIn() && plug.getInitialStatus().equals("On") && !plug.getStringOnTime().equals("0001-01-01_01:01:01")
                        && !plug.getStringOffTime().equals("0001-01-01_01:01:01")){
                    plug.calculateEnergy();
                }
            }
            for (SmartCamera camera:SmartCamera.smartCameras){
                if (camera.getInitialStatus().equals("On") && camera.getPeriod()>=0  && !camera.getStringOnTime().equals("0001-01-01_01:01:01")
                        && !camera.getStringOffTime().equals("0001-01-01_01:01:01")){
                    camera.calculateConsumedMegabytes();
                }
            }
            controlDevices temporary = new controlDevices();
            temporary.changeInitialStatus(); // checking and changing status' of devices if necessary
            if (FileInput.lines.get(i).startsWith("SetTime\t")){
                String[] temp = FileInput.lines.get(i).split("\t|_|-|:");
                FileOutput.writeToFile(writePath,"COMMAND: "+FileInput.lines.get(i),true, true);
                try{
                    if (setTime.currentTime.isAfter(LocalDateTime.of(Integer.parseInt(temp[1]), Integer.parseInt(temp[2]),
                            Integer.parseInt(temp[3]), Integer.parseInt(temp[4]), Integer.parseInt(temp[5]), Integer.parseInt(temp[6])))){
                        FileOutput.writeToFile(writePath,"ERROR: Time cannot be reversed!",true, true);
                        continue;
                    } else if (setTime.currentTime.equals(LocalDateTime.of(Integer.parseInt(temp[1]), Integer.parseInt(temp[2]),
                            Integer.parseInt(temp[3]), Integer.parseInt(temp[4]), Integer.parseInt(temp[5]), Integer.parseInt(temp[6])))) {
                        FileOutput.writeToFile(writePath,"ERROR: There is nothing to change!",true, true);
                        continue;
                    }
                    new setTime(temp[1], temp[2],temp[3],temp[4],temp[5],temp[6]);
                    controlDevices control = new controlDevices();
                    control.changeInitialStatus();
                }catch (java.time.DateTimeException dte){
                    FileOutput.writeToFile(Main.writePath,"ERROR: Time format is not correct!",true,true);
                }

            }else if (FileInput.lines.get(i).startsWith("Add\t")){
                controlDevices.checkList.clear();
                String[] temp = FileInput.lines.get(i).split("\t");
                FileOutput.writeToFile(writePath,"COMMAND: "+FileInput.lines.get(i),true, true);
                controlDevices control = new controlDevices();
                control.checkNames(temp[2]);
                if(controlDevices.contains){
                    continue;
                }
                switch (temp[1]) {
                    case "SmartPlug":
                        if (temp.length == 3) {
                            SmartPlug.smartPlugs.add(new SmartPlug(temp[2]));
                        } else if (temp.length == 4) {
                            control.checkInitialStatus(temp[3]);
                            if (!controlDevices.initialStatusBool) {
                                continue;
                            }
                            SmartPlug.smartPlugs.add(new SmartPlug(temp[2], temp[3]));
                        } else if (temp.length == 5) {
                            if (!(Double.parseDouble(temp[4]) > 0)) {
                                FileOutput.writeToFile(writePath, "ERROR: Ampere value must be a positive number!", true, true);
                                continue;
                            }
                            control.checkInitialStatus(temp[3]);
                            if (!controlDevices.initialStatusBool) {
                                continue;
                            }

                            SmartPlug.smartPlugs.add(new SmartPlug(temp[2], temp[3], Double.parseDouble(temp[4])));
                            SmartPlug.smartPlugs.get(SmartPlug.smartPlugs.size()-1).setAmpere(Double.parseDouble(temp[4]));
                            SmartPlug.pluggedIns.add(SmartPlug.smartPlugs.get(SmartPlug.smartPlugs.size()-1));
                            isPlugInList = true;
                            SmartPlug.smartPlugs.get(SmartPlug.smartPlugs.size()-1).setIsPluggedIn(true);
                            SmartPlug.smartPlugs.get(SmartPlug.smartPlugs.size()-1).setPlugInTime(setTime.currentTime);
                            if (SmartPlug.smartPlugs.get(SmartPlug.smartPlugs.size()-1).getInitialStatus().equals("On")){
                                SmartPlug.smartPlugs.get(SmartPlug.smartPlugs.size()-1).setOnTime(setTime.currentTime);
                            }
                        } else {
                            FileOutput.writeToFile(writePath, "ERROR: Erroneous command!", true, true);
                            continue;
                        }
                        break;
                    case "SmartCamera":
                        if (Double.parseDouble(temp[3]) <= 0) {
                            FileOutput.writeToFile(writePath, "ERROR: Megabyte value must be a positive number!", true, true);
                            continue;
                        }
                        if (temp.length == 4) {
                            SmartCamera.smartCameras.add(new SmartCamera(temp[2], Double.parseDouble(temp[3])));
                        } else if (temp.length == 5) {
                            control.checkInitialStatus(temp[4]);
                            if (!controlDevices.initialStatusBool) {
                                continue;
                            }
                            SmartCamera.smartCameras.add(new SmartCamera(temp[2], Double.parseDouble(temp[3]), temp[4]));
                            if (temp[4].equals("On")){
                                SmartCamera.smartCameras.get(SmartCamera.smartCameras.size()-1).setOnTime(setTime.currentTime);
                            }
                        } else {
                            FileOutput.writeToFile(writePath, "ERROR: Erroneous command!", true, true);
                        }

                        break;
                    case "SmartLamp":
                        if (temp.length == 3) {
                            SmartLamp.smartLamps.add(new SmartLamp(temp[2]));
                        } else if (temp.length == 4) {
                            control.checkInitialStatus(temp[3]);
                            if (!controlDevices.initialStatusBool) {
                                continue;
                            }
                            SmartLamp.smartLamps.add(new SmartLamp(temp[2], temp[3]));
                        } else if (temp.length == 6) {
                            control.checkInitialStatus(temp[3]);
                            if (!controlDevices.initialStatusBool) {
                                continue;
                            }
                            control.checkKelvin(temp[4]);
                            if (!controlDevices.isKelvinNumeric) {
                                continue;
                            }
                            if (!controlDevices.isKelvinInRange) {
                                continue;
                            }
                            control.checkBrightness(temp[5]);
                            if (!controlDevices.isBrightnessNumeric) {
                                continue;
                            }
                            if (!controlDevices.isBrightnessInRange) {
                                continue;
                            }
                            SmartLamp.smartLamps.add(new SmartLamp(temp[2], temp[3], Integer.parseInt(temp[4]), Integer.parseInt(temp[5])));
                        } else {
                            FileOutput.writeToFile(writePath, "ERROR: Erroneous command!", true, true);
                        }
                        break;
                    case "SmartColorLamp":
                        if (temp.length == 3) {
                            SmartColorLamp.smartColorLamps.add(new SmartColorLamp(temp[2]));
                        } else if (temp.length == 4) {
                            control.checkInitialStatus(temp[3]);
                            if (!controlDevices.initialStatusBool) {
                                continue;
                            }
                            SmartColorLamp.smartColorLamps.add(new SmartColorLamp(temp[2], temp[3]));
                        } else if (temp.length == 6 && temp[4].startsWith("0x")) {
                            control.checkInitialStatus(temp[3]);
                            if (!controlDevices.initialStatusBool) {
                                continue;
                            }
                            control.checkHexadecimal(temp[4]);
                            if (!controlDevices.isHexNum) {
                                FileOutput.writeToFile(Main.writePath, "ERROR: Erroneous command!", true, true);
                                continue;
                            }
                            if (!controlDevices.isHexInRange) {
                                FileOutput.writeToFile(Main.writePath, "ERROR: Color code value must be in range of 0x0-0xFFFFFF!"
                                        , true, true);
                                continue;
                            }
                            control.checkBrightness(temp[5]);
                            if (!controlDevices.isBrightnessNumeric) {
                                continue;
                            }
                            if (!controlDevices.isBrightnessInRange) {
                                continue;
                            }
                            SmartColorLamp.smartColorLamps.add(new SmartColorLamp(temp[2], temp[3], temp[4], Integer.parseInt(temp[5])));
                        } else if (temp.length == 6 && !temp[4].startsWith("0x")) {
                            control.checkInitialStatus(temp[3]);
                            if (!controlDevices.initialStatusBool) {
                                continue;
                            }
                            control.checkKelvin(temp[4]);
                            if (!controlDevices.isKelvinNumeric) {
                                continue;
                            }
                            if (!controlDevices.isKelvinInRange) {
                                continue;
                            }
                            control.checkBrightness(temp[5]);
                            if (!controlDevices.isBrightnessNumeric) {
                                continue;
                            }
                            if (!controlDevices.isBrightnessInRange) {
                                continue;
                            }
                            SmartColorLamp.smartColorLamps.add(new SmartColorLamp(temp[2], temp[3], Integer.parseInt(temp[4])
                                    , Integer.parseInt(temp[5])));
                        } else {
                            FileOutput.writeToFile(writePath, "ERROR: Erroneous command!", true, true);
                        }
                        break;
                }
                controlDevices.checkList.clear(); // clearing to avoid duplication
                controlDevices.checkList.addAll(SmartPlug.smartPlugs);
                controlDevices.checkList.addAll(SmartCamera.smartCameras);
                controlDevices.checkList.addAll(SmartLamp.smartLamps);
                controlDevices.checkList.addAll(SmartColorLamp.smartColorLamps);

            } else if (FileInput.lines.get(i).startsWith("ChangeName\t")) {
                String[] temp = FileInput.lines.get(i).split("\t");
                FileOutput.writeToFile(writePath,"COMMAND: "+FileInput.lines.get(i),true, true);
                if (temp.length == 3){
                    if (temp[1].equals(temp[2])){
                        FileOutput.writeToFile(writePath,"ERROR: Both of the names are the same, nothing changed!",true, true);
                        continue;
                    }
                    controlDevices control = new controlDevices();
                    control.checkNames(temp[2]);
                    if(controlDevices.contains){
                        continue;
                    }
                    for (Device element: controlDevices.checkList){
                        if (element.getName().equals(temp[1])){
                            element.setName(temp[2]);
                        }
                    }
                }else{
                    FileOutput.writeToFile(writePath,"ERROR: Erroneous command!",true, true);
                }

            } else if (FileInput.lines.get(i).startsWith("SkipMinutes\t")) {
                String[] temp = FileInput.lines.get(i).split("\t");
                FileOutput.writeToFile(writePath,"COMMAND: "+FileInput.lines.get(i),true, true);
                if (temp.length > 2 ){
                    FileOutput.writeToFile(writePath,"ERROR: Erroneous command!",true, true);
                    continue;
                }
                try{
                    if (Integer.parseInt(temp[1])<0){
                        FileOutput.writeToFile(writePath,"ERROR: Time cannot be reversed!",true, true);
                    } else if (Integer.parseInt(temp[1])==0) {
                        FileOutput.writeToFile(writePath,"ERROR: There is nothing to skip!",true, true);
                    } else {
                        setTime.currentTime = setTime.currentTime.plusMinutes(Integer.parseInt(temp[1]));
                    }
                }catch (Exception e){
                    FileOutput.writeToFile(writePath,"ERROR: Erroneous command!",true, true);
                }
            } else if (FileInput.lines.get(i).startsWith("Remove\t")) {
                String[] temp = FileInput.lines.get(i).split("\t");
                FileOutput.writeToFile(writePath,"COMMAND: "+FileInput.lines.get(i),true, true);
                for (SmartPlug plug:SmartPlug.smartPlugs){
                    if(temp[1].equals(plug.getName())){
                        FileOutput.writeToFile(writePath,"SUCCESS: Information about removed smart device is as follows:",true, true);
                        if (plug.getInitialStatus().equals("On")) {
                            plug.setInitialStatus("Off");
                            plug.setOffTime(setTime.currentTime);
                            plug.setPlugOffTime(setTime.currentTime);
                        }
                        plug.calculateEnergy();
                        //added this setting ontime here to calculate energy properly
                        if (plug.getOnTime().isBefore(setTime.currentTime) | plug.getOnTime().equals(setTime.currentTime)){
                            plug.setOnTime(LocalDateTime.of(1,1,1,1,1,1));
                        }
                        if (plug.getOffTime().isBefore(setTime.currentTime) | plug.getOffTime().equals(setTime.currentTime)){
                            plug.setOffTime(LocalDateTime.of(1,1,1,1,1,1));
                        }
                        if (plug.getInitialStatus() != null && plug.getStringOnTime().equals(String.valueOf(LocalDateTime.of
                                (1,1,1,1,1,1))) && plug.getStringOffTime()
                                .equals(String.valueOf(LocalDateTime.of(1,1,1,1,1,1)))){
                            FileOutput.writeToFile(writePath,"Smart Plug " +plug.getName()+" is "+
                                    plug.getInitialStatus().toLowerCase()+" and consumed "+plug.getTotalEnergy()+
                                    "W so far (excluding current device), and its time to switch its status is null.",true, true);
                        } else{
                            FileOutput.writeToFile(writePath,"Smart Plug " +plug.getName()+" is "+
                                    plug.getInitialStatus().toLowerCase()+" and consumed "+plug.getTotalEnergy()+
                                    "W so far (excluding current device), and its time to switch its status is "+
                                    plug.getLaterSwitchTime() +".",true, true);
                        }
                        SmartPlug.smartPlugs.remove(plug);
                        break;
                    }
                }
                for (SmartCamera camera:SmartCamera.smartCameras){
                    if(temp[1].equals(camera.getName())) {

                        FileOutput.writeToFile(writePath, "SUCCESS: Information about removed smart device is as follows:", true, true);
                        if (camera.getInitialStatus().equals("On")) {
                            camera.setInitialStatus("Off");
                            camera.setOffTime(setTime.currentTime);
                        }
                        if (camera.getPeriod() >= 0 && !camera.getStringOnTime().equals("0001-01-01_01:01:01") &&
                                !camera.getStringOffTime().equals("0001-01-01_01:01:01")){
                            camera.calculateConsumedMegabytes();
                        }
                        if (camera.getOffTime().isBefore(setTime.currentTime) | camera.getOffTime().equals(setTime.currentTime)){
                            camera.setOffTime(LocalDateTime.of(1,1,1,1,1,1));
                        }
                        if (camera.getOnTime().isBefore(setTime.currentTime) | camera.getOnTime().equals(setTime.currentTime)){
                            camera.setOnTime(LocalDateTime.of(1,1,1,1,1,1));
                        }
                        if (camera.getInitialStatus() != null && camera.getStringOnTime().equals(String.valueOf(LocalDateTime.of
                                (1,1,1,1,1,1))) && camera.getStringOffTime().equals(
                                        String.valueOf(LocalDateTime.of(1,1,1,1,1,1)))){
                            FileOutput.writeToFile(writePath,"Smart Camera " +camera.getName()+" is "+
                                    camera.getInitialStatus().toLowerCase()+" and used "+camera.getTotalMegabytesConsumed()+
                                    " MB of storage so far (excluding current status), and its time to switch its status is null.",true, true);
                        } else{
                            FileOutput.writeToFile(writePath,"Smart Camera " +camera.getName()+" is "+
                                    camera.getInitialStatus().toLowerCase()+" and used "+camera.getTotalMegabytesConsumed()+
                                    " MB of storage so far (excluding current status), and its time to switch its status is "+
                                    camera.getLaterSwitchTime()+".",true, true);
                        }
                        SmartCamera.smartCameras.remove(camera);
                        break;
                    }
                }
                for (SmartLamp lamp:SmartLamp.smartLamps){
                    if(temp[1].equals(lamp.getName())){
                        if (lamp.getOffTime().isBefore(setTime.currentTime) | lamp.getOffTime().equals(setTime.currentTime)){
                            lamp.setOffTime(LocalDateTime.of(1,1,1,1,1,1));
                        }
                        if (lamp.getOnTime().isBefore(setTime.currentTime) | lamp.getOnTime().equals(setTime.currentTime)){
                            lamp.setOnTime(LocalDateTime.of(1,1,1,1,1,1));
                        }
                        FileOutput.writeToFile(writePath, "SUCCESS: Information about removed smart device is as follows:", true, true);
                        lamp.setInitialStatus("Off");
                        if (lamp.getStringOnTime().equals(String.valueOf(LocalDateTime.of(1,1,1,1,1,1)))
                                && lamp.getStringOffTime().equals(String.valueOf(LocalDateTime.of(1,1,1,1,1,1)))){
                            FileOutput.writeToFile(writePath,"Smart Lamp "+lamp.getName()+" is off and its kelvin value is "+
                                    lamp.getKelvinValue()+"K with "+lamp.getBrightness()+"% brightness, and its time to switch its status is null.",true, true);
                        } else{
                            FileOutput.writeToFile(writePath,"Smart Lamp "+lamp.getName()+" is off and its kelvin value is "+
                                    lamp.getKelvinValue()+"K with "+lamp.getBrightness()+"% brightness, and its time to switch its status is "+
                                    lamp.getLaterSwitchTime()+".",true, true);
                        }
                        lamp.setOffTime(setTime.currentTime);
                        SmartLamp.smartLamps.remove(lamp);
                        break;
                    }
                }for (SmartColorLamp colorLamp:SmartColorLamp.smartColorLamps){
                    if(temp[1].equals(colorLamp.getName())){
                        if (colorLamp.getOffTime().isBefore(setTime.currentTime) | colorLamp.getOffTime().equals(setTime.currentTime)){
                            colorLamp.setOffTime(LocalDateTime.of(1,1,1,1,1,1));
                        }
                        if (colorLamp.getOnTime().isBefore(setTime.currentTime) | colorLamp.getOnTime().equals(setTime.currentTime)){
                            colorLamp.setOnTime(LocalDateTime.of(1,1,1,1,1,1));
                        }
                        colorLamp.setInitialStatus("Off");
                        FileOutput.writeToFile(writePath, "SUCCESS: Information about removed smart device is as follows:", true, true);
                        if (colorLamp.getIsColorModeOn()){
                            if (colorLamp.getStringOnTime().equals(String.valueOf(LocalDateTime.of(1,1,1,1,1,1)))
                                    && colorLamp.getStringOffTime().equals(String.valueOf(LocalDateTime.of(1,1,1,1,1,1)))){
                                FileOutput.writeToFile(writePath,"Smart Color Lamp "+colorLamp.getName()+
                                        " is off and its color value is "+colorLamp.getColorCode()+" with "+
                                        colorLamp.getBrightness()+"% brightness, and its time to switch its status is null.",true, true);
                            } else{
                                FileOutput.writeToFile(writePath,"Smart Color Lamp "+colorLamp.getName()+
                                        " is off and its color value is "+colorLamp.getColorCode()+" with "+
                                        colorLamp.getBrightness()+"% brightness, and its time to switch its status is "+
                                        colorLamp.getLaterSwitchTime()+".",true, true);
                            }
                        } else{
                            if (colorLamp.getStringOnTime().equals(String.valueOf(LocalDateTime.of(1,1,1,1,1,1)))
                                    && colorLamp.getStringOffTime().equals(String.valueOf(LocalDateTime.of(1,1,1,1,1,1)))){
                                FileOutput.writeToFile(writePath,"Smart Color Lamp "+colorLamp.getName()+
                                        " is off and its color value is "+colorLamp.getKelvinValue()+"K with "+
                                        colorLamp.getBrightness()+"% brightness, and its time to switch its status is null.",true, true);
                            } else{
                                FileOutput.writeToFile(writePath,"Smart Color Lamp "+colorLamp.getName()+
                                        " is off and its color value is "+colorLamp.getKelvinValue()+"K with "+
                                        colorLamp.getBrightness()+"% brightness, and its time to switch its status is "+
                                        colorLamp.getLaterSwitchTime()+".",true, true);
                            }
                        }

                        colorLamp.setOffTime(setTime.currentTime);
                        SmartColorLamp.smartColorLamps.remove(colorLamp);
                        break;
                    }
                }
            } else if (FileInput.lines.get(i).startsWith("Switch\t")) {
                isDeviceInList = false; // to initialize
                String[] temp = FileInput.lines.get(i).split("\t");
                controlDevices control = new controlDevices();
                control.checkInitialStatus(temp[2]);
                FileOutput.writeToFile(writePath,"COMMAND: "+FileInput.lines.get(i),true, true);
                if (!controlDevices.initialStatusBool){
                    continue;
                }
                for (SmartPlug plug:SmartPlug.smartPlugs){
                    if (temp[1].equals(plug.getName())){
                        isDeviceInList = true;
                        if (temp[2].equals(plug.getInitialStatus())){
                            FileOutput.writeToFile(writePath,"ERROR: This device is already switched "+
                                    temp[2].toLowerCase()+"!",true, true);
                            break;
                        }else {
                            if (temp[2].equals("On")){
                                plug.setOnTime(setTime.currentTime);

                            } else if (temp[2].equals("Off")) {
                                plug.setOffTime(setTime.currentTime);
                            }
                            plug.setInitialStatus(temp[2]);
                            break;
                        }
                    }
                }
                for (SmartCamera camera:SmartCamera.smartCameras){
                    if (temp[1].equals(camera.getName())){
                        isDeviceInList = true;
                        if (temp[2].equals(camera.getInitialStatus())){
                            FileOutput.writeToFile(writePath,"ERROR: This device is already switched "+
                                    temp[2].toLowerCase()+"!",true, true);
                            break;
                        }else {
                            if (temp[2].equals("On")){
                                camera.setOnTime(setTime.currentTime);
                            } else if (temp[2].equals("Off")) {
                                camera.setOffTime(setTime.currentTime);
                                camera.calculateConsumedMegabytes();
                            }
                            camera.setInitialStatus(temp[2]);
                            break;
                        }
                    }
                }
                for (SmartLamp lamp:SmartLamp.smartLamps){
                    if (temp[1].equals(lamp.getName())){
                        isDeviceInList = true;
                        if (temp[2].equals(lamp.getInitialStatus())){
                            FileOutput.writeToFile(writePath,"ERROR: This device is already switched "+
                                    temp[2].toLowerCase()+"!",true, true);
                            break;
                        }else {
                            if (temp[2].equals("On")){
                                lamp.setOnTime(setTime.currentTime);
                            } else if (temp[2].equals("Off")) {
                                lamp.setOffTime(setTime.currentTime);
                            }
                            lamp.setInitialStatus(temp[2]);
                            break;
                        }
                    }
                }
                for (SmartColorLamp colorLamp:SmartColorLamp.smartColorLamps){
                    if (temp[1].equals(colorLamp.getName())){
                        isDeviceInList = true;
                        if (temp[2].equals(colorLamp.getInitialStatus())){
                            FileOutput.writeToFile(writePath,"ERROR: This device is already switched "+
                                    temp[2].toLowerCase()+"!",true, true);
                            break;
                        }else {
                            if (temp[2].equals("On")){
                                colorLamp.setOnTime(setTime.currentTime);
                            } else if (temp[2].equals("Off")) {
                                colorLamp.setOffTime(setTime.currentTime);
                            }
                            colorLamp.setInitialStatus(temp[2]);
                            break;
                        }
                    }
                }
                if (!isDeviceInList){
                    FileOutput.writeToFile(writePath, "ERROR: There is not such a device!", true, true);
                }
            } else if (FileInput.lines.get(i).startsWith("SetSwitchTime\t")) {
                isDeviceInList = false;
                String[] temp = FileInput.lines.get(i).split("\t|_|-|:");
                FileOutput.writeToFile(writePath,"COMMAND: "+FileInput.lines.get(i),true, true);
                for (SmartPlug plug:SmartPlug.smartPlugs) {
                    if (temp[1].equals(plug.getName())) {
                        isDeviceInList = true;
                        if (plug.getInitialStatus().equals("On")){
                            plug.setOffTime(LocalDateTime.of(Integer.parseInt(temp[2]), Integer.parseInt(temp[3]),
                                    Integer.parseInt(temp[4]), Integer.parseInt(temp[5]), Integer.parseInt(temp[6]),
                                    Integer.parseInt(temp[7])));
                            if (!timeList.contains(LocalDateTime.of(Integer.parseInt(temp[2]), Integer.parseInt(temp[3]),
                                    Integer.parseInt(temp[4]), Integer.parseInt(temp[5]), Integer.parseInt(temp[6]),
                                    Integer.parseInt(temp[7])))){
                                timeList.add(LocalDateTime.of(Integer.parseInt(temp[2]), Integer.parseInt(temp[3]),
                                        Integer.parseInt(temp[4]), Integer.parseInt(temp[5]), Integer.parseInt(temp[6]),
                                        Integer.parseInt(temp[7])));
                            }
                            break;
                        } else if (plug.getInitialStatus().equals("Off")) {
                            plug.setOnTime(LocalDateTime.of(Integer.parseInt(temp[2]), Integer.parseInt(temp[3]),
                                    Integer.parseInt(temp[4]), Integer.parseInt(temp[5]), Integer.parseInt(temp[6]),
                                    Integer.parseInt(temp[7])));
                            if (!timeList.contains(LocalDateTime.of(Integer.parseInt(temp[2]), Integer.parseInt(temp[3]),
                                    Integer.parseInt(temp[4]), Integer.parseInt(temp[5]), Integer.parseInt(temp[6]),
                                    Integer.parseInt(temp[7])))) {
                                timeList.add(LocalDateTime.of(Integer.parseInt(temp[2]), Integer.parseInt(temp[3]),
                                        Integer.parseInt(temp[4]), Integer.parseInt(temp[5]), Integer.parseInt(temp[6]),
                                        Integer.parseInt(temp[7])));
                            }
                            break;
                        }
                    }
                }
                for (SmartCamera camera:SmartCamera.smartCameras) {
                    if (temp[1].equals(camera.getName())) {
                        isDeviceInList = true;
                        if (camera.getInitialStatus().equals("On")){
                            camera.setOffTime(LocalDateTime.of(Integer.parseInt(temp[2]), Integer.parseInt(temp[3]),
                                    Integer.parseInt(temp[4]), Integer.parseInt(temp[5]), Integer.parseInt(temp[6]),
                                    Integer.parseInt(temp[7])));
                            if (!timeList.contains(LocalDateTime.of(Integer.parseInt(temp[2]), Integer.parseInt(temp[3]),
                                    Integer.parseInt(temp[4]), Integer.parseInt(temp[5]), Integer.parseInt(temp[6]),
                                    Integer.parseInt(temp[7])))){
                                timeList.add(LocalDateTime.of(Integer.parseInt(temp[2]), Integer.parseInt(temp[3]),
                                        Integer.parseInt(temp[4]), Integer.parseInt(temp[5]), Integer.parseInt(temp[6]),
                                        Integer.parseInt(temp[7])));
                            }
                            break;
                        } else if (camera.getInitialStatus().equals("Off")) {
                            camera.setOnTime(LocalDateTime.of(Integer.parseInt(temp[2]), Integer.parseInt(temp[3]),
                                    Integer.parseInt(temp[4]), Integer.parseInt(temp[5]), Integer.parseInt(temp[6]),
                                    Integer.parseInt(temp[7])));
                            if (!timeList.contains(LocalDateTime.of(Integer.parseInt(temp[2]), Integer.parseInt(temp[3]),
                                    Integer.parseInt(temp[4]), Integer.parseInt(temp[5]), Integer.parseInt(temp[6]),
                                    Integer.parseInt(temp[7])))) {
                                timeList.add(LocalDateTime.of(Integer.parseInt(temp[2]), Integer.parseInt(temp[3]),
                                        Integer.parseInt(temp[4]), Integer.parseInt(temp[5]), Integer.parseInt(temp[6]),
                                        Integer.parseInt(temp[7])));
                            }
                            break;
                        }
                    }
                }
                for (SmartLamp lamp:SmartLamp.smartLamps) {
                    if (temp[1].equals(lamp.getName())) {
                        isDeviceInList = true;
                        if (lamp.getInitialStatus().equals("On")){
                            lamp.setOffTime(LocalDateTime.of(Integer.parseInt(temp[2]), Integer.parseInt(temp[3]),
                                    Integer.parseInt(temp[4]), Integer.parseInt(temp[5]), Integer.parseInt(temp[6]),
                                    Integer.parseInt(temp[7])));
                            if (!timeList.contains(LocalDateTime.of(Integer.parseInt(temp[2]), Integer.parseInt(temp[3]),
                                    Integer.parseInt(temp[4]), Integer.parseInt(temp[5]), Integer.parseInt(temp[6]),
                                    Integer.parseInt(temp[7])))){
                                timeList.add(LocalDateTime.of(Integer.parseInt(temp[2]), Integer.parseInt(temp[3]),
                                        Integer.parseInt(temp[4]), Integer.parseInt(temp[5]), Integer.parseInt(temp[6]),
                                        Integer.parseInt(temp[7])));
                            }
                            break;
                        } else if (lamp.getInitialStatus().equals("Off")) {
                            lamp.setOnTime(LocalDateTime.of(Integer.parseInt(temp[2]), Integer.parseInt(temp[3]),
                                    Integer.parseInt(temp[4]), Integer.parseInt(temp[5]), Integer.parseInt(temp[6]),
                                    Integer.parseInt(temp[7])));
                            if (!timeList.contains(LocalDateTime.of(Integer.parseInt(temp[2]), Integer.parseInt(temp[3]),
                                    Integer.parseInt(temp[4]), Integer.parseInt(temp[5]), Integer.parseInt(temp[6]),
                                    Integer.parseInt(temp[7])))) {
                                timeList.add(LocalDateTime.of(Integer.parseInt(temp[2]), Integer.parseInt(temp[3]),
                                        Integer.parseInt(temp[4]), Integer.parseInt(temp[5]), Integer.parseInt(temp[6]),
                                        Integer.parseInt(temp[7])));
                            }
                            break;
                        }
                    }
                }
                for (SmartColorLamp colorLamp:SmartColorLamp.smartColorLamps) {
                    if (temp[1].equals(colorLamp.getName())) {
                        isDeviceInList = true;
                        if (colorLamp.getInitialStatus().equals("On")){
                            colorLamp.setOffTime(LocalDateTime.of(Integer.parseInt(temp[2]), Integer.parseInt(temp[3]),
                                    Integer.parseInt(temp[4]), Integer.parseInt(temp[5]), Integer.parseInt(temp[6]),
                                    Integer.parseInt(temp[7])));
                            if (!timeList.contains(LocalDateTime.of(Integer.parseInt(temp[2]), Integer.parseInt(temp[3]),
                                    Integer.parseInt(temp[4]), Integer.parseInt(temp[5]), Integer.parseInt(temp[6]),
                                    Integer.parseInt(temp[7])))){
                                timeList.add(LocalDateTime.of(Integer.parseInt(temp[2]), Integer.parseInt(temp[3]),
                                        Integer.parseInt(temp[4]), Integer.parseInt(temp[5]), Integer.parseInt(temp[6]),
                                        Integer.parseInt(temp[7])));
                            }
                            break;
                        } else if (colorLamp.getInitialStatus().equals("Off")) {
                            colorLamp.setOnTime(LocalDateTime.of(Integer.parseInt(temp[2]), Integer.parseInt(temp[3]),
                                    Integer.parseInt(temp[4]), Integer.parseInt(temp[5]), Integer.parseInt(temp[6]),
                                    Integer.parseInt(temp[7])));
                            if (!timeList.contains(LocalDateTime.of(Integer.parseInt(temp[2]), Integer.parseInt(temp[3]),
                                    Integer.parseInt(temp[4]), Integer.parseInt(temp[5]), Integer.parseInt(temp[6]),
                                    Integer.parseInt(temp[7])))) {
                                timeList.add(LocalDateTime.of(Integer.parseInt(temp[2]), Integer.parseInt(temp[3]),
                                        Integer.parseInt(temp[4]), Integer.parseInt(temp[5]), Integer.parseInt(temp[6]),
                                        Integer.parseInt(temp[7])));
                            }
                            break;
                        }
                    }
                }
                if (!isDeviceInList){
                    FileOutput.writeToFile(writePath, "ERROR: There is not such a device!", true, true);
                }
            } else if (FileInput.lines.get(i).startsWith("PlugIn\t")) {
                isPlugInList = false;
                String[] temp = FileInput.lines.get(i).split("\t");
                FileOutput.writeToFile(writePath,"COMMAND: "+FileInput.lines.get(i),true, true);
                for (SmartPlug plug:SmartPlug.smartPlugs){
                    if (temp[1].equals(plug.getName())){
                        if (!(Integer.parseInt(temp[2]) >0)){
                            FileOutput.writeToFile(writePath, "ERROR: Ampere value must be a positive number!",true,true);
                            isPlugInList = true;
                            break;
                        }
                        if (SmartPlug.pluggedIns.contains(plug)){
                            FileOutput.writeToFile(writePath,"ERROR: There is already an item plugged in to that plug!",true, true);
                            isPlugInList = true;
                            break;
                        }
                        plug.setAmpere(Double.parseDouble(temp[2]));
                        SmartPlug.pluggedIns.add(plug);
                        isPlugInList = true;
                        plug.setIsPluggedIn(true);
                        plug.setPlugInTime(setTime.currentTime);
                        break;
                    }
                }
                if (!isPlugInList){
                    FileOutput.writeToFile(writePath,"ERROR: This device is not a smart plug!",true, true);
                }
            } else if (FileInput.lines.get(i).startsWith("PlugOut\t")) {
                isPlugInList = false;
                String[] temp = FileInput.lines.get(i).split("\t");
                FileOutput.writeToFile(writePath,"COMMAND: "+FileInput.lines.get(i),true, true);
                for (SmartPlug plug:SmartPlug.smartPlugs){
                    if (temp[1].equals(plug.getName())){
                        if (SmartPlug.pluggedIns.contains(plug)){
                            SmartPlug.pluggedIns.remove(plug);
                            plug.setPlugOffTime(setTime.currentTime);
                            plug.setIsPluggedIn(false);
                        }else{
                            FileOutput.writeToFile(writePath,"ERROR: This plug has no item to plug out from that plug!",true, true);
                            isPlugInList = true;
                            break;
                        }
                        isPlugInList = true;
                        break;
                    }
                }
                if (!isPlugInList){
                    FileOutput.writeToFile(writePath,"ERROR: This device is not a smart plug!",true, true);
                }
            } else if (FileInput.lines.get(i).startsWith("SetKelvin\t")) {
                isDeviceInList = false;
                isColorLampInList = false;
                isLampInList = false;
                controlDevices control = new controlDevices();
                String[] temp = FileInput.lines.get(i).split("\t");
                FileOutput.writeToFile(writePath,"COMMAND: "+FileInput.lines.get(i),true, true);
                for (Device device:controlDevices.checkList){
                    if (temp[1].equals(device.getName())) {
                        isDeviceInList = true;
                    }
                }
                if (!isDeviceInList){
                    FileOutput.writeToFile(writePath, "ERROR: There is not such a device!", true, true);
                    continue;
                }
                for (SmartLamp smartLamp:SmartLamp.smartLamps){
                    if (temp[1].equals(smartLamp.getName())){
                        isLampInList = true;
                        control.checkKelvin(temp[2]);
                        if (!controlDevices.isKelvinNumeric){
                            continue;
                        }
                        if (!controlDevices.isKelvinInRange){
                            continue;
                        }
                        smartLamp.setKelvinValue(Integer.parseInt(temp[2]));
                    }
                }
                for (SmartColorLamp smartColorLamp:SmartColorLamp.smartColorLamps){
                    if(temp[1].equals(smartColorLamp.getName())){
                        isColorLampInList = true;
                        control.checkKelvin(temp[2]);
                        if (!controlDevices.isKelvinNumeric){
                            continue;
                        }
                        if (!controlDevices.isKelvinInRange){
                            continue;
                        }
                        smartColorLamp.setKelvinValue(Integer.parseInt(temp[2]));
                        smartColorLamp.setColorModeOn(false);
                    }
                }
                if (!isLampInList && !isColorLampInList){
                    FileOutput.writeToFile(writePath, "ERROR: This device is not a smart lamp!", true, true);
                }

            }else if (FileInput.lines.get(i).startsWith("SetBrightness\t")) {
                isDeviceInList = false;
                isColorLampInList = false;
                isLampInList = false;
                controlDevices control = new controlDevices();
                String[] temp = FileInput.lines.get(i).split("\t");
                FileOutput.writeToFile(writePath,"COMMAND: "+FileInput.lines.get(i),true, true);
                for (Device device:controlDevices.checkList){
                    if (temp[1].equals(device.getName())) {
                        isDeviceInList = true;
                    }
                }
                if (!isDeviceInList){
                    FileOutput.writeToFile(writePath, "ERROR: There is not such a device!", true, true);
                    continue;
                }
                for (SmartLamp smartLamp:SmartLamp.smartLamps){
                    if (temp[1].equals(smartLamp.getName())){
                        isLampInList = true;
                        control.checkBrightness(temp[2]);
                        if (!controlDevices.isBrightnessNumeric){
                            continue;
                        }
                        if (!controlDevices.isBrightnessInRange){
                            continue;
                        }
                        smartLamp.setBrightness(Integer.parseInt(temp[2]));
                    }
                }
                for (SmartColorLamp smartColorLamp:SmartColorLamp.smartColorLamps){
                    if(temp[1].equals(smartColorLamp.getName())){
                        isColorLampInList = true;
                        control.checkBrightness(temp[2]);
                        if (!controlDevices.isBrightnessNumeric){
                            continue;
                        }
                        if (!controlDevices.isBrightnessInRange){
                            continue;
                        }
                        smartColorLamp.setBrightness(Integer.parseInt(temp[2]));
                    }
                }
                if (!isLampInList && !isColorLampInList) {
                    FileOutput.writeToFile(writePath, "ERROR: This device is not a smart lamp!", true, true);
                }
            } else if (FileInput.lines.get(i).startsWith("SetColorCode\t")) {
                isDeviceInList = false;
                isColorLampInList = false;
                controlDevices control = new controlDevices();
                String[] temp = FileInput.lines.get(i).split("\t");
                FileOutput.writeToFile(writePath,"COMMAND: "+FileInput.lines.get(i),true, true);
                for (Device device:controlDevices.checkList){
                    if (temp[1].equals(device.getName())) {
                        isDeviceInList = true;
                    }
                }
                if (!isDeviceInList){
                    FileOutput.writeToFile(writePath, "ERROR: There is not such a device!", true, true);
                    continue;
                }
                for (SmartColorLamp smartColorLamp:SmartColorLamp.smartColorLamps){
                    if(temp[1].equals(smartColorLamp.getName())){
                        isColorLampInList = true;
                        break;
                    }
                }
                if (!isColorLampInList) {
                    FileOutput.writeToFile(writePath, "ERROR: This device is not a smart color lamp!", true, true);
                    continue;
                }
                control.checkHexadecimal(temp[2]);
                if (!controlDevices.isHexNum){
                    FileOutput.writeToFile(Main.writePath,"ERROR: Erroneous command!",true,true);
                    continue;
                }
                if (!controlDevices.isHexInRange){
                    FileOutput.writeToFile(Main.writePath,"ERROR: Color code value must be in range of 0x0-0xFFFFFF!",true,true);
                    continue;
                }
                for (SmartColorLamp smartColorLamp:SmartColorLamp.smartColorLamps){
                    if(temp[1].equals(smartColorLamp.getName())){
                        smartColorLamp.setColorCode(temp[2]);
                        smartColorLamp.setColorModeOn(true);
                        break;
                    }
                }
            } else if (FileInput.lines.get(i).startsWith("SetWhite\t")) {
                isDeviceInList = false;
                isColorLampInList = false;
                isLampInList = false;
                controlDevices control = new controlDevices();
                String[] temp = FileInput.lines.get(i).split("\t");
                FileOutput.writeToFile(writePath,"COMMAND: "+FileInput.lines.get(i),true, true);
                for (Device device:controlDevices.checkList){
                    if (temp[1].equals(device.getName())) {
                        isDeviceInList = true;
                    }
                }
                if (!isDeviceInList){
                    FileOutput.writeToFile(writePath, "ERROR: There is not such a device!", true, true);
                    continue;
                }
                for (SmartLamp smartLamp:SmartLamp.smartLamps){
                    if (temp[1].equals(smartLamp.getName())){
                        isLampInList = true;
                        control.checkKelvin(temp[2]);
                        if (!controlDevices.isKelvinNumeric){
                            continue;
                        }
                        if (!controlDevices.isKelvinInRange){
                            continue;
                        }
                        control.checkBrightness(temp[3]);
                        if (!controlDevices.isBrightnessNumeric){
                            continue;
                        }
                        if (!controlDevices.isBrightnessInRange){
                            continue;
                        }
                        smartLamp.setKelvinValue(Integer.parseInt(temp[2]));
                        smartLamp.setBrightness(Integer.parseInt(temp[3]));
                    }
                }
                for (SmartColorLamp smartColorLamp:SmartColorLamp.smartColorLamps){
                    if(temp[1].equals(smartColorLamp.getName())){
                        isColorLampInList = true;
                        control.checkKelvin(temp[2]);
                        if (!controlDevices.isKelvinNumeric){
                            continue;
                        }
                        if (!controlDevices.isKelvinInRange){
                            continue;
                        }
                        control.checkBrightness(temp[3]);
                        if (!controlDevices.isBrightnessNumeric){
                            continue;
                        }
                        if (!controlDevices.isBrightnessInRange){
                            continue;
                        }
                        smartColorLamp.setKelvinValue(Integer.parseInt(temp[2]));
                        smartColorLamp.setBrightness(Integer.parseInt(temp[3]));
                        smartColorLamp.setColorModeOn(false);
                    }
                }
                if (!isLampInList && !isColorLampInList){
                    FileOutput.writeToFile(writePath, "ERROR: This device is not a smart lamp!", true, true);
                }
            } else if (FileInput.lines.get(i).startsWith("SetColor\t")) {
                isDeviceInList = false;
                isColorLampInList = false;
                controlDevices control = new controlDevices();
                String[] temp = FileInput.lines.get(i).split("\t");
                FileOutput.writeToFile(writePath,"COMMAND: "+FileInput.lines.get(i),true, true);
                for (Device device:controlDevices.checkList){
                    if (temp[1].equals(device.getName())) {
                        isDeviceInList = true;
                    }
                }
                if (!isDeviceInList){
                    FileOutput.writeToFile(writePath, "ERROR: There is not such a device!", true, true);
                    continue;
                }
                for (SmartColorLamp smartColorLamp:SmartColorLamp.smartColorLamps){
                    if(temp[1].equals(smartColorLamp.getName())){
                        isColorLampInList = true;
                        control.checkHexadecimal(temp[2]);
                        if (!controlDevices.isHexNum){
                            FileOutput.writeToFile(Main.writePath,"ERROR: Erroneous command!",true,true);
                            continue;
                        }
                        if (!controlDevices.isHexInRange){
                            FileOutput.writeToFile(Main.writePath,"ERROR: Color code value must be in range of 0x0-0xFFFFFF!",true,true);
                            continue;
                        }
                        control.checkBrightness(temp[3]);
                        if (!controlDevices.isBrightnessNumeric){
                            continue;
                        }
                        if (!controlDevices.isBrightnessInRange){
                            continue;
                        }
                        smartColorLamp.setColorCode(temp[2]);
                        smartColorLamp.setColorModeOn(true);
                        smartColorLamp.setBrightness(Integer.parseInt(temp[3]));
                    }
                }
                if (!isColorLampInList) {
                    FileOutput.writeToFile(writePath, "ERROR: This device is not a smart color lamp!", true, true);
                }
            } else if (FileInput.lines.get(i).equals("Nop")) {
                Collections.sort(timeList);
                FileOutput.writeToFile(writePath,"COMMAND: "+FileInput.lines.get(i),true, true);
                if (timeList.isEmpty()){
                    FileOutput.writeToFile(writePath,"ERROR: There is nothing to switch!",true, true);
                } else if (timeList.size() == 1) {
                    setTime.currentTime = timeList.get(0);
                } else{
                    List<LocalDateTime> tempList = new ArrayList<LocalDateTime>();
                    for (LocalDateTime switchTime:timeList){
                        if(setTime.currentTime.isAfter(switchTime) && !setTime.currentTime.isBefore(switchTime)){
                            tempList.add(switchTime);
                        }
                    }
                    if (tempList.isEmpty()) {
                        FileOutput.writeToFile(writePath, "ERROR: There is nothing to switch!", true, true);
                    }else {
                        for (LocalDateTime temp:tempList){
                            timeList.remove(temp);
                        }
                        Collections.sort(timeList);
                        if (timeList.isEmpty()) {
                            FileOutput.writeToFile(writePath, "ERROR: There is nothing to switch!", true, true);
                            continue;
                        }
                        setTime.currentTime = timeList.get(0);
                        timeList.remove(setTime.currentTime);
                    }
                }

            } else if (FileInput.lines.get(i).equals("ZReport")) {
                FileOutput.writeToFile(writePath,"COMMAND: "+FileInput.lines.get(i),true, true);
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");
                FileOutput.writeToFile(writePath,"Time is:\t"+setTime.currentTime.format(dateTimeFormatter),true, true);
                controlDevices devices = new controlDevices();
                devices.printZReport();
            } else{
                FileOutput.writeToFile(writePath,"COMMAND: "+FileInput.lines.get(i),true, true);
                FileOutput.writeToFile(writePath,"ERROR: Erroneous command!",true, true);
            }
        }
        if (!FileInput.lines.get(FileInput.lines.size()-1).equals("ZReport")){
            FileOutput.writeToFile(writePath,"ZReport:",true, true);
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");
            FileOutput.writeToFile(writePath,"Time is:\t"+setTime.currentTime.format(dateTimeFormatter),true, true);
            controlDevices devices = new controlDevices();
            devices.printZReport();
        }
    }
}