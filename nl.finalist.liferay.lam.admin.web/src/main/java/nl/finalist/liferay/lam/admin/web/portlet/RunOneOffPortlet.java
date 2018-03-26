package nl.finalist.liferay.lam.admin.web.portlet;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;

import javax.portlet.Portlet;

import org.osgi.service.component.annotations.Component;

/**
 * @author mathon.gijsbers
 */
@Component(
	immediate = true,
	property = {
        "com.liferay.portlet.add-default-resource=true",
        "com.liferay.portlet.render-weight=100",
        "com.liferay.portlet.display-category=category.hidden",
		"javax.portlet.display-name=LAM - Run One-off",
        "javax.portlet.name=nl_finalist_liferay_lam_admin_web_RunOneOffPortlet",
		"javax.portlet.init-param.template-path=/",
		"javax.portlet.init-param.view-template=/view_oneoff.jsp",
        "javax.portlet.expiration-cache=0",
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.security-role-ref=power-user,user"
	},
	service = Portlet.class
)
public class RunOneOffPortlet extends MVCPortlet {
}