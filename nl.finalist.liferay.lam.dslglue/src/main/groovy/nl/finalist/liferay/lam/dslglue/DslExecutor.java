package nl.finalist.liferay.lam.dslglue;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.io.File;
import java.io.Reader;

import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ImportCustomizer;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyShell;
import nl.finalist.liferay.lam.api.CustomFields;
import nl.finalist.liferay.lam.api.PortalProperties;
import nl.finalist.liferay.lam.api.PortalSettings;
import nl.finalist.liferay.lam.api.Vocabulary;
import nl.finalist.liferay.lam.builder.CreateFactoryBuilder;
import nl.finalist.liferay.lam.builder.DeleteFactoryBuilder;
import nl.finalist.liferay.lam.builder.UpdateFactoryBuilder;
import nl.finalist.liferay.lam.builder.ValidateFactoryBuilder;
import nl.finalist.liferay.lam.dslglue.Executor;
import nl.finalist.liferay.lam.model.Entities;
import nl.finalist.liferay.lam.model.Roles;

/**
 * Executor that evaluates configured scripts using a context containing all
 * available APIs.
 */
@Component(immediate = true, service = Executor.class)
public class DslExecutor implements Executor {

    private static final Log LOG = LogFactoryUtil.getLog(DslExecutor.class);

    @Reference
    private CustomFields customFieldsService;
    @Reference
    private PortalSettings portalSettingsService;
    @Reference
    private Vocabulary vocabularyService;
    @Reference
    private PortalProperties portalPropertiesService;

    @Activate
    public void activate() {
        LOG.debug("Bundle Activate DslExecutor");
    }

    @Override
    public void runScripts(Reader... scripts) {
        LOG.debug("DSL Executor running the available scripts");

        Binding sharedData = new Binding();
        
        // Add all available API classes to the context of the scripts
        sharedData.setVariable("LOG", LOG);

        sharedData.setVariable("create", new CreateFactoryBuilder(customFieldsService, vocabularyService));
        sharedData.setVariable("update", new UpdateFactoryBuilder(portalSettingsService, vocabularyService));
        sharedData.setVariable("validate", new ValidateFactoryBuilder(portalPropertiesService));
        sharedData.setVariable("delete", new DeleteFactoryBuilder(customFieldsService, vocabularyService));

        sharedData.setVariable("Roles", new Roles());
        sharedData.setVariable("Entities", new Entities());
        CompilerConfiguration conf = new CompilerConfiguration();
        ImportCustomizer imports = new ImportCustomizer();

        // Make these imports available to the scripts

        conf.addCompilationCustomizers(imports);

        // Use the classloader of this class
        ClassLoader classLoader = this.getClass().getClassLoader();

        GroovyShell shell = new GroovyShell(classLoader, sharedData, conf);

        for (Reader script : scripts) {
            shell.evaluate(script);
        }
    }
}