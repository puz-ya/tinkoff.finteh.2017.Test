import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;

/**
 * Created by Puzino Yury on 20.02.2017.
 */
public class Action_connect implements Callable<String> {

    private String mMainURL = "http://api.fixer.io/latest?base=111&symbols=222";

    private HttpURLConnection mUrlConnection = null;
    private BufferedReader mReader = null;
    private String mResultJson = "";

    private boolean mErrorConnect = false;  //suggest successful connection

    private String mFrom;
    private String mTo;

    public boolean isError_connect() {
        return mErrorConnect;
    }

    public Action_connect(){

    }

    public Action_connect(String sFrom, String sTo){
        this.mFrom = sFrom;
        this.mTo = sTo;
    }

    public String getmResultJson() {
        return mResultJson;
    }

    /** set new currencies (from and to) and start connection
     * @param from - currency From;
     * @param to - currency To;
     * */
    public void connect(String from, String to) throws CancellationException {

        this.mFrom = from;
        this.mTo = to;

        connect();
    }

    /** try to connect to fixer.io and try to get json data (as string)
     * throws cancellation if error
     * */
    private void connect() throws CancellationException {

        String newURL;
        newURL = mMainURL.replace("111", mFrom);
        newURL = newURL.replace("222", mTo);

        try{
            URL url = new URL(newURL);

            //clear
            mErrorConnect = false;

            //connecting
            mUrlConnection = (HttpURLConnection) url.openConnection();
            mUrlConnection.setRequestMethod("GET");
            mUrlConnection.setConnectTimeout(5000);  //5 sec
            mUrlConnection.setReadTimeout(5000);  //5 sec
            mUrlConnection.connect();

            try {
                InputStream inputStream = mUrlConnection.getInputStream();
                StringBuilder builder = new StringBuilder();

                mReader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = mReader.readLine()) != null) {
                    builder.append(line);
                }

                //to string
                mResultJson = builder.toString();

            }catch (IOException ex_io){
                mErrorConnect = true;
                //close connection
                mUrlConnection.disconnect();
                Main.show("Sorry! Web answer read timeout. May be wrong currencies: " + mFrom + " => " + mTo);
                Main.show("Please, check your input values and try again!");

                throw new CancellationException();
            }

            //close reader + connection
            mReader.close();
            mUrlConnection.disconnect();

        } catch (MalformedURLException ex_url) {
            mErrorConnect = true;
            Main.show("Sorry! URL is wrong: " + newURL);

            throw new CancellationException();

        }catch (IOException ex_io) {
            mErrorConnect = true;
            //close connection
            mUrlConnection.disconnect();
            Main.show("Sorry! Error: could not connect to " + newURL);
            Main.show("Please, check your internet connection and try again!");

            throw new CancellationException();
        }
    }

    @Override
    public String call() throws CancellationException{

        connect();

        return mResultJson;
    }

}
