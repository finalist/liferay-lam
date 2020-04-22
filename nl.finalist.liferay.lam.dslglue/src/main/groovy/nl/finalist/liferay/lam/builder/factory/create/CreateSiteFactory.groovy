package nl.finalist.liferay.lam.builder.factory.create

import nl.finalist.liferay.lam.api.Site;
import nl.finalist.liferay.lam.dslglue.model.SiteModel;
import nl.finalist.liferay.lam.util.LocaleMapConverter

class CreateSiteFactory extends AbstractFactory {

    Site siteService;

    CreateSiteFactory(Site site) {
        this.siteService = site;
    }

    @Override
    Object newInstance(FactoryBuilderSupport builder, Object objectName, Object value, Map attributes)
    throws InstantiationException, IllegalAccessException {
        new SiteModel(attributes);
    }

    @Override
    void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        super.onNodeCompleted(builder, parent, node);
        SiteModel model = (SiteModel) node;
        // Method call updated to use webIds available in groovy model
        siteService.addSite(model.webIds, LocaleMapConverter.convert(model.nameMap), LocaleMapConverter.convert(model.descriptionMap), model.friendlyURL, model.customFields, model.pages);
    }
}
