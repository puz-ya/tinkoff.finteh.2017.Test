import java.io.*;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Puzino Yury on 20.02.2017.
 * work with text file ("cache") and store / update currencies rates
 */
public class Action_cache {

    private String mFrom;
    private String mTo;
    private double mRate;

    private final String FILE_NAME = "puzino_currconv.txt";
    private String[] fileContent;
    List<String> mStringList;

    public Action_cache(String from, String to){
        this.mFrom = from;
        this.mTo = to;
    }

    public String getFrom() {
        return mFrom;
    }

    public String getTo() {
        return mTo;
    }

    public double getRate() {
        return mRate;
    }

    /** update rate for current pair from-to
     * */
    public void setRate(double rate){
        this.mRate = rate;
    }

    /** check file for from-to pairs, check dates.
     * if outdated => connect to site
     * @return b - (false) if need web update, (true) data is ok
     * */
    public boolean checkFile(){

        File file = new File(FILE_NAME);
        if(!file.exists()){

            //creating cache file
            try {
                if (!file.createNewFile()) {
                    //Main.show("File already exists!");
                    //if file exists, go ahead and read it
                    return true;
                }else {
                    //file was created, it's empty now, need to fill it with data, check failed
                    return false;
                }
            }catch (IOException ex){
                Main.show("Sorry: Could not create cache file, check your write privileges.");
                return false;
            }
        }

        //load all from file
        boolean bLoad = loadDataFromFile();
        if(!bLoad){return false;}

        //search rate from query
        boolean bSearch = searchRate();
        if(!bSearch){return false;}

        return true;
    }

    /** Load values from local file */
    public boolean loadDataFromFile(){

        try {

            // It's even easier in Java 8
            mStringList = Files.readAllLines(new File(FILE_NAME).toPath());

            /* {{{ Code for example, but it's working too }}}

            InputStream is = new FileInputStream(FILE_NAME);
            BufferedReader buf = new BufferedReader(new InputStreamReader(is));

            StringBuilder sb = new StringBuilder();
            String line = buf.readLine();

            while(line != null){
                sb.append(line).append("\n");
                line = buf.readLine();
            }

            String fileAsString = sb.toString();
            System.out.println("Contents : " + fileAsString);

            is.close();
            */
        } catch (IOException e) {
            Main.show("Sorry: I/O error while loading cache.");
        }

        if(mStringList.isEmpty()){
           //nothing to show, file was empty
            return false;
        }

        return true;
    }

    public boolean searchRate(){
        List<String[]> stringLists = new ArrayList<>();

        //mStringList not empty (checked before)
        for(String sTmp : mStringList){

            //parse each line
            String[] sArray = sTmp.split("\\|");

            if(sArray.length != 3){
                //wrong data, cache was corrupted
                Main.show("Error: cache data was corrupted.");
                return false;
            }

            stringLists.add(sArray);
        }

        String date;
        Double rate;

        for(String[] sArray : stringLists){
            String sCurr1 = getFrom() + "-" + getTo();
            String sCurr2 = getTo() + "-" + getFrom();

            if(sArray[0].equals(sCurr1)){
                date = sArray[1];
                try{
                    rate = Double.parseDouble(sArray[2]);
                }catch (Exception ex){
                    Main.show("Incorrect rate in cache");
                    return false;
                }
                //rate was founded

                //check cache currency date
                LocalDate localDateToday = LocalDate.now();
                LocalDate localDate = LocalDate.parse( date );
                if(localDateToday.getYear() != localDate.getYear() ||
                        localDateToday.getMonthValue() != localDate.getMonthValue() ||
                        localDateToday.getDayOfMonth() - localDate.getDayOfMonth() > 0){
                    //cache is out of date, delete this object, rewrite cache, send update command
                    stringLists.remove(sArray);
                    rewriteCache(stringLists);
                    return false;
                }

                //everything is ok, cache is up-to-date
                setRate(rate);
                return true;
            }

            //reverse currency
            if(sArray[0].equals(sCurr2)){
                date = sArray[1];
                try {
                    rate = 1.0 / Double.parseDouble(sArray[2]);
                }catch (Exception ex){
                    Main.show("Incorrect rate in cache");
                    return false;
                }
                //check cache currency date
                LocalDate localDateToday = LocalDate.now();
                LocalDate localDate = LocalDate.parse( date );
                if(localDateToday.getYear() != localDate.getYear() ||
                        localDateToday.getMonthValue() != localDate.getMonthValue() ||
                        localDateToday.getDayOfMonth() - localDate.getDayOfMonth() > 0){
                    //cache is out of date, delete this object, rewrite cache, send update command
                    stringLists.remove(sArray);
                    rewriteCache(stringLists);
                    return false;
                }

                //everything is ok, cache is up-to-date
                setRate(rate);
                return true;
            }
        }


        return false;
    }

    /** Save names & descriptions to local file */
    /*
    boolean SaveDataToFile(String from, String to, String date, Double rate){

        name = name.replace("\n", " ");

        // split into 2 parts - name & meaning
        int limit = name.indexOf("(");
        if(limit == -1){	//Without a meaning - Orcs, Tieflings...
            limit = name.length();
        }
        String newname = name.substring(0, limit);
        String meaning = name.substring(limit, name.length());

        //Name, Meaning, Race, Gender
        String input_string = "<b>"+newname+"</b><br />"+meaning+"<br />"+race+", "+gender+".";

        //Data save process
        try {
            FileOutputStream outputStream = ctx.openFileOutput(FILE_NAME, Context.MODE_APPEND);
            DataOutputStream out = new DataOutputStream(outputStream);

            //In pro-version
            out.writeUTF(input_string);
            out.flush();
            out.close();

        } catch (IOException e) {
            Log.i("Data Input", "I/O Error SaveToFile");
            return false;
        }

        return true;

    }
    */

    /** Delete value with Number "num" */
    /*
    void DeleteValue(int num){

        String[] tmp = loadDataFromFile();

        if (num > tmp.length) return;

        FileOutputStream outputStream;

        try {
            //Rewrite file
            outputStream = openFileOutput(FILE_NAME, MODE_PRIVATE);
            DataOutputStream out = new DataOutputStream(outputStream);
            out.writeUTF("");
            out.flush();
            out.close();

            outputStream = openFileOutput(FILE_NAME, MODE_APPEND);
            out = new DataOutputStream(outputStream);
            for (int i=0; i<tmp.length; i++){
                if (i != num){
                    out.writeUTF(tmp[i]+".");
                }
            }
            out.flush();
            out.close();

        } catch (IOException e) {
            Main.show("Data Input : I/O Error Delete");
        }
    }
    //*/
}
