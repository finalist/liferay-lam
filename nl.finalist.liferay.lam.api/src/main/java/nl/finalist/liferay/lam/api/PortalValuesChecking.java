package nl.finalist.liferay.lam.api;

import org.osgi.service.component.annotations.Component;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

@Component(immediate = true)
public class PortalValuesChecking {
	private static final Log LOG = LogFactoryUtil.getLog(PortalValuesChecking.class);

	public void checkingPortalProperties(Map<String, String> propertyValues) throws IOException {
		LOG.info(String.format("Start the comparing the portal property values"));

		Properties prop = new Properties();
		InputStream input = null;
		String filePath = new File("src\\main\\resources\\config.properties").getAbsolutePath();
		input = new FileInputStream(filePath);
		prop.load(input);

		String[] keys = propertyValues.keySet().stream().toArray(String[]::new);
		LOG.info(String.format("Set of key values %d", keys.length));

		for (String key : keys) {
			if (prop.getProperty(key) != null && propertyValues.get(key) != null
					&& prop.getProperty(key).equals(propertyValues.get(key))) {
				LOG.info("Passed the check");

			} else {
				if (prop.getProperty(key) == null) {
					LOG.info("The property doesn't exist in the first file");
				} else if (propertyValues.get(key) == null) {
					LOG.info("The property doesn't exist in the second file");
				} else {
					LOG.info(String.format("%s is this value for %s but should be %s", propertyValues.get(key), key,
							prop.getProperty(key)));
				}
			}
		}
		input.close();

	}

}
