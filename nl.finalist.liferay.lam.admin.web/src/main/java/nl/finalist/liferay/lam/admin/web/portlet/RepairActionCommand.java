package nl.finalist.liferay.lam.admin.web.portlet;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.servlet.SessionMessages;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

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
        "mvc.command.name=repair"
    },
    service = MVCActionCommand.class
)
public class RepairActionCommand implements MVCActionCommand {

    @Reference
    private StateManager stateManager;

    @Override
    public boolean processAction(ActionRequest actionRequest, ActionResponse actionResponse) {
        stateManager.repair();
        SessionMessages.add(actionRequest, "repair-success", "");
        return true;
    }
}
