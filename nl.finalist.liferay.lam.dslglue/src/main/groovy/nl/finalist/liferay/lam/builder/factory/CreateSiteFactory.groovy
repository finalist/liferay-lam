package nl.finalist.liferay.lam.builder.factory

import nl.finalist.liferay.lam.api.model.PageModel
import nl.finalist.liferay.lam.util.LocaleMapConverter
import nl.finalist.liferay.lam.api.Site;
import nl.finalist.liferay.lam.dslglue.model.SiteModel;

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
        /*
        List<Map> pageMapList = model.pages;
        List<PageModel> pages = new ArrayList<>();
        for (Map pageMap: pageMapList) {
            PageModel page = new PageModel(
                pageMap.get("privatePage"),
 				LocaleMapConverter.convert(attributes.get("nameMap")),
                LocaleMapConverter.convert(attributes.get("titleMap")),
                LocaleMapConverter.convert(attributes.get("descriptionMap")),
                LocaleMapConverter.convert(attributes.get("friendlyUrlMap")),
                pageMap.get("typeSettings"));
            pages.add(page);
        }*/
        siteService.addSite(LocaleMapConverter.convert(model.nameMap), LocaleMapConverter.convert(model.descriptionMap), model.friendlyURL, model.customFields, model.pages);
    }
}
