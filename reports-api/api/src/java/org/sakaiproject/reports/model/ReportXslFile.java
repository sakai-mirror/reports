package org.sakaiproject.reports.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.content.api.ContentHostingService;
import org.sakaiproject.content.api.ContentResource;
import org.sakaiproject.exception.IdUnusedException;
import org.sakaiproject.exception.PermissionException;
import org.sakaiproject.exception.TypeException;
import org.sakaiproject.metaobj.shared.model.Id;
import org.sakaiproject.reports.model.ReportXsl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.util.Arrays;

 public class ReportXslFile 
{ 
 
      protected final transient Log logger = LogFactory.getLog(getClass()); 
    /** the link to the report definition */ 
 
    private String reportXslFileRef = null; 
    private Id reportXslFileId; 
    private byte[] xslFile; 
    private String xslFileHash; 
    /** 
     * the getter for the reportId property 
     */ 
    public ReportXslFile(){ 
         
    } 
 
    public ReportXslFile(ReportXsl reportXsl, ContentHostingService contentHosting) 
    { 
       try { String id = reportXsl.getXslLink(); 
        ContentResource resource = contentHosting.getResource(id); 
        setXslFile(readStreamToBytes(resource.streamContent())); 
        setReportXslFileRef(reportXsl.getXslLink()); 
       } 
        catch(PermissionException pe) { 
         logger.warn("Failed loading content: no permission to view file", pe); 
         throw new RuntimeException("Permission Error loading the following xsl file:" + reportXsl.getXslLink()); 
      } catch(TypeException te) { 
         logger.warn("Wrong type", te); 
           throw new RuntimeException("Error loading the following xsl file:" + reportXsl.getXslLink()); 
      } catch(IdUnusedException iue) { 
         logger.warn("UnusedId: ", iue); 
           throw new RuntimeException("Error loading the following xsl file:" + reportXsl.getXslLink()); 
      } 
        catch (Exception e) { 
            e.printStackTrace(); 
            throw new RuntimeException("Error loading the following xsl file:" + reportXsl.getXslLink()); 
        } 
    } 
 
    public String getReportXslFileRef() { 
        return reportXslFileRef; 
    } 
 
    public void setReportXslFileRef(String reportXslFileRef) { 
        this.reportXslFileRef = reportXslFileRef; 
    } 
 
    public byte[] getXslFile() { 
        return xslFile; 
    } 
 
    public void setXslFile(byte[] xslFile) { 
        this.xslFile = xslFile; 
    } 
 
     private byte[] readStreamToBytes(InputStream inStream) throws IOException { 
      ByteArrayOutputStream bytes = new ByteArrayOutputStream(); 
      byte data[] = new byte[10 * 1024]; 
 
      int count; 
      while ((count = inStream.read(data, 0, 10 * 1024)) != -1) { 
         bytes.write(data, 0, count); 
      } 
      byte[] tmp = bytes.toByteArray(); 
      bytes.close(); 
      return tmp; 
   } 
 
    public Id getReportXslFileId() { 
        return reportXslFileId; 
    } 
 
    public void setReportXslFileId(Id reportXslFileId) { 
        this.reportXslFileId = reportXslFileId; 
    } 
 
    public int hashCode() { 
        return xslFileHash.hashCode(); 
    } 
 
    public boolean equals(Object object) { 
        if (object != null && object instanceof ReportXslFile) { 
            ReportXslFile that = (ReportXslFile) object; 
            if (this.getXslFileHash() == null || that.getXslFileHash() == null) { 
                return Arrays.equals(this.getXslFile(), that.getXslFile()); 
            } 
            return this.xslFileHash.equals(that.xslFileHash); 
        } 
        return false; 
    } 
 
 
    public String getXslFileHash() { 
        return xslFileHash; 
    } 
 
    public void setXslFileHash(String xslFileHash) { 
        this.xslFileHash = xslFileHash; 
    } 
 
}  