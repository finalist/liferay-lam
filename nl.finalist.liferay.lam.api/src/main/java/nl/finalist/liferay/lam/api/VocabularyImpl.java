package nl.finalist.liferay.lam.api;

import com.liferay.asset.kernel.model.AssetVocabulary;
import com.liferay.asset.kernel.service.AssetVocabularyLocalService;
import com.liferay.counter.kernel.service.CounterLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.StringUtil;
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
    private CounterLocalService counterService;
    @Reference
    private CompanyLocalService companyService;
    @Reference
    private UserLocalService userService;

    private Company defaultCompany;

    private static final Log LOG = LogFactoryUtil.getLog(VocabularyImpl.class);

    @Override
    public void addVocabulary(String vocabularyName) {
        long groupId = getGlobalGroupId();
        addVocabulary(vocabularyName, groupId);
    }

    @Override
    public void addVocabulary(String vocabularyName, long groupId) {
        defaultCompany = getDefaultCompany();
        long userId = getDefaultUserId();
        Locale locale = null;
        Map<Locale, String> titleMap = new HashMap<>();
        try {
            locale = LocaleUtil.getSiteDefault();
            titleMap.put(locale, vocabularyName);
            vocabularyService.addVocabulary(userId, groupId, vocabularyName, titleMap,
                            new HashMap<Locale, String>(), "", new ServiceContext());
        } catch (PortalException e) {
            LOG.error(String.format("Error while adding vocabulary %s, error is %s", vocabularyName, e.getMessage()));
        }

    }

    @Override
    public void deleteVocabulary(String vocabularyName) {
        long groupId = getGlobalGroupId();
        deleteVocabulary(vocabularyName, groupId);

    }

    @Override
    public void deleteVocabulary(String vocabularyName, long groupId) {
        AssetVocabulary vocabulary = getAssetVocabulary(vocabularyName, groupId);
        if (Validator.isNotNull(vocabulary)) {
            try {
                vocabularyService.deleteAssetVocabulary(vocabulary.getVocabularyId());
            } catch (PortalException e) {
                LOG.error(String.format("Error while deleting vocabulary %s, error is %s", vocabularyName,
                                e.getMessage()));
            }
        } else {
            LOG.debug(String.format("Vocabulary %s with groupId %d does not exist or is not retrievable",
                            vocabularyName, groupId));
        }

    }

    @Override
    public void updateVocabularyTranslation(String languageId, String translatedName, String vocabularyName) {
        long groupId = getGlobalGroupId();
        updateVocabularyTranslation(languageId, translatedName, vocabularyName, groupId);

    }

    @Override
    public void updateVocabularyTranslation(String languageId, String translatedName, String vocabularyName,
                    long groupId) {
        AssetVocabulary vocabulary = getAssetVocabulary(vocabularyName, groupId);
        if (Validator.isNotNull(vocabulary)) {
            String[] languageAndCountry = StringUtil.split(languageId, "_");
            Locale locale = null;
            Map<Locale, String> titleMap = vocabulary.getTitleMap();
            if (languageAndCountry.length > 1) {
                locale = new Locale(languageAndCountry[0], languageAndCountry[1]);
            } else {
                locale = new Locale(languageAndCountry[0]);
            }
            titleMap.put(locale, translatedName);
            vocabulary.setTitleMap(titleMap);
            vocabularyService.updateAssetVocabulary(vocabulary);
        } else {
            LOG.debug(String.format("Vocabulary %s with groupId %d does not exist or is not retrievable",
                            vocabularyName, groupId));
        }

    }

    private AssetVocabulary getAssetVocabulary(String vocabularyName, long groupId) {
        AssetVocabulary vocabulary = null;
        try {
            vocabulary = vocabularyService.getGroupVocabulary(groupId, vocabularyName);
        } catch (PortalException e) {
            LOG.error(String.format("Error while retrieving vocabulary %s, error is %s", vocabularyName,
                            e.getMessage()));
        }
        return vocabulary;
    }

    private Company getDefaultCompany() {
        if (defaultCompany == null) {
            String webId = PropsUtil.get("company.default.web.id");
            try {
                defaultCompany = companyService.getCompanyByWebId(webId);
            } catch (PortalException e) {
                LOG.error(String.format("Error while retrieving default company, error is %s", e.getMessage()));
            }
        }
        return defaultCompany;
    }

    private long getDefaultUserId() {
        defaultCompany = getDefaultCompany();
        long userId = 0;
        try {
            userId = defaultCompany.getDefaultUser().getUserId();
        } catch (PortalException e) {
            LOG.error(String.format("Error while retrieving default userId, error is %s", e.getMessage()));
        }
        return userId;
    }

    private long getGlobalGroupId() {
        defaultCompany = getDefaultCompany();
        long groupId = 0;
        try {
            groupId = defaultCompany.getGroupId();
        } catch (PortalException e) {
            LOG.error(String.format("Error while retrieving global groupId, error is %s", e.getMessage()));
        }
        return groupId;
    }

}
