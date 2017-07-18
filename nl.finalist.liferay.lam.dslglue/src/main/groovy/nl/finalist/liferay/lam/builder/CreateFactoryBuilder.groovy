package nl.finalist.liferay.lam.builder;

import groovy.util.FactoryBuilderSupport;
import nl.finalist.liferay.lam.api.CustomFields;
import nl.finalist.liferay.lam.api.Vocabulary;
import nl.finalist.liferay.lam.builder.factory.CreateVocabularyFactory;
import nl.finalist.liferay.lam.builder.factory.CreateCustomFieldsFactory;

class CreateFactoryBuilder extends FactoryBuilderSupport {

    CreateFactoryBuilder(CustomFields customFieldsService, Vocabulary vocabularyService) {
        registerFactory("customField", new CreateCustomFieldsFactory(customFieldsService));
        registerFactory("vocabulary", new CreateVocabularyFactory(vocabularyService));
    }
}
