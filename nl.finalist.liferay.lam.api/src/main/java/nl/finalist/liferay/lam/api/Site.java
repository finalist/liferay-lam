package nl.finalist.liferay.lam.api;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import nl.finalist.liferay.lam.api.model.PageModel;

public interface Site {

	void addSite(Map<Locale, String> nameMap, Map<Locale, String> descriptionMap, String friendlyURL, Map<String, String> customFields, List<PageModel> pages);

	void updateSite(String groupKey, Map<Locale, String> nameMap, Map<Locale, String> descriptionMap,
			String friendlyURL, Map<String, String> customFields, List<PageModel> pages);

	void deleteSite(String groupKey);

}
