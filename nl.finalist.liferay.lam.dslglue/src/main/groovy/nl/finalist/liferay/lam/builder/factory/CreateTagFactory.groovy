package nl.finalist.liferay.lam.builder.factory
import nl.finalist.liferay.lam.api.Tag;
import nl.finalist.liferay.lam.dslglue.model.TagModel;


class CreateTagFactory extends AbstractFactory {

    Tag tagService;

    CreateTagFactory(Tag tagService) {
        this.tagService = tagService;
    }

    @Override
    Object newInstance(FactoryBuilderSupport builder, Object objectName, Object value, Map attributes)
                    throws InstantiationException, IllegalAccessException {
        new TagModel(attributes);
    }

    @Override
    void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        super.onNodeCompleted(builder, parent, node);
        TagModel model = (TagModel) node;
        tagService.createTag(model.name, model.forSite);
    }
}