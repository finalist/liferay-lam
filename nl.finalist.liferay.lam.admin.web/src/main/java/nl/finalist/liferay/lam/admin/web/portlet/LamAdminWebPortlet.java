package nl.finalist.liferay.lam.admin.web.portlet;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.PortletApp;
import com.liferay.portal.kernel.portlet.LiferayPortletConfig;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.servlet.ServletContextPool;

import java.io.IOException;
import java.util.List;

import javax.portlet.Portlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import nl.finalist.liferay.lam.admin.service.model.Changelog;
import nl.finalist.liferay.lam.admin.service.service.ChangelogLocalService;

/**
 * @author danielle.ardon
 */
@Component(
    immediate = true,
    property = {
        "com.liferay.portlet.add-default-resource=true",
        "com.liferay.portlet.render-weight=100",
        "com.liferay.portlet.display-category=category.hidden",
        "javax.portlet.display-name=LAM - Migration Logs",
        "javax.portlet.name=nl_finalist_liferay_lam_admin_web_LamAdminWebPortlet",
        "javax.portlet.init-param.template-path=/",
        "javax.portlet.expiration-cache=0",
        "javax.portlet.init-param.view-template=/view.jsp",
        "javax.portlet.resource-bundle=content.Language",
        "javax.portlet.security-role-ref=administrator"
    },
    service = Portlet.class
)
public class LamAdminWebPortlet extends MVCPortlet {

    private static final Log LOG = LogFactoryUtil.getLog(LamAdminWebPortlet.class);
    private ChangelogLocalService changelogLocalService;

    @Reference(unbind = "-")
    protected void setChangelogLocalService(ChangelogLocalService changelogLocalService) {
        this.changelogLocalService = changelogLocalService;
    }


    @Override
    public void render(RenderRequest request, RenderResponse response) throws IOException, PortletException {
        List<Changelog> changelogs = changelogLocalService.getChangelogs(0, changelogLocalService.getChangelogsCount());
        LOG.debug("Number of changelog entries: " + changelogs.size());
        request.setAttribute("changelogs", changelogs);
        super.render(request, response);
    }

    @Override
    public void destroy() {
        PortletContext portletContext = getPortletContext();

        ServletContextPool.remove(portletContext.getPortletContextName());

        super.destroy();
    }

    @Override
    public void init(PortletConfig portletConfig) throws PortletException {
        super.init(portletConfig);
        LiferayPortletConfig liferayPortletConfig = (LiferayPortletConfig) portletConfig;
        com.liferay.portal.kernel.model.Portlet portlet = liferayPortletConfig.getPortlet();
        PortletApp portletApp = portlet.getPortletApp();
        ServletContextPool.put(portletApp.getServletContextName(), portletApp.getServletContext());
    }
}