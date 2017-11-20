package nl.finalist.liferay.lam.admin.web.portlet;

import com.liferay.application.list.BasePanelApp;
import com.liferay.application.list.PanelApp;
import com.liferay.application.list.constants.PanelCategoryKeys;
import com.liferay.portal.kernel.model.Portlet;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
@Component(
                immediate = true,
                property = {
                                "panel.category.key=" + PanelCategoryKeys.CONTROL_PANEL_APPS,
                                "service.ranking:Integer=100"
                },
                service = PanelApp.class
                )
public class LamControlPanelApp extends BasePanelApp {

    @Override
    public String getPortletId() {
        return "nl_finalist_liferay_lam_admin_web_LamAdminWebPortlet";
    }

    @Override
    @Reference(
                    target = "(javax.portlet.name=nl_finalist_liferay_lam_admin_web_LamAdminWebPortlet)",
                    unbind = "-"
                    )
    public void setPortlet(Portlet portlet) {
        super.setPortlet(portlet);
    }
}
