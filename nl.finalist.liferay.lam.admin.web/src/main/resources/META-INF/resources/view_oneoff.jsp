<%@ include file="/init.jsp" %>

<div class="container-fluid">
    <div class="row">
        <div class="col-md-12">
            <h1>Upload and run Groovy DSL script</h1> 
        </div>
    </div>
    <div class="row">
        <div class="col-md-12">
            <p><i>Currently, only scripts without the need of necessary artifact files are supported, i.e. scripts working with webcontent, ADT, template, structure or document types are not (yet) supported </i></p>
            <p><i>Samples for scripts can be found in the samples-oneoff resourcefolder of nl.finalist.liferay.lam.admin.web</i></p>
        </div>
    </div>
    <div class="row">
        <div class="col-md-12">
            <liferay-portlet:actionURL name="runoneoff" var="runoneoffURL" />
            <aui:form action="<%=runoneoffURL%>" method="post" name="fm" enctype="multipart/form-data">
                <aui:input type="file" name="dslscript" label="Indicate the script here" />
                <aui:input type="file" name="content"  multiple="true" />
                <aui:button type="submit" value="Run" />
            </aui:form>
        </div>
    </div>
</div>
