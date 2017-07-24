package nl.finalist.liferay.lam.api;

public interface Category {

	void addCategory(String categoryName, String vocabularyName, String title);

	void updateCategory(String categoryName, String vocabularyName, String updateName);

	void deleteCategory(String categoryName, String vocabularyName);
}
