import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;

/**
 * Created by Puzino Yury on 20.02.2017.
 */
public class Action_parse implements Callable<Double> {

    /** JSON string to parse. */
    private String mResultJson = "";
    /** Rate if any. */
    private double mRate = 0.0;

    public Action_parse(final String sRes) {
        this.mResultJson = sRes;
    }

    /** parse json string to ApiResponse object and RateObject.
     * @throws CancellationException if error
     * */
    private void parse() throws CancellationException {

        if (mResultJson.equals("")) {
            Main.show("Sorry: error while parsing JSON (empty).");

            throw new CancellationException();
        }

        //create gson with api & rates deserializers
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(ApiResponseObject.class, new ApiDeserializer())
                .create();

        //parse API response
        ApiResponseObject apiResponseObject = gson.fromJson(mResultJson, ApiResponseObject.class);
        //get Rate from response object
        RateObject rateObject = apiResponseObject.getRate();

        if (rateObject == null) {
            Main.show("Sorry: error while parsing JSON (no rate).");
            Main.show("Please, check your input values and try again!");

            throw new CancellationException();
        }

        mRate = rateObject.getmRate();
    }

    @Override
    public Double call() throws CancellationException {

        parse();

        return mRate;
    }

}
