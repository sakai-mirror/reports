<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://sakaiproject.org/jsf/sakai" prefix="sakai" %>
<% response.setContentType("text/html; charset=UTF-8"); %>
<f:view>
    <sakai:view title="#{msgs.title_save_report_results}">
            <h:form>
                <sakai:view_title value="#{msgs.title_save_report_results}" indent="1" />
                
				<sakai:instruction_message value="#{msgs.save_report_results_instructions}" />
   
                <sakai:button_bar>
                    <sakai:button_bar_item
                        action="#{ReportsTool.processSaveResultsToDB}"
                        value="#{msgs.btn_save_results}" />
                    <sakai:button_bar_item
                        action="#{ReportsTool.processCancelSave}"
                        value="#{msgs.btn_cancel_save_results}" />
                </sakai:button_bar>
                
            </h:form>
    </sakai:view>
</f:view>
