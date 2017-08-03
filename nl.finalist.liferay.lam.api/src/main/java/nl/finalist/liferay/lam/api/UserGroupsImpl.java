package nl.finalist.liferay.lam.api;

import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.UserGroup;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserGroupLocalService;

@Component(immediate = true, service = UserGroups.class)
public class UserGroupsImpl implements UserGroups {

    @Reference
    private UserGroupLocalService userGroupLocalService;
    
    @Reference
    private CustomFields customFieldsService;
    
    @Reference
    private DefaultValue defaultValue;

    private static final Log LOG = LogFactoryUtil.getLog(UserGroupsImpl.class);

    @Override
    public void addUserGroup(String name, String description, Map<String, String> customFields) {
        try {
        	UserGroup group = userGroupLocalService.fetchUserGroup(defaultValue.getDefaultCompany().getCompanyId(), name);
        	if (group != null) {
        		LOG.debug("The user group already exists");
        	} else {
				group = userGroupLocalService.addUserGroup(defaultValue.getDefaultUserId(), defaultValue.getDefaultCompany().getCompanyId(), name, description, new ServiceContext());
        	}
        	LOG.info(String.format("Added user group %s", name));

        	if (customFields != null) {
	            for (String fieldName : customFields.keySet()) {
	                customFieldsService.addCustomFieldValue(UserGroup.class.getName(), fieldName, group.getPrimaryKey(), customFields.get(fieldName));
	            }
        	}
		} catch (PortalException e) {
			LOG.error("Adding the user group failed", e);
		}
    }
}
