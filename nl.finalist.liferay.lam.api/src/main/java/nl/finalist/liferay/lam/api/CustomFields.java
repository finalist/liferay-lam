package nl.finalist.liferay.lam.api;

/**
 * Service for managing custom fields.
 */
public interface CustomFields {

    /**
     * Add a custom field of type text.
     *
     * @param webIds
     *            company that the field is added to
     * @param entityName
     *            entity that the field is added to
     * @param fieldName
     *            name of the field
     * @param defaultValue
     *            default value of the field
     */
    void addCustomTextField(String[] webIds, String entityName, String fieldName, String defaultValue, String[] roles);

    /**
     * Add a custom field of type text array.
     * 
     * @param webIds
     *            company that the field is added to
     * @param entityName
     *            entity that the field is added to
     * @param fieldName
     *            name of the field
     * @param possibleValues
     *            possible values of the field, comma separated
     * @param displayType
     *            how to display the values of the text array
     */
    void addCustomTextArrayField(String[] webIds, String entityName, String fieldName, String possibleValues, String[] roles, String displayType);

    /**
     * Add a custom field of type integer
     * 
     * @param webIds
     *            company that the field is added to
     * @param entityName
     *            entity that the field is added to
     * @param fieldName
     *            name of the field
     * @param defaultValue
     *            default value of the field
     */
    void addCustomIntegerField(String[] webIds, String entityName, String fieldName, int defaultValue, String[] roles);

    /**
     * Delete a custom field
     * 
     * @param webIds
     *            company that the field is deleted from
     * @param entityName
     *            entity to which the field applies
     * @param fieldName
     *            name of the field
     */
    void deleteCustomField(String[] webIds, String entityName, String fieldName);

    /**
     * Add a value to a custom field
     * 
     * @param webIds
     *            company where field is added
     * @param entityName
     *            entity to which the field applies
     * @param fieldName
     *            name of the field
     * @param classPK
     *            primary key of the entity that the value belongs to
     * @param content
     *            value of the field
     */
    void addCustomFieldValue(String[] webIds, String entityName, String fieldName, long classPK, String content);

    /**
     * Update the value of a custom field
     * 
     * @param webIds
     *            company in which the field is updated
     * @param entityName
     *            entity to which the field applies
     * @param fieldName
     *            name of the field
     * @param classPK
     *            primary key of the entity that the value belongs to
     * @param content
     *            new value of the field
     */
    void updateCustomFieldValue(String[] webIds, String entityName, String fieldName, long classPK, String content);
}
