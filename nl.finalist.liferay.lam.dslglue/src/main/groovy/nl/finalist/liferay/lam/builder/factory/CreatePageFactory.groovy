package nl.finalist.liferay.lam.builder.factory

import nl.finalist.liferay.lam.api.Page
import nl.finalist.liferay.lam.api.model.PageModel
import nl.finalist.liferay.lam.dslglue.model.SiteModel

class CreatePageFactory extends AbstractFactory {

    Page pageService;

    CreatePageFactory(Page pageService) {
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
            pageService.addPage(((PageModel)node).getSiteKey(), (PageModel) node)
        }
    }
}
