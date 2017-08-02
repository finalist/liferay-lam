package nl.finalist.liferay.lam.dslglue

class LocaleMapConverter {
    static Map<Locale,String> convert(Map<String, String> origin) {
        Map<Locale, String> result = new HashMap<>();
        for(String key: origin.keySet()) {
            String[] languageParts = key.split('_');
            Locale locale = new Locale(languageParts[0], languageParts[1]);
            result.put(locale, origin.get(key));
        }
        return result;
    }
}
