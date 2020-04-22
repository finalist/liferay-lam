package nl.finalist.liferay.lam.api;

import java.util.Locale;
import java.util.Map;

import org.osgi.framework.Bundle;

public interface ADT {

    /**
     * Creates or updates an ADT(if it already exists) in provided webIds.
     * 
     * @param webIds
     *            The string array of webIds where ADT is to be created
     * @param adtKey
     *            The String adtkey that will be used to identify this adt
     * @param fileUrl
     *            The String relative url of the ADT script
     * @param bundle
     *            The Bundle containing the ADT script file
     * @param className
     *            The fully qualified String class name for which the adt can be
     *            used (e.g. com.liferay.asset.kernel.model.AssetEntry for the
     *            asset publisher)
     * @param nameMap
     *            The Map<Locale, String> containing the locale & titles of the
     *            ADT
     * @param descriptionMap
     *            The Map<Locale, String> containing the locale & descriptions
     *            of the ADT
     */
    void createOrUpdateADT(String[] webIds, String adtKey, String fileUrl, Bundle bundle, String className, Map<Locale, String> nameMap,
                           Map<Locale, String> descriptionMap);
}
