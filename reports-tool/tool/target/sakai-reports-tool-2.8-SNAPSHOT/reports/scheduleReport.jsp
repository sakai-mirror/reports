<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://sakaiproject.org/jsf/sakai" prefix="sakai" %>


<f:view>
    <fmt:setLocale value="${locale}" />
    <sakai:view_container title="#{msgs.title_trigger}">
	  <h:form>
  	  <sakai:tool_bar_message value="#{msgs.edit_trigger_for_job} #{ReportsTool.jobDetail.name}"/>
  	  <sakai:tool_bar>
		   <sakai:tool_bar_item
		     action="createTrigger"
			   value="#{msgs.bar_create_trigger}" />
		   <sakai:tool_bar_item
  		   rendered="#{!empty ReportsTool.jobDetailWrapper.triggerWrapperList}"
		     action="#{ReportsTool.processRefreshFilteredTriggers}"
			   value="#{msgs.bar_delete_triggers}" />
		 			 <sakai:tool_bar_item
		     action="#{ReportsTool.processActionCancel}"
			   value="#{msgs.return_main}" />
   	  </sakai:tool_bar>
   	  <sakai:view_content>
  	    <h:dataTable rendered="#{!empty ReportsTool.jobDetailWrapper.triggerWrapperList}" value="#{ReportsTool.jobDetailWrapper.triggerWrapperList}" var="wrapper" styleClass="chefFlatListViewTable" >
  	      <h:column>
    	      <f:facet name="header">
    	        <h:commandButton alt="SelectAll" image="/sakai-scheduler-tool/images/checkbox.gif" action="#{ReportsTool.processSelectAllTriggers}"/>
    	      </f:facet>
    	      <h:selectBooleanCheckbox value="#{wrapper.isSelected}"/>
    	    </h:column>
  	      <h:column>
    	      <f:facet name="header">
    	        <h:outputText value="#{msgs.trigger_name}"/>
    	      </f:facet>
   	        <h:outputText value="#{wrapper.trigger.name}"/>
    	    </h:column>
    	    <h:column>
    	      <f:facet name="header">
    	        <h:outputText value="#{msgs.trigger_expression}"/>
    	      </f:facet>
   	        <h:outputText value="#{wrapper.trigger.cronExpression}"/>
    	    </h:column>
        </h:dataTable>
		  </sakai:view_content>
  	</h:form>
	</sakai:view_container>
</f:view>