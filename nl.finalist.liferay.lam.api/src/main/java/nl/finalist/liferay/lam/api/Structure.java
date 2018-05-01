package nl.finalist.liferay.lam.api;

import java.util.Locale;
import java.util.Map;

import org.osgi.framework.Bundle;

public interface Structure {
    /**
     * Creates or updates a structure if it already exists.
     *
     * @param siteKey   The String siteKey that will be used to place the ADT in the right side.
     *                  If not provided the ADT will be placed in default space.
     * @param structureKey The string structurekey of the structure which will be used for further identifying purposes
     * @param fileUrl The String relative url to the Structure json file
     * @param bundle The Bundle containing the json structure file
     * @param nameMap   The Map<Locale, String> nameMap containing the localized titles of the structure
     * @param descriptionMap    The Map<Locale, String> descriptionMap containing the localized descriptions of the structure
     */
    void createOrUpdateStructure(String siteKey, String structureKey, String fileUrl, Bundle bundle, Map<Locale, String> nameMap, Map<Locale, String> descriptionMap);
}
