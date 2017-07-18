package nl.finalist.liferay.lam.builder;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import groovy.util.FactoryBuilderSupport;
import nl.finalist.liferay.lam.api.PortalSettings;
import nl.finalist.liferay.lam.api.Vocabulary;
import nl.finalist.liferay.lam.builder.factory.UpdatePortalSettingsFactory;
import nl.finalist.liferay.lam.builder.factory.UpdateVocabularyFactory;

public class UpdateFactoryBuilder extends FactoryBuilderSupport {
    private static final Log LOG = LogFactoryUtil.getLog(UpdateFactoryBuilder.class);

    public UpdateFactoryBuilder(PortalSettings portalSettingsService, Vocabulary vocabularyService) {
        LOG.info("Registering Updatefactorybuilder");
        registerFactory("portalSettings", new UpdatePortalSettingsFactory(portalSettingsService));
        registerFactory("vocabulary", new UpdateVocabularyFactory(vocabularyService));
    }

}