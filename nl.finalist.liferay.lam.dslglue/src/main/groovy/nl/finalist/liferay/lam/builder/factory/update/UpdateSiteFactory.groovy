package nl.finalist.liferay.lam.builder.factory.update

import nl.finalist.liferay.lam.api.Site
import nl.finalist.liferay.lam.dslglue.model.SiteModel
import nl.finalist.liferay.lam.util.LocaleMapConverter

class UpdateSiteFactory extends AbstractFactory {

    Site siteService;

    UpdateSiteFactory(Site siteService) {
        this.siteService = siteService;
    }

    @Override
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes)
    throws InstantiationException, IllegalAccessException {
        new SiteModel(attributes);
    }

    @Override
    void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        super.onNodeCompleted(builder, parent, node);
        SiteModel model = (SiteModel) node;
        // Method call updated to use webIds available in groovy model
        siteService.updateSite(model.webIds, model.siteKey, LocaleMapConverter.convert(model.nameMap), LocaleMapConverter.convert(model.descriptionMap), model.friendlyURL, model.customFields, model.pages, model.stagingEnabled);
    }
}
