package nl.finalist.liferay.lam.api;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.liferay.asset.kernel.model.AssetCategory;
import com.liferay.asset.kernel.model.AssetVocabulary;
import com.liferay.asset.kernel.service.AssetCategoryLocalService;
import com.liferay.asset.kernel.service.AssetVocabularyLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.PropsUtil;

/**
 * Implementation for {@link nl.finalist.liferay.lam.api.Category}
 */
@Component(immediate = true, service = Category.class)
public class CategoryImpl implements Category {

	private static final Log LOG = LogFactoryUtil.getLog(CategoryImpl.class);

	private Company defaultCompany;

	@Reference
	private CompanyLocalService companyService;

	@Reference
	private AssetCategoryLocalService assetCategoryService;

	@Reference
	private AssetVocabularyLocalService assetVocabularyLocalService;

	@Override
	public void addCategory(String categoryName, String vocabularyName, String title) {
		LOG.debug(String.format(
				"Starting to add the category %s to vocabulary %s with title %s ",
				categoryName, vocabularyName, title));
		try {
			AssetVocabulary vocabulary = assetVocabularyLocalService.getGroupVocabulary(getGlobalGroupId(),
					vocabularyName);
			String[] existingCategoriesNames = assetCategoryService.getCategoryNames();
			boolean addCondition = true;
			for (String existingCategoryName : existingCategoriesNames) {
				if (existingCategoryName.equals(categoryName)) {
					addCondition = false;
				}
			}
			if (addCondition) {
				assetCategoryService.addCategory(getDefaultUserId(), getGlobalGroupId(),
						categoryName, vocabulary.getVocabularyId(), new ServiceContext());
				LOG.info(String.format("Added category %s to vocabulary %s", categoryName, vocabularyName));
			} else {
				LOG.info(String.format("Cannot add category %s because it already exists", categoryName));
			}
		} catch (PortalException e) {
			LOG.error(String.format("adding category %s failed", categoryName),e);
		}

	}

	@Override
	public void updateCategory(String categoryName, String vocabularyName, String updateName) {
		LOG.debug(String.format(
				"Starting to update category %s in vocabulary %s, the new name will be %s",
				categoryName, vocabularyName, updateName));
		try {
			AssetVocabulary vocabulary = assetVocabularyLocalService.getGroupVocabulary(getGlobalGroupId(),
					vocabularyName);
			List<AssetCategory> existingCategories = vocabulary.getCategories();
			for (AssetCategory existingCategory : existingCategories) {
				if (existingCategory.getName().equals(categoryName)) {
					Map<Locale, String> titleMap = new HashMap<Locale, String>();
					titleMap.put(LocaleUtil.getDefault(), updateName);
					existingCategory.setTitleMap(titleMap);
					existingCategory.setName(updateName);

					assetCategoryService.updateAssetCategory(existingCategory);
					LOG.debug(String.format(
							"Updated category %s in vocabulary %s, the new name is now %s",
							categoryName, vocabularyName, updateName));
					return;
				}

			}
			LOG.info(String.format("Category %s doesn't exist", categoryName));

		} catch (PortalException e) {
			LOG.error(String.format("Update of category %s failed", categoryName), e);
		}

	}

	@Override
	public void deleteCategory(String categoryName, String vocabularyName) {
		LOG.debug(String.format("Starting to delete category %s from vocabulary %s", categoryName,
				vocabularyName));
		try {
			AssetVocabulary vocabulary = assetVocabularyLocalService.getGroupVocabulary(getGlobalGroupId(),
					vocabularyName);
			List<AssetCategory> existingCategories = vocabulary.getCategories();
			for (AssetCategory existingCategory : existingCategories) {
				if (existingCategory.getName().equals(categoryName)) {
					assetCategoryService.deleteCategory(existingCategory);
					LOG.info(String.format("Deleted category %s from vocabulary %s", categoryName, vocabularyName));
					return;
				}

			}
			LOG.info(String.format("Category %s doesn't exist", categoryName));
		} catch (PortalException e) {
			LOG.error(String.format("Deleting category %s failed", categoryName), e);
		}

	}

	private long getGlobalGroupId() {
		defaultCompany = getDefaultCompany();
		long groupId = 0;
		try {
			groupId = defaultCompany.getGroupId();
		} catch (PortalException e) {
			LOG.error("Error while retrieving global groupId", e);
		}
		return groupId;
	}

	private long getDefaultUserId() {
		defaultCompany = getDefaultCompany();
		long userId = 0;
		try {
			userId = defaultCompany.getDefaultUser().getUserId();
		} catch (PortalException e) {
			LOG.error("Error while retrieving default userId", e);
		}
		return userId;
	}

	private Company getDefaultCompany() {
		if (defaultCompany == null) {
			String webId = PropsUtil.get("company.default.web.id");
			try {
				defaultCompany = companyService.getCompanyByWebId(webId);
			} catch (PortalException e) {
				LOG.error("Error while retrieving default company", e);
			}
		}
		return defaultCompany;
	}

}
