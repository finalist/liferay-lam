package nl.finalist.liferay.lam.model;

import java.util.Arrays;

public class CustomField {

	private String name;
	
	private Object defaultValue;
	
	private Object value;
	
	private int type;
	
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
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
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
		return "CustomField [name=" + name + "\n\tdefaultValue=" + defaultValue + "\n\tvalue=" + value + "\n\ttype=" + type
				+ "\n\troles=" + Arrays.toString(roles) + "]";
	}
	
	
	

}
