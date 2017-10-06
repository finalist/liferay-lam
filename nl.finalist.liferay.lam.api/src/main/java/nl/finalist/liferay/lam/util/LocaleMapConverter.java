package nl.finalist.liferay.lam.util;

import java.util.*;

public class LocaleMapConverter {

    public static Map<Locale,String> convert(Map<String, String> origin) {
        if (origin == null) {
            return null;
        }
        Map<Locale, String> result = new HashMap<>();
        for(String key: origin.keySet()) {
            String[] parts = key.split("_");
            Locale locale = new Locale.Builder().setLanguage(parts[0]).setRegion(parts[1]).build();
            result.put(locale, origin.get(key));
        }
        return result;
    }
}