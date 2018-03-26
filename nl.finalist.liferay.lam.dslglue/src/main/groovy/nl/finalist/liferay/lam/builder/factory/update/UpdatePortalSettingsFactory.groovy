package nl.finalist.liferay.lam.builder.factory.update

import com.liferay.portal.kernel.log.Log
import com.liferay.portal.kernel.log.LogFactoryUtil
import com.liferay.portal.kernel.util.Validator
import nl.finalist.liferay.lam.api.PortalSettings

import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

class UpdatePortalSettingsFactory extends AbstractFactory {

    private static final Log LOG = LogFactoryUtil.getLog(UpdatePortalSettingsFactory.class);
    PortalSettings portalSettingsservice

    UpdatePortalSettingsFactory(PortalSettings portalSettingsService) {
        this.portalSettingsservice = portalSettingsService
    }

    @SuppressWarnings("unchecked")
    @Override
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes)
                    throws InstantiationException, IllegalAccessException {
        attributes.forEach { key, entry -> callUpdateMethod((String) key, entry)};
        return attributes;
    }

    @Override
    void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        super.onNodeCompleted(builder, parent, node);
    }

    private void callUpdateMethod(String key, Object value) {
        Method method = getMethodIfExists(getMethodName(key));
        if (Validator.isNotNull(method)) {
            try {
                method.invoke(portalSettingsservice, value);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                LOG.error(String.format("The set method for %s can not be invoked with %s", key, value));
            }
        }
    }

    private Method getMethodIfExists(String methodName){
        Method method = null;
        try {
            Class<?> c = portalSettingsservice.getClass();
            method = c.getDeclaredMethod(methodName, String.class);
        } catch (NoSuchMethodException | SecurityException e) {
            LOG.error(String.format("The method %s is not supported", methodName));
        }
        return method;
    }

    private String getMethodName(String attributeName) {
        return String.format("set%s%s", attributeName.substring(0, 1).toUpperCase(),attributeName.substring(1));
    }
}
