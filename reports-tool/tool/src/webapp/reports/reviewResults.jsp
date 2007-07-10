<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://sakaiproject.org/jsf/sakai" prefix="sakai" %>
<% response.setContentType("text/html; charset=UTF-8"); %>
<f:view>
    <fmt:setLocale value="${locale}" />
    <sakai:view title="#{msgs.title_report_results}">
            <h:form>
                <sakai:tool_bar>
                    <sakai:tool_bar_item
                        action="#{ReportsTool.processSaveResults}"
                        value="#{msgs.saveResults}" 
                        rendered="#{!ReportsTool.workingResult.isSaved && !ReportsTool.workingResult.isLive}"/>
                    <sakai:tool_bar_item
                        action="#{ReportsTool.processSaveResults}"
                        value="#{msgs.saveSnapshot}" 
                        rendered="#{!ReportsTool.workingResult.isSaved && ReportsTool.workingResult.isLive}"/>
                    <sakai:tool_bar_item
                        action="#{ReportsTool.processSaveReport}"
                        value="#{msgs.saveLiveReport}" 
                        rendered="#{!ReportsTool.workingReport.isSaved && ReportsTool.workingResult.isLive}"/>
				  <f:subview id="spacer_" 
				  	rendered="#{ReportsTool.workingResult.exportable}">
                     <sakai:tool_bar_spacer />
                     </f:subview>
                     <h:selectOneMenu value="#{ReportsTool.workingResult.currentExportXsl}"
                        valueChangeListener="#{ReportsTool.workingResult.changeExportXsl}"
                        onchange="this.form.submit();"
                        rendered="#{ReportsTool.workingResult.exportable}">
                        <f:selectItems value="#{ReportsTool.workingResult.exportXslSeletionList}" />
                     </h:selectOneMenu>
                    <h:outputLink value="#{ReportsTool.workingResult.currentExportLink}"
                        rendered="#{ReportsTool.workingResult.exportable}"
                        target="#{ReportsTool.workingResult.exportXsl.target}">
                        <h:outputText value="#{msgs.exportResults}" />
                     </h:outputLink>
                </sakai:tool_bar>
                
                <sakai:view_title value="#{ReportsTool.workingResult.title}" indent="1" />
                
                            <h:outputText value="#{msgs.live_report_saved} <br /><br />" style="color: #009900" 
                                rendered="#{ReportsTool.savedLiveReport}" escape="false" />
                <h:outputText value="#{msgs.select_view}" />
                
                <h:selectOneMenu value="#{ReportsTool.workingResult.currentViewXsl}">
                    <f:selectItems value="#{ReportsTool.workingResult.viewXslSeletionList}" />
                </h:selectOneMenu>
                <sakai:button_bar>
                    <sakai:button_bar_item
                        action="#{ReportsTool.processChangeViewXsl}"
                        value="#{msgs.btn_change_xsl}" />
                </sakai:button_bar>
                
                <h:outputText value="<br /><br />" escape="false" />
                <h:outputText value="#{ReportsTool.workingResult.currentViewResults}" escape="false" />
                
            </h:form>
    </sakai:view>
</f:view>
