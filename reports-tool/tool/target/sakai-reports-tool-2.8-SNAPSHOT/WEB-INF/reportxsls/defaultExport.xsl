<?xml version="1.0" ?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	
	<xsl:template match="reportResult">
	   <reportResult>
	      	<xsl:copy-of select="*"></xsl:copy-of>
	   </reportResult>
	</xsl:template>

</xsl:stylesheet>

