package nl.finalist.liferay.lam.api;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.liferay.asset.kernel.model.AssetVocabulary;
import com.liferay.asset.kernel.service.AssetVocabularyLocalService;
import com.liferay.counter.kernel.service.CounterLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.PropsUtil;

@Component(immediate = true, service=Vocabulary.class)
public class VocabularyImpl implements Vocabulary {

	@Reference
	AssetVocabularyLocalService vocabularyService;
	@Reference
	CounterLocalService counterService;
	@Reference
	CompanyLocalService companyService;
	@Reference
	UserLocalService userService;

	private Company defaultCompany;
	private User defaultUser;

	private static final Log LOG = LogFactoryUtil.getLog(VocabularyImpl.class);

	/**
	 * Adds a vocabulary to the chosen group/site
	 * @param vocabularyName The name of the vocabulary
	 * @param groupId The groupId of the vocabulary 
	 */
	@Override
	public void addVocabulary(String vocabularyName, long groupId) {
		defaultCompany = getDefaultCompany();
		defaultUser = getDefaultUser();

		Map<Locale, String> titleMap = new HashMap<>();
		Set<Locale> locales = LanguageUtil.getAvailableLocales(groupId);
		for (Locale locale : locales) {
			titleMap.put(locale, vocabularyName);
		}
		try {
			vocabularyService.addVocabulary(defaultUser.getUserId(), groupId, null, titleMap,
					new HashMap<Locale, String>(), "", new ServiceContext());
		} catch (PortalException e) {
			LOG.error(String.format("Error while adding vocabulary %s, error is %s", vocabularyName, e.getMessage()));
		}
	}
	/**
	 * Deletes a vocabulary if it exists
	 * @param vocabularyName The name of the vocabulary
	 * @param groupId The groupId of the vocabulary 
	 */
	@Override
	public void deleteVocabulary(String vocabularyName, long groupId) {
		AssetVocabulary vocabulary = getAssetVocabulary(vocabularyName, groupId);
		if (Validator.isNotNull(vocabulary)) {
			try {
				vocabularyService.deleteAssetVocabulary(vocabulary.getVocabularyId());
			} catch (PortalException e) {
				LOG.error(String.format("Error while deleting vocabulary %s, error is %s", vocabularyName, e.getMessage()));
			}
		} else {
			LOG.debug(String.format("Vocabulary %s with groupId %d does not exist or is not retrievable",
					vocabularyName, groupId));
		}

	}
	/**
	 * Updates the name of the Vocabulary in the chosen locale
	 * @param languageId The languageId of the translation that has to be added (e.g. "nl_NL")
	 * @param translatedName The translated name that has to be added/ changed
	 * @param vocabularyName The name of the vocabulary
	 * @param groupId The groupId of the vocabulary 
	 */
	@Override
	public void updateVocabularyTranslation(String languageId, String translatedName, String vocabularyName, long groupId) {
		AssetVocabulary vocabulary = getAssetVocabulary(vocabularyName, groupId);
		if(Validator.isNotNull(vocabulary)){
			String[] languageAndCountry = StringUtil.split(languageId, "_");
			Locale locale = null;
			Map<Locale, String> titleMap = vocabulary.getTitleMap();
			if(languageAndCountry.length > 1){
				locale = new Locale(languageAndCountry[0], languageAndCountry[1]);
			}
			else{
				locale = new Locale(languageAndCountry[0]);
			}
			titleMap.put(locale, translatedName);
			vocabulary.setTitleMap(titleMap);
			vocabularyService.updateAssetVocabulary(vocabulary);
		}
		else{
			LOG.debug(String.format("Vocabulary %s with groupId %d does not exist or is not retrievable", vocabularyName, groupId));
		}
		
	}

	private AssetVocabulary getAssetVocabulary(String vocabularyName, long groupId) {
		AssetVocabulary vocabulary = null;
		try {
			vocabulary = vocabularyService.getGroupVocabulary(groupId, vocabularyName);
		} catch (PortalException e) {
			LOG.error(String.format("Error while retrieving vocabulary %s, error is %s", vocabularyName, e.getMessage()));
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

	private User getDefaultUser() {
		defaultCompany = getDefaultCompany();
		try {
			userService.getDefaultUser(defaultCompany.getCompanyId());
		} catch (PortalException e) {
			LOG.error(String.format("Error while retrieving default user, error is %s", e.getMessage()));
		}
		return defaultUser;
	}


}
