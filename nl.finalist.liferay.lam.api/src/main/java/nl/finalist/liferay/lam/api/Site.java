package nl.finalist.liferay.lam.api;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public interface Site {
	 /**
     * Adds a site.
     *
     * @param nameMap
     *            a map of name with respective Locale
     * @param descriptionMap
     *            a map of description with respective Locale
     * @param friendlyURL
     *            a friendlyURL for the site
     */
	void addSite(Map<Locale, String> nameMap, Map<Locale, String> descriptionMap, String friendlyURL, Map<String, String> customFields, List<PageModel> pages);
	
	 /**
     * Update a site.
     *
     * @param groupKey
     *            The site-id of the site to be update
     * @param nameMap
     *            a map of name with respective Locale
     * @param descriptionMap
     *            a map of description with respective Locale
     * @param friendlyURL
     *            a friendlyURL for the site
     */
	void updateSite(String groupKey, Map<Locale, String> nameMap, Map<Locale, String> descriptionMap,
			String friendlyURL, Map<String, String> customFields, List<PageModel> pages);
	
	 /**
     * Deletes a site
     *
     * @param groupKey
     *            the site-d of the site to be deleted
     */
	void deleteSite(String groupKey);

}
