package use_case.search.util;

import java.util.*;

public class SynonymExpander {
    private static final Map<String, List<String>> synonymDict = new HashMap<>();

    static {
        // ----- Electronics -----
        synonymDict.put("laptop", Arrays.asList("notebook", "macbook", "computer", "pc", "comp", "computor", "loptop"));
        synonymDict.put("phone", Arrays.asList("mobile", "smartphone", "cell", "cellphone", "iphone", "android", "phn", "moblie"));
        synonymDict.put("tablet", Arrays.asList("ipad", "tab", "android tablet", "tabllet"));
        synonymDict.put("earbuds", Arrays.asList("earphones", "headphones", "airpods", "earpods", "headsets", "buds"));
        synonymDict.put("charger", Arrays.asList("cable", "cord", "usb", "power adapter", "chargr", "chager"));
        synonymDict.put("watch", Arrays.asList("smartwatch", "fitbit", "applewatch", "wacth"));

        // ----- Personal Items -----
        synonymDict.put("wallet", Arrays.asList("purse", "billfold", "card holder", "walet", "wallt"));
        synonymDict.put("keys", Arrays.asList("keychain", "housekey", "car key", "room key", "keyz"));
        synonymDict.put("bag", Arrays.asList("backpack", "tote", "purse", "handbag", "satchel", "bckpack", "bakpak"));
        synonymDict.put("id card", Arrays.asList("student card", "utorid", "identification", "license", "ID", "id", "idd"));
        synonymDict.put("passport", Arrays.asList("travel document", "passprt", "pasport"));

        // ----- Clothing and Accessories -----
        synonymDict.put("jacket", Arrays.asList("coat", "hoodie", "windbreaker", "jaket", "jakit"));
        synonymDict.put("glasses", Arrays.asList("spectacles", "shades", "sunglasses", "eyeglasses", "glass"));
        synonymDict.put("hat", Arrays.asList("cap", "beanie", "bucket hat"));
        synonymDict.put("scarf", Arrays.asList("muffler"));
        synonymDict.put("umbrella", Arrays.asList("brolly", "umbrlla"));

        // ----- Study Supplies -----
        synonymDict.put("notebook", Arrays.asList("journal", "pad", "book"));
        synonymDict.put("pen", Arrays.asList("pencil", "marker", "highlighter", "stylus", "writing tool"));
        synonymDict.put("textbook", Arrays.asList("coursebook", "txtbook", "cbook"));

        // ----- Cards -----
        synonymDict.put("credit card", Arrays.asList("bank card", "visa", "mastercard", "debit card", "ccard"));
        synonymDict.put("student card", Arrays.asList("id", "tcard", "utorid", "campus card"));
    }

    public static List<String> expand(String word) {
        List<String> result = new ArrayList<>();
        result.add(word.toLowerCase());
        result.addAll(synonymDict.getOrDefault(word.toLowerCase(), new ArrayList<>()));
        return result;
    }
}
