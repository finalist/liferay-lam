package nl.finalist.liferay.lam.api;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.util.PropsUtil;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;


@Component(immediate = true, service = DefaultValue.class)
public class DefaultValueImpl implements DefaultValue {
    private static final Log LOG = LogFactoryUtil.getLog(DefaultValueImpl.class);
    @Reference
    private CompanyLocalService companyService;
    private Company defaultCompany;

    @Override
    public long getDefaultUserId() {
        defaultCompany = getDefaultCompany();
        long userId = 0;
        try {
            userId = defaultCompany.getDefaultUser().getUserId();
        } catch (PortalException e) {
            LOG.error(String.format("Error while retrieving default userId, error is %s", e.getMessage()));
        }
        return userId;
    }


    @Override
    public Company getDefaultCompany() {
        defaultCompany = null;
        String webId = PropsUtil.get("company.default.web.id");
        try {
            defaultCompany = companyService.getCompanyByWebId(webId);
        } catch (PortalException e) {
            LOG.error(String.format("Error while retrieving default company, error is %s", e.getMessage()));
        }
        return defaultCompany;
    }

    @Override
    public long getGlobalGroupId() {
        defaultCompany = getDefaultCompany();
        long groupId = 0;
        try {
            groupId = defaultCompany.getGroupId();
        } catch (PortalException e) {
            LOG.error("Error while retrieving global groupId", e);
        }
        return groupId;
    }
}
