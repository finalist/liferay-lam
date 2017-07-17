package nl.finalist.liferay.lam.api;

public interface Vocabulary {

    /**
     * Adds a vocabulary to the Global group/site
     *
     * @param vocabularyName
     *            The name of the vocabulary
     */
    public void addVocabulary(String vocabularyName);

    /**
     * Adds a vocabulary to the chosen group/site
     *
     * @param vocabularyName
     *            The name of the vocabulary
     * @param groupId
     *            The groupId of the vocabulary
     */
    public void addVocabulary(String vocabularyName, long groupId);

    /**
     * Deletes a vocabulary if it exists in the Global site
     *
     * @param vocabularyName
     *            The name of the vocabulary
     */
    public void deleteVocabulary(String vocabularyName);

    /**
     * Deletes a vocabulary if it exists
     *
     * @param vocabularyName
     *            The name of the vocabulary
     * @param groupId
     *            The groupId of the vocabulary
     */
    public void deleteVocabulary(String vocabularyName, long groupId);

    /**
     * Updates the name of the Vocabulary created in the Global site in the
     * chosen locale
     *
     * @param languageId
     *            The languageId of the translation that has to be added (e.g.
     *            "nl_NL")
     * @param translatedName
     *            The translated name that has to be added/ changed
     * @param vocabularyName
     *            The name of the vocabulary
     */
    public void updateVocabularyTranslation(String languageId, String translatedName, String vocabularyName);

    /**
     * Updates the name of the Vocabulary in the chosen locale
     *
     * @param languageId
     *            The languageId of the translation that has to be added (e.g.
     *            "nl_NL")
     * @param translatedName
     *            The translated name that has to be added/ changed
     * @param vocabularyName
     *            The name of the vocabulary
     * @param groupId
     *            The groupId of the vocabulary
     */
    public void updateVocabularyTranslation(String languageId, String translatedName, String vocabularyName,
                    long groupId);

}
