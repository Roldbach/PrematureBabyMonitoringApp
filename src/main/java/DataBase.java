import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DataBase
{
    private Hashtable<String, String> user;
    private Hashtable<String, String> administrator;
    private Hashtable<String, Baby> babyList;
    private ArrayList<String> logFile;
    private ArrayList<Double> calibrationParameter;
    private String lagTime;
    private String permissionTime;

    public DataBase()
    {
        /*
            By initiating the default database, the lag time is set to be 10min and
        the permission time is set to be 5min
         */
        user=new Hashtable<>();
        administrator=new Hashtable<>();
        babyList=new Hashtable<>();
        logFile=new ArrayList<>();
        calibrationParameter=new ArrayList<>();
        lagTime="10";
        permissionTime="5";
    }

    public DataBase(String directory)
    {
        /*
            Initiate the class DataBase with given file path. The file is automatically loaded

        input:
            directory: String, the file path which contains the information about the database

        throws:
            FileNotFoundException: file is not successfully loaded due to the incorrect path
         */
    }

    public Boolean[] logIn(String userID, String password)
    {
        /*
            Verify whether the ID and the password are matched and set the status and priority
        according to this result and the account

        input:
            userID: String, the unique ID for each user
            password: String, the password set by the user

        return:
            result: Boolean[], item 0: whether the user could log in successfully
                               item 1: whether the user is an administrator or not
          */
        Boolean[] result={false, false};
        if (password.equals(user.get(userID))) {result[0]=true;}
        if (password.equals(administrator.get(userID)))
        {
            result[0]=true;
            result[1]=true;
        }
        return result;
    }

    public Hashtable<String, String> getUser()
    {
        /*
            Return the whole list of user id and matching password (Might not be used until version 2)

        return:
            user: Hashtable<String, String>, key=user id, value=password
         */
        return user;
    }

    public Hashtable<String, String> getAdministrator()
    {
        /*
            Return the whole list of administrator id and matching password (Might not be used until version 2)

        return:
            administrator: Hashtable<String, String>, key=administrator id, value=password
         */
        return administrator;
    }

    public ArrayList<String> getBabyList()
    {
        /*
            Return the whole list of baby id (Might not be used until version 2)

        return:
            result: ArrayList<String>, the list of ID of all babies saved in the database
         */
        ArrayList<String> result=new ArrayList<>();
        Enumeration<String> keys=babyList.keys();
        while(keys.hasMoreElements())
        {
            result.add(keys.nextElement());
        }
        return result;
    }

    public ArrayList<String> getLogFile()
    {
        /*
            Return the log file which records every detailed modification to the database

         return:
            logFile: ArrayList<String>, the log file which records every detailed modification to the database
         */
        return logFile;
    }

    public ArrayList<Double> getCalibrationParameter()
    {
        /*
            Return the calibration parameter which could be used for calibration of current reading

            The calibration curve only supports polynomials with float coefficient/order for version 1+
            The index of an element represents the order and the element represents the coefficient
            For example, the function f(x)=1-2x+3x^2 is represented by {1.0,-2.0,3.0}

        return:
            calibrationParameter: ArrayList<Double>, the calibration curve which could be used to
        predict the concentration data from the current reading
         */
        return calibrationParameter;
    }

    public String getLagTime()
    {
        /*
            Return the lag time in minute which could be used to calibrate the timestamp
         of the skin concentration data

         return:
            lagTime: String, the time in minute which specifies the time difference between the input time
        and the sampled time
         */
        return lagTime;
    }

    public String getPermissionTime()
    {
        /*
            Return the permission time in minute which limit the modification to the database for protection

         return:
            permissionTime: String, the time in minute which specifies the time interval the user could
        modify the input
         */
        return permissionTime;

    }

    public LinkedHashMap<String, Double> getGlucoseConcentration(String babyID)
    {
        /*
            Return the glucose concentration data with timestamp of the specific baby with the matched hospital number

        input:
            babyID: String, the unique ID (hospital number) of the baby

        return:
            glucoseConcentration: LinkedHashMap<String,Double>, key=timestamp, value=concentration with put-in order
         */
        return babyList.get(babyID).getGlucoseConcentration();
    }

    public LinkedHashMap<String, Double> getSkinCurrent(String babyID)
    {
        /*
            Return the skin current data with timestamp of the specific baby with the matched hospital number
            (Might not be used until version 2)

        input:
            babyID: String, the unique ID (hospital number) of the baby

        return:
            skinCurrent: LinkedHashMap<String,Double>, key=timestamp, value=current with put-in order
         */
        return babyList.get(babyID).getSkinCurrent();
    }

    public LinkedHashMap<String, Double> getSkinConcentration(String babyID)
    {
        /*
            Return the skin concentration data with timestamp of the specific baby with the matched hospital number

        input:
            babyID: String, the unique ID (hospital number) of the baby

        return:
            skinConcentration: LinkedHashMap<String,Double>, key=timestamp, value=concentration with put-in order
         */
        return babyList.get(babyID).getSkinConcentration();
    }

    public LinkedHashMap<String, String> getEvent(String babyID)
    {
        /*
            Return the event data with timestamp of the specific baby with the matched hospital number

        input:
            babyID: String, the unique ID (hospital number) of the baby

        return:
            event: LinkedHashMap<String,String>, key=timestamp, value=event with put-in order
         */
        return babyList.get(babyID).getEvent();
    }

    public void updateLogFile(String time, String userID, String babyID, String action, String result)
    {
        /*
            Record the detailed modification to the database for management
            The sentence is in the format: time,userID,babyID,action,result if related to baby data modification
            The sentence is in the format: time,userID,None,action,result if related to user account management

        input:
            time: String, the time at which the user performed the action
            userID: String, the unique ID of the user who performed the action
            babyID: String, the unique ID of the baby who is monitored
            action: String, description of the action done
            result: String, the object of the action
         */
        String sentence=time+","+userID+","+babyID+","+action+","+result;
        logFile.add(sentence);
    }

    public boolean addUser(String userID, String newID, String password, boolean givePriority, String time)
    {
        /*
            Add a new user to the system and update the log file only if the given newID is not already in the database
            The user would become an administrator if given priority

        input:
            userID: String, the ID of the user who performed this action
            newID: String, the new ID that could be used in the future
            password: String, the matched password of the new ID
            givePriority: boolean, true if the new ID has the administrator priority, false otherwise
            time: String, the time at which the user performed the action

        return:
            result: boolean, true if added successfully, false otherwise
         */

        //Check whether the new ID is already been used within the database
        boolean status;
        if (givePriority) status = administrator.containsKey(newID);
        else status = user.containsKey(newID);

        //Add the new ID and the matched password into the database according to the result and the givenPriority
        if (!status&&givePriority)
        {
            administrator.put(newID, password);
            updateLogFile(time,userID,"None","Add Administrator",newID);
            return true;
        }
        else if(!status)
        {
            user.put(newID, password);
            updateLogFile(time,userID,"None","Add User",newID);
            return true;
        }
        else
        {
            return false;
        }
    }

    public void addGlucoseConcentration(String userID, String babyID, double value, String time)
    {
        /*
            Add a glucose concentration reading with timestamp for the baby
        with the matched hospital number and update the log file

            Assume there is no big time difference between the input time and measurement time
        (The time could be modified later)

         input:
            userID: String, the unique ID of the user who performed the action
            babyID: String, the unique ID of the baby who is monitored
            value: double, the glucose concentration reading
            time: String, the time at which the user performed the action
         */
        babyList.get(babyID).addGlucoseConcentration(value, time);
        updateLogFile(time, userID, babyID, "Add Glucose Concentration",Double.toString(value));
    }

    public void addSkinConcentration(String userID, String babyID, double current, double concentration, String time)
    {
        /*
            Add both skin current/concentration reading with timestamp for the baby
        with the matched hospital number and update the log file

            Assume there is no big time difference between the input time and measurement time
        (The time could be modified later)

         input:
            userID: String, the unique ID of the user who performed the action
            babyID: String, the unique ID of the baby who is monitored
            current: double, the skin current reading from the sensor directly
            concentration: double, the skin concentration reading after skin current calibration
            time: String, the time which has taken lag time into account
         */
        babyList.get(babyID).addSkinConcentration(current, concentration, time);
        updateLogFile(time, userID, babyID, "Add Skin Current/Concentration", current +"/"+ concentration);
    }

    public void addEvent(String userID, String babyID, String detail, String time)
    {
        /*
            Add an event with timestamp for the baby with the matched hospital number and update the log file

         input:
            userID: String, the unique ID of the user who performed the action
            babyID: String, the unique ID of the baby who is monitored
            detail: String, information about the event which might influence the concentration measurement
            time: String, the time at which the user performed the action
         */
        babyList.get(babyID).addEvent(detail,time);
        updateLogFile(time, userID, babyID, "Add Event",detail);
    }

    public boolean checkPermission(String inputTime, String currentTime)
    {
        /*
            Verify whether the user could modify this specific input by calculating the
        absolute time difference between the current time and the input time

            Assume the inputTime/currentTime are in the form: yyyy/MM/dd HH:mm:ss

        input:
            inputTime: String, in the form yyyy/MM/dd HH:mm:ss, the timestamp of the input value
            currentTime: String, in the form yyyy/MM/dd HH:mm:ss, the time at which the user performed the action

        return:
            true if the user could modify this value, false otherwise

        throws:
            ParseException: the string doesn't match the time pattern designed
         */
        //Format the input time and the current time to the form HH:mm:ss
        SimpleDateFormat format=new SimpleDateFormat("HH:mm:ss");
        String current=currentTime.substring(11);
        String target=inputTime.substring(11);
        //Calculate the time difference in millisecond
        try {
            Date currentDate=format.parse(current);
            Date targetDate=format.parse(target);
            float difference=currentDate.getTime()-targetDate.getTime();
            return !(Math.abs(difference) >= Long.parseLong(permissionTime) * 60000);
        } catch (ParseException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public boolean changePassword(String userID, String targetID, String newPassword, String time)
    {
        /*
            Check whether the target ID is in the user list and change
        the password as well as updating log file if it exists

        input:
            userID: String, the unique ID of the user who performed the action
            targetID: String, the unique ID of the target whose password requires modification
            newPassword: String, the new password for the target ID
            time: String, the time at which the user performed the action

        return:
            true if change successfully, false otherwise
         */
        if (user.containsKey(targetID))
        {
            user.put(targetID, newPassword);
            updateLogFile(time,userID,"None","Change Password",targetID);
            return true;
        }
        else {return false;}
    }

    public void changeCalibrationParameter(String userID, ArrayList<Double> newCalibrationParameter, String time)
    {
        /*
            Change the calibration parameter of the database and update the log file

        input:
            userID: String, the unique ID of the user who performed the action
            newCalibrationParameter: ArrayList<Double>, new calibration parameter that
                                could give more accurate predictions
            time: String, the time at which the user performed the action
         */
        calibrationParameter=newCalibrationParameter;
        updateLogFile(time,userID,"None","Change Calibration Parameter", "None");
    }

    public void changeLagTime(String userID, String newLagTime, String time)
    {
        /*
            Change the lag time in minute which could be used to calibrate the timestamp
         of the skin concentration data and update the log file

            Assume the new time is in minute

        input:
            userID: String, the unique ID of the user who performed the action
            newLagTime: String, new lag time in minute
            time: String, the time at which the user performed the action
         */
        lagTime=newLagTime;
        updateLogFile(time, userID, "None","Change Lag Time", newLagTime);
    }

    public void changePermissionTime(String userID, String newPermissionTime, String time)
    {
        /*
            Change the permission time in minute which limit the modification to
        the database for protection and update the log file

            Assume the new time is in minute

        input:
            userID: String, the unique ID of the user who performed the action
            new time: String, new permission time in minute
            time: String, the time at which the user performed the action
         */
        permissionTime=newPermissionTime;
        updateLogFile(time, userID, "None","Change Permission Time", newPermissionTime);
    }

    public void changeGlucoseConcentration(String userID, String babyID, String targetTime, double newValue, String time)
    {
        /*
            Change a glucose concentration reading using timestamp for the baby
        with the matched hospital number and update the log file

        input:
            userID: String, the unique ID of the user who performed the action
            babyID: String, the unique ID of the baby who is monitored
            targetTime: String, the time of the old value which requires modification
            newValue: double, the right glucose concentration that should be saved
            time: String, the time at which the user performed the action
         */
        babyList.get(babyID).changeGlucoseConcentration(targetTime, newValue);
        updateLogFile(time, userID, babyID, "Change Glucose Concentration", Double.toString(newValue));
    }

    public void changeEvent(String userID, String babyID, String targetTime, String newEvent, String time)
    {
        /*
            Change an event using timestamp for the baby with the matched hospital number
        and update the log file

        input:
            userID: String, the unique ID of the user who performed the action
            babyID: String, the unique ID of the baby who is monitored
            targetTime: String, the time of the old value which requires modification
            newEvent: String, the right event that should be saved
            time: String, the time at which the user performed the action
         */
        babyList.get(babyID).changeEvent(targetTime, newEvent);
        updateLogFile(time, userID, babyID, "Change Event", newEvent);
    }

    public void changeGlucoseConcentrationTimestamp(String userID, String babyID, String oldTime, String newTime, String time)
    {
        /*
            Change the glucose concentration timestamp for the baby with the matched hospital number
        due to the difference between measurement time and input time and then update the log file

        input:
            userID: String, the unique ID of the user who performed the action
            babyID: String, the unique ID of the baby who is monitored
            oldTime: String, the old timestamp which requires modification
            newTime: String, the right timestamp that should be saved
            time: String, the time at which the user performed the action
        */
        babyList.get(babyID).changeGlucoseConcentrationTimestamp(oldTime, newTime);
        updateLogFile(time, userID, babyID, "Change Glucose Concentration Timestamp", newTime);
    }

    public void changeEventTimestamp(String userID, String babyID, String oldTime, String newTime, String time)
    {
        /*
            Change the event timestamp for the baby with the matched hospital number
        due to the difference between measurement time and input time and then update the log file

        input:
            userID: String, the unique ID of the user who performed the action
            babyID: String, the unique ID of the baby who is monitored
            oldTime: String, the old timestamp which requires modification
            newTime: String, the right timestamp that should be saved
            time: String, the time at which the user performed the action
        */
        babyList.get(babyID).changeEventTimestamp(oldTime, newTime);
        updateLogFile(time, userID, babyID, "Change Event Timestamp", newTime);
    }

    public boolean deleteUser(String userID, String targetID, String time)
    {
        /*
            Delete the user with given target ID and update the log file
        only if the target ID is in the database

            The administrator could only delete a user account but not
        an administrator account

        input:
            userID: String, the unique ID of the user who performed the action
            targetID: String, the unique ID of the user which requires deletion
            time: String, the time at which the user performed the action

        return:
            true if delete successfully, false otherwise
         */
        if (user.containsKey(targetID))
        {
            user.remove(targetID);
            updateLogFile(time,userID,"None","Delete User",targetID);
            return true;
        }
        else {return false;}
    }

    public void deleteGlucoseConcentration(String userID, String babyID, String targetTime, String time)
    {
        /*
           Delete the glucose concentration using timestamp for the baby with the matched hospital number
       and update the log file using the deleted concentration

         input:
            userID: String, the unique ID of the user who performed the action
            babyID: String, the unique ID of the baby who is monitored
            targetTime: String, the time of the glucose concentration which requires deletion
            time: String, the time at which the user performed the action
         */
        Double result=babyList.get(babyID).getGlucoseConcentration().get(targetTime);
        updateLogFile(time, userID, babyID,"Delete Glucose Concentration",Double.toString(result));
        babyList.get(babyID).deleteGlucoseConcentration(targetTime);
    }

    public void deleteEvent(String userID, String babyID, String targetTime, String time)
    {
        /*
           Delete the event using timestamp for the baby with the matched hospital number
       and update the log file

         input:
            userID: String, the unique ID of the user who performed the action
            babyID: String, the unique ID of the baby who is monitored
            targetTime: String, the time of the glucose concentration which requires deletion
            time: String, the time at which the user performed the action
         */
        String result=babyList.get(babyID).getEvent().get(targetTime);
        updateLogFile(time, userID, babyID,"Delete Event", result);
        babyList.get(babyID).deleteEvent(targetTime);
    }

    public void saveDataBase(String directory, String babyDirectory)
    {
        /*
            Save the formatted data for database to the target directory

            This will overwrite any previous data file with the same name
    `
            By default, the files could be saved under: Base\DataBase


            Data Formatting:
            (1) The user list, administrator list will be saved within the same text file named "account.txt"
            (2) The user will be added "us:" in the front and the administrator will be added "ad:" in the front
            (3) For any ID-password pair, they are separated by ","
            (4) Each line represents 1 ID-password pair

            (5) General settings like calibration parameter, lag time and the permission time will
                be saved within the same text file named "setting.txt"
            (6) The calibration parameter will be added "cp:" in the front and each item is separated by "," in a line
            (7) The lag time is added "lt:" in the front and the permission time is added "pt:" in the front
            (8) For each baby, it is saved using its method defined in the class Baby, and they are saved under:
                Base\DataBase\Baby

            (9) The log file is saved within the text file named "log file.txt"
            (10) Each line represents 1 full sentence in the log file
        input:
            directory: String, the directory path where files except baby data could be saved
            babyDirectory: String, the directory path where all baby data could be saved

        throws:
            IOException: there is something wrong with the input/output operations
        */

        //Save the user and administrator data
        try
        {
            String fileName="account.txt";
            //Create a new file that is named "account.txt"
            FileWriter userWriter=new FileWriter(directory+"\\"+fileName,false);
            //Loop through both hash table and add those data
            for (Map.Entry<String, String> pair:user.entrySet())
            {
                userWriter.write("us:"+pair.getKey()+","+pair.getValue()+"\n");
            }
            for (Map.Entry<String, String> pair:administrator.entrySet())
            {
                userWriter.write("ad:"+pair.getKey()+","+pair.getValue()+"\n");
            }
            userWriter.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        //Save the general setting data
        try
        {
            String fileName="setting.txt";
            //Create a new file that is named "setting.txt"
            FileWriter settingWriter=new FileWriter(directory+"\\"+fileName,false);
            //Save the calibration parameters in one line
            settingWriter.write("cp:");
            int index=0;
            while (index<calibrationParameter.size())
            {
                if (index==calibrationParameter.size()-1) {settingWriter.write(calibrationParameter.get(index)+"\n");}
                else {settingWriter.write(calibrationParameter.get(index)+",");}
                index++;
            }
            //Save lag time
            settingWriter.write("lt:"+lagTime+"\n");
            //Save permission time
            settingWriter.write("pt:"+permissionTime+"\n");
            settingWriter.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        //Save all baby to the baby directory
        for (String key:babyList.keySet())
        {
            babyList.get(key).saveBaby(babyDirectory);
        }

        //Save the log file
        try
        {
            String fileName="log file.txt";
            //Create a new file that is named "log file.txt"
            FileWriter logWriter=new FileWriter(directory+"\\"+fileName,false);
            //Loop through the log file list and save each line
            for (String line:logFile) {logWriter.write(line+"\n");}
            logWriter.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public String formatTime(String minute)
    {
        /*
            Return the modified time in the format "yyyy/MM/dd HH:mm:ss"
            Return the current local time if given "0"

        input:
            minute: String, the time difference between the target time and the current time

        return:
            time: String, the target time in the format "yyyy/MM/dd HH:mm:ss"
         */
        LocalDateTime time=LocalDateTime.now().minusMinutes(Long.parseLong(minute));
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        return formatter.format(time);
    }

    public void addBaby(String hospitalNumber)
    {
        /*
            Only for testing use
         */
        Baby newBaby=new Baby(hospitalNumber);
        babyList.put(hospitalNumber,newBaby);
    }
}
