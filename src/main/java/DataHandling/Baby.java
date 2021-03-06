package DataHandling;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Baby {
    private String ID;
    private LinkedHashMap<String, Double> glucoseConcentration;
    private LinkedHashMap<String, Double> skinCurrent;
    private LinkedHashMap<String, Double> skinConcentration;
    private LinkedHashMap<String, String> event;

    public Baby(String hospitalNumber)
    {
        /*
            Initiate the class DataHandling.Baby with given hospital number

        input:
            hospitalNumber: String, the unique hospital number for each baby
         */
        ID=hospitalNumber;
        glucoseConcentration=new LinkedHashMap<>();
        skinCurrent=new LinkedHashMap<>();
        skinConcentration=new LinkedHashMap<>();
        event=new LinkedHashMap<>();
    }

    public Baby(String path, boolean action)
    {
        /*
            Initiate the class DataHandling.Baby with the given file path

        input:
            path: String, the file path which contains the information about the baby
            action: boolean, true if the String parameter represents a file path

        throws:
            FileNotFoundException, file is not successfully loaded due to the incorrect path
        */
        if (action)
        {
            ID=null;
            glucoseConcentration=new LinkedHashMap<>();
            skinCurrent=new LinkedHashMap<>();
            skinConcentration=new LinkedHashMap<>();
            event=new LinkedHashMap<>();
            try
            {
                //Open the file that contains information about one specific baby
                File babyFile=new File(path);
                Scanner babyReader=new Scanner(babyFile);
                //Read line by line and add formatted data according to the flag in each line
                while (babyReader.hasNextLine())
                {
                    String currentLine=babyReader.nextLine();
                    if (currentLine.contains("id")) {ID=currentLine.substring(3);}
                    else
                    {
                        int index=currentLine.indexOf(",");
                        if (currentLine.contains("gc"))
                        {
                            glucoseConcentration.put(currentLine.substring(3,index),Double.parseDouble(currentLine.substring(index+1)));
                        }
                        else if (currentLine.contains("sa"))
                        {
                            skinCurrent.put(currentLine.substring(3,index),Double.parseDouble(currentLine.substring(index+1)));
                        }
                        else if (currentLine.contains("sc"))
                        {
                            skinConcentration.put(currentLine.substring(3,index),Double.parseDouble(currentLine.substring(index+1)));
                        }
                        else
                        {
                            event.put(currentLine.substring(3,index),currentLine.substring(index+1));
                        }
                    }
                }
                babyReader.close();
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
        }
    }

    public String getID()
    {
        /*
            Return the hospital number of the baby

        return:
            ID: String, the unique hospital number for each baby
         */
        return ID;
    }

    public LinkedHashMap<String,Double> getGlucoseConcentration()
    {
        /*
            Return the glucose concentration data with timestamp of the baby for data processing and plotting

        return:
            glucoseConcentration: LinkedHashMap<String,Double>, key=timestamp, value=concentration with put-in order
         */
        return glucoseConcentration;
    }

    public LinkedHashMap<String,Double> getSkinCurrent()
    {
        /*
            Return the skin current data with timestamp of the baby for data processing and plotting

        return:
            skinCurrent: LinkedHashMap<String,Double>, key=timestamp, value=current with put-in order
         */
        return skinCurrent;
    }

    public LinkedHashMap<String,Double> getSkinConcentration()
    {
        /*
            Return the skin concentration data with timestamp of the baby for data processing and plotting

        return:
            skinConcentration: LinkedHashMap<String,Double>, key=timestamp, value=concentration with put-in order
         */
        return skinConcentration;
    }

    public LinkedHashMap<String,String> getEvent()
    {
        /*
            Return the event data with timestamp of the baby for data processing and plotting

        return:
            event: LinkedHashMap<String,String>, key=timestamp, value=event with put-in order
         */
        return event;
    }

    public void addGlucoseConcentration(double value, String time)
    {
        /*
            Add a glucose concentration reading for the baby with timestamp

         input:
            value: double, the glucose concentration reading
            time: String, the measurement time for this reading
         */
        glucoseConcentration.put(time, value);
    }

    public void addSkinConcentration(double current, double concentration, String time)
    {
        /*
            Add both skin current/concentration reading for the baby with timestamp simultaneously
        The concentration data is obtained after skin current calibration

         input:
            current: double, the skin current reading from the sensor directly
            concentration: double, the skin concentration reading after skin current calibration
            time: String, the measurement time for this reading after lag time calibration
         */
        skinCurrent.put(time, current);
        skinConcentration.put(time, concentration);
    }

    public void addEvent(String detail, String time)
    {
        /*
            Add an event for the baby with timestamp

         input:
            detail: String, information about the event which might influence the concentration measurement
            time: String, the time for this event
         */
        event.put(time, detail);
    }

    public void changeGlucoseConcentration(String targetTime, double newValue)
    {
        /*
            Change the glucose concentration reading which might be a typo or miss click

        input:
            targetTime: String, the time of the old value which requires modification
            newValue: double, the right glucose concentration that should be saved
        */
        glucoseConcentration.put(targetTime, newValue);
    }

    public void changeEvent(String targetTime,String newEvent)
    {
        /*
            Change the event information which might be a typo or miss click

         input:
            targetTime: String, the time of the old event which requires modification
            newEvent: String, the right event that should be saved
         */
        event.put(targetTime, newEvent);

    }

    public void changeGlucoseConcentrationTimestamp(String oldTime, String newTime)
    {
        /*
            Change the glucose concentration timestamp due to the difference between
        measurement time and input time

        input:
            oldTime: String, the old timestamp which requires modification
            newTime: String, the right timestamp that should be saved
        */

        //Copy all pairs of keys and values except the oldTime key, replace the oldTime key with newTime key
        LinkedHashMap <String, Double> newGlucoseConcentration=new LinkedHashMap<>();
        for (Map.Entry<String, Double> pair: glucoseConcentration.entrySet())
        {
            if (oldTime.equals(pair.getKey())) {newGlucoseConcentration.put(newTime,pair.getValue());}
            else {newGlucoseConcentration.put(pair.getKey(),pair.getValue());}
        }
        glucoseConcentration=newGlucoseConcentration;
    }

    public void changeEventTimestamp(String oldTime, String newTime)
    {
        /*
            Change the event timestamp due to the difference between measurement
        time and input time

        input:
            oldTime: String, the old timestamp which requires modification
            newTime: String, the right timestamp that should be saved
        */

        //Copy all pairs of keys and values except the oldTime key, replace the oldTime key with newTime key
        LinkedHashMap <String, String> newEvent=new LinkedHashMap<>();
        for (Map.Entry<String, String> pair: event.entrySet())
        {
            if (oldTime.equals(pair.getKey())) {newEvent.put(newTime,pair.getValue());}
            else {newEvent.put(pair.getKey(),pair.getValue());}
        }
        event=newEvent;
    }

    public void deleteGlucoseConcentration(String targetTime)
    {
        /*
           Delete the glucose concentration with timestamp which might be a typo or miss click

         input:
            targetTime: String, the time of the glucose concentration which requires deletion
         */
        glucoseConcentration.remove(targetTime);
    }

    public void deleteEvent(String targetTime)
    {
        /*
           Delete the event with timestamp which might be a typo or miss click

         input:
            targetTime: String, the time of the event which requires deletion
         */
        event.remove(targetTime);
    }

    public void loadBaby(String path)
    {
        /*
            Reset the class and load the file which contains data for a specific baby

            By default, the file could be loaded from: Base\DataBase\Baby\name.txt

        input:
            path: String, the file path which contains data for a specific baby

        throws:
            FileNotFoundException: file is not successfully loaded due to the incorrect path
        */
        ID=null;
        glucoseConcentration=new LinkedHashMap<>();
        skinCurrent=new LinkedHashMap<>();
        skinConcentration=new LinkedHashMap<>();
        event=new LinkedHashMap<>();
        try
        {
            //Open the file that contains information about one specific baby
            File babyFile=new File(path);
            Scanner babyReader=new Scanner(babyFile);
            //Read line by line and add formatted data according to the flag in each line
            while (babyReader.hasNextLine())
            {
                String currentLine=babyReader.nextLine();
                if (currentLine.contains("id:"))
                {
                    ID=currentLine.substring(3);
                }
                else
                {
                    int index=currentLine.indexOf(",");
                    if (currentLine.contains("gc:"))
                    {
                        glucoseConcentration.put(currentLine.substring(3,index),Double.parseDouble(currentLine.substring(index+1)));
                    }
                    else if (currentLine.contains("sa:"))
                    {
                        skinCurrent.put(currentLine.substring(3,index),Double.parseDouble(currentLine.substring(index+1)));
                    }
                    else if (currentLine.contains("sc:"))
                    {
                        skinConcentration.put(currentLine.substring(3,index),Double.parseDouble(currentLine.substring(index+1)));
                    }
                    else
                    {
                        event.put(currentLine.substring(3,index),currentLine.substring(index+1));
                    }
                }
            }
            babyReader.close();
        }
        catch (FileNotFoundException e)
        {
            System.out.println("File not loaded");
        }
    }

    public void saveBaby(String directory)
    {
        /*
            Save the formatted data for a specific baby to the target directory

            Sort the data first before saving

            This will overwrite any previous data file with the same name
    `       By default, the file could be saved under: Base\DataBase\Baby

            Data Formatting:
            (1) The hospital number of the baby is used as the file name
            (2) The hospital number will be added "id:" in the front
            (3) The glucose concentration data will be added "gc:" in the front
            (4) The skin current data will be added "sa:" in the front
            (5) The skin concentration data will be added "sc:" in the front
            (6) The event data will be added "ev:" in the front
            (7) For any time-value matched pairs, they are separated by ","
            (8) Each line represents 1 time-value matching pairs

         input:
            directory: String, the directory path where the file could be saved

         throws:
            IOException: there is something wrong with the input/output operations
         */
        sortTimestamp();
        String fileName=ID+".txt";
        try
        {
            //Create a new file that is named by the baby's hospital number
            FileWriter babyWriter=new FileWriter(directory+"/"+fileName,false);
            //Save hospital number to the file
            babyWriter.write("id:"+ID+"\n");
            //Loop through each linked hash map and add those data in order
            for (Map.Entry<String, Double> pair:glucoseConcentration.entrySet())
            {
                babyWriter.write("gc:"+pair.getKey()+","+pair.getValue()+"\n");
            }
            for (Map.Entry<String, Double> pair:skinCurrent.entrySet())
            {
                babyWriter.write("sa:"+pair.getKey()+","+pair.getValue()+"\n");
            }
            for (Map.Entry<String, Double> pair:skinConcentration.entrySet())
            {
                babyWriter.write("sc:"+pair.getKey()+","+pair.getValue()+"\n");
            }
            for (Map.Entry<String, String> pair:event.entrySet())
            {
                babyWriter.write("ev:"+pair.getKey()+","+pair.getValue()+"\n");
            }
            babyWriter.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void sortTimestamp()
    {
        /*
            Sort the glucose concentration data, skin concentration/current data, event
        data according to their timestamps

            The timestamp could be directly sorted as they have exactly the same format: yyyy/MM/dd HH:MM:ss
         */
       //Extract all timestamp as an Array<String> and sort this array
        String[] glucoseTimestamp=new String[glucoseConcentration.size()];
        int index=0;
        for (String time:glucoseConcentration.keySet())
        {
            glucoseTimestamp[index]=time;
            index++;
        }
        Arrays.sort(glucoseTimestamp);
        //According to the order in the array, update the stored Linked Hashmap
        LinkedHashMap<String, Double> newGlucoseConcentration=new LinkedHashMap<>();
        for (int i=0;i<glucoseConcentration.size();i++) newGlucoseConcentration.put(glucoseTimestamp[i],glucoseConcentration.get(glucoseTimestamp[i]));
        glucoseConcentration=newGlucoseConcentration;

        //Do the same for the skin current/concentration as they use exactly the same timestamp
        String[] skinTimestamp=new String[skinCurrent.size()];
        index=0;
        for (String time:skinCurrent.keySet())
        {
            skinTimestamp[index]=time;
            index++;
        }
        Arrays.sort(skinTimestamp);
        LinkedHashMap<String, Double> newSkinCurrent=new LinkedHashMap<>();
        LinkedHashMap<String, Double> newSkinConcentration=new LinkedHashMap<>();
        for (int i=0;i<skinCurrent.size();i++)
        {
            newSkinCurrent.put(skinTimestamp[i],skinCurrent.get(skinTimestamp[i]));
            newSkinConcentration.put(skinTimestamp[i],skinConcentration.get(skinTimestamp[i]));
        }
        skinCurrent=newSkinCurrent;
        skinConcentration=newSkinConcentration;
        //Do the same for the event
        String[] eventTimestamp=new String[event.size()];
        index=0;
        for (String time:event.keySet())
        {
            eventTimestamp[index]=time;
            index++;
        }
        Arrays.sort(eventTimestamp);
        LinkedHashMap<String,String> newEvent=new LinkedHashMap<>();
        for (int i=0;i<event.size();i++) {newEvent.put(eventTimestamp[i],event.get(eventTimestamp[i]));}
        event=newEvent;
    }

    public void resetSkinConcentration(ArrayList<String> timestamp, ArrayList<Double> current, ArrayList<Double> concentration)
    {
        /*
            Reset the stored skin current/concentration data using the new skinData given

            The timestamp and the skinData have the same row number and assume they are both in order

        input:
            timestamp: String[], array that contains the timestamp of each measurement in order
            current: Double[], array that contains the skin current data in order
            concentration: Double[], array that contains the skin concentration data in order
         */
        LinkedHashMap<String, Double> newSkinCurrent=new LinkedHashMap<>();
        LinkedHashMap<String, Double> newSkinConcentration=new LinkedHashMap<>();
        for (int i=0;i< timestamp.size();i++)
        {
            newSkinCurrent.put(timestamp.get(i), current.get(i));
            newSkinConcentration.put(timestamp.get(i), concentration.get(i));
        }
        skinCurrent=newSkinCurrent;
        skinConcentration=newSkinConcentration;
    }
}
