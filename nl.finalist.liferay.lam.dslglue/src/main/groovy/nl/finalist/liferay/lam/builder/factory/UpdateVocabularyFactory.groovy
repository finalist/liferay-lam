package nl.finalist.liferay.lam.builder.factory

import nl.finalist.liferay.lam.api.Vocabulary;
import nl.finalist.liferay.lam.dslglue.model.VocabularyModel;

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
        
        vocabularyService.updateVocabularyTranslation(vocabulary.forLanguage, vocabulary.translation, vocabulary.name);
    }
}
