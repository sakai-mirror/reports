<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://sakaiproject.org/jsf/sakai" prefix="sakai" %>
<% response.setContentType("text/html; charset=UTF-8"); %>
<f:view>
    <sakai:view title="#{msgs.title_create_report}">
        <h:form>

            <sakai:view_title value="#{msgs.title_create_report}" indent="1"/>

            The report is:
            <h:outputText value="#{ReportsTool.workingReportDefinition.reportDefinition.title}"/>


            <sakai:group_box>

                <sakai:panel_edit>
                    <sakai:doc_section>
                        <h:outputText value="*" style="color: red"/>
                        <h:outputText value="#{msgs.report_title}"/>
                    </sakai:doc_section>
                    <sakai:doc_section>
                        <h:inputText value="#{ReportsTool.workingReport.report.title}" id="title"/>
                        <h:outputText value="#{msgs.empty_title_validate}" style="color: red"
                                      rendered="#{ReportsTool.workingReport.invalidTitle}"/>
                    </sakai:doc_section>

                    <sakai:doc_section>
                        <h:outputText value="#{msgs.report_live_checkbox}"/>
                    </sakai:doc_section>
                    <sakai:doc_section>
                        <h:selectBooleanCheckbox value="#{ReportsTool.workingReport.report.isLive}"/>
                    </sakai:doc_section>


                    <h:outputText value="#{msgs.report_description}"/>
                    <h:inputTextarea value="#{ReportsTool.workingReport.report.description}" rows="10"
                                          cols="70"/>

                </sakai:panel_edit>
            </sakai:group_box>


            <sakai:button_bar>
                <sakai:button_bar_item
                        action="#{ReportsTool.processReportBaseProperties}"
                        value="#{msgs.continue}"/>
                <sakai:button_bar_item
                        action="#{ReportsTool.processCancelReport}"
                        value="#{msgs.cancel}"/>
            </sakai:button_bar>

            <h:messages/>

        </h:form>
    </sakai:view>
</f:view>
