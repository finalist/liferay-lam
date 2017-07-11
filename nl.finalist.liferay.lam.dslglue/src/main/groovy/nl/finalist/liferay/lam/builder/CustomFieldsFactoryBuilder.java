package nl.finalist.liferay.lam.builder;

import groovy.util.FactoryBuilderSupport;

public class CustomFieldsFactoryBuilder extends FactoryBuilderSupport {

	public CustomFieldsFactoryBuilder() {
		
		super();
		System.out.println("Register factorybuilder");
		registerFactory("customField", new CustomFieldsFactory());
		
	}

	public CustomFieldsFactoryBuilder(boolean init) {
		super(init);
		// TODO Auto-generated constructor stub
	}

}
