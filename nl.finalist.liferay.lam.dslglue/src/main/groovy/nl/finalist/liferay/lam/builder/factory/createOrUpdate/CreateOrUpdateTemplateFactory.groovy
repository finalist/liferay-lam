package nl.finalist.liferay.lam.builder.factory.createOrUpdate
import org.osgi.framework.Bundle;

import nl.finalist.liferay.lam.api.Template;
import nl.finalist.liferay.lam.dslglue.model.TemplateModel;
import nl.finalist.liferay.lam.util.LocaleMapConverter;


class CreateOrUpdateTemplateFactory extends AbstractFactory {

    Template templateService;
    Bundle bundle;

    CreateOrUpdateTemplateFactory(Template templateService, Bundle bundle) {
        this.templateService = templateService;
        this.bundle = bundle;
    }

    @Override
    Object newInstance(FactoryBuilderSupport builder, Object objectName, Object value, Map attributes)
    throws InstantiationException, IllegalAccessException {
        new TemplateModel(attributes);
    }

    @Override
    void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        super.onNodeCompleted(builder, parent, node);
        TemplateModel model = (TemplateModel) node;

        // Method call updated to use webIds available in groovy model
        templateService.createOrUpdateTemplate(model.webIds, model.templateKey, model.file, bundle ,model.forStructure, LocaleMapConverter.convert(model.nameMap), LocaleMapConverter.convert(model.descriptionMap), model.siteKey);
    }
}