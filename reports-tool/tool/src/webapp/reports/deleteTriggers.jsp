<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://sakaiproject.org/jsf/sakai" prefix="sakai" %>

<f:view>
    <sakai:view_container title="#{msgs.title_job}">
        <h:form>
            <h:graphicImage value="/images/quartz.jpg" alt="Powered By Quartz"/>
            <sakai:view_content>
                <h:outputText value="#{msgs.select_triggers_first}" styleClass="alertMessage"
                              rendered="#{empty ReportsTool.filteredTriggersWrapperList}"/>
                <h:dataTable rendered="#{!empty ReportsTool.filteredTriggersWrapperList}"
                             value="#{ReportsTool.filteredTriggersWrapperList}" var="triggers"
                             styleClass="chefFlatListViewTable">
                    <h:column>
                        <f:facet name="header">
                            <h:outputText value="#{msgs.delete_triggers_confirm}" styleClass="alertMessage"/>
                        </f:facet>
                        <h:panelGroup>
                            <h:outputText value="#{triggers.trigger.name}    "/>
                            <h:outputText value="(#{triggers.trigger.cronExpression})"/>
                        </h:panelGroup>
                    </h:column>
                </h:dataTable>
                <sakai:button_bar>
                    <sakai:button_bar_item
                            action="#{ReportsTool.processDeleteTriggers}"
                            value="#{msgs.ok}"/>
                    <sakai:button_bar_item
                            rendered="#{!empty ReportsTool.filteredTriggersWrapperList}"
                            action="scheduleReport"
                            value="#{msgs.cancel}"/>
                </sakai:button_bar>
            </sakai:view_content>
        </h:form>
    </sakai:view_container>
</f:view>
