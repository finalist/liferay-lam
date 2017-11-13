package nl.finalist.liferay.lam.builder;

import groovy.util.FactoryBuilderSupport;
import nl.finalist.liferay.lam.api.CustomFields
import nl.finalist.liferay.lam.api.Site;
import nl.finalist.liferay.lam.api.Vocabulary;
import nl.finalist.liferay.lam.api.Category;
import nl.finalist.liferay.lam.api.WebContent;
import nl.finalist.liferay.lam.api.User;
import nl.finalist.liferay.lam.builder.factory.DeleteCustomFieldsFactory
import nl.finalist.liferay.lam.builder.factory.DeleteSiteFactory;
import nl.finalist.liferay.lam.builder.factory.DeleteVocabularyFactory;
import nl.finalist.liferay.lam.builder.factory.DeleteCategoryFactory;
import nl.finalist.liferay.lam.builder.factory.DeleteWebContentFactory;
import nl.finalist.liferay.lam.builder.factory.DeleteUserFactory;

class DeleteFactoryBuilder  extends FactoryBuilderSupport {


    DeleteFactoryBuilder(CustomFields customFieldsService, Vocabulary vocabularyService, Site siteService, Category categoryService, 
    WebContent webContentService, User userService){
        registerFactory("customField", new DeleteCustomFieldsFactory(customFieldsService));
        registerFactory("vocabulary", new DeleteVocabularyFactory(vocabularyService));
        registerFactory("site", new DeleteSiteFactory(siteService));
        registerFactory("category", new DeleteCategoryFactory(categoryService));
        registerFactory("webcontent", new DeleteWebContentFactory(webContentService));
        registerFactory("user", new DeleteUserFactory(userService));
    }
}
