

<%@ include file="init.jsp" %>

<% List<Changelog> changelogs = (List<Changelog>)request.getAttribute("changelogs"); %>


<nav class="navbar navbar-default">
<div class="container-fluid-1280">
		        <ul class="nav nav-tabs nav-tabs-default" role="tablist">
		            <li class="active" role="presentation"><a aria-controls="LAM-executed-migrations" href="#LAM-executed-migrations" data-toggle="tab" role="tab">Executed Liferay Automatic Migrations</a></li>
		            <li role="presentation"><a aria-controls="LAM-admin-actions" href="#LAM-admin-actions" data-toggle="tab" role="tab">Administrator Tools</a></li>
		         </ul>
		         </div>
</nav>

	<div class="tab-content">
        <div role="tabpanel" class="tab-pane active fade in" id="LAM-executed-migrations" style="margin-top:1em">          
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
          </div>
        <div role="tabpanel" class="tab-pane fade" id="LAM-admin-actions" style="margin-top:1em">
            <div class="container-fluid-1280">
				<div class="row">
					 <div class="col-md-12">
		            	<div class="alert alert-danger">
		    				<strong class="lead">Warning</strong> The <i>Baseline</i>, <i>Clean</i> & <i>Remove last migration</i> tools should only be used in development/test mode. These tools will irreversibly alter the migration metadata table.
						</div>
		        	</div>
					<div class="col-md-3">
						<div class="panel panel-default">
							<div class="panel-heading"><b>Repair Tool</b></br> Repairs metadata table</div>
							<div class="panel-body">
								<p>	 Repair is your tool to fix issues with the metadata table. It has two main uses:</p>
				     			<p>  Remove failed migration entries (only for databases that do NOT support DDL transactions)</p>
				     			<p>  Realign the checksums of the applied migrations to the ones of the available migrations</p>
				     			<br>
				     			<br>
							</div>
							<div class="panel-footer">
								<liferay-portlet:actionURL name="repair" var="repairURL" />
								<aui:form action="<%= repairURL %>" method="post" name="fm1">
									<aui:button name="repair" cssClass="btn btn-primary" onClick="showAddNoteDialog('fm1', 'Repair')"value="Repair"/>
								</aui:form>	
								<liferay-ui:success key="repair-success" message="Database repaired." />
							</div>	
						</div>
					</div>	
					<div class="col-md-3">
						<div class="panel panel-default">
							<div class="panel-heading"><b>Baseline Tool</b> </br>(Re)sets migrations to baseline</div>
							<div class="panel-body">
								<p> Baseline is for introducing Flyway to existing databases by baselining them at a specific version.</p>
				            	<p>The will cause Migrate to ignore all migrations upto and including the baseline version.</p>
								<p> Newer migrations will then be applied as usual. </p>
								<br>
								<br>
							</div>
							<div class="panel-footer">
								<liferay-portlet:actionURL name="baseline" var="baselineURL" />
								<aui:form action="<%= baselineURL %>" method="post" name="fm2">
					            		<aui:button name="baseline" cssClass="btn btn-danger" onClick="showAddNoteDialog('fm2', 'Baseline')" value="Baseline"/>
										<liferay-ui:success key="baseline-success" message="Database baselined." />
					        			<liferay-ui:error key="baseline-error" message="Database baseline failed, error message logged." />
					       		</aui:form>
							</div>
						</div>
					</div>	
					<div class="col-md-3">
						<div class="panel panel-default">
							<div class="panel-heading"><b>Clean tool</b> </br>Removes all migration entries</div>
							<div class="panel-body">
								<p>Clean is a great help in development and test. It will effectively give you a fresh start.</p>
				          		<p> This is NOT the native Flyway clean command as that wipes out the entire schema. Instead, it only wipes out the Flyway changelog table.</p>
				          		<p>Make sure your migrations and your portal can handle repeated migrations.</p>
							</div>
							<div class="panel-footer">
								<liferay-portlet:actionURL name="clean" var="cleanURL" />
					    		<aui:form action="<%= cleanURL %>" method="post" name="fm3">
					            		<aui:button name="clean" cssClass="btn btn-danger"  onClick="showAddNoteDialog('fm3', 'Clean')" value="Clean" title="Test"/>
					        			<liferay-ui:success key="clean-success" message="Database cleaned." />
					        			<liferay-ui:error key="clean-error" message="Database clean failed, error message logged." />
					   			 </aui:form>
							</div>
						</div>
					</div>	
						<div class="col-md-3">
						<div class="panel panel-default">
							<div class="panel-heading"><b>Remove last migration tool</b> </br>Removes last migration</div>
							<div class="panel-body">
								<p>The remove last migration is a great help in development and test. It will remove the last migration from the metadata table.</p>
								<p> This will allow you to migrate the last migration again.</p>
				          		<p>Make sure your migrations and your portal can handle repeated migrations.</p>
				          		<br>
							</div>
							<div class="panel-footer">
								<liferay-portlet:actionURL name="removeLast" var="removeLastURL" />
					    		<aui:form action="<%= removeLastURL %>" method="post" name="fm4">
					            		<aui:button name="removeLast" cssClass="btn btn-danger"  onClick="showAddNoteDialog('fm4', 'Remove last migration')" value="Remove last migration" />
					        			<liferay-ui:success key="remove-success" message="Last migration removed." />
					        			<liferay-ui:error key="remove-error" message="Remove last migration failed, error message logged." />
					   			 </aui:form>
							</div>
						</div>
					</div>	
				</div>
			</div>
		</div>	
    </div>
    
    
   <script>
    
    function showAddNoteDialog(identifier, toolName){
  
   AUI().use('aui-modal', function(A) {
       var modal = new A.Modal(
         {
           bodyContent: '<h3>Are you sure you want to execute <b>'+toolName+'</b>?</h3>',
           centered: true,
           modal: true,
           render: '#modal',
           zIndex: 100,
           width: 500
         }
       ).render();
        modal.addToolbar(
              [
                {
                  label: 'Cancel',
                  on: {
                    click: function() {
                     modal.hide();
                    }
                  }
                },
                {
                   label: 'Yes',
                   on: {
                     click: function() {
                      modal.hide();
                      document.getElementById("<portlet:namespace/>"+identifier).submit();
                     }
                   }
                 },
                ]
       );
     }
   );
    }
</script>