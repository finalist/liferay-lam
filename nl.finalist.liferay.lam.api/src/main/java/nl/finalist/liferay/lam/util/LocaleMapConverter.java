package nl.finalist.liferay.lam.util;

import java.util.*;

public class LocaleMapConverter {

	/**
	 * Converts a Map with keys of type String to a Map with a key of type Locale
	 * 
	 * @param origin Original map
	 * @return converted map
	 */
    public static Map<Locale,String> convert(Map<String, String> origin) {
        Map<Locale, String> result = new HashMap<>();
        if (origin != null) {
            for(String key: origin.keySet()) {
                String[] parts = key.split("_");
                Locale locale = new Locale.Builder().setLanguage(parts[0]).setRegion(parts[1]).build();
                result.put(locale, origin.get(key));
            }
        }
        return result;
    }
}