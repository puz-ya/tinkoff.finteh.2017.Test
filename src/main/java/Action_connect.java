import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;

/**
 * Created by Puzino Yury on 20.02.2017.
 */
public class Action_connect implements Callable<String> {

    /** address, where to connect. */
    private final String mMainURL = "http://api.fixer.io/latest?base=111&symbols=222";

    /** is used to make a single request. */
    private HttpURLConnection mUrlConnection = null;
    /** is used to read answer from request. */
    private BufferedReader mReader = null;
    /** JSON string. */
    private String mResultJson = "";

    /** From currency code. */
    private String mFrom;
    /** To currency code. */
    private String mTo;

    public Action_connect(final String sFrom, final String sTo) {
        this.mFrom = sFrom;
        this.mTo = sTo;
    }

    /** try to connect to fixer.io and try to get json data (as string).
     * @throws CancellationException if error
     * */
    private void connect() throws CancellationException {

        String newURL;
        newURL = mMainURL.replace("111", mFrom);
        newURL = newURL.replace("222", mTo);

        int timeOut = 5000; //5 seconds

        try {
            URL url = new URL(newURL);

            //connecting
            mUrlConnection = (HttpURLConnection) url.openConnection();
            mUrlConnection.setRequestMethod("GET");
            mUrlConnection.setConnectTimeout(timeOut);  //5 sec
            mUrlConnection.setReadTimeout(timeOut);  //5 sec
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

            } catch (IOException ex_io) {

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
            Main.show("Sorry! URL is wrong: " + newURL);

            throw new CancellationException();

        } catch (IOException ex_io) {

            //close connection
            mUrlConnection.disconnect();
            Main.show("Sorry! Error: could not connect to " + newURL);
            Main.show("Please, check your internet connection and try again!");

            throw new CancellationException();
        }
    }

    @Override
    public String call() throws CancellationException {

        connect();

        return mResultJson;
    }

}
