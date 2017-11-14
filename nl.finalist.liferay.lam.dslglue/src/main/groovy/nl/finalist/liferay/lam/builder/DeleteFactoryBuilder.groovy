package nl.finalist.liferay.lam.builder

import nl.finalist.liferay.lam.api.*
import nl.finalist.liferay.lam.builder.factory.*

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
