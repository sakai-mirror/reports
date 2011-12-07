<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
 <!--   <link rel="stylesheet" media="all"
					href="/library/skin/default/tool.css" type="text/css" />-->
    <xsl:template match="reportResult">
           <style>
            .reportTable1 {
                font-family: Arial, Helvetica, sans-serif;
                font-size: 11px;
                font-style: normal;
                font-weight: normal;
                font-variant: normal;
                border: 1px solid #CCCCCC;
                color: #666666;
            }
            .reportTable1Label1 {
                background-color: #F4F4F4;
                width: 40%;
                border-right: 1px solid #CCCCCC;
                border-bottom: 1px solid #CCCCCC;
            }
            .reportTable2Header {
                background-color: #E5E5E5;
                border-bottom: 1px solid White;
                font-weight: bold;
                padding-top: 12px;
                padding-right: 12px;
                padding-bottom: 12px;
                padding-left: 8px;
            }
            .reportTable2 {
                font-family: Arial, Helvetica, sans-serif;
                font-size: 11px;
                font-style: normal;
                font-weight: normal;
                font-variant: normal;
                color: #666666;
            }
            .reportTable2Labels {
                font-weight: bold;
                background-color: #F4F4F4;
                width: 20%;
            }
            .reportTable2Data {
                border-bottom-width: 1px;
                border-bottom-style: dashed;
                border-bottom-color: #E5E5E5;
            }
            .reportTable2Footer {
                font-weight: bold;
                border-bottom-width: medium;
                border-bottom-style: solid;
                border-bottom-color: #F4F4F4;
            }
            .reportTable1Data1 {
                padding-left: 20px;
                vertical-align: top;
                border-bottom: 1px solid #CCCCCC;
                line-height: 18px;
            }
            .reportTable2Labels2 {

                font-weight: bold;
                background-color: #F4F4F4;
            }
            .reportTable1Label2 {
                background-color: #F4F4F4;
                width: 40%;
                border-right: 1px solid #CCCCCC;
                vertical-align: top;
            }
            .reportTable1Data2 {
                padding-left: 20px;
                vertical-align: top;
                line-height: 18px;
            }
            .reportTableLabelEmphasis {
                font-weight: bold;
            }
            .reportData1 {
                font-family: Arial, Helvetica, sans-serif;
                font-size: 14px;
                color: #666666;
            }
            .reportInfo {
                color: #999999;
            }
            .reportTableDataEmphasis {

                font-weight: bold;
            }
            .reportTable1 a, .reportTable1 a:link {
                color: #6699CC;
                padding-left: 5px;
            }

        </style>
        <table width="100%" border="0" cellpadding="8" cellspacing="0" class="reportTable2">
			<tr>
				<td class="reportTable2Header">ID</td>
				<td class="reportTable2Header">Last Name</td>
				<td class="reportTable2Header">First Name</td>
				<td class="reportTable2Header">Disk Space Used in Workspace</td>
				<td class="reportTable2Header">Disk Space Used in Site Folder(s)</td>
				<td class="reportTable2Header">Total Disk Space Used</td>
			</tr>
			<xsl:for-each select="data/datarow">
				<xsl:sort select="element[@colName='LAST_NAME']" />
				<xsl:variable name="setClass">
				<xsl:choose>
					<xsl:when test="@index mod 2 = 0"><xsl:text>reportTable2DataOdd</xsl:text></xsl:when>
					<xsl:otherwise><xsl:text>reportTable2DataEven</xsl:text></xsl:otherwise>
				</xsl:choose>
				</xsl:variable>
				<tr>
					<td>
						<xsl:attribute name="class"><xsl:value-of select="$setClass"/></xsl:attribute>
						<xsl:value-of select="element[@colName='USER_EID']"/>
					</td>
					<td>
						<xsl:attribute name="class"><xsl:value-of select="$setClass"/></xsl:attribute>
						<xsl:value-of select="element[@colName='LAST_NAME']"/>
					</td>
					<td>
						<xsl:attribute name="class"><xsl:value-of select="$setClass"/></xsl:attribute>
						<xsl:value-of select="element[@colName='FIRST_NAME']"/>
					</td>
					<td>
						<xsl:attribute name="class"><xsl:value-of select="$setClass"/></xsl:attribute>
						<xsl:call-template name="chooseSize">
							<xsl:with-param name="num">
								<xsl:value-of select="element[@colName='WORK_SPACE_USED']"/>
							</xsl:with-param>
						</xsl:call-template>
					</td>
					<td>
						<xsl:attribute name="class"><xsl:value-of select="$setClass"/></xsl:attribute>
						<xsl:call-template name="chooseSize">
							<xsl:with-param name="num">
								<xsl:value-of select="element[@colName='SITE_SPACE_USED']"/>
							</xsl:with-param>
						</xsl:call-template>
					</td>
					<td>
						<xsl:attribute name="class"><xsl:value-of select="$setClass"/></xsl:attribute>
						<xsl:call-template name="chooseSize">
							<xsl:with-param name="num">
								<xsl:value-of select="element[@colName='(WORK_SPACE_USED +  SITE_SPACE_USED)']"/>
							</xsl:with-param>
						</xsl:call-template>
					</td>
				</tr>
			</xsl:for-each>
		</table>
	</xsl:template>
	<xsl:template name="chooseSize">
		<xsl:param name="num"/>
		<xsl:choose>
			<xsl:when test="($num div 1048576) >= 1">
				<xsl:value-of select="format-number($num div 1048576,'###,###.#')"/> GB
			</xsl:when>
			<xsl:when test="($num div 1024) >= 1">
				<xsl:value-of select="format-number($num div 1024,'###,###.#')"/> MB
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="format-number($num,'###,##0.0')"/> KB
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>
