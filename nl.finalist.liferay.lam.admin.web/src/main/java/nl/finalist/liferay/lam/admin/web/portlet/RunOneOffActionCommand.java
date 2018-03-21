package nl.finalist.liferay.lam.admin.web.portlet;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.upload.FileItem;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.WebKeys;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Map;
import java.util.Map.Entry;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import nl.finalist.liferay.lam.dslglue.Executor;

@Component(
	    immediate = true,
	    property = {
	        "javax.portlet.name=nl_finalist_liferay_lam_admin_web_RunOneOffPortlet",
	        "mvc.command.name=runoneoff"
	    },
	    service = MVCActionCommand.class
)
public class RunOneOffActionCommand implements MVCActionCommand {
    private static final String TEMP_LAM_SUBDIR = System.getProperty("java.io.tmpdir") + "/lam";
    private static final Log LOG = LogFactoryUtil.getLog(RunOneOffActionCommand.class);

    @Reference
    private Executor executor;

    @Override
	public boolean processAction(ActionRequest actionRequest, ActionResponse actionResponse) throws PortletException {
		ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
		String scriptName = uploadFiles(themeDisplay, actionRequest);
		if (scriptName != null) {
		    File script = runScript(scriptName);
	        script.delete();
		}
		
		return true;
	}

    private String uploadFiles(ThemeDisplay themeDisplay, ActionRequest actionRequest) {
		UploadPortletRequest uploadPortletRequest = PortalUtil.getUploadPortletRequest(actionRequest);
		Map<String, FileItem[]> files = uploadPortletRequest.getMultipartParameterMap();
        String scriptName = null;
		for (Entry<String, FileItem[]> f : files.entrySet()) {
			FileItem item[] = f.getValue();
			try {
				for (FileItem fileItem : item) {
					String fileName = fileItem.getFileName();
					InputStream is = fileItem.getInputStream();
					File dir = new File(TEMP_LAM_SUBDIR);
					dir.mkdir();
				    
			        File file;
					if (fileName.contains(".groovy")) {
	                    file = File.createTempFile("tmp", ".groovy", dir);
	                    scriptName = file.getName();
					} else {
                        file = new File(TEMP_LAM_SUBDIR +  StringPool.SLASH + fileName);
					}

					OutputStream os = new FileOutputStream(file);
	                byte[] buffer = new byte[4096];
	                int bytesRead = -1;
	                while ((bytesRead = is.read(buffer)) != -1) {
	                    os.write(buffer, 0, bytesRead);
	                }
	                os.close();
				}
			} catch (IOException e) {
	            LOG.error("IOException while uploading files", e);
			}
		}
		return scriptName;
	}
	
    private File runScript(String scriptName) {
	    Bundle bundle = FrameworkUtil.getBundle(this.getClass());
	    File script = new File(TEMP_LAM_SUBDIR + StringPool.SLASH + scriptName);
	    try {
	        InputStream is = new FileInputStream(script);
	        executor.runScripts(bundle, new InputStreamReader(is));
        } catch (FileNotFoundException e) {
            LOG.error(String.format("Script not found %s", script.getAbsolutePath()), e);
	    }
	    return script;
	}
}
