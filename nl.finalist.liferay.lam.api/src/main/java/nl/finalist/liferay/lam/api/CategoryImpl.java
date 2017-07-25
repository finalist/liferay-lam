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
import com.liferay.portal.kernel.util.PropsUtil;

/**
 * Implementation for {@link nl.finalist.liferay.lam.api.Category}
 */
@Component(immediate = true, service = Category.class)
public class CategoryImpl implements Category {

	private static final Log LOG = LogFactoryUtil.getLog(CategoryImpl.class);

	private static final Locale NL_LOCALE = new Locale("nl", "NL");
	private Company defaultCompany;

	@Reference
	private CompanyLocalService companyService;

	@Reference
	private AssetCategoryLocalService assetCategoryService;

	@Reference
	private AssetVocabularyLocalService assetVocabularyLocalService;

	@Override
	public void addCategory(String categoryName, String vocabularyName, String title) {
		LOG.info(String.format(
				"Starting to add the category with Category name: %s , Vocabulary name: %s and Title: %s ",
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
				AssetCategory newCategory = assetCategoryService.addCategory(getDefaultUserId(), getGlobalGroupId(),
						categoryName, vocabulary.getVocabularyId(), new ServiceContext());
				LOG.info(String.format("Added the categroy with name : %S", newCategory.getName()));
			} else {
				LOG.error(String.format("Cannot add the category with category name: %s because it already exists",
						categoryName));
			}
			LOG.info(String.format("Added the category with categroy name: %s", categoryName));
		} catch (PortalException e) {
			LOG.error(String.format("addition of the category with name %s went wrong ", categoryName, e.getMessage()));
		}

	}

	@Override
	public void updateCategory(String categoryName, String vocabularyName, String updateName) {
		LOG.info(String.format(
				"Starting to update the category with name: %s to be updated to name : %s for  vocabulary: %s ",
				categoryName, vocabularyName, updateName));
		try {
			AssetVocabulary vocabulary = assetVocabularyLocalService.getGroupVocabulary(getGlobalGroupId(),
					vocabularyName);
			List<AssetCategory> existingCategories = vocabulary.getCategories();
			for (AssetCategory existingCategory : existingCategories) {
				if (existingCategory.getName().equals(categoryName)) {
					LOG.info(String.format("Found the category with the following details %s", existingCategory));
					Map<Locale, String> titleMap = new HashMap<Locale, String>();
					titleMap.put(NL_LOCALE, existingCategory.getName());
					existingCategory.setTitleMap(titleMap);
					existingCategory.setName(updateName);

					AssetCategory updatedCategory = assetCategoryService.updateAssetCategory(existingCategory);
					LOG.info(String.format("Updated the category with name: %s", updatedCategory.getName()));
					return;

				}

			}
			LOG.info(String.format("Category doesnot exists with name: %s", categoryName));

		} catch (PortalException e) {
			LOG.info(String.format("Update of a category with name: %s went wrong %s", categoryName, e.getMessage()));
		}

	}

	@Override
	public void deleteCategory(String categoryName, String vocabularyName) {
		LOG.info(String.format("Starting to delete the category with name: %s for the vocabulary %s", categoryName,
				vocabularyName));
		try {
			AssetVocabulary vocabulary = assetVocabularyLocalService.getGroupVocabulary(getGlobalGroupId(),
					vocabularyName);
			List<AssetCategory> existingCategories = vocabulary.getCategories();
			for (AssetCategory existingCategory : existingCategories) {
				if (existingCategory.getName().equals(categoryName)) {
					assetCategoryService.deleteCategory(existingCategory);
					LOG.info(String.format("deletion of  the category with name: %s is successful", existingCategory));
					return;
				}

			}
			LOG.info(String.format("Category doesn't exist with name: %s , so deletion is not possible", categoryName));
		} catch (PortalException e) {
			LOG.info(String.format("Deletion of a category with name: %s went wrong %s", categoryName, e.getMessage()));
		}

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

}
