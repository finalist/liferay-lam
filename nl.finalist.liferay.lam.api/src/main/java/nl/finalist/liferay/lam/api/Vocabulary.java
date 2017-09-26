package nl.finalist.liferay.lam.api;

import java.util.Locale;
import java.util.Map;

public interface Vocabulary {

    /**
     * Adds a vocabulary to the Global group/site
     *
     * @param vocabularyName
     *            Map of the names of the vocabulary with the Locale
     */
    public void addVocabulary(Map<Locale, String> vocabularyName);


    /**
     * Deletes a vocabulary if it exists in the Global site
     *
     * @param vocabularyName
     *            The name of the vocabulary
     */
    public void deleteVocabulary(String existingName);


    /**
     * Updates the name of the Vocabulary created in the Global site
     *
     * @param updateVocabularyName
     *            The updated names of the vocabulary
     */
    public void updateVocabularyTranslation(String existingName,Map<Locale, String> updateVocabularyName);
}
