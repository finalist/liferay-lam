package nl.finalist.liferay.lam.dslglue.model;

class CustomFieldModel {

    String[] webIds;
    String name;
    Object defaultValue;
    String type;
    String entityName;
    String[] roles;
    String displayType;

    @Override
    public String toString() {
        return "CustomField [name=" + name + "\n\twebIds=" + Arrays.toString(webIds) + "\n\tdefaultValue=" + defaultValue + "\n\tentityName=" + entityName + "\n\ttype=" + type
        + "\n\troles=" + Arrays.toString(roles) + "\n\tdisplayType=" + displayType + "]";
    }
}
