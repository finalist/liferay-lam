package nl.finalist.liferay.lam.admin.web.portlet;

import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.servlet.SessionMessages;

import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import nl.finalist.liferay.lam.admin.service.model.Changelog;
import nl.finalist.liferay.lam.admin.service.service.ChangelogLocalService;

@Component(
	    immediate = true,
	    property = {
	        "javax.portlet.name=nl_finalist_liferay_lam_admin_web_LamAdminWebPortlet",
	        "mvc.command.name=removeLast"
	    },
	    service = MVCActionCommand.class
	)
public class RemoveLastMigrationCommand implements MVCActionCommand{

    private static final Log LOG = LogFactoryUtil.getLog(RemoveLastMigrationCommand.class);

    @Reference
    private ChangelogLocalService changelogLocalService;

    @Override
    public boolean processAction(ActionRequest actionRequest, ActionResponse actionResponse) throws PortletException {

        LOG.info("About to delete last Changelog record.");
        List<Changelog> changelogs = changelogLocalService.getChangelogs(QueryUtil.ALL_POS, QueryUtil.ALL_POS);
        Changelog lastChangelog = changelogs.get(changelogs.size() - 1);
        
        changelogLocalService.deleteChangelog(lastChangelog);

        SessionMessages.add(actionRequest, "remove-success", "");
        return true;
    }

}
