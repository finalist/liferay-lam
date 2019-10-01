package nl.finalist.liferay.lam.api;

import java.util.Locale;
import java.util.Map;

import org.osgi.framework.Bundle;

public interface Template {

    /**
     * 
     * Creates or updates a Web Content template(if it already exists) in
     * provided webIds.
     * 
     * @param webIds
     *            The string array of webIds where tepmlate is to be created
     * @param templateKey
     *            The String templateKey that will be used as an identifier
     * @param fileUrl
     *            The String relative url to the file containing the template
     *            script
     * @param bundle
     *            The Bundle containing the template script file
     * @param structureKey
     *            The String structureKey for which the template has to be
     *            created
     * @param nameMap
     *            The Map<Locale, String> nameMap containing the localized
     *            titles of the template
     * @param descriptionMap
     *            The Map<Locale, String> description containing the localized
     *            descriptions of the template
     */
    void createOrUpdateTemplate(String[] webIds, String templateKey, String fileUrl, Bundle bundle, String structureKey, Map<Locale, String> nameMap,
                                Map<Locale, String> descriptionMap, String siteKey);
}
