package nl.finalist.liferay.lam.api;

import java.util.Locale;
import java.util.Map;

public interface Structure {

    void createStructure(String content, Map<Locale, String> nameMap, Map<Locale, String> descriptionMap);
}
