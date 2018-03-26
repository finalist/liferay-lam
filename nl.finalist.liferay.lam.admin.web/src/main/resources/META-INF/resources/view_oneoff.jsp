<%@ include file="/init.jsp" %>

<div class="container-fluid">
    <div class="row">
        <div class="col-md-12">
            <h1>Upload and run Groovy DSL script</h1> 
        </div>
    </div>
    <div class="row">
        <div class="col-md-12">
            <p><i>Some scripts need additional artifact files in order to run. For example, a script working with webcontent needs an article (xml), a structure and a template to go with it.</i></p>
            <p><i>Samples for scripts can be found in the samples-oneoff resourcefolder of nl.finalist.liferay.lam.admin.web</i></p>
        </div>
    </div>
    <div class="row">
        <div class="col-md-12">
            <liferay-portlet:actionURL name="runoneoff" var="runoneoffURL" />
            <aui:form action="<%=runoneoffURL%>" method="post" name="fm" enctype="multipart/form-data">
                <aui:input type="file" name="dslscript" label="Select the script" />
                <aui:input type="file" name="content" label="Optionally select one or more necessary artifact files" multiple="true" />
                <aui:button type="submit" value="Run" />
            </aui:form>
        </div>
    </div>
</div>
