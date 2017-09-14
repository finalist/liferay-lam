package nl.finalist.liferay.lam.builder.factory

import nl.finalist.liferay.lam.api.WebContent;
import nl.finalist.liferay.lam.dslglue.model.WebContentModel;
import nl.finalist.liferay.lam.util.LocaleMapConverter

class UpdateWebContentFactory extends AbstractFactory {

    WebContent webContentService;

    UpdateWebContentFactory(WebContent webContentService) {
        this.webContentService = webContentService;
    }

    @Override
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes)
                    throws InstantiationException, IllegalAccessException {
        new WebContentModel(attributes);
    }

    @Override
    void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        super.onNodeCompleted(builder, parent, node);
        WebContentModel model = (WebContentModel) node;
        
        webContentService.updateWebContent(LocaleMapConverter.convert(model.titleMap), 
                                                   LocaleMapConverter.convert(model.descriptionMap), model.content, model.urlTitle);
    }
}
