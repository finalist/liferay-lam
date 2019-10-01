package nl.finalist.liferay.lam.builder.factory.update

import nl.finalist.liferay.lam.api.Vocabulary;
import nl.finalist.liferay.lam.dslglue.model.VocabularyModel;
import nl.finalist.liferay.lam.util.LocaleMapConverter;
class UpdateVocabularyFactory extends AbstractFactory {

    Vocabulary vocabularyService;

    UpdateVocabularyFactory(Vocabulary vocabularyService) {
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
        vocabularyService.updateVocabularyTranslation(vocabulary.webIds, vocabulary.existingName,LocaleMapConverter.convert(vocabulary.name));
    }
}
