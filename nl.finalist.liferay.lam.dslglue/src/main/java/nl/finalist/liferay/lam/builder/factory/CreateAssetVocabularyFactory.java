package nl.finalist.liferay.lam.builder.factory;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.util.Map;

import groovy.util.AbstractFactory;
import groovy.util.FactoryBuilderSupport;
import nl.finalist.liferay.lam.api.Vocabulary;

public class CreateAssetVocabularyFactory extends AbstractFactory  {
    private static final Log LOG = LogFactoryUtil.getLog(CreateAssetVocabularyFactory.class);
    Vocabulary vocabularyService;

    public  CreateAssetVocabularyFactory(Vocabulary vocabularyService) {
        this.vocabularyService = vocabularyService;
    }

    @Override
    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes)
                    throws InstantiationException, IllegalAccessException {
        String vocabularyName = (String) attributes.get("name");
        vocabularyService.addVocabulary(vocabularyName);
        return attributes;
    }

    @Override
    public void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        super.onNodeCompleted(builder, parent, node);
        LOG.info("Vocabulary node completed");

    }
}
