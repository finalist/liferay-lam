package nl.finalist.liferay.lam.api;

/**
 * Service for managing custom fields.
 */
public interface CustomFields {

    /**
     * Add a custom field of type text.
     *
     * @param companyId company ID that the field is valid for
     * @param entityName entity that the field is added to
     * @param fieldName name of the field
     * @param defaultValue default value of the field
     */
    void addCustomTextField(long companyId, String entityName, String fieldName, String defaultValue, String[] roles);

    /**
     * Same as {@link #addCustomTextField(long, String, String, String, String[])} but without companyId, assuming
     * the default one.
     */
    void addCustomTextField(String entityName, String fieldName, String defaultValue, String[] roles);

    /**
     * Add a custom field of type integer
     *
     * @param companyId company ID that the field is valid for
     * @param entityName entity that the field is added to
     * @param fieldName name of the field
     * @param defaultValue default value of the field
     */
    void addCustomIntegerField(long companyId, String entityName, String fieldName, int defaultValue, String[] roles);
    /**
     * Same as {@link #addCustomIntegerField(long, String, String, int, String[])} but without companyId, assuming
     * the default one.
     */
    void addCustomIntegerField(String entityName, String fieldName, int defaultValue, String[] roles);

    /**
     * Delete a custom field
     *
     * @param companyId company ID that the field is valid for
     * @param entityName entity to which the field applies
     * @param fieldName name of the field
     */
    void deleteCustomField(long companyId, String entityName, String fieldName);

    /**
     * Delete a custom field for the default company
     *
     * @param entityName entity to which the field applies
     * @param fieldName name of the field
     */
    void deleteCustomField(String entityName, String fieldName);
}
