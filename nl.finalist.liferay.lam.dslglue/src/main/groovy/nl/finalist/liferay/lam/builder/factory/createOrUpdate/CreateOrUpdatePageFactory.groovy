package nl.finalist.liferay.lam.builder.factory.createOrUpdate

import nl.finalist.liferay.lam.api.Page
import nl.finalist.liferay.lam.api.model.PageModel
import nl.finalist.liferay.lam.dslglue.model.SiteModel

class CreateOrUpdatePageFactory extends AbstractFactory {

    Page pageService;

    CreateOrUpdatePageFactory(Page pageService) {
        this.pageService = pageService;
    }

    @Override
    Object newInstance(FactoryBuilderSupport builder, Object objectName, Object value, Map attributes)
    throws InstantiationException, IllegalAccessException {
        new PageModel(attributes)
    }

    @Override
    void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        super.onNodeCompleted(builder, parent, node);

        if( parent instanceof  SiteModel) {
            SiteModel site = (SiteModel)parent;
            site.addPage((PageModel)node);
        } else {
            // Method call updated to use webIds available in groovy model
            pageService.addPage(((PageModel)node).getWebIds(),((PageModel)node).getSiteKey(), (PageModel) node)
        }
    }
}
