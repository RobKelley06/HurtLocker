import org.omg.CORBA.NamedValue;

import java.awt.image.Kernel;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by rkelly on 2/22/16.
 */
public class JerksonParser {


    private static TreeMap pushToFinal(TreeMap<String,ArrayList<String>> finalData, String name, String price, String type, String expiration, String amount) {
        try { if (!finalData.get(name).get(0).equals(price)) name = name+ "2";} catch (NullPointerException e) {}
        try { if (name.contains("2")) amount = Integer.toString(Integer.parseInt(finalData.get(name).get(3)) + 1);}
        catch (NullPointerException e) { amount = "1"; }
        ArrayList<String> aL = new ArrayList<String>(Arrays.asList(price, type, expiration, amount));
        finalData.put(name, aL);
        return finalData;
    }

    private static TreeMap compileData(TreeMap<String,ArrayList<String>> finalData, String name, String price, String type, String expiration) {
        if (!price.isEmpty() && !name.isEmpty() && !type.isEmpty() && !expiration.isEmpty()) {
            if (finalData.containsKey(name))  finalData = pushToFinal(finalData, name, price, type, expiration, Integer.toString(Integer.parseInt(finalData.get(name).get(3)) + 1));
            else finalData = pushToFinal(finalData, name, price, type, expiration, "1");
        } else {
            try {
                finalData.containsKey("Error");
                finalData = pushToFinal(finalData, "Error", "", "", "", Integer.toString(Integer.parseInt(finalData.get("Error").get(3)) + 1));
            } catch (NullPointerException e) { finalData = pushToFinal(finalData, "Error", "", "", "", "1"); }
        }
        return finalData;
    }

    private static void printWithHeader(TreeMap<String,ArrayList<String>> finalData, String key ,ArrayList<String> value){
        int secondItem = 0;
        try { secondItem = Integer.parseInt(finalData.get(key+"2").get(3)); }
        catch (NullPointerException e) { }
        System.out.printf("\nname:%8s\t\tseen:%2s times\n", key, Integer.toString(Integer.parseInt(value.get(3)) + secondItem));
        System.out.printf("=============\t\t=============\n");
        printWithoutHeader(key, value);
    }

    private static void printWithoutHeader(String key, ArrayList<String> value) {
        System.out.printf("Price:%7s\t\tseen:%2s times\n", value.get(0), value.get(3));
        if (!key.contains("2")) System.out.printf("-------------\t\t-------------\n");
    }

    private static void displayData(TreeMap<String,ArrayList<String>> finalData, String key, ArrayList<String> value) {
        if (!key.equals("Error")) {
            if (!key.contains("2")) printWithHeader(finalData,key,value);
            else printWithoutHeader(key,value);
        }
    }

    public static String display(TreeMap<String,ArrayList<String>> finalData) {
        for(Map.Entry<String,ArrayList<String>> e : finalData.entrySet()) {
            displayData(finalData, e.getKey(),e.getValue());
        }
        System.out.printf("\nErrors\t\t\t\tseen:%2s times\n", finalData.get("Error").get(3));
        return null;
    }


    public static String readJerkson(String info){
        Matcher data = Pattern.compile("([^#]+#)#").matcher(info);
        TreeMap<String,ArrayList<String>> finalData = new TreeMap<String, ArrayList<String>>();
        while(data.find()) {
            Item i = new Item(data.group(1));
            finalData = compileData(finalData, i.name, i.price, i.type, i.expiration);
        }
        display(finalData);
        return finalData.toString();
    }
}
