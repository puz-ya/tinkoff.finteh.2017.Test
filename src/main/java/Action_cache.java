/**
 * Created by Puzino Yury on 20.02.2017.
 * work with text file ("cache") and store / update currencies rates
 */
public class Action_cache {

    private String mFrom;
    private String mTo;
    private double mRate;

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

    /** check file for from-to pairs, check dates.
     * if outdated => connect to site
     * @return b - (false) if need web update, (true) data is ok
     * */
    public boolean check(){
        return false;
    }

    /** update rate for current pair from-to
     * */
    public void update(double rate){
        this.mRate = rate;

        //insert into file

    }
}
