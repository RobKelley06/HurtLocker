import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by rkelly on 2/22/16.
 */
public class Item {
    public String name;
    public String price;
    public String type;
    public String expiration;

    public Item(String info) {
        name = grabName(info);
        price = grabPrice(info);
        type = grabType(info);
        expiration = grabExpiration(info);
    }

    private static String grabName(String info){
        Matcher data = Pattern.compile("[nN][aA][mM][eE]:([^;]+);").matcher(info);
        StringBuilder ret = new StringBuilder();
        while(data.find()) ret.append(rewrite(data.group(1)));
        return ret.toString();
    }

    private static String grabPrice(String info) {
        Matcher data = Pattern.compile("[pP][rR][iI][cC][ee]:([^;]+);").matcher(info);
        StringBuilder ret = new StringBuilder();
        while(data.find()) ret.append(data.group(1));
        return ret.toString();
    }


    private static String grabType(String info) {
        Matcher data = Pattern.compile("type:([^;\\^%*!@]+)[;\\^%*!@]").matcher(info);
        StringBuilder ret = new StringBuilder();
        while(data.find()) ret.append(data.group(1));
        return ret.toString();
    }

    private static String grabExpiration(String info) {
        Matcher data = Pattern.compile("expiration:([^;]+)#").matcher(info);
        StringBuilder ret = new StringBuilder();
        while(data.find()) ret.append(data.group(1));
        return ret.toString();
    }


    private static String rewrite(String name) {
        if (name.matches("[mM][iI][lL][kK]")) return "Milk";
        else if (name.matches("[bB][rR][eE][aA][dD]")) return "Bread";
        else if (name.matches("[cC][oO0]+[kK][iI][eE][sS]")) return "Cookies";
        else if (name.matches("[aA][pP]+[lL][eE][sS]")) return "Apples";
        else return "";
    }

}
