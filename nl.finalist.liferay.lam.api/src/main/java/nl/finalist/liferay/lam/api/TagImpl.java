package nl.finalist.liferay.lam.api;

import com.liferay.asset.kernel.model.AssetTag;
import com.liferay.asset.kernel.service.AssetTagLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
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

    @Override
    public void createTag(String name, String siteFriendlyURL) {
        long userId = defaultValue.getDefaultUserId();
        long groupId = getGroupId(siteFriendlyURL);
        AssetTag tag = getAssetTagIfExists(name, groupId);
        if(Validator.isNull(tag)){
            try {
                assetTagLocalService.addTag(userId, groupId, name, new ServiceContext());
                LOG.info(String.format("Tag %s succesfully added.", name));
            } catch (PortalException e) {
                LOG.error(String.format("PortalException while creating tag %s", name)+e);
            }
        }
        else{
            LOG.info(String.format("Tag %s already exists.", name));
        }
    }

    @Override
    public void deleteTag(String name, String siteFriendlyURL) {
        long groupId = getGroupId(siteFriendlyURL);
        AssetTag assetTag = getAssetTagIfExists(name, groupId);
        if(Validator.isNull(assetTag)){
            LOG.info(String.format("Tag %s does not exist", name));
        }
        else{
            assetTagLocalService.deleteAssetTag(assetTag);
            LOG.info(String.format("Tag %s successfully deleted", name));
        }

    }

    private AssetTag getAssetTagIfExists(String name, long groupId){
        AssetTag tag = assetTagLocalService.fetchTag(groupId, name);
        return tag;
    }

    private long getGroupId(String siteFriendlyURL){
        long companyId = defaultValue.getDefaultCompany().getCompanyId();
        long groupId = defaultValue.getGlobalGroupId();
        if(Validator.isNotNull(siteFriendlyURL)  && !Validator.isBlank(siteFriendlyURL)){
            Group group = groupLocalService.fetchFriendlyURLGroup(companyId, siteFriendlyURL);
            if(Validator.isNotNull(group)){
                groupId = group.getGroupId();
            }
            else{
                LOG.error(String.format("Site %s can not be found, tag is added to or deleted from global group", siteFriendlyURL));
            }
        }
        return groupId;

    }

}
