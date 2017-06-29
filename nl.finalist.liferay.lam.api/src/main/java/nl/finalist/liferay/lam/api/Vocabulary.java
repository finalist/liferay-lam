package nl.finalist.liferay.lam.api;

public interface Vocabulary {

	public void addVocabulary(String vocabularyName, long groupId);
	public void deleteVocabulary(String vocabularyName, long groupId);
	public void updateVocabularyTranslation(String languageId, String translatedName, String vocabularyName, long groupId);
}
