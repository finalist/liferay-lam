package nl.finalist.liferay.lam.api;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import nl.finalist.liferay.lam.api.model.PageModel;

public interface Site {

    /**
     * Adds a site.
     *
     * @param webIds
     *            an array of webIds where this site is to be created
     * @param nameMap
     *            a map of name with respective Locale
     * @param descriptionMap
     *            a map of description with respective Locale
     * @param friendlyURL
     *            a friendlyURL for the site
     */
    void addSite(String[] webIds, Map<Locale, String> nameMap, Map<Locale, String> descriptionMap, String friendlyURL,
                 Map<String, String> customFields, List<PageModel> pages);

    /**
     * Update a site.
     * 
     * @param webIds
     *            an array of webIds where this site is to be updated
     * @param groupKey
     *            The site-id of the site to be update
     * @param nameMap
     *            a map of name with respective Locale
     * @param descriptionMap
     *            a map of description with respective Locale
     * @param friendlyURL
     *            a friendlyURL for the site
     */
    void updateSite(String[] webIds, String groupKey, Map<Locale, String> nameMap, Map<Locale, String> descriptionMap, String friendlyURL,
                    Map<String, String> customFields, List<PageModel> pages, Boolean stagingEnabled);

    /**
     * Deletes a site
     * 
     * @param webIds
     *            an array of webIds from where this site is to be deleted
     * @param groupKey
     *            the site-d of the site to be deleted
     */
    void deleteSite(String[] webIds, String groupKey);

}
