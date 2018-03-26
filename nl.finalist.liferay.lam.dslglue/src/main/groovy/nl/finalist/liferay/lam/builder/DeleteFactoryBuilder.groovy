package nl.finalist.liferay.lam.builder

import nl.finalist.liferay.lam.api.*
import nl.finalist.liferay.lam.builder.factory.delete.DeleteCategoryFactory
import nl.finalist.liferay.lam.builder.factory.delete.DeleteCustomFieldsFactory
import nl.finalist.liferay.lam.builder.factory.delete.DeleteSiteFactory
import nl.finalist.liferay.lam.builder.factory.delete.DeleteTagFactory
import nl.finalist.liferay.lam.builder.factory.delete.DeleteUserFactory
import nl.finalist.liferay.lam.builder.factory.delete.DeleteVocabularyFactory
import nl.finalist.liferay.lam.builder.factory.delete.DeleteWebContentFactory

class DeleteFactoryBuilder  extends FactoryBuilderSupport {


    DeleteFactoryBuilder(CustomFields customFieldsService, Vocabulary vocabularyService, Site siteService, Category categoryService,
    WebContent webContentService, Tag tagService, User userService){
        registerFactory("customField", new DeleteCustomFieldsFactory(customFieldsService));
        registerFactory("vocabulary", new DeleteVocabularyFactory(vocabularyService));
        registerFactory("site", new DeleteSiteFactory(siteService));
        registerFactory("category", new DeleteCategoryFactory(categoryService));
        registerFactory("webcontent", new DeleteWebContentFactory(webContentService));
        registerFactory("tag", new DeleteTagFactory(tagService));
        registerFactory("user", new DeleteUserFactory(userService));
    }
}
