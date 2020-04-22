package nl.finalist.liferay.lam.builder.factory.delete

import nl.finalist.liferay.lam.api.WebContent;
import nl.finalist.liferay.lam.dslglue.model.WebContentModel;

class DeleteWebContentFactory extends AbstractFactory{

    WebContent webContentService;

    DeleteWebContentFactory(WebContent webContentService) {
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
        // Method call updated to use webIds available in groovy model
        webContentService.deleteWebContent(model.webIds, model.urlTitle);
    }
}
