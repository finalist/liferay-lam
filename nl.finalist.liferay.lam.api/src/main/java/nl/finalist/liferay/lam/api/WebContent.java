/**
 *
 */
package nl.finalist.liferay.lam.api;

import java.util.Locale;
import java.util.Map;

import org.osgi.framework.Bundle;

public interface WebContent {

	/**
	 * Create or update webcontent.
	 * 
	 * @param articleId id of the webcontent
	 * @param siteFriendlyURL map of locale and site friendly urls
	 * @param titleMap map of locale and title
	 * @param fileUrl The String relative url to the webcontent xml file 
	 * @param bundle OSGi bundle containing the file
	 * @param urlTitle urlTitle of the webcontent
	 * @param structureKey Key of the structure that is used for this content
	 * @param templateKey Key of the template that is used for this content
	 */
    void createOrUpdateWebcontent(String articleId, String siteFriendlyURL, Map<Locale,String> titleMap, String fileUrl, Bundle bundle,
                    String urlTitle, String structureKey, String templateKey);
    /**
     * deletes webcontent in the Global group/site
     *
     * @param urlTitle
     *            the web content article's accessible URL title
     */
    void deleteWebContent(String urlTitle);
}
