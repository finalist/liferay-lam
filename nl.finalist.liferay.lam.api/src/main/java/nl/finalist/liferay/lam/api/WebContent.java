/**
 *
 */
package nl.finalist.liferay.lam.api;

import java.util.Locale;
import java.util.Map;

public interface WebContent {
    /**
     * Adds webcontent to the Global group/site
     *
     * @param titleMap
     *            map of locale and title to be added
     * @param descriptionMap
     *            map of locale and description to be added
     * @param content
     *            content of the web content to be added.
     * @param urlTitle
     *            urlTitle of the Existing added
     */
    void addWebContent(Map<Locale, String> titleMap, Map<Locale, String> descriptionMap, String content,
                    String urlTitle);

    /**
     * updates webcontent to the Global group/site
     *
     * @param newTitle
     *            map of locale and title to be updated
     * @param descriptionMap
     *            map of locale and description to be updated
     * @param content
     *            content of the web content to be updated.
     * @param urlTitle
     *            urlTitle of the Existing webcontent
     */
    void updateWebContent(Map<Locale, String> newTitle, Map<Locale, String> descriptionMap, String content,
                    String urlTitle);

    /**
     * deletes webcontent in the Global group/site
     *
     * @param urlTitle
     *            the web content article's accessible URL title
     */
    void deleteWebContent(String urlTitle);
}
