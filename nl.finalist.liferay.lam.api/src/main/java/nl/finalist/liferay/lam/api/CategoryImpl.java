package nl.finalist.liferay.lam.api;

import com.liferay.asset.kernel.exception.DuplicateCategoryException;
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
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * Implementation for {@link nl.finalist.liferay.lam.api.Category}
 */
@Component(immediate = true, service = Category.class)
public class CategoryImpl implements Category {

    private static final Log LOG = LogFactoryUtil.getLog(CategoryImpl.class);

    @Reference
    private AssetCategoryLocalService assetCategoryService;

    @Reference
    private AssetVocabularyLocalService assetVocabularyLocalService;

    @Reference
    private DefaultValue defaultValue;

    @Reference
    private CompanyLocalService companyService;

    @Override
    public void addCategory(String[] webIds, Map<Locale, String> categoryName, String vocabularyName, String title, String parentCategoryName) {
        LOG.debug(String.format("Starting to add the category %s to vocabulary %s with title %s ", categoryName, vocabularyName, title));
        if (ArrayUtil.isNotEmpty(webIds)) {
            for (String webId : webIds) {
                createCategoryInCompany(webId, vocabularyName, title, parentCategoryName, categoryName);
            }
        } else {
            String webId = defaultValue.getDefaultCompany().getWebId();
            createCategoryInCompany(webId, vocabularyName, title, parentCategoryName, categoryName);
        }

    }

    private void createCategoryInCompany(String webId, String vocabularyName, String title, String parentCategoryName,
                                         Map<Locale, String> categoryName) {
        long groupId = 0;
        long defaultUserId = 0;
        try {
            Company company = companyService.getCompanyByWebId(webId);
            groupId = company.getGroupId();
            defaultUserId = company.getDefaultUser().getUserId();
        } catch (PortalException e) {
            LOG.error(String.format("Company not found with webId %s, skipping add category for this company", webId));
            LOG.error(e);
        }
        if (groupId > 0 && defaultUserId > 0) {
            try {
                AssetVocabulary vocabulary = assetVocabularyLocalService.getGroupVocabulary(groupId, vocabularyName);
                String[] existingCategoriesNames = assetCategoryService.getCategoryNames();
                boolean addCondition = true;
                for (String existingCategoryName : existingCategoriesNames) {
                    if (existingCategoryName.equals(categoryName)) {
                        addCondition = false;
                    }
                }
                if (addCondition) {
                    createCategory(groupId, defaultUserId, vocabularyName, title, parentCategoryName, categoryName, vocabulary);
                } else {
                    LOG.info(String.format("Cannot add category %s because it already exists in company with webId %s", categoryName, webId));
                }
            } catch (DuplicateCategoryException e1) {
                LOG.info(String.format("Cannot add category %s in company with webId %s because it already exists", categoryName, webId));
            } catch (PortalException e) {
                LOG.error(String.format("adding category %s in company with webId %s failed", categoryName, webId), e);
            }
        }
    }

    private void createCategory(long groupId, long defaultUserId, String vocabularyName, String title, String parentCategoryName,
                                Map<Locale, String> categoryName, AssetVocabulary vocabulary)
            throws PortalException {
        long parentCategoryId = 0;
        if (!Validator.isBlank(parentCategoryName)) {
            List<AssetCategory> existingCategories = vocabulary.getCategories();
            LOG.info(String.format("Category %s has a parent category %s, adding it as a nested category", title, parentCategoryName));
            parentCategoryId = existingCategories.stream()
                                                 .filter(assetCategory -> assetCategory.getName().equalsIgnoreCase(parentCategoryName))
                                                 .map(AssetCategory::getCategoryId)
                                                 .findFirst()
                                                 .orElse(0L);
        }
        assetCategoryService.addCategory(defaultUserId, groupId, parentCategoryId, categoryName, new HashMap<>(), vocabulary.getVocabularyId(),
                new String[0], new ServiceContext());

        LOG.info(String.format("Added category %s to vocabulary %s", categoryName, vocabularyName));
    }

