package nl.finalist.liferay.lam.api;

import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.util.Validator;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component
public class ScopeHelperImpl implements ScopeHelper {

    @Reference
    private DefaultValue defaultValue;

    @Reference
    private GroupLocalService groupService;

    @Override
    public long getGroupIdByName(String siteKey) {
        Group site = null;
        if (!Validator.isBlank(siteKey)) {
            site = groupService.fetchGroup(defaultValue.getDefaultCompany().getCompanyId(), siteKey);
        }
        return site != null ? site.getGroupId() : defaultValue.getGlobalGroupId();
    }
}
