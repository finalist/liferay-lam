package nl.finalist.liferay.lam.api;

import java.util.Locale;
import java.util.Map;

public interface Vocabulary {

    /**
     * Adds a vocabulary to the Global group/site in companies of given webIds
     * 
     * @param webIds
     *            The WebIds of companies where Vocabulary is to be added
     * @param vocabularyName
     *            Map of the names of the vocabulary with the Locale
     */
    public void addVocabulary(String[] webIds, Map<Locale, String> vocabularyName);

    /**
     * Deletes a vocabulary if it exists in the Global site in companies of
     * given webIds
     * 
     * @param webIds
     *            The WebIds of companies from where Vocabulary is to be deleted
     *
     * @param vocabularyName
     *            The name of the vocabulary
     */
    public void deleteVocabulary(String[] webIds, String existingName);

    /**
     * Updates the name of the Vocabulary created in the Global site in
     * companies of given webIds
     *
     * @param webIds
     *            The WebIds of companies where Vocabulary is to be updated
     *
     * @param updateVocabularyName
     *            The updated names of the vocabulary
     */
    public void updateVocabularyTranslation(String[] webIds, String existingName, Map<Locale, String> updateVocabularyName);
}
