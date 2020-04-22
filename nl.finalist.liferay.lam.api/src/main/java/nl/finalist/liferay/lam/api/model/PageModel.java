package nl.finalist.liferay.lam.api.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PageModel {

    private String[] webIds;

    private String siteKey;

    private boolean privatePage;

    private Map<String, String> nameMap;

    private Map<Locale, String> titleMap;

    private Map<Locale, String> descriptionMap;

    private Map<String, String> friendlyUrlMap;

    private Map<String, String> customFields;

    private String typeSettings;

    private String parentUrl;

    private boolean hiddenPage;

    private String linkedPageUrl;

    private String externalUrl;

    private List<ColumnModel> columns = new ArrayList<>();

    public PageModel(LinkedHashMap<String, Object> map) {
        this((List<String>) map.get("webIds"), (String) map.get("siteKey"), (Boolean) map.get("privatePage"), (Map) map.get("nameMap"),
             (Map) map.get("titleMap"), (Map) map.get("descriptionMap"), (Map) map.get("friendlyUrlMap"), (String) map.get("typeSettings"),
             (Map) map.get("customFields"), (String) map.get("parentUrl"), (String) map.get("linkedPageUrl"), (String) map.get("externalUrl"));
    }

    public PageModel(List<String> webIds,
                     String siteKey,
                     boolean privatePage,
                     Map<String, String> nameMap,
                     Map<Locale, String> titleMap,
                     Map<Locale, String> descriptionMap,
                     Map<String, String> friendlyUrlMap,
                     String typeSettings,
                     Map<String, String> customFields,
                     String parentUrl,
                     String linkedPageUrl,
                     String externalUrl) {
        this.setWebIds(webIds.toArray(new String[webIds.size()]));
        this.privatePage = privatePage;
        this.nameMap = nameMap;
        this.titleMap = titleMap;
        this.descriptionMap = descriptionMap;
        this.friendlyUrlMap = friendlyUrlMap;
        this.typeSettings = typeSettings;
        this.customFields = customFields;
        this.parentUrl = parentUrl;
        this.linkedPageUrl = linkedPageUrl;
        this.setExternalUrl(externalUrl);
    }

    public String[] getWebIds() {
        return webIds;
    }

    public void setWebIds(String[] webIds) {
        this.webIds = webIds;
    }

    /**
     * Return a list of all the portlet ids contained in all the columns
     * 
     * @return
     */
    public List<String> getPortletIds() {
        List<String> ids = new ArrayList<>();
        for (ColumnModel column : columns) {
            for (PortletModel portlet : column.getPortlets()) {
                ids.add(portlet.getId());
            }
        }
        return ids;
    }

    public boolean isPrivatePage() {
        return privatePage;
    }

    public void setPrivatePage(boolean privatePage) {
        this.privatePage = privatePage;
    }

    public Map<String, String> getNameMap() {
        return nameMap;
    }

    public void setNameMap(Map<String, String> nameMap) {
        this.nameMap = nameMap;
    }

    public Map<Locale, String> getTitleMap() {
        return titleMap;
    }

    public void setTitleMap(Map<Locale, String> titleMap) {
        this.titleMap = titleMap;
    }

    public Map<Locale, String> getDescriptionMap() {
        return descriptionMap;
    }

    public void setDescriptionMap(Map<Locale, String> descriptionMap) {
        this.descriptionMap = descriptionMap;
    }

    public Map<String, String> getFriendlyUrlMap() {
        return friendlyUrlMap;
    }

    public void setFriendlyUrlMap(Map<String, String> friendlyUrlMap) {
        this.friendlyUrlMap = friendlyUrlMap;
    }

    public String getTypeSettings() {
        return typeSettings;
    }

    public void setTypeSettings(String typeSettings) {
        this.typeSettings = typeSettings;
    }

    public Map<String, String> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(Map<String, String> customFields) {
        this.customFields = customFields;
    }

    public String getParentUrl() {
        return parentUrl;
    }

    public void setParentUrl(String parentUrl) {
        this.parentUrl = parentUrl;
    }

    public boolean isHiddenPage() {
        return hiddenPage;
    }

    public void setHiddenPage(boolean hiddenPage) {
        this.hiddenPage = hiddenPage;

    }

    public void setSiteKey(String siteKey) {
        this.siteKey = siteKey;
    }

    public String getSiteKey() {
        return siteKey;
    }

    public String getLinkedPageUrl() {
        return linkedPageUrl;
    }

    public void setLinkedPageUrl(String linkedPageUrl) {
        this.linkedPageUrl = linkedPageUrl;
    }

    public String getExternalUrl() {
        return externalUrl;
    }

    public void setExternalUrl(String externalUrl) {
        this.externalUrl = externalUrl;
    }

    public List<ColumnModel> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnModel> columns) {
        this.columns = columns;
    }

    public void addColumn(ColumnModel column) {
        this.columns.add(column);
    }

    @Override
    public String toString() {
        return "PageModel [privatePage=" + privatePage
               + ", nameMap="
               + nameMap
               + ", titleMap="
               + titleMap
               + ", descriptionMap="
               + descriptionMap
               + ", friendlyUrlMap="
               + friendlyUrlMap
               + ", typeSettings="
               + typeSettings
               + ", customFields="
               + customFields
               + ", parentUrl="
               + parentUrl
               + ", hidden = "
               + hiddenPage
               + ", "
               + columns.size()
               + " columns ]";
    }

}
