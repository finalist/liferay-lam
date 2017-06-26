package nl.finalist.liferay.lam.api;

import java.io.IOException;
import java.util.Map;

public interface PortalProperties {
	public boolean validatePortalProperties(Map<String, String> propertyValues) throws IOException;

}
