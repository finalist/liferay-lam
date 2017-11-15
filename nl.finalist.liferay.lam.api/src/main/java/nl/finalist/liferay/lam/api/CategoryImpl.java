package nl.finalist.liferay.lam.api;

import com.liferay.asset.kernel.exception.DuplicateCategoryException;
import com.liferay.asset.kernel.model.AssetCategory;
import com.liferay.asset.kernel.model.AssetVocabulary;
import com.liferay.asset.kernel.service.AssetCategoryLocalService;
import com.liferay.asset.kernel.service.AssetVocabularyLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
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
    private CompanyLocalService companyService;

    @Reference
    private AssetCategoryLocalService assetCategoryService;

    @Reference
    private AssetVocabularyLocalService assetVocabularyLocalService;

    @Reference
    private DefaultValue defaultValue;
    AssetCategory assetCategory;

    @Override
    public void addCategory(Map<Locale, String> categoryName, String vocabularyName, String title, String parentCategoryName) {
        LOG.debug(String.format(
                        "Starting to add the category %s to vocabulary %s with title %s ",
                        categoryName, vocabularyName, title));
        try {
            AssetVocabulary vocabulary = assetVocabularyLocalService.getGroupVocabulary(defaultValue.getGlobalGroupId(),
                            vocabularyName);
            String[] existingCategoriesNames = assetCategoryService.getCategoryNames();
            boolean addCondition = true;
            for (String existingCategoryName : existingCategoriesNames) {
                if (existingCategoryName.equals(categoryName)) {
                    addCondition = false;
                }
            }
            if (addCondition) {

                long parentCategoryId = 0;
                if(!Validator.isBlank(parentCategoryName)){
                    List<AssetCategory> existingCategories = vocabulary.getCategories();
                    LOG.info(String.format("Category %s has a parent category %s, adding it as a nested category", title, parentCategoryName));
                    parentCategoryId = existingCategories.stream()
                                    .filter(assetCategory -> assetCategory.getName().equalsIgnoreCase(parentCategoryName))
                                    .map(AssetCategory::getCategoryId)
                                    .findFirst()
                                    .orElse(0l);
                }
                assetCategoryService.addCategory(defaultValue.getDefaultUserId(), defaultValue.getGlobalGroupId(),
                                parentCategoryId, categoryName, new HashMap<>(), vocabulary.getVocabularyId(), new String[0], new ServiceContext());

                LOG.info(String.format("Added category %s to vocabulary %s", categoryName, vocabularyName));
            } else {
                LOG.info(String.format("Cannot add category %s because it already exists", categoryName));
            }
        } catch(DuplicateCategoryException e1) {
            LOG.info(String.format("Cannot add category %s because it already exists", categoryName));
        }
        catch (PortalException e) {
            LOG.error(String.format("adding category %s failed", categoryName),e);
        }

    }

    @Override
    public void updateCategory(String categoryName, String vocabularyName, Map<Locale, String> updateName) {
        LOG.debug(String.format(
                        "Starting to update category %s in vocabulary %s, the new name will be %s",
                        categoryName, vocabularyName, updateName));
        try {
            AssetVocabulary vocabulary = assetVocabularyLocalService.getGroupVocabulary(defaultValue.getGlobalGroupId(),
                            vocabularyName);
            List<AssetCategory> existingCategories = vocabulary.getCategories();
            for (AssetCategory existingCategory : existingCategories) {
                if (existingCategory.getName().equals(categoryName)) {

                    existingCategory.setTitleMap(updateName);


                    assetCategoryService.updateAssetCategory(existingCategory);
                    LOG.debug(String.format(
                                    "Updated category %s in vocabulary %s, the new name is now %s",
                                    categoryName, vocabularyName, updateName));
                    return;
                }

            }
            LOG.info(String.format("Category %s doesn't exist", categoryName));

        } catch (Exception e) {
            LOG.error(String.format("Update of category %s failed", categoryName), e);
        }

    }

    @Override
    public void deleteCategory(String categoryName, String vocabularyName) {
        LOG.debug(String.format("Starting to delete category %s from vocabulary %s", categoryName,
                        vocabularyName));
        try {
            AssetVocabulary vocabulary = assetVocabularyLocalService.getGroupVocabulary(defaultValue.getGlobalGroupId(),
                            vocabularyName);
            List<AssetCategory> existingCategories = vocabulary.getCategories();
            LOG.debug(String.format("number of categories is %d ", existingCategories.size()));
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
}
