package nl.finalist.liferay.lam.builder;

import groovy.util.FactoryBuilderSupport;
import nl.finalist.liferay.lam.api.CustomFields;
import nl.finalist.liferay.lam.builder.factory.CreateCustomFieldsFactory;
import nl.finalist.liferay.lam.builder.factory.UpdatePortalSettingsFactory;

public class CreateFactoryBuilder extends FactoryBuilderSupport {



	public CreateFactoryBuilder(CustomFields customFieldsService) {
		// TODO Auto-generated constructor stub
		System.out.println("Register factorybuilder");
		registerFactory("customField", new CreateCustomFieldsFactory(customFieldsService));
	}

}
