public class Help {

    static String getCity(String str) {
        char symbol='-';
        int indexCust=str.indexOf(symbol)+1;
        return str.substring(indexCust);
    }
}
