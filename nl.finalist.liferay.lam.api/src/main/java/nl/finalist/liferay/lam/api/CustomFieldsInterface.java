package nl.finalist.liferay.lam.api;

public interface CustomFieldsInterface {
    void addCustomTextField(long companyId, String entityName, String fieldName, String value, String[] roles);
    void addCustomIntegerField(long companyId, String entityName, String fieldName, String value, String[] roles);
    void deleteCustomField(long companyId, String entityName, String fieldName);
}
