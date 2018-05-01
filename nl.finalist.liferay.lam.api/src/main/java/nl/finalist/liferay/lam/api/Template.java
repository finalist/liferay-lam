package nl.finalist.liferay.lam.api;

import java.util.Locale;
import java.util.Map;

import org.osgi.framework.Bundle;

public interface Template {
    /**
     * Creates a Web Content template or updates it if it does not exist yest
     *
     * @param siteKey   The String siteKey that will be used to place the ADT in the right side.
     *                  If not provided the ADT will be placed in default space.
     * @param templateKey The String templateKey that will be used as an identifier
     * @param fileUrl   The String relative url to the file containing the template script
     * @param bundle    The Bundle containing the template script file
     * @param structureKey The String structureKey for which the template has to be created
     * @param nameMap The Map<Locale, String> nameMap containing the localized titles of the template
     * @param descriptionMap The Map<Locale, String> description containing the localized descriptions of the template
     */
    void createOrUpdateTemplate(String siteKey,String templateKey, String fileUrl, Bundle bundle, String structureKey, Map<Locale, String> nameMap, Map<Locale, String> descriptionMap);
}
