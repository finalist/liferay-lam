package nl.finalist.liferay.lam.builder;

import groovy.util.FactoryBuilderSupport;
import nl.finalist.liferay.lam.api.CustomFields;
import nl.finalist.liferay.lam.api.Vocabulary;
import nl.finalist.liferay.lam.builder.factory.DeleteCustomFieldsFactory;
import nl.finalist.liferay.lam.builder.factory.DeleteVocabularyFactory;

public class DeleteFactoryBuilder  extends FactoryBuilderSupport {

    public DeleteFactoryBuilder(CustomFields customFieldsService, Vocabulary vocabularyService){
        registerFactory("customField", new DeleteCustomFieldsFactory(customFieldsService));
        registerFactory("vocabulary", new DeleteVocabularyFactory(vocabularyService));
    }

}
