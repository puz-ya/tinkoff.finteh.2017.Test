import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by Puzino Yury on 20.02.2017.
 */
public class Action_parse {

    private String mResultJson = "";
    private boolean mErrorParse = false;  //suggest successful json parse
    private double mRate = 0.0;

    public double getRate() {
        return mRate;
    }

    public void setmResultJson(String mResultJson) {
        this.mResultJson = mResultJson;
    }

    public boolean isError_parse() {
        return mErrorParse;
    }

    public Action_parse(){

    }

    /** parse json string to ApiResponse object and RateObject */
    public void parse(){

        //create gson with api & rates deserializers
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(ApiResponseObject.class, new ApiDeserializer())
                .create();

        //parse API response
        ApiResponseObject apiResponseObject = gson.fromJson(mResultJson, ApiResponseObject.class);
        //get Rate from response object
        RateObject rateObject = apiResponseObject.getRate();

        if(rateObject == null){
            mErrorParse = true;
            return;
        }

        mRate = rateObject.getmRate();
    }

}
