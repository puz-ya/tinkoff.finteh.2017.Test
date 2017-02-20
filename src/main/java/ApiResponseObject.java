/**
 * Created by Puzino Yury on 20.02.2017.
 */
public class ApiResponseObject {
    private String mBase;   //currency
    private String mDate;   //date
    private RateObject mRate;  //RateObject

    public ApiResponseObject(String sBase, String sDate, RateObject rateObj){
        this.mBase = sBase;
        this.mDate = sDate;
        this.mRate = rateObj;
    }

    public RateObject getRate() {
        return mRate;
    }
}