<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    version="1.0">
    <!-- This file is to get the internal XML out to the developer of the report -->
<xsl:template match="/">
    <div>
    <h5>The internal XML:</h5>
    
    <xsl:copy-of select="/" />
	
	</div>
	
</xsl:template>
</xsl:stylesheet>