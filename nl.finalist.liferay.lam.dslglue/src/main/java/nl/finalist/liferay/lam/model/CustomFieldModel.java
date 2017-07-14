package nl.finalist.liferay.lam.model;

import java.util.Arrays;

public class CustomFieldModel {

    private String name;

    private Object defaultValue;


    private String type;

    private String entityName;

    private String[] roles;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Object getDefaultValue() {
        return defaultValue;
    }
    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }



    public String[] getRoles() {
        return roles;
    }
    public void setRoles(String[] roles) {
        this.roles = roles;
    }
    @Override
    public String toString() {
        return "CustomField [name=" + name + "\n\tdefaultValue=" + defaultValue + "\n\tentityName=" + entityName + "\n\ttype=" + type
                        + "\n\troles=" + Arrays.toString(roles) + "]";
    }
    public String getEntityName() {
        return entityName;
    }
    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }




}
