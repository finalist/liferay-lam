package nl.finalist.liferay.lam.api;

import com.liferay.asset.kernel.model.AssetTag;
import com.liferay.asset.kernel.service.AssetTagLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.Validator;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(immediate = true, service = Tag.class)
public class TagImpl implements Tag {

    private static final Log LOG = LogFactoryUtil.getLog(TagImpl.class);

    @Reference
    AssetTagLocalService assetTagLocalService;

    @Reference
    private GroupLocalService groupLocalService;

    @Reference
    private DefaultValue defaultValue;

    @Reference
    private CompanyLocalService companyService;

    @Override
    public void createTag(String[] webIds, String name, String siteFriendlyURL) {

        if (ArrayUtil.isNotEmpty(webIds)) {
            for (String webId : webIds) {
                createTagInCompany(webId, name, siteFriendlyURL);
            }
        } else {
            String webId = defaultValue.getDefaultCompany().getWebId();
            createTagInCompany(webId, name, siteFriendlyURL);
        }

    }

    private void createTagInCompany(String webId, String name, String siteFriendlyURL) {

        Company company = null;
        long userId = 0;
        long groupId = 0;
        try {
            company = companyService.getCompanyByWebId(webId);
            userId = company.getDefaultUser().getUserId();
            groupId = getGroupId(company.getCompanyId(), company.getGroupId(), siteFriendlyURL);
        } catch (PortalException e) {
            LOG.error(String.format("Company not found with webId %s, skipping Create Tag for this company", webId));
            LOG.error(e);
        }
        if (company != null && userId > 0 && groupId > 0) {

            AssetTag tag = getAssetTagIfExists(name, groupId);
            if (Validator.isNull(tag)) {
                try {
                    assetTagLocalService.addTag(userId, groupId, name, new ServiceContext());
                    LOG.info(String.format("Tag %s succesfully added in company with webId %s.", name, webId));
                } catch (PortalException e) {
                    LOG.error(String.format("PortalException while creating tag %s in company with web Id %s", name, webId) + e);
                }
            } else {
                LOG.info(String.format("Tag %s already exists.", name));
            }
        }

    }

    @Override
    public void deleteTag(String[] webIds, String name, String siteFriendlyURL) {

        if (ArrayUtil.isNotEmpty(webIds)) {
            for (String webId : webIds) {
                deleteTagInCompany(webId, name, siteFriendlyURL);
            }
        } else {
            String webId = defaultValue.getDefaultCompany().getWebId();
            deleteTagInCompany(webId, name, siteFriendlyURL);
        }

    }

    private void deleteTagInCompany(String webId, String name, String siteFriendlyURL) {
        Company company = null;
        long groupId = 0;
        try {
            company = companyService.getCompanyByWebId(webId);
            groupId = getGroupId(company.getCompanyId(), company.getGroupId(), siteFriendlyURL);

        } catch (PortalException e) {
            LOG.error(String.format("Company not found with webId %s, skipping Delete Tag for this company", webId));
            LOG.error(e);
        }
        AssetTag assetTag = getAssetTagIfExists(name, groupId);
        if (Validator.isNull(assetTag)) {
            LOG.info(String.format("Tag %s does not exist in company with webId %s", name, webId));
        } else {
            assetTagLocalService.deleteAssetTag(assetTag);
            LOG.info(String.format("Tag %s successfully deleted from company with webId %s", name, webId));
        }
    }

    private AssetTag getAssetTagIfExists(String name, long groupId) {
        return assetTagLocalService.fetchTag(groupId, name);
    }

    private long getGroupId(long companyId, long companyGroupId, String siteFriendlyURL) {
        if (Validator.isNotNull(siteFriendlyURL) && !Validator.isBlank(siteFriendlyURL)) {
            Group group = groupLocalService.fetchFriendlyURLGroup(companyId, siteFriendlyURL);
            if (Validator.isNotNull(group)) {
                companyGroupId = group.getGroupId();
            } else {
                LOG.error(String.format("Site %s can not be found, tag is added to or deleted from global group", siteFriendlyURL));
            }
        }
        return companyGroupId;

    }

    @Reference
    public void setCompanyLocalService(CompanyLocalService companyLocalService) {
        this.companyService = companyLocalService;
    }
}
