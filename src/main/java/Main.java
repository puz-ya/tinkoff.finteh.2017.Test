/**
 * Created by Puzino Yury on 20.02.2017.
 */
public class Main {

    public static void main(String[] args) {

        show("Welcome to Currency Converter!");

        //input From and To Strings
        Welcome welcome = new Welcome();
        welcome.enterCurrencies();

        //init parse class object
        Action_parse action_parse = new Action_parse();
        //init connect class object & get data from site
        Action_connect action_connect = new Action_connect();
        action_connect.connect(welcome.getmFromCurrency(),welcome.getmToCurrency());

        if(!action_connect.isError_connect()) {
            action_parse.setmResultJson(action_connect.getmResultJson());
            action_parse.parse();
        }else{
            //some action_connect error
            return;
        }

        if(!action_parse.isError_parse()){
            show(welcome.getmFromCurrency() + " => " + welcome.getmToCurrency() + " : " + action_parse.getRate());
        }else{
            //some parse json error
            show("Sorry: Error while parsing JSON");
        }

    }

    /** too lazy to write System... everytime
     * */
    public static void show(String string){
        System.out.println(string);
    }

}
