import java.util.concurrent.*;

/**
 * Created by Puzino Yury on 20.02.2017.
 */
public class Main {

    public static void main(String[] args) {

        show("Welcome to Currency Converter!");

        //input From and To Strings
        Welcome welcome = new Welcome();
        welcome.enterCurrencies();

        String sJSON;
        double dRate;

        //check our cache file (or create new one)
        Action_cache action_cache = new Action_cache(welcome.getmFromCurrency(),welcome.getmToCurrency());
        boolean bIsNew = action_cache.check();

        if(!bIsNew) {

            ExecutorService executorService = Executors.newSingleThreadExecutor();

            Future<String> future1 = executorService.submit(
                    //init connect class object & get data from site
                    new Action_connect(welcome.getmFromCurrency(), welcome.getmToCurrency())
            );

            //async waiting and drawing
            System.out.print("Connecting.");
            while (!future1.isDone()) {
                System.out.print(".");  //draw dots in command line
                try {
                    Thread.sleep(50); //sleep for 100 millisecond before checking again
                } catch (Exception e) {
                    //some action_connect error
                    executorService.shutdown();
                    show("Sorry: error in the thread.");
                    return;
                }
            }
            show("");   //new line

            //get throws callable
            try {
                //get values from the future ... hehe
                sJSON = future1.get();
            }catch (ExecutionException|InterruptedException e) {
                executorService.shutdown();
                return;
            }

            Future<Double> future2 = executorService.submit(
                //init connect class object & get data from site
                new Action_parse(sJSON)
            );

            //async waiting and drawing
            System.out.print("Parsing.");
            while (!future2.isDone()) {
                System.out.print(".");  //draw dots in command line
                try {
                    Thread.sleep(50); //sleep for 10 millisecond before checking again
                } catch (Exception e) {
                    //some action_connect error
                    executorService.shutdown();
                    show("Sorry: error in the thread.");
                    return;
                }
            }
            show("");   //new line

            try {
                //get values from the future ... again
                dRate = future2.get();
            }catch (ExecutionException|InterruptedException e) {
                executorService.shutdown();
                return;
            }

            //show result
            show(welcome.getmFromCurrency() + " => " + welcome.getmToCurrency() + " : " + dRate);

            executorService.shutdown();

        }else{
            //our file data (cache) is up-to-date, show it
            show(action_cache.getFrom() + " => " + action_cache.getTo() + " : " + action_cache.getRate());
        }
    }

    /** too lazy to write System... many times
     * */
    public static void show(String string){
        System.out.println(string);
    }

}
