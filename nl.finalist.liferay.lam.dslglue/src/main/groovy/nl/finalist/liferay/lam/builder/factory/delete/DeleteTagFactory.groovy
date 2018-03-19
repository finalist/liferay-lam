package nl.finalist.liferay.lam.builder.factory

import nl.finalist.liferay.lam.api.Tag
import nl.finalist.liferay.lam.dslglue.model.TagModel;

class DeleteTagFactory extends AbstractFactory{
    Tag tagService;

    DeleteTagFactory(Tag tagService){
        this.tagService = tagService;
    }

    @Override
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes)
                    throws InstantiationException, IllegalAccessException {
        new TagModel(attributes);
    }

    @Override
    void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        super.onNodeCompleted(builder, parent, node);
        TagModel model = (TagModel) node;
        tagService.deleteTag(model.name, model.forSite);
    }
}
