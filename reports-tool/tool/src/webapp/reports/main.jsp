<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://sakaiproject.org/jsf/sakai" prefix="sakai" %>
<% response.setContentType("text/html; charset=UTF-8"); %>
<f:view>
<sakai:view title="#{msgs.title_main}">
<h:form>
<sakai:tool_bar>
    <h:commandLink rendered="#{ReportsTool.maintainer}"
                   action="#{ReportsTool.processPermissions}">
        <h:outputText value="#{msgs.permissions_link}"/>
    </h:commandLink>
    <h:commandLink rendered="#{ReportsTool.maintainer}"
                   action="#{ReportsTool.processImportDefinition}">
        <h:outputText value="#{msgs.import_report_def}"/>
    </h:commandLink>
</sakai:tool_bar>

<sakai:view_title value="#{msgs.title_main}" indent="1"/>

<h:dataTable var="report" styleClass="listHier"
             value="#{ReportsTool.reports}" rendered="#{ReportsTool.userCan.create}">
    <h:column>
        <f:facet name="header">
            <h:outputText value="#{msgs.title}"/>
        </f:facet>

        <h:commandLink action="#{report.selectReportDefinition}">
            <h:outputText value="#{report.reportDefinition.title}"/>
        </h:commandLink>
    </h:column>
    <h:column>
         <f:facet name="header">
            <h:outputText value="#{msgs.description}"/>
        </f:facet>

        <h:outputText value="#{report.reportDefinition.description}"/>
    </h:column>
    <h:column  rendered="#{ReportsTool.userCan.delete}">
         <f:facet name="header">
            <h:outputText value="#{msgs.delete}"/>
        </f:facet>
        <h:commandLink action="#{report.processDelete}"
                       rendered="#{report.reportDefinition.dbLoaded}">
            <h:outputText value="#{msgs.delete_report}"/>
        </h:commandLink>
    </h:column>
</h:dataTable>

<h:outputText value="<br/><br/>#{msgs.report_results}" escape="false"/>
<h:messages  infoClass = "success"/>
<h:dataTable var="result" styleClass="listHier"
             value="#{ReportsTool.results}"
             rendered="#{ReportsTool.userCan.run ||
                         ReportsTool.userCan.view ||
                         ReportsTool.userCan.edit ||
                         ReportsTool.userCan.delete}">
    <h:column>
        <f:facet name="header">
            <h:outputText value="#{msgs.title}"/>
        </f:facet>

        <h:outputText value="#{result.title}"/>

        <h:outputText rendered="#{result.isLive}"
                      value=" (#{msgs.is_a_live_report})"/>

        <f:verbatim escape="false">
            <div class="itemAction">
        </f:verbatim>

         <h:outputText value="#{' &nbsp; | &nbsp; '}" escape="false"
                      rendered="#{!result.isLive && ReportsTool.userCan.share &&
                                               ReportsTool.userCan.view && result.isOwner}"/>
        <h:commandLink action="#{result.processSelectReportResult}"
                       rendered="#{!result.isLive && ReportsTool.userCan.view}">
            <h:outputText value="#{msgs.view_report}"/>
        </h:commandLink>

        <h:commandLink action="#{result.processSelectReportResult}"
                       rendered="#{result.isLive && ReportsTool.userCan.run}">
            <h:outputText value="#{msgs.run_report}"/>
        </h:commandLink>

        <h:outputText value="#{' &nbsp; | &nbsp; '}" escape="false"
                      rendered="#{result.isLive && ReportsTool.userCan.run &&
                                  ReportsTool.userCan.edit}"/>
        <h:commandLink action="#{result.processEditReport}"
                       rendered="#{result.isLive && ReportsTool.userCan.edit}">
            <h:outputText value="#{msgs.edit_report}"/>
        </h:commandLink>

        <h:outputText value="#{' &nbsp; | &nbsp; '}" escape="false"
                      rendered="#{ReportsTool.userCan.delete &&
                                ((!result.isLive && ReportsTool.userCan.view && result.isOwner) ||
                                 (result.isLive && ReportsTool.userCan.run) ||
                                 (result.isLive && ReportsTool.userCan.edit))}"/>
        <h:commandLink action="#{result.processDelete}"
                       rendered="#{ReportsTool.userCan.delete &&
                                ((!result.isLive && ReportsTool.userCan.view && result.isOwner) ||
                                 (result.isLive && ReportsTool.userCan.run) ||
                                 (result.isLive && ReportsTool.userCan.edit))}">
            <h:outputText value="#{msgs.delete_report}"/>
        </h:commandLink>

        <h:outputText value="#{' &nbsp; | &nbsp; '}" escape="false"
                      rendered="#{(!result.isLive && ReportsTool.userCan.edit && result.isOwner)}"/>
        <h:commandLink action="#{result.processSaveResultToResources}"
                       rendered="#{(!result.isLive && ReportsTool.userCan.edit && result.isOwner)}">
              <h:outputText value="#{msgs.save_to_resouces}"/>
        </h:commandLink>



        <h:outputText value="#{' &nbsp; | &nbsp; '}" escape="false"
                      rendered="#{result.isLive && ReportsTool.userCan.run &&
                                  ReportsTool.userCan.edit}"/>
        <h:commandLink action="#{result.processScheduleReport}"
                       rendered="#{result.isLive && ReportsTool.userCan.run}">
            <h:outputText value="#{msgs.schedule_report}"/>
        </h:commandLink>


        <f:verbatim escape="false">
            </div>
        </f:verbatim>
    </h:column>
    <h:column>
        <f:facet name="header">
            <h:outputText value="#{msgs.creation_date}"/>
        </f:facet>
        <h:outputText value="#{result.creationDate}"/>
    </h:column>
</h:dataTable>
</h:form>
</sakai:view>
</f:view>
