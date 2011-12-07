<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://sakaiproject.org/jsf/sakai" prefix="sakai" %>
<% response.setContentType("text/html; charset=UTF-8"); %>
<f:view>
    <sakai:view title="#{msgs.title_import_report_def}">
        <h:form>

            <sakai:view_title value="#{msgs.title_import_report_def}" indent="1"/>


            <sakai:messages />
            <h:outputText value="#{ReportsTool.invalidImportMessage}" style="color: red"
                                      rendered="#{ReportsTool.invalidImport}"/>
   <sakai:panel_edit>
      <h:outputLabel for="files" id="filesLabel" value="#{msgs.import_xml_file}" />


      <h:panelGroup>
         <h:inputText id="files" value="#{ReportsTool.importFilesString}" disabled="true" />
         <h:commandLink action="#{ReportsTool.processPickImportFiles}">
            <h:outputText value="#{msgs.import_select_files}"/>
         </h:commandLink>
         <h:message for="files" styleClass="validationEmbedded" />
      </h:panelGroup>
   </sakai:panel_edit>

   <sakai:button_bar>
       <sakai:button_bar_item id="import" value="#{msgs.import_continue}"
          action="#{ReportsTool.processImportReports}" />
       <sakai:button_bar_item id="cancel" value="#{msgs.btn_cancel_save_results}"
          action="#{ReportsTool.processActionCancel}" />
   </sakai:button_bar>
</h:form>
    </sakai:view>
</f:view>
