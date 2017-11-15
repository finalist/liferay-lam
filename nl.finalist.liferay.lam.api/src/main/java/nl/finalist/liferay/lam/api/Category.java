package nl.finalist.liferay.lam.api;

import java.util.Locale;
import java.util.Map;

/**
 * Service for managing Category.
 */
public interface Category {
    /**
     * Add a category of type text.
     *
     * @param categoryNames names of the category with the respective Locale to be added
     * @param vocabularyName name of the Vocabulary for which category is being added
     * @param title title to the category
     */
    void addCategory(Map<Locale,String> categoryNames, String vocabularyName, String title, String parentCategoryName);
    /**
     * Update an existing Category
     *
     * @param categoryName name of the existing category
     * @param vocabularyName name of the vocabulary for which category exists
     * @param updateName updating the name of the existing category
     */
    void updateCategory(String categoryName, String vocabularyName, Map<Locale,String> updateName);
    /**
     * Delete a Category
     *
     * @param categoryName name of the category which is to be deleted
     * @param vocabularyName name of the vocabulary for which category exists
     */
    void deleteCategory(String categoryName, String vocabularyName);
}
