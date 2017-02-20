import com.google.gson.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Puzino Yury on 20.02.2017.
 */
public class Action_connect {

    private String mMainURL = "http://api.fixer.io/latest?base=111&symbols=222";

    private HttpURLConnection mUrlConnection = null;
    private BufferedReader mReader = null;
    private String mResultJson = "";

    private boolean mErrorConnect = false;  //suggest successful connection

    public boolean isError_connect() {
        return mErrorConnect;
    }

    public Action_connect(){

    }

    public String getmResultJson() {
        return mResultJson;
    }

    /** try to connect to fixer.io API and try to get json data (as string)
     * @param from - currency From;
     * @param to - currency To;
     * */
    public void connect(String from, String to){

        String newURL;
        newURL = mMainURL.replace("111", from);
        newURL = newURL.replace("222", to);

        try{
            URL url = new URL(newURL);

            //обнуляем
            mErrorConnect = false;

            //соединяемся
            mUrlConnection = (HttpURLConnection) url.openConnection();
            mUrlConnection.setRequestMethod("GET");
            mUrlConnection.setConnectTimeout(5000);  //5 секунд
            mUrlConnection.setReadTimeout(5000);  //5 секунд
            mUrlConnection.connect();

            //1 step
            System.out.print(".");

            try {
                InputStream inputStream = mUrlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                mReader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = mReader.readLine()) != null) {
                    //N steps
                    System.out.print(".");
                    buffer.append(line);
                }

                //to string
                mResultJson = buffer.toString();
                //final connect step
                System.out.print(".");

            }catch (IOException ex){
                mErrorConnect = true;
                //close connection
                mUrlConnection.disconnect();
                Main.show("Sorry! Read timeout. May be wrong currencies: " + from + " => " + to);
                Main.show("Please, check your input values and try again!");
            }
            //отсылаем обновление о переводе в строку
            //publishProgress("1.2");

            //close reader + connection
            mReader.close();
            mUrlConnection.disconnect();

        } catch (MalformedURLException e) {
            System.out.println("Sorry! URL is wrong: " + newURL);
        }catch (Exception e) {
            mErrorConnect = true;
            //close connection
            mUrlConnection.disconnect();
            Main.show("Sorry! Error: could not connect to " + newURL);
            Main.show("Please, check your internet connection and try again!");
        }
    }

}
