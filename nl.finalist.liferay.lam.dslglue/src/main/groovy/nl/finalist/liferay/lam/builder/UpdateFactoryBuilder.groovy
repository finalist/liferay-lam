package nl.finalist.liferay.lam.builder;

import groovy.util.FactoryBuilderSupport;
import nl.finalist.liferay.lam.api.PortalSettings;
import nl.finalist.liferay.lam.api.Vocabulary;
import nl.finalist.liferay.lam.api.Category;
import nl.finalist.liferay.lam.builder.factory.UpdatePortalSettingsFactory;
import nl.finalist.liferay.lam.builder.factory.UpdateVocabularyFactory;
import nl.finalist.liferay.lam.builder.factory.UpdateCategoryFactory;

class UpdateFactoryBuilder extends FactoryBuilderSupport {

    UpdateFactoryBuilder(PortalSettings portalSettingsService, Vocabulary vocabularyService, Category categoryService) {
        registerFactory("portalSettings", new UpdatePortalSettingsFactory(portalSettingsService));
        registerFactory("vocabulary", new UpdateVocabularyFactory(vocabularyService));
        registerFactory("category", new UpdateCategoryFactory(categoryService));
    }
}
