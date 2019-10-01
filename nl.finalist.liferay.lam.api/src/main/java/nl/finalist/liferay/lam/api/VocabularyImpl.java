package nl.finalist.liferay.lam.api;

import com.liferay.asset.kernel.exception.DuplicateVocabularyException;
import com.liferay.asset.kernel.model.AssetVocabulary;
import com.liferay.asset.kernel.service.AssetVocabularyLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * Implementation for {@link nl.finalist.liferay.lam.api.Vocabulary}
 */
@Component(immediate = true, service = Vocabulary.class)
public class VocabularyImpl implements Vocabulary {

    @Reference
    private AssetVocabularyLocalService vocabularyService;

    @Reference
    private DefaultValue defaultValue;

    @Reference
    private CompanyLocalService companyService;

    private static final Log LOG = LogFactoryUtil.getLog(VocabularyImpl.class);

    @Override
    public void addVocabulary(String[] webIds, Map<Locale, String> vocabularyName) {

        if (ArrayUtil.isNotEmpty(webIds)) {
            for (String webId : webIds) {
                addVocabulary(webId, vocabularyName);
            }
        } else {
            String webId = defaultValue.getDefaultCompany().getWebId();
            addVocabulary(webId, vocabularyName);
        }
    }

    private void addVocabulary(String webId, Map<Locale, String> vocabularyName) {

        long userId = 0;
        long groupId = 0;
        try {
            Company company = companyService.getCompanyByWebId(webId);
            userId = company.getDefaultUser().getUserId();
            groupId = company.getGroupId();
        } catch (PortalException e) {
            LOG.error(String.format("Company not found with webId %s, skipping Add Vocabulary for this company", webId));
            LOG.error(e);
        }
        if (groupId > 0 && userId > 0) {
            try {
                LOG.debug(String.format("Vocabulary Name to be addded is %s", vocabularyName));

                vocabularyService.addVocabulary(userId, groupId, null, vocabularyName, new HashMap<>(), "", new ServiceContext());
                LOG.info(String.format("Added vocabulary %s to group %d", vocabularyName, groupId));
            } catch (DuplicateVocabularyException e) {
                LOG.info(String.format("Vocabulary %s already exists in group %d", vocabularyName, groupId));
            } catch (PortalException e) {
                LOG.error(String.format("Error while adding vocabulary %s in group %d", vocabularyName, groupId), e);
            }
        }
    }

    @Override
    public void deleteVocabulary(String[] webIds, String vocabularyName) {

        if (ArrayUtil.isNotEmpty(webIds)) {
            for (String webId : webIds) {
                deleteVocabulary(webId, vocabularyName);
            }
        } else {
            String webId = defaultValue.getDefaultCompany().getWebId();
            deleteVocabulary(webId, vocabularyName);
        }

        LOG.info(String.format("Deleted vocabulary %s", vocabularyName));
    }

    private void deleteVocabulary(String webId, String vocabularyName) {

        Company company = null;
        long groupId = 0;
        try {
            company = companyService.getCompanyByWebId(webId);
            groupId = company.getGroupId();

        } catch (PortalException e) {
            LOG.error(String.format("Company not found with webId %s, skipping Delete Vocabulary for this company", webId));
            LOG.error(e);
        }
        if (company != null && groupId > 0) {
            AssetVocabulary vocabulary = getAssetVocabulary(vocabularyName, groupId);
            if (Validator.isNotNull(vocabulary)) {
                try {
                    vocabularyService.deleteAssetVocabulary(vocabulary.getVocabularyId());
                    LOG.info(String.format("Deleted vocabulary %s from group %d", vocabularyName, groupId));
                } catch (PortalException e) {
                    LOG.error(String.format("Error while deleting vocabulary %s from group %d", vocabularyName, groupId), e);
                }
            } else {
                LOG.info(String.format("Vocabulary %s with groupId %d does not exist or is not retrievable", vocabularyName, groupId));
            }
        }
    }

    @Override
    public void updateVocabularyTranslation(String[] webIds, String existingName, Map<Locale, String> vocabularyName) {

        if (ArrayUtil.isNotEmpty(webIds)) {
            for (String webId : webIds) {
                updateVocabularyTranslation(webId, existingName, vocabularyName);
            }
        } else {
            String webId = defaultValue.getDefaultCompany().getWebId();
            updateVocabularyTranslation(webId, existingName, vocabularyName);
        }
        LOG.info(String.format("Updated vocabulary %s to add translation", vocabularyName));
    }

    private void updateVocabularyTranslation(String webId, String existingName, Map<Locale, String> vocabularyName) {

        Company company = null;
        long groupId = 0;
        try {
            company = companyService.getCompanyByWebId(webId);
            groupId = company.getGroupId();

        } catch (PortalException e) {
            LOG.error(String.format("Company not found with webId %s, skipping Update Vocabulary for this company", webId));
            LOG.error(e);
        }
        if (company != null && groupId > 0) {
            AssetVocabulary vocabulary = getAssetVocabulary(existingName, groupId);
            if (vocabulary != null) {
                vocabulary.setTitleMap(vocabularyName);
                vocabularyService.updateAssetVocabulary(vocabulary);
                LOG.info(String.format("Updated vocabulary %s from group %d", vocabularyName, groupId));
            } else {
                LOG.debug(String.format("Vocabulary %s with groupId %d does not exist or is not retrievable", vocabularyName, groupId));
            }
        }

    }

    private AssetVocabulary getAssetVocabulary(String vocabularyName, long groupId) {
        AssetVocabulary vocabulary = null;
        try {
            vocabulary = vocabularyService.getGroupVocabulary(groupId, vocabularyName);
        } catch (PortalException e) {
            LOG.error(String.format("Error while retrieving vocabulary %s", vocabularyName), e);
        }
        return vocabulary;
    }

    @Reference
    public void setCompanyLocalService(CompanyLocalService companyLocalService) {
        this.companyService = companyLocalService;
    }
}
