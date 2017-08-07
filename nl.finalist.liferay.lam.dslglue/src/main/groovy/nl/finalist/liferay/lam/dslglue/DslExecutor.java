package nl.finalist.liferay.lam.dslglue;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.security.permission.ActionKeys;

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
import nl.finalist.liferay.lam.api.*;
import nl.finalist.liferay.lam.builder.*;
import nl.finalist.liferay.lam.dslglue.*;

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
    @Reference
    private Category categoryService;
    @Reference
    private RoleAndPermissions roleAndPermissionsService;
    @Reference
    private UserGroups userGroupsService;
    @Reference
    private WebContent webContentService;

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

        sharedData.setVariable("create", new CreateFactoryBuilder(customFieldsService, vocabularyService, categoryService, userGroupsService, roleAndPermissionsService, webContentService));
        sharedData.setVariable("update", new UpdateFactoryBuilder(portalSettingsService, vocabularyService, categoryService, webContentService));
        sharedData.setVariable("validate", new ValidateFactoryBuilder(portalPropertiesService));
        sharedData.setVariable("delete", new DeleteFactoryBuilder(customFieldsService, vocabularyService, categoryService, webContentService));

        sharedData.setVariable("Roles", new Roles());
        sharedData.setVariable("Entities", new Entities());
        sharedData.setVariable("ActionKeys", new ActionKeys());

        CompilerConfiguration conf = new CompilerConfiguration();
        ImportCustomizer imports = new ImportCustomizer();

        imports.addImport("TypeOfRole", "nl.finalist.liferay.lam.api.TypeOfRole");
        imports.addImport("CustomFieldType", "nl.finalist.liferay.lam.dslglue.CustomFieldType");
        imports.addImport("DisplayType", "nl.finalist.liferay.lam.dslglue.DisplayType");

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