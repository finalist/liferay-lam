package nl.finalist.liferay.lam.api;

/**
 * Service for managing custom fields.
 */
public interface CustomFields {

    /**
     * Add a custom field of type text.
     *
     * @param entityName entity that the field is added to
     * @param fieldName name of the field
     * @param defaultValue default value of the field
     */
    void addCustomTextField(String entityName, String fieldName, String defaultValue, String[] roles);

    /**
     * Add a custom field of type integer
     *
     * @param entityName entity that the field is added to
     * @param fieldName name of the field
     * @param defaultValue default value of the field
     */
    void addCustomIntegerField(String entityName, String fieldName, int defaultValue, String[] roles);

    /**
     * Delete a custom field
     *
     * @param entityName entity to which the field applies
     * @param fieldName name of the field
     */
    void deleteCustomField(String entityName, String fieldName);
}
