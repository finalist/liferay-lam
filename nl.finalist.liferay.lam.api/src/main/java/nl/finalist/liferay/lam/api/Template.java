package nl.finalist.liferay.lam.api;

import java.util.Locale;
import java.util.Map;

import org.osgi.framework.Bundle;

public interface Template {
    void createOrUpdateTemplate(String templateKey, String fileUrl, Bundle bundle, String structureKey, Map<Locale, String> nameMap, Map<Locale, String> descriptionMap);
}
