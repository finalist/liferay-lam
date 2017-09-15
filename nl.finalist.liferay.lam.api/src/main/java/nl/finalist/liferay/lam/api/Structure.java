package nl.finalist.liferay.lam.api;

import java.util.Locale;
import java.util.Map;

import org.osgi.framework.Bundle;

public interface Structure {
    /**
     * Creates or updates a structure if it already exists.
     * @param fileUrl The String relative url to the file
     * @param bundle The bundle that has the structure in the resources folder
     * @param nameMap   The nameMap of the structure
     * @param descriptionMap    The descriptionMap of the structure
     */
    void createOrUpdateStructure(String fileUrl, Bundle bundle, Map<Locale, String> nameMap, Map<Locale, String> descriptionMap);
}
