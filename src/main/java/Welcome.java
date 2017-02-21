import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * Created by Puzino Yury on 20.02.2017.
 */
public class Welcome {

    /** Input string code From. */
    private String mFromCurrency;
    /** Input string code To. */
    private String mToCurrency;
    /** Max length of currency code. */
    private final int MAX_CUR_SIZE = 3;

    /** getter.
     * @return From currency code */
    public String getmFromCurrency() {
        return mFromCurrency;
    }

    /** getter.
     * @return To currency code */
    public String getmToCurrency() {
        return mToCurrency;
    }

    /** two loops for input 3 symbolic currencies.
     * */
    public void enterCurrencies() {

        do {
            scanFrom();
        } while (!checkCurrencies(mFromCurrency, 1));
        Main.show("Filtered input is: [" + mFromCurrency + "]");

        do {
            scanTo();
        } while (!checkCurrencies(mToCurrency, 2));
        Main.show("Filtered input is: [" + mToCurrency + "]");

    }

    /** Check input values.
     * @param sCurr - 3 letters currency name (ex: USD)
     * @param mode - select if "From" or "To" string
     * @return - correctness of currency (3 alphabetical symbols)
     * */
    private boolean checkCurrencies(String sCurr, final int mode) {

        sCurr = sCurr.replaceAll("[^a-zA-Z]+", "");          //delete wrong symbols
        sCurr = sCurr.trim().replaceAll("\\s{2,}", " ");    //delete spaces
        if (sCurr.equals("") || sCurr.length() < MAX_CUR_SIZE) {
            Main.show("Incorrect filtered input currency: [" + sCurr + "]");
            return false;
        }
        sCurr = sCurr.substring(0, MAX_CUR_SIZE);       //only MAX_CUR_SIZE symbols allowed (standard)
        sCurr = sCurr.toUpperCase();

        if (mode == 1) {
            mFromCurrency = sCurr;
        } else {
            mToCurrency = sCurr;
        }

        return true;
    }

    /** scan FROM string.
     * */
    private void scanFrom() {
        System.out.println("Enter 'from' currency:");
        Scanner scanner = new Scanner(new InputStreamReader(System.in));
        mFromCurrency = scanner.next();
    }

    /** scan TO string.
     * */
    private void scanTo() {
        System.out.println("Enter 'to' currency:");
        Scanner scanner = new Scanner(new InputStreamReader(System.in));
        mToCurrency = scanner.next();
    }

}
