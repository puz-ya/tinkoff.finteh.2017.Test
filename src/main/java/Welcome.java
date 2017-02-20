import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * Created by Puzino Yury on 20.02.2017.
 */
public class Welcome {

    /** input string From */
    private String mFromCurrency;
    /** input string To */
    private String mToCurrency;

    /** two loops for input
     * */
    public void enterCurrencies() {

        do{
            scanFrom();
        }while(!checkCurrencies(mFromCurrency,1));
        Main.show("Filtered input is: [" + mFromCurrency + "]");

        do {
            scanTo();
        }while (!checkCurrencies(mToCurrency,2));
        Main.show("Filtered input is: [" + mToCurrency + "]");

    }

    /** check input values
     * @param sCurr - 3 letters currency name (ex: USD)
     * @return b - correctness of currency (3 alphabetical symbols)
     * */
    public boolean checkCurrencies(String sCurr, int mode){

        sCurr = sCurr.replaceAll("[^a-zA-Z]+","");          //delete wrong symbols
        sCurr = sCurr.trim().replaceAll("\\s{2,}", " ");    //delete spaces
        if(sCurr.equals("") || sCurr.length()<3){
            Main.show("Incorrect filtered input currency: [" + sCurr + "]");
            return false;
        }
        sCurr = sCurr.substring(0,3);                       //only 3 symbols allowed (standard)
        sCurr = sCurr.toUpperCase();

        if(mode == 1){
            mFromCurrency = sCurr;
        }
        else{
            mToCurrency = sCurr;
        }

        return true;
    }

    /** scan FROM string
     * */
    public void scanFrom(){
        System.out.println("Enter from currency:");
        Scanner scanner = new Scanner(new InputStreamReader(System.in));
        mFromCurrency = scanner.next();
    }

    /** scan TO string
     * */
    public void scanTo(){
        System.out.println("Enter to currency:");
        Scanner scanner = new Scanner(new InputStreamReader(System.in));
        mToCurrency = scanner.next();
    }

    public String getmFromCurrency() {
        return mFromCurrency;
    }

    public void setmFromCurrency(String mFromCurrency) {
        this.mFromCurrency = mFromCurrency;
    }

    public String getmToCurrency() {
        return mToCurrency;
    }

    public void setmToCurrency(String mToCurrency) {
        this.mToCurrency = mToCurrency;
    }
}
