package nl.finalist.liferay.lam.builder;

import groovy.util.FactoryBuilderSupport;
import nl.finalist.liferay.lam.api.CustomFields;
import nl.finalist.liferay.lam.api.Vocabulary;
import nl.finalist.liferay.lam.api.Category;
import nl.finalist.liferay.lam.builder.factory.DeleteCustomFieldsFactory;
import nl.finalist.liferay.lam.builder.factory.DeleteVocabularyFactory;
import nl.finalist.liferay.lam.builder.factory.DeleteCategoryFactory;

class DeleteFactoryBuilder  extends FactoryBuilderSupport {

    DeleteFactoryBuilder(CustomFields customFieldsService, Vocabulary vocabularyService, Category categoryService){
        registerFactory("customField", new DeleteCustomFieldsFactory(customFieldsService));
        registerFactory("vocabulary", new DeleteVocabularyFactory(vocabularyService));
        registerFactory("category", new DeleteCategoryFactory(categoryService));
    }
}
