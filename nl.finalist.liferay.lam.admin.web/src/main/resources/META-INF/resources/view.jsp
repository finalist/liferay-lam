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


<div class="container">
<div class="row">
<liferay-portlet:actionURL name="repair" var="repairURL" />
<aui:form action="<%= repairURL %>" method="post" name="fm">

    <div class="text-default">Repair is your tool to fix issues with the metadata table. It has two main uses:<br />
        Remove failed migration entries (only for databases that do NOT support DDL transactions)<br />
        Realign the checksums of the applied migrations to the ones of the available migrations
    </div>
    <div class="button-holder">
        <aui:button name="repair" cssClass="btn btn-default" type="submit" value="Repair"/>
    </div>
    <liferay-ui:success key="repair-success" message="Database repaired." />
    
</aui:form>

    <liferay-portlet:actionURL name="baseline" var="baselineURL" />
    <aui:form action="<%= baselineURL %>" method="post" name="fm2">

        <div class="text-danger">Baseline is for introducing Flyway to existing databases by baselining them
            at a specific version.
            The will cause Migrate to ignore all migrations upto and including the baseline version.
            Newer migrations will then be applied as usual.</div>
        <div class="button-holder">
            <aui:button name="baseline" cssClass="btn btn-danger" type="submit" value="Baseline"/>
        </div>
        <liferay-ui:success key="baseline-success" message="Database baselined." />
        <liferay-ui:error key="baseline-error" message="Database baseline failed, error message logged." />
    </aui:form>

    <liferay-portlet:actionURL name="clean" var="cleanURL" />
    <aui:form action="<%= cleanURL %>" method="post" name="fm2">

        <div class="text-danger">Clean is a great help in development and test. It will effectively give you a fresh
            start.<br />
            This is NOT the native Flyway clean command as that wipes out the entire schema. Instead, it only wipes
            out the Flyway changelog table. Make sure your migrations and your portal can handle repeated migrations.
        </div>
        <div class="button-holder">
            <aui:button name="clean" cssClass="btn btn-danger" type="submit" value="Clean"/>
        </div>
        <liferay-ui:success key="clean-success" message="Database cleaned." />
        <liferay-ui:error key="clean-error" message="Database clean failed, error message logged." />
    </aui:form>

</div>
</div>