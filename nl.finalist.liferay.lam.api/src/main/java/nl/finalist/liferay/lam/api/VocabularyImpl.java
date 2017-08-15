package nl.finalist.liferay.lam.api;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.liferay.asset.kernel.exception.DuplicateVocabularyException;
import com.liferay.asset.kernel.model.AssetVocabulary;
import com.liferay.asset.kernel.service.AssetVocabularyLocalService;
import com.liferay.counter.kernel.service.CounterLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.Validator;
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
    private UserLocalService userService;

    @Reference
    private DefaultValue defaultValue;

    private static final Log LOG = LogFactoryUtil.getLog(VocabularyImpl.class);

    @Override
    public void addVocabulary(Map<Locale, String> vocabularyName) {
        long groupId = defaultValue.getGlobalGroupId();
        addVocabulary(vocabularyName, groupId);
    }

    
    public void addVocabulary(Map<Locale, String>  vocabularyName, long groupId) {
        long userId = defaultValue.getDefaultUserId();
       
        try {
            LOG.debug(String.format("Vocabulary Name to be addded is %s", vocabularyName));
           
            vocabularyService.addVocabulary(userId, groupId, null, vocabularyName,
                            new HashMap<Locale, String>(), "", new ServiceContext());
            LOG.info(String.format("Added vocabulary %s to group %d", vocabularyName, groupId));
        } catch (DuplicateVocabularyException e) {
        	LOG.info(String.format("Vocabulary %s already exists in group %d", vocabularyName, groupId));
        } catch (PortalException e) {
            LOG.error(String.format("Error while adding vocabulary %s", vocabularyName), e);
        }

    }

    @Override
    public void deleteVocabulary(String vocabularyName) {
        long groupId = defaultValue.getGlobalGroupId();
        deleteVocabulary(vocabularyName, groupId);
        LOG.info(String.format("Deleted vocabulary %s", vocabularyName));
    }

    
    public void deleteVocabulary(String vocabularyName, long groupId) {
        AssetVocabulary vocabulary = getAssetVocabulary(vocabularyName, groupId);
        if (Validator.isNotNull(vocabulary)) {
            try {
                vocabularyService.deleteAssetVocabulary(vocabulary.getVocabularyId());
                LOG.info(String.format("Deleted vocabulary %s from group %d", vocabularyName, groupId));
            } catch (PortalException e) {
                LOG.error(String.format("Error while deleting vocabulary %s", vocabularyName), e);
            }
        } else {
            LOG.info(String.format("Vocabulary %s with groupId %d does not exist or is not retrievable",
                            vocabularyName, groupId));
        }
    }

    @Override
    public void updateVocabularyTranslation(String existingName, Map<Locale, String> vocabularyName) {
        long groupId = defaultValue.getGlobalGroupId();
        updateVocabularyTranslation(vocabularyName, groupId, existingName);
        LOG.info(String.format("Updated vocabulary %s to add translation", vocabularyName));
    }

    
    public void updateVocabularyTranslation(Map<Locale, String> vocabularyName,
                    long groupId, String existingName) {
        AssetVocabulary vocabulary = getAssetVocabulary(existingName, groupId);
        if (Validator.isNotNull(vocabulary)) {
           
            vocabulary.setTitleMap(vocabularyName);
            vocabularyService.updateAssetVocabulary(vocabulary);
            LOG.info(String.format("Updated vocabulary %s from group %d", vocabularyName, groupId));
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
            LOG.error(String.format("Error while retrieving vocabulary %s", vocabularyName), e);
        }
        return vocabulary;
    }
}
