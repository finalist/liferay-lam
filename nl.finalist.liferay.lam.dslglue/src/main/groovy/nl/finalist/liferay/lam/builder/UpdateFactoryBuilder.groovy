package nl.finalist.liferay.lam.builder

import nl.finalist.liferay.lam.api.*
import nl.finalist.liferay.lam.builder.factory.*

class UpdateFactoryBuilder extends FactoryBuilderSupport {



    UpdateFactoryBuilder(PortalSettings portalSettingsService, Vocabulary vocabularyService, Site siteService, 
    Category categoryService, User userService) {

        registerFactory("portalSettings", new UpdatePortalSettingsFactory(portalSettingsService));
        registerFactory("vocabulary", new UpdateVocabularyFactory(vocabularyService));
        registerFactory("site", new UpdateSiteFactory(siteService));
        registerFactory("category", new UpdateCategoryFactory(categoryService));
         registerFactory("user", new UpdateUserFactory(userService));
    }
}
