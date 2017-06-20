package nl.finalist.liferay.lam.api;

public interface CustomFieldsInterface {
    void addCustomTextField(long companyId, String entityName, String fieldName, String value);
    void addCustomIntegerField(long companyId, String entityName, String fieldName, String value);
    void deleteCustomField(long companyId, String entityName, String fieldName);
}
