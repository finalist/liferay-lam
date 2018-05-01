package nl.finalist.liferay.lam.api;

import java.util.Locale;
import java.util.Map;

import org.osgi.framework.Bundle;

public interface ADT {
    /**
     * Creates a new ADT if it an adt with this key does not exist, or updates it if it does exist.
     * @param siteKey   The String siteKey that will be used to place the ADT in the right side.
     *                  If not provided the ADT will be placed in default space.
     * @param adtKey    The String adtkey that will be used to identify this adt
     * @param fileUrl   The String relative url of the ADT script
     * @param bundle    The Bundle containing the ADT script file
     * @param className The fully qualified String class name for which the
     *                  adt can be used (e.g. com.liferay.asset.kernel.model.AssetEntry for the asset publisher)
     * @param nameMap   The Map<Locale, String> containing the locale & titles of the ADT
     * @param descriptionMap    The Map<Locale, String> containing the locale & descriptions of the ADT
     */
    void createOrUpdateADT(String siteKey, String adtKey,String fileUrl, Bundle bundle, String className,
                    Map<Locale, String> nameMap, Map<Locale, String> descriptionMap);
}
