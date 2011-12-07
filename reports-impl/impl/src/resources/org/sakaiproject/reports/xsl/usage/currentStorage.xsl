<?xml version="1.0" encoding="utf-8"?>
<?altova_samplexml currentStorageReport.xml?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

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
        <table width="100%" border="0" cellpadding="8" cellspacing="0" class="reportTable1">
			<tr>
				<td class="reportTable1Label1">
					<span class="reportTableDataEmphasis">Average Number of Files Stored</span>
					<br/>
					<span class="reportInfo">On average, users are storing this many files</span>
				</td>
				<td class="reportTable1Data1">
					<xsl:value-of select="data/datarow/element[@colName='AVG(FILE_COUNT)']"/>
				</td>
			</tr>
			<tr>
				<td class="reportTable1Label1">
					<span class="reportTableDataEmphasis">Average Size of Files Stored</span>
					<br/>
					<span class="reportInfo">On average, user files are this large</span>
				</td>
				<td class="reportTable1Data1">
					<xsl:call-template name="chooseSize">
						<xsl:with-param name="num"><xsl:value-of select="data/datarow/element[@colName='AVG(AVG_SIZE)']"/></xsl:with-param>
					</xsl:call-template>
				</td>
			</tr>
			<tr>
				<td class="reportTable1Label2">
					<span class="reportTableDataEmphasis">Average Disk Space Used</span>
					<br/>
					<span class="reportInfo">On average, users occupy this much disk space</span>
				</td>
				<td class="reportTable1Data2">
					<xsl:call-template name="chooseSize">
						<xsl:with-param name="num"><xsl:value-of select="data/datarow/element[@colName='AVG(AVG_SPACE)']"/></xsl:with-param>
					</xsl:call-template>
				</td>
			</tr>
		</table>
		<br/>
		<table width="100%" border="0" cellpadding="8" cellspacing="0" class="reportTable2">
			<tr>
				<td colspan="4" class="reportTable2Header">Storage Summary</td>
			</tr>
			<tr>
				<td class="reportTable2Labels">File Type</td>
				<td class="reportTable2Labels2">Total</td>
				<td class="reportTable2Labels2">Avg. Size</td>
				<td class="reportTable2Labels2">Total Size</td>
			</tr>
			<xsl:for-each select="extraReportResult/data/datarow">
				<tr>
					<td class="reportTable2Data">
						<xsl:value-of select="element[@colName='FILE_TYPE']"/>
					</td>
					<td class="reportTable2Data">
						<xsl:value-of select="element[@colName='TOTAL_FILES']"/>
					</td>
					<td class="reportTable2Data">
					<xsl:if test="element[@colName='AVG_SIZE'] and element[@colName='AVG_SIZE'] != ''">
						<xsl:call-template name="chooseSize">
							<xsl:with-param name="num"><xsl:value-of select="element[@colName='AVG_SIZE']"/></xsl:with-param>
						</xsl:call-template>
					</xsl:if>
					</td>
					<td class="reportTable2Data">
					<xsl:if test="element[@colName='TOTAL_SIZE'] and element[@colName='TOTAL_SIZE'] != ''">
						<xsl:call-template name="chooseSize">
							<xsl:with-param name="num"><xsl:value-of select="element[@colName='TOTAL_SIZE']"/></xsl:with-param>
						</xsl:call-template>
					</xsl:if>
					</td>
				</tr>
			</xsl:for-each>
			<tr>
				<td class="reportTable2Footer">Totals:</td>
				<td class="reportTable2Footer">
					<xsl:value-of select="sum(extraReportResult/data/datarow/element[@colName='TOTAL_FILES'])"/>
				</td>
				<td class="reportTable2Footer">
					<xsl:call-template name="chooseSize">
						<xsl:with-param name="num"><xsl:value-of select="sum(extraReportResult/data/datarow/element[@colName='AVG_SIZE']) div count(extraReportResult/data/datarow)"/></xsl:with-param>
					</xsl:call-template>
				</td>
				<td class="reportTable2Footer">
					<xsl:call-template name="chooseSize">
						<xsl:with-param name="num"><xsl:value-of select="sum(extraReportResult/data/datarow/element[@colName='TOTAL_SIZE'])"/></xsl:with-param>
					</xsl:call-template>
					<span class="reportInfo">
						<xsl:text> - Out of </xsl:text>
						<xsl:call-template name="chooseSize">
							<xsl:with-param name="num"><xsl:value-of select="extraReportResult[@index='1']/data/datarow/element[@colName='TOTALALOCATEDSPACE']"/></xsl:with-param>
						</xsl:call-template>
					</span>
				</td>
			</tr>
		</table>
	</xsl:template>
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
				<xsl:value-of select="format-number($num,'###,##0.0')"/> KB
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

</xsl:stylesheet>
