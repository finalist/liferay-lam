package nl.finalist.liferay.lam.api;

import java.util.Locale;
import java.util.Map;

import org.osgi.framework.Bundle;

public interface ADT {
    void createOrUpdateADT(String adtKey,String fileUrl, Bundle bundle, String className, Map<Locale, String> nameMap, Map<Locale, String> descriptionMap);
}
