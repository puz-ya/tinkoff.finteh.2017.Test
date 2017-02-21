/**
 * Created by Puzino Yury on 20.02.2017.
 */
public class ApiResponseObject {
    /** currency FROM. */
    private String mBase;
    /** date. */
    private String mDate;
    /** rate object, may be corrupted or null. */
    private RateObject mRate;

    public ApiResponseObject(final String sBase, final String sDate, final RateObject rateObj) {
        this.mBase = sBase;
        this.mDate = sDate;
        this.mRate = rateObj;
    }

    /** getter.
     * @return current rateObject
     * */
    public RateObject getRate() {
        return mRate;
    }
}
