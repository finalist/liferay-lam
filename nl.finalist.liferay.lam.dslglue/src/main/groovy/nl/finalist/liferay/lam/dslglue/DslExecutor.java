package nl.finalist.liferay.lam.dslglue;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.security.permission.ActionKeys;

import java.io.Reader;

import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ImportCustomizer;
import org.osgi.framework.Bundle;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import nl.finalist.liferay.lam.api.ADT;
import nl.finalist.liferay.lam.api.Category;
import nl.finalist.liferay.lam.api.CustomFields;
import nl.finalist.liferay.lam.api.Page;
import nl.finalist.liferay.lam.api.PortalProperties;
import nl.finalist.liferay.lam.api.PortalSettings;
import nl.finalist.liferay.lam.api.RoleAndPermissions;
import nl.finalist.liferay.lam.api.Site;
import nl.finalist.liferay.lam.api.Structure;
import nl.finalist.liferay.lam.api.Tag;
import nl.finalist.liferay.lam.api.Template;
import nl.finalist.liferay.lam.api.User;
import nl.finalist.liferay.lam.api.UserGroups;
import nl.finalist.liferay.lam.api.Vocabulary;
import nl.finalist.liferay.lam.api.WebContent;
import nl.finalist.liferay.lam.builder.CreateFactoryBuilder;
import nl.finalist.liferay.lam.builder.CreateOrUpdateFactoryBuilder;
import nl.finalist.liferay.lam.builder.DeleteFactoryBuilder;
import nl.finalist.liferay.lam.builder.UpdateFactoryBuilder;
import nl.finalist.liferay.lam.builder.ValidateFactoryBuilder;



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
    private Site siteService;
    @Reference
    private Category categoryService;
    @Reference
    private RoleAndPermissions roleAndPermissionsService;
    @Reference
    private UserGroups userGroupsService;
    @Reference
    private Structure structureService;
    @Reference
    private Template templateService;
    @Reference
    private ADT adtService;
    @Reference
    private Page pageService;
    @Reference
    private WebContent webContentService;
    @Reference
    private User userService;
    @Reference
    private Tag tagService;

    @Activate
    public void activate() {
        LOG.debug("Bundle Activate DslExecutor");
    }

    @Override
    public void runScripts(Bundle bundle, Reader... scripts) {
        LOG.debug("DSL Executor running the available scripts");

        Binding sharedData = new Binding();

        // Add all available API classes to the context of the scripts
        sharedData.setVariable("LOG", LOG);

        sharedData.setVariable("create", new CreateFactoryBuilder(customFieldsService, vocabularyService, siteService,
                        categoryService, userGroupsService, roleAndPermissionsService, pageService, tagService,
                        userService));

        sharedData.setVariable("update", new UpdateFactoryBuilder(portalSettingsService, vocabularyService,
                        siteService, categoryService, userService));
        sharedData.setVariable("validate", new ValidateFactoryBuilder(portalPropertiesService));
        sharedData.setVariable("delete", new DeleteFactoryBuilder(customFieldsService, vocabularyService,
                        siteService, categoryService, webContentService, tagService, userService));
        sharedData.setVariable("createOrUpdate", new CreateOrUpdateFactoryBuilder(structureService,templateService,
                        adtService, webContentService, bundle));

        sharedData.setVariable("Roles", new Roles());
        sharedData.setVariable("Entities", new Entities());
        sharedData.setVariable("ActionKeys", new ActionKeys());
        sharedData.setVariable("Templates", new Templates());
        sharedData.setVariable("ADTTypes", new ADTTypes());

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