package nl.finalist.liferay.lam.admin.web.portlet;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import nl.finalist.liferay.lam.statemgnt.api.StateManager;

/**
 * @author Liferay
 */
@Component(
    immediate = true,
    property = {
        "javax.portlet.name=nl_finalist_liferay_lam_admin_web_LamAdminWebPortlet",
        "mvc.command.name=baseline"
    },
    service = MVCActionCommand.class
)
public class BaselineActionCommand implements MVCActionCommand {

    private static final Log LOG = LogFactoryUtil.getLog(BaselineActionCommand.class);

    @Reference
    private StateManager stateManager;

    @Override
    public boolean processAction(ActionRequest actionRequest, ActionResponse actionResponse) throws PortletException {
        try {
            stateManager.baseline();
            SessionMessages.add(actionRequest, "baseline-success", "");
            return false;
        } catch (Exception e) {
            LOG.error("While calling baseline command from admin portlet", e);
            SessionMessages.add(actionRequest, "baseline-error", "");
            return true;
        }
    }
}
