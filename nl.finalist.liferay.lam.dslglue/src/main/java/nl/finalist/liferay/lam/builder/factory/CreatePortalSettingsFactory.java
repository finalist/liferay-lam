package nl.finalist.liferay.lam.builder.factory;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.util.Map;

import groovy.util.AbstractFactory;
import groovy.util.FactoryBuilderSupport;

public class CreatePortalSettingsFactory extends AbstractFactory {

	private static final Log LOG = LogFactoryUtil.getLog(CreatePortalSettingsFactory.class);
	 
	@Override
	public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes)
			throws InstantiationException, IllegalAccessException {
	
			LOG.info("Initialize portal settings from groovy script");
		return null;
	}

}
