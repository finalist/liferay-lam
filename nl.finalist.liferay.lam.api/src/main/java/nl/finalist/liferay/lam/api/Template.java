package nl.finalist.liferay.lam.api;

import java.util.Locale;
import java.util.Map;

import org.osgi.framework.Bundle;

public interface Template {
    void createOrUpdateTemplate(String fileUrl, Bundle bundle, String forStructure, Map<Locale, String> nameMap, Map<Locale, String> descriptionMap);
}
