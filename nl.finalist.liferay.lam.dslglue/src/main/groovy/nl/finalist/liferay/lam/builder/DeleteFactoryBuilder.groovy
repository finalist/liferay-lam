package nl.finalist.liferay.lam.builder;

import groovy.util.FactoryBuilderSupport;
import nl.finalist.liferay.lam.api.CustomFields
import nl.finalist.liferay.lam.api.Site;
import nl.finalist.liferay.lam.api.Vocabulary;
import nl.finalist.liferay.lam.builder.factory.DeleteCustomFieldsFactory
import nl.finalist.liferay.lam.builder.factory.DeleteSiteFactory;
import nl.finalist.liferay.lam.builder.factory.DeleteVocabularyFactory;

class DeleteFactoryBuilder  extends FactoryBuilderSupport {

    DeleteFactoryBuilder(CustomFields customFieldsService, Vocabulary vocabularyService, Site siteService){
        registerFactory("customField", new DeleteCustomFieldsFactory(customFieldsService));
        registerFactory("vocabulary", new DeleteVocabularyFactory(vocabularyService));
        registerFactory("site", new DeleteSiteFactory(siteService));
    }
}
