<?xml version="1.0" ?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

   <xsl:output method="xml" version="1.0" encoding="UTF-8" omit-xml-declaration="no"
               doctype-public="-//SPRING//DTD BEAN//EN"
               doctype-system="http://www.springframework.org/dtd/spring-beans.dtd"
               indent="yes" />

   <xsl:template match="bean[@class='org.theospi.portfolio.reports.model.ReportDefinition']">
      <xsl:copy>
         <xsl:apply-templates select="@*" >
         </xsl:apply-templates>
         <xsl:attribute name="class">org.sakaiproject.reports.model.ReportDefinition</xsl:attribute>
         <xsl:apply-templates select="node()" >
         </xsl:apply-templates>
      </xsl:copy>      
   </xsl:template>

   <xsl:template match="bean[@class='org.theospi.portfolio.reports.model.ReportDefinitionParam']">
      <xsl:copy>
         <xsl:apply-templates select="@*" >
         </xsl:apply-templates>
         <xsl:attribute name="class">org.sakaiproject.reports.model.ReportDefinitionParam</xsl:attribute>
         <xsl:apply-templates select="node()" >
         </xsl:apply-templates>
      </xsl:copy>      
   </xsl:template>

   <xsl:template match="bean[@class='org.theospi.portfolio.reports.model.ReportXsl']">
      <xsl:copy>
         <xsl:apply-templates select="@*" >
         </xsl:apply-templates>
         <xsl:attribute name="class">org.sakaiproject.reports.model.ReportXsl</xsl:attribute>
         <xsl:apply-templates select="node()" >
         </xsl:apply-templates>
      </xsl:copy>      
   </xsl:template>

   <xsl:template match="ref[@bean='org.theospi.portfolio.reports.model.ResultsPostProcessor.csv']">
      <xsl:copy>
         <xsl:apply-templates select="@*" >
         </xsl:apply-templates>
         <xsl:attribute name="bean">org.sakaiproject.reports.service.ResultsPostProcessor.csv</xsl:attribute>
         <xsl:apply-templates select="node()" >
         </xsl:apply-templates>
      </xsl:copy>      
   </xsl:template>

   <!-- Identity transformation -->
   <xsl:template match="@*|*">
      <xsl:copy>
         <xsl:apply-templates select="@*|node()" >
         </xsl:apply-templates>
      </xsl:copy>
   </xsl:template>

</xsl:stylesheet>