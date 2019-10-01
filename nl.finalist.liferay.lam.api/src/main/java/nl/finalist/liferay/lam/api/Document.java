/**
 *
 */
package nl.finalist.liferay.lam.api;

import org.osgi.framework.Bundle;

public interface Document {

    /**
     * Create or update document in specific companies.
     * 
     * @param articleId
     *            id of the document
     * @param webIds
     *            WebIds of companies where document is to be created/updated.
     * @param siteFriendlyURL
     *            map of locale and site friendly urls
     * @param titleMap
     *            map of locale and title
     * @param fileUrl
     *            The String relative url to the document xml file
     * @param bundle
     *            OSGi bundle containing the file
     * @param urlTitle
     *            urlTitle of the document
     * @param structureKey
     *            Key of the structure that is used for this content
     * @param templateKey
     *            Key of the template that is used for this content
     */
    void createOrUpdateDocument(String[] webIds, String siteFriendlyURL, String title, String fileUrl, Bundle bundle);

    /**
     * deletes document in the Global group/site from companies having provided
     * webIds
     *
     * @param urlTitle
     *            the document's accessible URL title
     */
    void deleteDocument(String[] webIds, String title);
}
