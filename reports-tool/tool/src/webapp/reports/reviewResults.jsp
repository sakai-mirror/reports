<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://sakaiproject.org/jsf/sakai" prefix="sakai" %>
<% response.setContentType("text/html; charset=UTF-8"); %>
<%
 String thisId = request.getParameter("panel");
 if (thisId == null) 
 {
 thisId = "Main" + org.sakaiproject.tool.cover.ToolManager.getCurrentPlacement().getId();
 }
 %>
 
 <script type="text/javascript">
     function resize(){
     mySetMainFrameHeightViewCell('<%= org.sakaiproject.util.Web.escapeJavascript(thisId)%>');
     }
     
     
     function mySetMainFrameHeightViewCell(id)
     {
     // run the script only if this window's name matches the id parameter
     // this tells us that the iframe in parent by the name of 'id' is the one who spawned us
     if (typeof window.name != "undefined" && id != window.name) return;
     
     var frame = parent.document.getElementById(id);
     if (frame)
     {
     
     var objToResize = (frame.style) ? frame.style : frame;
     
     // SAK-11014 revert           if ( false ) {
     
     var height; 		
     var offsetH = document.body.offsetHeight;
     var innerDocScrollH = null;
     
     if (typeof(frame.contentDocument) != 'undefined' || typeof(frame.contentWindow) != 'undefined')
     {
     // very special way to get the height from IE on Windows!
     // note that the above special way of testing for undefined variables is necessary for older browsers
     // (IE 5.5 Mac) to not choke on the undefined variables.
     var innerDoc = (frame.contentDocument) ? frame.contentDocument : frame.contentWindow.document;
     innerDocScrollH = (innerDoc != null) ? innerDoc.body.scrollHeight : null;
     }
     
     if (document.all && innerDocScrollH != null)
     {
     // IE on Windows only
     height = innerDocScrollH;
     }
     else
     {
     // every other browser!
     height = offsetH;
     }
     // SAK-11014 revert		} 
     
     // SAK-11014 revert             var height = getFrameHeight(frame);
     
     // here we fudge to get a little bigger
     var newHeight = height + 40;
     
     // but not too big!
     if (newHeight > 32760) newHeight = 32760;
     
     // capture my current scroll position
     var scroll = findScroll();
     
     // resize parent frame (this resets the scroll as well)
     objToResize.height=newHeight + "px";
     
     // reset the scroll, unless it was y=0)
     if (scroll[1] > 0)
     {
     var position = findPosition(frame);
     parent.window.scrollTo(position[0]+scroll[0], position[1]+scroll[1]);
     }
     }
     }
 </script> 
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
