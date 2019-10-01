package nl.finalist.liferay.lam.api;

import java.util.Locale;
import java.util.Map;

import org.osgi.framework.Bundle;

public interface Structure {

    /**
     * Creates or updates a structure(if it already exists) in provided webIds.
     * 
     * @param webIds
     *            The string array of webIds where structure is to be created
     * @param structureKey
     *            The string structurekey of the structure which will be used
     *            for further identifying purposes
     * @param fileUrl
     *            The String relative url to the Structure json file
     * @param bundle
     *            The Bundle containing the json structure file
     * @param nameMap
     *            The Map<Locale, String> nameMap containing the localized
     *            titles of the structure
     * @param descriptionMap
     *            The Map<Locale, String> descriptionMap containing the
     *            localized descriptions of the structure
     */
    void createOrUpdateStructure(String[] webIds, String structureKey, String fileUrl, Bundle bundle, Map<Locale, String> nameMap,
                                 Map<Locale, String> descriptionMap, String siteKey);
}
