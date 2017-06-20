package nl.finalist.liferay.lam.api;

import java.io.IOException;
import java.util.Map;

public interface PortalValuesInterface {
	public boolean checkingPortalProperties(Map<String, String> propertyValues) throws IOException;

}
