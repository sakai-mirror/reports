<?xml version="1.0" encoding="utf-8"?>
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
	font-weight: normal;
}
.reportTableDataEmphasis {
	font-weight: bold;
}
.reportTable1 a, .reportTable1 a:link {
	color: #6699CC;
	padding-left: 5px;
}
.reportTable2Header a, .reportTable2Header a:link {
	color: #666666;
	text-decoration: none;
}
.reportTable2Header a:hover {
	text-decoration: underline;
}

.reportTable2DataOdd {
	background-color: #F4F4F4;
}
.reportTable2DataEven {
	background-color: White;
}
</style>
		<xsl:variable name="absentees">
			<xsl:value-of select="count(extraReportResult[@index='2']/data/datarow)"/>
		</xsl:variable>
		<xsl:variable name="totalVisitors">
			<xsl:value-of select="$absentees + extraReportResult/data/datarow/element[@colName='UNIQUEVISITS']"/>
		</xsl:variable>		
		<xsl:variable name="avgsession">
			<xsl:value-of select="extraReportResult/data/datarow/element[@colName='AVGSESSIONINSECONDS']"/>
		</xsl:variable>
		<xsl:variable name="avgsessionHours">
			<xsl:value-of select="floor($avgsession div 3600)"/>
		</xsl:variable>
		<xsl:variable name="avgsessionMinutes">
			<xsl:value-of select="floor((($avgsession - $avgsessionHours*3600) div 60))"/>
		</xsl:variable>
		<xsl:variable name="avgsessionSeconds">
			<xsl:value-of select="format-number((($avgsession - $avgsessionHours*3600 - $avgsessionMinutes*60)),'###.0')"/>
		</xsl:variable>
		<script type="">
			function toggle(obj) {
				var el = document.getElementById(obj);
				if ( el.style.display != 'none' ) {
					el.style.display = 'none';
				}
				else {
					el.style.display = '';
				}
			}		
		</script>
		<div class="reportData1">Date Range: <xsl:value-of select="parameters/parameter[@name='startdate']"/> - <xsl:value-of select="parameters/parameter[@name='enddate']"/>
		</div>
		<br/>
		<table width="100%" border="0" cellpadding="8" cellspacing="0" class="reportTable1">
			<tr>
				<td class="reportTable1Label1">
					<span class="reportTableLabelEmphasis">Total Visitors:</span>
					<br/>
					<span class="reportInfo">All visitors (both unique and returning) that have logged in during the given date range</span>
				</td>
				<td class="reportTable1Data1">
					<xsl:value-of select="$totalVisitors"/>
				</td>
			</tr>
			<tr>
				<td class="reportTable1Label1">
					<span class="reportTableLabelEmphasis">Total Unique Visitors:</span>
					<br/>
					<span class="reportInfo">Only unique visitors that have logged in during the given date range</span>
				</td>
				<td class="reportTable1Data1">
					<xsl:value-of select="extraReportResult/data/datarow/element[@colName='UNIQUEVISITS']"/> (about <xsl:value-of select="format-number(extraReportResult/data/datarow/element[@colName='UNIQUEVISITS'] div $totalVisitors*100,'###.0')"/>
					<xsl:text>%</xsl:text>)<br/>
					<span class="reportInfo">Percentage is with respect to the total number of users in the system</span>
				</td>
			</tr>
			<tr>
				<td class="reportTable1Label1">
					<span class="reportTableLabelEmphasis">Average Session Length:</span>
					<br/>
					<span class="reportInfo">Average length of time each visitor was logged in for during the given date range</span>
				</td>
				<td class="reportTable1Data1">
					<xsl:value-of select="$avgsessionHours"/>h:<xsl:value-of select="$avgsessionMinutes"/>m:<xsl:value-of select="$avgsessionSeconds"/>s
				</td>
			</tr>
			<tr>
				<td class="reportTable1Label2">
					<span class="reportTableLabelEmphasis">Absentees:</span>
					<br/>
					<span class="reportInfo">Users that have not logged-in during the given date range </span>
				</td>
				<td class="reportTable1Data2">
					<span class="reportTableDataEmphasis">
						<xsl:value-of select="$absentees"/>
					</span>
					<xsl:if test="$absentees != 0">
						<a id="listLink" href="javascript:toggle('absenteeList');">view/hide list</a>
						<div id="absenteeList" style="display:none;">
						<table border="0" cellspacing="0" cellpadding="5" class="reportTable2">
							<tr>
								<td class="reportTableDataEmphasis">User Id:</td>
								<td class="reportTableDataEmphasis">First Name:</td>
								<td class="reportTableDataEmphasis">Last Name:</td>
							</tr>
							<xsl:for-each select="extraReportResult[@index='2']/data/datarow">
							<tr>
								<td><xsl:value-of select="element[@colName='USER_EID']"/></td>
								<td>
								<xsl:choose>
									<xsl:when test="element[@colName='FIRST_NAME'] = ''">
										<xsl:text>(not provided)</xsl:text>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="element[@colName='FIRST_NAME']"/>
									</xsl:otherwise>
								</xsl:choose>
								</td>
								<td>
								<xsl:choose>
									<xsl:when test="element[@colName='LAST_NAME'] = ''">
										<xsl:text>(not provided)</xsl:text>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="element[@colName='LAST_NAME']"/>
									</xsl:otherwise>
								</xsl:choose>
								</td>
							</tr>
							</xsl:for-each>
						</table>
						</div>
					</xsl:if>
				</td>
			</tr>
		</table>
	</xsl:template>
</xsl:stylesheet>
