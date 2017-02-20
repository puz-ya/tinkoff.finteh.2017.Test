import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Puzino Yury on 20.02.2017.
 */
public class Main {

    public static void main(String[] args) {

        show("Welcome to Currency Converter!");

        //input From and To Strings
        Welcome welcome = new Welcome();
        welcome.enterCurrencies();

        //check our cache file (or create new one)
        Action_cache action_cache = new Action_cache(welcome.getmFromCurrency(),welcome.getmToCurrency());
        boolean bIsNew = action_cache.check();

        if(!bIsNew) {
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            Future future = executorService.submit(new Callable(){
                public Object call() throws Exception {
                    System.out.println("Asynchronous Callable");
                    return "Callable Result";
                }
            });

            //init parse class object
            Action_parse action_parse = new Action_parse();
            //init connect class object & get data from site
            Action_connect action_connect = new Action_connect();
            action_connect.connect(welcome.getmFromCurrency(), welcome.getmToCurrency());

            if (!action_connect.isError_connect()) {
                action_parse.setmResultJson(action_connect.getmResultJson());
                action_parse.parse();
            } else {
                //some action_connect error
                return;
            }

            if (!action_parse.isError_parse()) {
                //everything ok, update our cache
                action_cache.update(action_parse.getRate());

                show(welcome.getmFromCurrency() + " => " + welcome.getmToCurrency() + " : " + action_parse.getRate());
            } else {
                //some parse json error
                show("Sorry: Error while parsing JSON");
                return;
            }

        }else{
            //our file data (cache) is up-to-date, show it
            show(action_cache.getFrom() + " => " + action_cache.getTo() + " : " + action_cache.getRate());
        }
    }

    /** too lazy to write System... everytime
     * */
    public static void show(String string){
        System.out.println(string);
    }

}
