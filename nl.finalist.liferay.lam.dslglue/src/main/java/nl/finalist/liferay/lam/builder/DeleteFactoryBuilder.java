package nl.finalist.liferay.lam.builder;

import groovy.util.FactoryBuilderSupport;
import nl.finalist.liferay.lam.api.CustomFields;
import nl.finalist.liferay.lam.builder.factory.DeleteCustomFieldsFactory;

public class DeleteFactoryBuilder  extends FactoryBuilderSupport {

    public DeleteFactoryBuilder(CustomFields customFieldsService){
        registerFactory("customField", new DeleteCustomFieldsFactory(customFieldsService));
    }

}
