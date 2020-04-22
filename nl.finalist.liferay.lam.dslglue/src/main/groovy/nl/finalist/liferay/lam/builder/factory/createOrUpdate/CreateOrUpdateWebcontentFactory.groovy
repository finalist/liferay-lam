package nl.finalist.liferay.lam.builder.factory.createOrUpdate
import org.osgi.framework.Bundle;

import nl.finalist.liferay.lam.api.WebContent;
import nl.finalist.liferay.lam.dslglue.model.WebContentModel;
import nl.finalist.liferay.lam.util.LocaleMapConverter;


class CreateOrUpdateWebcontentFactory extends AbstractFactory {

    WebContent webContentService;
    Bundle bundle;

    CreateOrUpdateWebcontentFactory(WebContent webContentService, Bundle bundle) {
        this.webContentService = webContentService;
        this.bundle = bundle;
    }

    @Override
    Object newInstance(FactoryBuilderSupport builder, Object objectName, Object value, Map attributes)
    throws InstantiationException, IllegalAccessException {
        new WebContentModel(attributes);
    }

    @Override
    void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        super.onNodeCompleted(builder, parent, node);
        WebContentModel model = (WebContentModel) node;
        // Method call updated to use webIds available in groovy model
        webContentService.createOrUpdateWebcontent(model.webIds, model.id, model.forSite, LocaleMapConverter.convert(model
                .titleMap), model.file, bundle , model.urlTitle, model.structure, model.template);
    }
}