/**
 * 
 */
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
import com.liferay.asset.kernel.service.AssetCategoryLocalServiceUtil;
import com.liferay.asset.kernel.service.AssetVocabularyLocalService;
import com.liferay.asset.kernel.service.AssetVocabularyLocalServiceUtil;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.PropsUtil;

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
		LOG.info("Trying to add the category");
		try {
			AssetVocabulary vocabulary = assetVocabularyLocalService.getGroupVocabulary(getGlobalGroupId(),
					vocabularyName);
			String[] names = assetCategoryService.getCategoryNames();
			boolean mayAdd = true;
			for (String name : names) {
				if(name.equals(categoryName)){
					mayAdd = false;
				}
			}
			if(mayAdd){
				AssetCategory newCategory = assetCategoryService.addCategory(getDefaultUserId(), getGlobalGroupId(),
						categoryName, vocabulary.getVocabularyId(), new ServiceContext());
				LOG.info(String.format("Added the categroy %S", newCategory.getName()));
			} else {
				LOG.error("canot add the category. It exists");
			}
			LOG.info("added the category");
		} catch (PortalException e) {
			LOG.error(String.format("Something went wrong", e.getMessage()));
		}

	}


	@Override
	public void updateCategory(String categoryName, String vocabularyName, String updateName) {
		LOG.info("Trying to update the category");
		LOG.info(String.format("Category Name %s", categoryName));
		try {
			AssetVocabulary vocabulary = AssetVocabularyLocalServiceUtil.getGroupVocabulary(getGlobalGroupId(),
					vocabularyName);
			LOG.info(String.format("found the vocabulary %d", vocabulary.getCategories().size()));
			AssetCategory assetCategory = getCategoryByNameAndVocabularyId(categoryName, vocabulary.getVocabularyId());
			
			LOG.info(String.format("Found the category %s", assetCategory));
			Map<Locale, String> titleMap = new HashMap<Locale, String>();
			titleMap.put(NL_LOCALE, assetCategory.getName());
			assetCategory.setTitleMap(titleMap);

			Map<Locale, String> descriptionMap = new HashMap<Locale, String>();
			descriptionMap.put(NL_LOCALE, assetCategory.getName());
			assetCategory.setDescriptionMap(descriptionMap);
			assetCategory.setDescription("Testing the update");
			assetCategory.setName(updateName);
			
			AssetCategory updatedCategory = assetCategoryService.updateAssetCategory(assetCategory);
			LOG.info((String.format("Updated the category %s", updatedCategory.getName())));
		} catch (PortalException e) {
			LOG.info(String.format("Update of a category went wrong %s", e.getMessage()));
		}

	}

	@Override
	public void deleteCategory(String categoryName, String vocabularyName) {
		LOG.info("Trying to delete the category");
		try {
			AssetVocabulary vocabulary = AssetVocabularyLocalServiceUtil.getGroupVocabulary(getGlobalGroupId(),
					vocabularyName);

			AssetCategory category = getCategoryByNameAndVocabularyId(categoryName, vocabulary.getVocabularyId());
			if (category != null) {
				assetCategoryService.deleteCategory(category);
				LOG.info("deleted the category");
			} else {
				LOG.info("category doesn't exist");
			}

		} catch (PortalException e) {
			LOG.error(String.format(
					"Something went wrong while deleting or while finding the category .please check the info provided %s",
					e.getMessage()));
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

	private AssetCategory getCategoryByNameAndVocabularyId(String name, long vocabularyId) throws SystemException {
		Map<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("vocabularyId", vocabularyId);
		queryParams.put("name", name);

		return getCategoryByDynamicQuery(queryParams);
	}

	private AssetCategory getCategoryByDynamicQuery(Map<String, Object> queryParams) throws SystemException {
		DynamicQuery categoryQuery = DynamicQueryFactoryUtil.forClass(AssetCategory.class);
		for (String key : queryParams.keySet()) {
			categoryQuery.add(PropertyFactoryUtil.forName(key).eq(queryParams.get(key)));
		}
		List<?> categories = AssetCategoryLocalServiceUtil.dynamicQuery(categoryQuery);
		AssetCategory category = null;
		if (categories.size() > 0) {
			category = (AssetCategory) categories.get(0);
		}
		return category;
	}

}
