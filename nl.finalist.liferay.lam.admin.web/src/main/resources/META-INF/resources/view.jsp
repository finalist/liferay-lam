<%@ include file="/init.jsp" %>

<% List<Changelog> changelogs = (List<Changelog>)request.getAttribute("changelogs");%>

<div class="lam-migrations" style="
	margin-right: 20%;
    margin-left: 20%;
    margin-top: 1em;"> 
	<p class="lead">
		<liferay-ui:message key="nl.finalist.liferay.lam.admin.web.caption"/>
	</p>

	<liferay-ui:search-container emptyResultsMessage="No changelogs found."
	    total="<%= changelogs.size() %>">
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
	    <liferay-ui:search-iterator/>
	</liferay-ui:search-container>
</div>