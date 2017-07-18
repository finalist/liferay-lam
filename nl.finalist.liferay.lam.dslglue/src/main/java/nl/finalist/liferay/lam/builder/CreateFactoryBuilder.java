package nl.finalist.liferay.lam.builder;

import groovy.util.FactoryBuilderSupport;
import nl.finalist.liferay.lam.api.CustomFields;
import nl.finalist.liferay.lam.api.Vocabulary;
import nl.finalist.liferay.lam.builder.factory.CreateVocabularyFactory;
import nl.finalist.liferay.lam.builder.factory.CreateCustomFieldsFactory;

public class CreateFactoryBuilder extends FactoryBuilderSupport {



    public CreateFactoryBuilder(CustomFields customFieldsService, Vocabulary vocabularyService) {
        // TODO Auto-generated constructor stub
        System.out.println("Register create factorybuilder");
        registerFactory("customField", new CreateCustomFieldsFactory(customFieldsService));
        registerFactory("vocabulary", new CreateVocabularyFactory(vocabularyService));
    }

}
