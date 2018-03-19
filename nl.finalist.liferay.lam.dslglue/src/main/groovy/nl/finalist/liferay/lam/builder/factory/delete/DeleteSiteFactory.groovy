package nl.finalist.liferay.lam.builder.factory.delete

import nl.finalist.liferay.lam.api.Site
import nl.finalist.liferay.lam.dslglue.model.SiteModel

class DeleteSiteFactory extends AbstractFactory{
    Site siteService;

    DeleteSiteFactory(Site siteService){
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
        siteService.deleteSite(model.siteKey);
    }
}
