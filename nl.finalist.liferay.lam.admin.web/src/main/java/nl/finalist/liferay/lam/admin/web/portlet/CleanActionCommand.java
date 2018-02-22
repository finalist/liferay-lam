package nl.finalist.liferay.lam.admin.web.portlet;

import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.servlet.SessionMessages;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import nl.finalist.liferay.lam.admin.service.service.ChangelogLocalService;

/**
 * @author Liferay
 */
@Component(
    immediate = true,
    property = {
        "javax.portlet.name=nl_finalist_liferay_lam_admin_web_LamAdminWebPortlet",
        "mvc.command.name=clean"
    },
    service = MVCActionCommand.class
)
public class CleanActionCommand implements MVCActionCommand {

    private static final Log LOG = LogFactoryUtil.getLog(CleanActionCommand.class);

    @Reference
    private ChangelogLocalService changelogLocalService;

    @Override
    public boolean processAction(ActionRequest actionRequest, ActionResponse actionResponse) throws PortletException {

        LOG.info("About to delete all Changelog records.");
        changelogLocalService.getChangelogs(QueryUtil.ALL_POS, QueryUtil.ALL_POS)
            .forEach(changelogLocalService::deleteChangelog);

        SessionMessages.add(actionRequest, "clean-success", "");
        return true;
    }
}
