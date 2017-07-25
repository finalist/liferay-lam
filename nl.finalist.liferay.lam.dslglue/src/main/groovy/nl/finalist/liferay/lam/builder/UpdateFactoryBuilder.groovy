package nl.finalist.liferay.lam.builder;

import groovy.util.FactoryBuilderSupport;
import nl.finalist.liferay.lam.api.PortalSettings
import nl.finalist.liferay.lam.api.Site;
import nl.finalist.liferay.lam.api.Vocabulary;
import nl.finalist.liferay.lam.builder.factory.UpdatePortalSettingsFactory
import nl.finalist.liferay.lam.builder.factory.UpdateSiteFactory;
import nl.finalist.liferay.lam.builder.factory.UpdateVocabularyFactory;

class UpdateFactoryBuilder extends FactoryBuilderSupport {

    UpdateFactoryBuilder(PortalSettings portalSettingsService, Vocabulary vocabularyService, Site siteService) {
        registerFactory("portalSettings", new UpdatePortalSettingsFactory(portalSettingsService));
        registerFactory("vocabulary", new UpdateVocabularyFactory(vocabularyService));
        registerFactory("site", new UpdateSiteFactory(siteService));
    }
}
