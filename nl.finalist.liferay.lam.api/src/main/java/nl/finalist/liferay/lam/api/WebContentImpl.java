package nl.finalist.liferay.lam.api;

import java.util.Locale;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.liferay.document.library.kernel.model.DLFolderConstants;
import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.service.JournalArticleLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.StringBundler;

/**
 * Implementation for {@link nl.finalist.liferay.lam.api.WebContent}
 */
@Component(immediate = true, service = WebContent.class)
public class WebContentImpl implements WebContent {

	@Reference
	private JournalArticleLocalService journalArticleService;
	@Reference
	private CompanyLocalService companyService;
	private Company defaultCompany;

	private static final Log LOG = LogFactoryUtil.getLog(WebContentImpl.class);

	@Override
	public void addWebContent(Map<Locale, String> titleMap, Map<Locale, String> descriptionMap, String content,
			String urlTitle) {
		try {
			LOG.debug(" Starting to add the article");
			JournalArticle article = journalArticleService.fetchArticleByUrlTitle(getGlobalGroupId(), urlTitle);
			if (article == null) {
				// TODO have a check, search the article if it is null then add it
				ServiceContext serviceContext = new ServiceContext();
				serviceContext.setScopeGroupId(getGlobalGroupId());
				serviceContext.setCurrentURL(urlTitle);
				journalArticleService.addArticle(getDefaultUserId(), getGlobalGroupId(),
						DLFolderConstants.DEFAULT_PARENT_FOLDER_ID, titleMap, descriptionMap,
						processJournalArticleContent(content), "BASIC-WEB-CONTENT", "BASIC-WEB-CONTENT",
						serviceContext);
				LOG.info(String.format("Added an article with Title Url as %s", urlTitle));
			} else {
				LOG.info(String.format("article with the URL Title %s already exists , so addition was not succesful",
						urlTitle));
			}
		} catch (PortalException e) {
			LOG.error(e);
		}
	}

	@Override
	public void updateWebContent(Map<Locale, String> newTitleMap,
			Map<Locale, String> newDescriptionMap, String content, String urlTitle) {
		JournalArticle article = journalArticleService.fetchArticleByUrlTitle(getGlobalGroupId(), urlTitle);
		LOG.info(" Starting to update the article");
		// TODO: If the article doesn't exists then add it.
		if (article == null) {
			addWebContent(newTitleMap, newDescriptionMap, content, urlTitle);
			LOG.info(String.format(
					"The article with UrlTitle %s does not exists so was not able to update so added the page",
					urlTitle));
		} else {
			try {
				ServiceContext serviceContext = new ServiceContext();
				serviceContext.setScopeGroupId(getGlobalGroupId());
				journalArticleService.updateArticle(getDefaultUserId(), getGlobalGroupId(), 0, article.getArticleId(),
						article.getVersion(), newTitleMap, newDescriptionMap, processJournalArticleContent(content),
						null, serviceContext);
				LOG.info(" Updated the article");
			} catch (PortalException e) {
				LOG.error(e);
			}
		}

	}

	@Override
	public void deleteWebContent(String urlTitle) {
		JournalArticle article = journalArticleService.fetchArticleByUrlTitle(getGlobalGroupId(), urlTitle);
		if (article != null) {
			try {
				journalArticleService.deleteJournalArticle(article.getId());
			} catch (PortalException e) {
				LOG.error(e);
			}
		} else {
			LOG.info(String.format("article with urlTitle %s doesn't exists so deletion was not possible", urlTitle));
		}

	}

	private String processJournalArticleContent(String content) {

		if (content.contains("<?xml version=\"1.0\"")) {
			return content;
		}

		StringBundler sb = new StringBundler(13);

		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<root available-locales=\"");
		sb.append(LocaleUtil.getDefault());
		sb.append("\" default-locale=\"");
		sb.append(LocaleUtil.getDefault());
		sb.append("\">");
		sb.append("<static-content language-id=\"");
		sb.append(LocaleUtil.getDefault());
		sb.append("\">");
		sb.append("<![CDATA[");
		sb.append(content);
		sb.append("]]>");
		sb.append("</static-content></root>");

		return sb.toString();
	}

	private long getDefaultUserId() {
		Company defaultCompany = getDefaultCompany();
		long userId = 0;
		try {
			userId = defaultCompany.getDefaultUser().getUserId();
		} catch (PortalException e) {
			LOG.error("Error while retrieving default userId", e);
		}
		return userId;
	}

	private long getGlobalGroupId() {
		defaultCompany = getDefaultCompany();
		long groupId = 0;
		try {
			groupId = defaultCompany.getGroupId();
		} catch (PortalException e) {
			LOG.error("Error while retrieving global groupId", e);
		}
		return groupId;
	}

	private Company getDefaultCompany() {
		if (defaultCompany == null) {
			String webId = PropsUtil.get("company.default.web.id");
			try {
				defaultCompany = companyService.getCompanyByWebId(webId);
			} catch (PortalException e) {
				LOG.error("Error while retrieving default company", e);
			}
		}
		return defaultCompany;
	}
}
