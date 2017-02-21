/**
 * Created by Puzino Yury on 20.02.2017.
 */
public class RateObject {

    /** Name of 'TO' currency. */
    private String mName;
    /** Rate of this currency. */
    private double mRate;

    public RateObject(final String name, final double rate) {
        this.mName = name;
        this.mRate = rate;
    }

    /** getter.
     * @return current rate */
    public double getmRate() {
        return mRate;
    }
}
