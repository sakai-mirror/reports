<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    version="1.0">
    
<xsl:template match="/">
    <div>
    <h5>Parameters</h5>
    <table>
        <tr>
            <th>  Parameter Title  </th>
            <th>  Name  </th>
            <th>  Type  </th>
            <th>  Value  </th>
        </tr>
        <xsl:for-each select="/reportResult/parameters/parameter">
            <tr>
                <td><xsl:value-of select="@title"/></td>
                <td><xsl:value-of select="@name"/></td>
                <td><xsl:value-of select="@type"/></td>
                <td><xsl:value-of select="@value"/></td>
            </tr>
        </xsl:for-each>
    </table>
    <BR/>
    
    <h5>Results: <xsl:value-of select="/reportResult/extraReportResult[@index='0']/data/datarow/element" /></h5>
    
    <table>
    <tr>
    <xsl:for-each select="/reportResult/columns/column">
        <th>  
            <xsl:value-of select="@title"/>
              
        </th>
    </xsl:for-each>
    </tr>
    
    <xsl:for-each select="/reportResult/data/datarow">
        <tr>
            <xsl:for-each select="element">
                <td>
                    <xsl:value-of select="."/>
                </td>
            </xsl:for-each>
        </tr>
    </xsl:for-each>
    </table>
    </div>
</xsl:template>
</xsl:stylesheet>

