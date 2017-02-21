import java.io.*;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Puzino Yury on 20.02.2017.
 * work with text file ("cache") and store / update currencies rates
 * cache data stored as follow: "USD-RUB|2017-02-21|0.234"
 */
public class Action_cache {

    private String mFrom;
    private String mTo;
    private double mRate;

    private final String FILE_NAME = "puzino_currconv.txt";
    private List<String> mStringList;

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

    public void setRate(double rate){
        this.mRate = rate;
    }

    /**
     * if file not exists - false, getFromWeb + try to set cache again
     * if file was created (empty) - false, getFromWeb + update
     * if file existed - temporary true, loadDataFromFile
     * @return b - (false) if need web update, (true) if data is ok
     * */
    public boolean checkFile(){

        File file = new File(FILE_NAME);
        if(!file.exists()){

            //creating cache file
            try {
                if (!file.createNewFile()) {
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
        if(!bSearch){return false;}     //no, it can't be simplified for the future.

        return true;
    }

    /**
     * Load all from cache file
     * */
    private boolean loadDataFromFile(){

        try {
            File file = new File(FILE_NAME);
            mStringList = Files.readAllLines(file.toPath());

        } catch (IOException e) {
            Main.show("Sorry: I/O error while loading cache.");
        }

        if(mStringList.isEmpty()){
           //nothing to show, file was empty
            return false;
        }

        return true;
    }

    /**
     * Check file for from-to pairs, check dates.
     * wrong content (corrupted) => erase
     * if outdated => rewrite
     * */
    private boolean searchRate(){
        List<String[]> stringLists = new ArrayList<>();

        //mStringList not empty (checked before)
        for(String sTmp : mStringList){

            //parse each line
            String[] sArray = sTmp.split("\\|");

            if(sArray.length != 3){
                //wrong data, cache was corrupted
                eraseFile();
                Main.show("Error: cache data was corrupted.");
                return false;
            }

            stringLists.add(sArray);
        }

        String date;
        Double rate;

        for(String[] sArray : stringLists){
            //two possibilities (ex: RUB-USD or USD-RUB), they are similar
            String sCurr1 = getFrom() + "-" + getTo();
            String sCurr2 = getTo() + "-" + getFrom();

            if(sArray[0].equals(sCurr1)){
                date = sArray[1];
                try{
                    rate = Double.parseDouble(sArray[2]);
                }catch (Exception ex){
                    //wrong data, cache was corrupted
                    eraseFile();
                    Main.show("Error: cache data was corrupted.");
                    return false;
                }
                //rate was found

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
                this.setRate(rate);
                return true;
            }

            //reverse currency
            if(sArray[0].equals(sCurr2)){
                date = sArray[1];
                try {
                    rate = 1.0 / Double.parseDouble(sArray[2]);
                }catch (Exception ex){
                    //wrong data, cache was corrupted
                    eraseFile();
                    Main.show("Error: cache data was corrupted.");
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
                this.setRate(rate);
                return true;
            }
        }

        //no such data in cache
        return false;
    }

    /**
     * In cause of corrupted data we can erase all data and getFromWeb new info */
    private void eraseFile(){

        String sEmpty = "";

        try {
            File file = new File(FILE_NAME);
            FileWriter writer = new FileWriter(file.getPath(), false);
            BufferedWriter bw = new BufferedWriter(writer);

            bw.write(sEmpty);
            bw.close();

        } catch (FileNotFoundException e) {
            //mysterious disappearance
            //no message here
        } catch (IOException e) {
            //try next launch
        }
    }

    /** rewrite all cache (delete outdated line) after outdated line was found */
    private void rewriteCache(List<String[]> list){

        try {
            File file = new File(FILE_NAME);
            FileWriter writer = new FileWriter(file.getPath(), false);
            BufferedWriter bw = new BufferedWriter(writer);

            for(String[] strings : list){
                String sInsert = strings[0] + "|" + strings[1] + "|" + strings[2] + "\r\n";
                bw.write(sInsert);
            }

            bw.close();

        } catch (FileNotFoundException e) {
            //mysterious disappearance
        } catch (IOException e) {
            //try next launch
        }
    }

    /** Save new values from web */
    public boolean insert(double rate){

        try {
            File file = new File(FILE_NAME);
            FileWriter writer = new FileWriter(file.getPath(), true);   //APPEND
            BufferedWriter bw = new BufferedWriter(writer);

            //get current date
            LocalDate localDateToday = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String localDateString = localDateToday.format(formatter);

            //result string to cache
            String sInsert = getFrom() + "-" + getTo() + "|" + localDateString + "|" + String.valueOf(rate) + "\r\n";
            bw.write(sInsert);

            bw.close();

        } catch (FileNotFoundException e) {
            //mysterious disappearance
            return false;

        } catch (IOException e) {
            //try next launch
            return false;
        }

        return true;

    }

}
