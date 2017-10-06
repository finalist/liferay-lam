package nl.finalist.liferay.lam.builder.factory

import nl.finalist.liferay.lam.api.Page
import nl.finalist.liferay.lam.api.model.PageModel
import nl.finalist.liferay.lam.dslglue.model.SiteModel
import nl.finalist.liferay.lam.util.LocaleMapConverter
class CreatePageFactory extends AbstractFactory {

    Page pageService;

    CreatePageFactory(Page site) {
        this.pageService = site;
    }


    @Override
    Object newInstance(FactoryBuilderSupport builder, Object objectName, Object value, Map attributes)
                    throws InstantiationException, IllegalAccessException {                    
        new PageModel(
            attributes.get("privatePage"),
            LocaleMapConverter.convert(attributes.get("nameMap")),
            LocaleMapConverter.convert(attributes.get("titleMap")),
            LocaleMapConverter.convert(attributes.get("descriptionMap")),
            attributes.get("friendlyUrlMap"),
            attributes.get("typeSettings"),
            attributes.get("customFields"),
            attributes.get("parentUrl"),
            attributes.get("type")
    	);
    }

    @Override
    void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        super.onNodeCompleted(builder, parent, node);
        if( parent instanceof  SiteModel) {
            SiteModel site = (SiteModel)parent;
            site.addPage((PageModel)node);
        } else {
            /// Help, standalone pages don't work anymore!!
        }
    }
}
