<?xml version="1.0" encoding="utf-8"?>
<?altova_samplexml userstoragedetail.xml?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <!--<link rel="stylesheet" media="all"
					href="/library/skin/default/tool.css" type="text/css" />-->
    <xsl:template match="reportResult">
		<div class="reportData1">User ID: <xsl:value-of select="parameters/parameter[@name='userId']"/></div>
		<br/>
		<table width="100%" border="0" cellpadding="8" cellspacing="0" class="reportTable2">
			<tr>
				<td colspan="4" class="reportTable2Header">My Workspace</td>
			</tr>
			<tr>
				<td class="reportTable2Labels">File Type</td>
				<td class="reportTable2Labels2">Total</td>
				<td class="reportTable2Labels2">Avg. Size</td>
				<td class="reportTable2Labels2">Total Size</td>
			</tr>
			<xsl:for-each select="extraReportResult[1]/data/datarow">
				<tr>
					<td class="reportTable2Data">
						<xsl:value-of select="element[@colName='FILE_TYPE']"/>
					</td>
					<td class="reportTable2Data">
						<xsl:value-of select="element[@colName='TOTAL_FILES']"/>
					</td>
					<td class="reportTable2Data">
						<xsl:call-template name="chooseSize">
							<xsl:with-param name="num"><xsl:value-of select="element[@colName='AVG_SIZE']"/></xsl:with-param>
						</xsl:call-template>
					</td>
					<td class="reportTable2Data">
						<xsl:call-template name="chooseSize">
							<xsl:with-param name="num"><xsl:value-of select="element[@colName='TOTAL_SIZE']"/></xsl:with-param>
						</xsl:call-template>
					</td>
				</tr>
			</xsl:for-each>
			<tr>
				<td class="reportTable2Footer">Totals:</td>
				<td class="reportTable2Footer">
					<xsl:value-of select="sum(extraReportResult[1]/data/datarow/element[@colName='TOTAL_FILES'])"/>
				</td>
				<td class="reportTable2Footer">
					<xsl:call-template name="chooseSize">
						<xsl:with-param name="num"><xsl:value-of select="sum(extraReportResult[1]/data/datarow/element[@colName='AVG_SIZE']) div count(extraReportResult[1]/data/datarow)"/></xsl:with-param>
					</xsl:call-template>
				</td>
				<td class="reportTable2Footer">
					<xsl:call-template name="chooseSize">
						<xsl:with-param name="num"><xsl:value-of select="sum(extraReportResult[1]/data/datarow/element[@colName='TOTAL_SIZE'])"/></xsl:with-param>
					</xsl:call-template>
					<span class="reportInfo"> <xsl:text> - Out of </xsl:text>
						<xsl:call-template name="chooseSize">
							<xsl:with-param name="num"><xsl:value-of select="extraReportResult[@index='2']/data/datarow/element[@colName='WORKSPACETOTALALLOCATEDSPACE']"/></xsl:with-param>
						</xsl:call-template></span>
				</td>
			</tr>
		</table>
		<br/>
		<table width="100%" border="0" cellpadding="8" cellspacing="0" class="reportTable2">
			<tr>
				<td colspan="4" class="reportTable2Header">Site Folder(s)</td>
			</tr>
			<tr>
				<td class="reportTable2Labels">File Type</td>
				<td class="reportTable2Labels2">Total</td>
				<td class="reportTable2Labels2">Avg. Size</td>
				<td class="reportTable2Labels2">Total Size</td>
			</tr>
			<xsl:for-each select="extraReportResult[2]/data/datarow">
				<tr>
					<td class="reportTable2Data">
						<xsl:value-of select="element[@colName='FILE_TYPE']"/>
					</td>
					<td class="reportTable2Data">
						<xsl:value-of select="element[@colName='TOTAL_FILES']"/>
					</td>
					<td class="reportTable2Data">
						<xsl:call-template name="chooseSize">
							<xsl:with-param name="num"><xsl:value-of select="element[@colName='AVG_SIZE']"/></xsl:with-param>
						</xsl:call-template>
					</td>
					<td class="reportTable2Data">
						<xsl:call-template name="chooseSize">
							<xsl:with-param name="num"><xsl:value-of select="element[@colName='TOTAL_SIZE']"/></xsl:with-param>
						</xsl:call-template>
					</td>
				</tr>
			</xsl:for-each>
			<tr>
				<td class="reportTable2Footer">Totals:</td>
				<td class="reportTable2Footer">
					<xsl:value-of select="sum(extraReportResult[2]/data/datarow/element[@colName='TOTAL_FILES'])"/>
				</td>
				<td class="reportTable2Footer">
					<xsl:call-template name="chooseSize">
						<xsl:with-param name="num"><xsl:value-of select="sum(extraReportResult[2]/data/datarow/element[@colName='AVG_SIZE']) div count(extraReportResult[2]/data/datarow)"/></xsl:with-param>
					</xsl:call-template>
				</td>
				<td class="reportTable2Footer">
					<xsl:call-template name="chooseSize">
						<xsl:with-param name="num"><xsl:value-of select="sum(extraReportResult[2]/data/datarow/element[@colName='TOTAL_SIZE'])"/></xsl:with-param>
					</xsl:call-template>
					<span class="reportInfo"> <xsl:text> - Out of </xsl:text>
						<xsl:call-template name="chooseSize">
							<xsl:with-param name="num"><xsl:value-of select="extraReportResult[@index='3']/data/datarow/element[@colName='SITETOTALALLOCATEDSPACE']"/></xsl:with-param>
						</xsl:call-template></span>
				</td>
			</tr>
		</table>	</xsl:template>
	<xsl:template name="chooseSize">
		<xsl:param name="num"/>
		<xsl:choose>
			<xsl:when test="($num div 1048576) >= 1">
				<xsl:value-of select="format-number($num div 1048576,'###,###.0')"/> GB
			</xsl:when>
			<xsl:when test="($num div 1024) >= 1">
				<xsl:value-of select="format-number($num div 1024,'###,###.0')"/> MB
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="format-number($num,'###,###.0')"/> KB
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>
