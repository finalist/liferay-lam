package nl.finalist.liferay.lam.api;

import java.util.Locale;
import java.util.Map;

public interface Site {

	void addSite(Map<Locale, String> nameMap, Map<Locale, String> descriptionMap, String friendlyURL, Map<String, String> customFields);

	void updateSite(String groupKey, Map<Locale, String> nameMap, Map<Locale, String> descriptionMap,
			String friendlyURL, Map<String, String> customFields);

	void deleteSite(String groupKey);

}
