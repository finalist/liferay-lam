package nl.finalist.liferay.lam.api;

import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.service.JournalArticleLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.PropsUtil;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ PropsUtil.class })
public class WebContentImplTest {

    @Mock
    JournalArticleLocalService journalArticleLocalService;
    @InjectMocks
    WebContentImpl webContentImpl;
    @Mock
    private CompanyLocalService companyService;
    @Mock
    private Company mockCompany;
    @Mock
    private User mockDefaultUser;
    @Mock
    private ServiceContext mockServiceContext;
    @Mock
    private JournalArticle journalArticle;

    @Before
    public void setUp() {
        webContentImpl = new WebContentImpl();
        PowerMockito.mockStatic(PropsUtil.class);

        PowerMockito.when(PropsUtil.get("company.default.web.id")).thenReturn("liferay.com");
        initMocks(this);
    }

    @Test
    public void testAddNewArticle() throws Exception {
        when(companyService.getCompanyByWebId("liferay.com")).thenReturn(mockCompany);
        when(mockCompany.getCompanyId()).thenReturn(1L);
        when(mockCompany.getDefaultUser()).thenReturn(mockDefaultUser);
        when(mockDefaultUser.getUserId()).thenReturn(10L);
        Map<Locale, String> titleMap = new HashMap<>();
        titleMap.put(Locale.US, " Title for US");

        Map<Locale, String> descriptionMap = new HashMap<>();
        descriptionMap.put(Locale.US, " description for US");
        String content = " Content for the map";
        String urlTitle = "Testing webcontent";

        ArgumentCaptor<ServiceContext> argument = ArgumentCaptor.forClass(ServiceContext.class);
        webContentImpl.addWebContent(titleMap, descriptionMap, content, urlTitle);
        verify(journalArticleLocalService).addArticle(anyLong(), anyLong(), anyLong(), anyMap(), anyMap(), anyString(),
                        anyString(), anyString(), argument.capture());
    }

    @Test
    public void testUpdateArticle() throws PortalException {
        when(companyService.getCompanyByWebId("liferay.com")).thenReturn(mockCompany);
        when(mockCompany.getCompanyId()).thenReturn(1L);
        when(mockCompany.getDefaultUser()).thenReturn(mockDefaultUser);
        when(mockCompany.getGroupId()).thenReturn(10L);
        when(mockDefaultUser.getUserId()).thenReturn(10L);

        Map<Locale, String> newTitleMap = new HashMap<>();
        newTitleMap.put(Locale.US, " Title for US");

        Map<Locale, String> newDescriptionMap = new HashMap<>();
        newDescriptionMap.put(Locale.US, " desscription for US");
        String content = " Content for the map";
        String urlTitle = "Testing webcontent";
        journalArticle.setArticleId("1");
        when(journalArticleLocalService.fetchArticleByUrlTitle(10L, urlTitle)).thenReturn(journalArticle);
        webContentImpl.updateWebContent(newTitleMap, newDescriptionMap, content, urlTitle);
        ArgumentCaptor<ServiceContext> argument = ArgumentCaptor.forClass(ServiceContext.class);
        verify(journalArticleLocalService).fetchArticleByUrlTitle(10L, urlTitle);
        verify(journalArticleLocalService).updateArticle(anyLong(), anyLong(), anyLong(), anyString(), anyDouble(),
                        anyMap(), anyMap(), anyString(), anyString(), argument.capture());

    }

    @Test
    public void testUpdateAddNewArticle() throws PortalException {
        when(companyService.getCompanyByWebId("liferay.com")).thenReturn(mockCompany);
        when(mockCompany.getCompanyId()).thenReturn(1L);
        when(mockCompany.getDefaultUser()).thenReturn(mockDefaultUser);
        when(mockCompany.getGroupId()).thenReturn(10L);
        when(mockDefaultUser.getUserId()).thenReturn(10L);

        Map<Locale, String> newTitleMap = new HashMap<>();
        newTitleMap.put(Locale.US, " Title for US");

        Map<Locale, String> newDescriptionMap = new HashMap<>();
        newDescriptionMap.put(Locale.US, " desscription for US");
        String content = " Content for the map";
        String urlTitle = "Testing webcontent";
        journalArticle.setArticleId("1");
        when(journalArticleLocalService.fetchArticleByUrlTitle(10L, urlTitle)).thenReturn(null);
        webContentImpl.updateWebContent(newTitleMap, newDescriptionMap, content, urlTitle);
        ArgumentCaptor<ServiceContext> argument = ArgumentCaptor.forClass(ServiceContext.class);
        verify(journalArticleLocalService, never()).updateArticle(anyLong(), anyLong(), anyLong(), anyString(),
                        anyDouble(), anyMap(), anyMap(), anyString(), anyString(), argument.capture());
        verify(journalArticleLocalService).addArticle(anyLong(), anyLong(), anyLong(), anyMap(), anyMap(), anyString(),
                        anyString(), anyString(), argument.capture());
    }

    @Test
    public void testDeleteArticle() throws PortalException {
        when(companyService.getCompanyByWebId("liferay.com")).thenReturn(mockCompany);
        when(mockCompany.getGroupId()).thenReturn(10L);
        String urlTitle = "Delete webcontent";
        when(journalArticleLocalService.fetchArticleByUrlTitle(10L, urlTitle)).thenReturn(journalArticle);
        webContentImpl.deleteWebContent(urlTitle);
        verify(journalArticleLocalService).fetchArticleByUrlTitle(10L, urlTitle);
        verify(journalArticleLocalService).deleteJournalArticle(anyLong());
    }

    @Test
    public void testNotDeleteArticle() throws PortalException {
        when(companyService.getCompanyByWebId("liferay.com")).thenReturn(mockCompany);
        when(mockCompany.getGroupId()).thenReturn(10L);
        String urlTitle = "Delete webcontent";
        when(journalArticleLocalService.fetchArticleByUrlTitle(10L, urlTitle)).thenReturn(null);
        webContentImpl.deleteWebContent(urlTitle);
        verify(journalArticleLocalService).fetchArticleByUrlTitle(10L, urlTitle);
        verify(journalArticleLocalService, never()).deleteJournalArticle(anyLong());
    }

}
