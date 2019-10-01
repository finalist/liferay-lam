package nl.finalist.liferay.lam.builder.factory.create

import nl.finalist.liferay.lam.api.Vocabulary;
import nl.finalist.liferay.lam.dslglue.model.VocabularyModel;
import nl.finalist.liferay.lam.util.LocaleMapConverter;

class CreateVocabularyFactory extends AbstractFactory  {
    Vocabulary vocabularyService;

    CreateVocabularyFactory(Vocabulary vocabularyService) {
        this.vocabularyService = vocabularyService;
    }

    @Override
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes)
    throws InstantiationException, IllegalAccessException {
        new VocabularyModel(attributes);
    }

    @Override
    void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        super.onNodeCompleted(builder, parent, node);
        VocabularyModel vocabulary = (VocabularyModel) node;
        // Method call updated to use webIds available in groovy model
        vocabularyService.addVocabulary(vocabulary.webIds, LocaleMapConverter.convert(vocabulary.name));
    }
}
