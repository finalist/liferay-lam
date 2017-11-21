<%@ include file="init.jsp" %>

<% List<Changelog> changelogs = (List<Changelog>)request.getAttribute("changelogs"); %>

<aui:nav-bar markupView="lexicon">
<aui:nav>
    <aui:nav-item href="#" label="nl.finalist.liferay.lam.admin.web.caption" />
</aui:nav>
</aui:nav-bar>

<div class="container-fluid-1280">
<liferay-ui:search-container total="<%= changelogs.size() %>" emptyResultsMessage="No changelogs found.">
    <liferay-ui:search-container-results
        results="<%= changelogs %>" />

    <liferay-ui:search-container-row 
        className="nl.finalist.liferay.lam.admin.service.model.Changelog" modelVar="changelog" escapedModel="true">

		<liferay-ui:search-container-column-text name="Installed rank" property="installed_rank" valign="top"/>
        <liferay-ui:search-container-column-text name="Version" property="version" valign="top"/>
        <liferay-ui:search-container-column-text name="Description" value="${changelog.description}" valign="top"/>
        <liferay-ui:search-container-column-text name="Type" value="${changelog.type}" valign="top"/>
        <liferay-ui:search-container-column-text name="Checksum" value="${changelog.checksum}" valign="top"/>
        <liferay-ui:search-container-column-text name="Installed by" value="${changelog.installed_by}" valign="top"/>
        <liferay-ui:search-container-column-text name="Installed on" value="${changelog.installed_on}" valign="top"/>
        <liferay-ui:search-container-column-text name="Execution time" value="${changelog.execution_time}" valign="top"/>
        <liferay-ui:search-container-column-text name="Success" value="${changelog.success}" valign="top"/>

    </liferay-ui:search-container-row>
    <liferay-ui:search-iterator markupView="lexicon" />
</liferay-ui:search-container>
</div>