    @Override
    public void updateCategory(String[] webIds, String categoryName, String vocabularyName, Map<Locale, String> updateName) {
        LOG.debug(
                String.format("Starting to update category %s in vocabulary %s, the new name will be %s", categoryName, vocabularyName, updateName));
        if (ArrayUtil.isNotEmpty(webIds)) {
            for (String webId : webIds) {
                updateCategoryInCompany(webId, categoryName, vocabularyName, updateName);
            }
        } else {
            String webId = defaultValue.getDefaultCompany().getWebId();
            updateCategoryInCompany(webId, categoryName, vocabularyName, updateName);
        }

    }

    private void updateCategoryInCompany(String webId, String categoryName, String vocabularyName, Map<Locale, String> updateName) {

        long groupId = 0;
        try {
            Company company = companyService.getCompanyByWebId(webId);
            groupId = company.getGroupId();
        } catch (PortalException e) {
            LOG.error(String.format("Company not found with webId %s, skipping update category for this company", webId));
            LOG.error(e);
        }

        if (groupId > 0) {

            try {
                AssetVocabulary vocabulary = assetVocabularyLocalService.getGroupVocabulary(groupId, vocabularyName);
                List<AssetCategory> existingCategories = vocabulary.getCategories();
                for (AssetCategory existingCategory : existingCategories) {
                    if (existingCategory.getName().equals(categoryName)) {

                        existingCategory.setTitleMap(updateName);

                        assetCategoryService.updateAssetCategory(existingCategory);
                        LOG.debug(String.format("Updated category %s in vocabulary %s in company with webId %s, the new name is now %s", categoryName,
                                vocabularyName, webId, updateName));
                        return;
                    }
                }
                LOG.info(String.format("Category %s doesn't exist in company with webId %s", categoryName, webId));

            } catch (Exception e) {
                LOG.error(String.format("Update of category %s failed in company with webId %s", categoryName, webId), e);
            }
        }

    }

    @Override
    public void deleteCategory(String[] webIds, String categoryName, String vocabularyName) {
        LOG.debug(String.format("Starting to delete category %s from vocabulary %s", categoryName, vocabularyName));

        if (ArrayUtil.isNotEmpty(webIds)) {
            for (String webId : webIds) {
                deleteCategory(webId, categoryName, vocabularyName);
            }
        } else {
            String webId = defaultValue.getDefaultCompany().getWebId();
            deleteCategory(webId, categoryName, vocabularyName);
        }

    }

    private void deleteCategory(String webId, String categoryName, String vocabularyName) {

        long groupId = 0;
        try {
            Company company = companyService.getCompanyByWebId(webId);
            groupId = company.getGroupId();
        } catch (PortalException e) {
            LOG.error(String.format("Company not found with webId %s, skipping delete category for this company", webId));
            LOG.error(e);
        }

        if (groupId > 0) {
            try {
                AssetVocabulary vocabulary = assetVocabularyLocalService.getGroupVocabulary(groupId, vocabularyName);
                List<AssetCategory> existingCategories = vocabulary.getCategories();
                LOG.debug(String.format("number of categories is %d ", existingCategories.size()));
                for (AssetCategory existingCategory : existingCategories) {
                    if (existingCategory.getName().equals(categoryName)) {
                        assetCategoryService.deleteCategory(existingCategory);
                        LOG.info(String.format("Deleted category %s from vocabulary %s from company with webId %s", categoryName, vocabularyName,
                                webId));
                        return;
                    }

                }
                LOG.info(String.format("Category %s doesn't exist in company with webId %s ", categoryName, webId));
            } catch (PortalException e) {
                LOG.error(String.format("Deleting category %s failed in company with webId %s ", categoryName, webId), e);
            }
        }

    }

    @Reference
    public void setCompanyLocalService(CompanyLocalService companyLocalService) {
        this.companyService = companyLocalService;
    }
}
