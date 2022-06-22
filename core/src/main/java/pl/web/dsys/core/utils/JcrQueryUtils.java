package pl.web.dsys.core.utils;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.AccessDeniedException;
import javax.jcr.ItemNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import javax.jcr.query.Row;
import javax.jcr.query.RowIterator;

import org.apache.commons.lang.StringUtils;
//import org.apache.sling.jcr.resource.JcrResourceUtil;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.asset.api.Asset;
import com.day.cq.wcm.api.Page;
/**
 * 
 * @author ksequeir
 *
 */
public class JcrQueryUtils {
	public static final Logger log = LoggerFactory.getLogger(JcrQueryUtils.class);
	public static String QUERY_PAGES = "cq:Page";
	public static String QUERY_ASSETS = "dam:AssetContent";
	
	/**
	 * 
	 * @param searchPath
	 * @param clause
	 * @return
	 */
	public static String getSQL2QueryString(String from, String[] searchPath, String clause, String orderBy, String sortOrder){
		String sql2QueryString = null;
		
		String pathIn = null;
		for(int i = 0;i<searchPath.length;i++){
			if(i == 0) {
				pathIn = "ISDESCENDANTNODE(parent, '"+searchPath[0]+"') "; 		
			} else {
				pathIn = pathIn + " OR ISDESCENDANTNODE(parent, '"+searchPath[i]+"') ";
			}
		}
		sql2QueryString = " SELECT child.* FROM ["+from+"] AS parent  "+
						  " INNER JOIN [nt:unstructured] AS child ON ISCHILDNODE(child,parent) "+
						  " WHERE ( "+pathIn+") AND "+ clause;
		if(StringUtils.isNotBlank(orderBy)){
			if(StringUtils.isBlank(sortOrder))
				sortOrder = "asc";
			sql2QueryString = sql2QueryString +" ORDER BY child.["+orderBy+"] " + sortOrder;
		}
		return sql2QueryString;
	}
	/**
	 * 
	 * @param contenttags
	 * @param sitetags
	 * @param authtag
	 * @param orcriteria
	 * @param wildcard
	 * @param auth
	 * @return
	 */
	public static String buildSQL2QueryCriteria(String[] contenttags,String[] sitetags, String authtag, boolean orcriteria, boolean wildcard, boolean auth){
	    String siteTagsSQL2 = null, contentTagSQL2 = null, sql2QueryString = null;
	    
	    if(sitetags != null){	    	
		    for(int i=0;i<sitetags.length;i++){     
		        if(i == 0)siteTagsSQL2 = "child.[cq:tags] ='" + sitetags[i] +"' ";
		        else siteTagsSQL2 = siteTagsSQL2 +" AND child.[cq:tags]='" + sitetags[i] +"' ";
		    }		    
		}
	    if(contenttags != null){
		    for(int i=0;i<contenttags.length;i++){       
		        if(i == 0) {
		        	if(wildcard)
		        		contentTagSQL2 = "  CONTAINS(child.[cq:tags],'" + contenttags[i] +"%') ";
		        	else
		        		contentTagSQL2 = "  child.[cq:tags] ='" + contenttags[i] +"' ";        	
		        } else {
		        	if(orcriteria) contentTagSQL2 = contentTagSQL2 + " OR "; 
		        	else contentTagSQL2 = contentTagSQL2 + " AND ";

		        	if(wildcard) contentTagSQL2 = contentTagSQL2 + "   CONTAINS(child.[cq:tags], '" + contenttags[i] +"') ";
		        	else contentTagSQL2 = contentTagSQL2 +"  child.[cq:tags] ='" + contenttags[i] +"' ";
		        }
		    }
		    if(auth && !StringUtils.isBlank( authtag))       
		         contentTagSQL2 = contentTagSQL2  + " and ( child.[cq:amfauth] IS NULL   or child.[cq:amfauth]='" +authtag+"'  ) ";             		              
		    
		    if(siteTagsSQL2 != null)sql2QueryString = siteTagsSQL2 +" and ( " +  contentTagSQL2 + ")";
		    else sql2QueryString = contentTagSQL2;
		}
	    return sql2QueryString;
	}
	/**
	 * 
	 * @param session
	 * @param sql2Query
	 * @return
	 */
	public static List<Asset> executeQuery(ResourceResolver resourceResolver, String sql2Query){
		
		java.util.List<Asset> listItems = new ArrayList<Asset>();
		try {
			//  JcrResourceUtil Doesn't exist in 6.4
			// QueryResult result = JcrResourceUtil.query(session, sql2Query,Query.JCR_SQL2);
			Session session = resourceResolver.adaptTo(Session.class);
			QueryManager queryManager = session.getWorkspace().getQueryManager();
			Query query = queryManager.createQuery(sql2Query,Query.JCR_SQL2);
			QueryResult result = query.execute();
			
	        RowIterator rowIterator = result.getRows();
	        
	        while(rowIterator.hasNext()) {
	           Row row = rowIterator.nextRow();                   
	           	           
	           Asset asset = resourceResolver.getResource(row.getNode("parent").getParent().getPath()).adaptTo(Asset.class);
	           listItems.add( asset);
	        }
		 } catch (ItemNotFoundException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
             log.error("executeQuery ItemNotFoundException :" , e.getMessage());
             System.out.println(e.getMessage());
         } catch (AccessDeniedException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
             log.error("executeQuery AccessDeniedException :" , e.getMessage());
             System.out.println(e.getMessage());
         } catch (RepositoryException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
             log.error("executeQuery RepositoryException :" , e.getMessage());
             System.out.println(e.getMessage());
         }
        return listItems;
	}
	/**
	 * 
	 * @param resourceResolver
	 * @param sql2Query
	 * @return
	 */
	public static List<Page> executePageQuery(ResourceResolver resourceResolver, String sql2Query){
		
		java.util.List<Page> listItems = new ArrayList<Page>();
		try {
			Session session = resourceResolver.adaptTo(Session.class);
			// QueryResult result = JcrResourceUtil.query(session, sql2Query,Query.JCR_SQL2);
			QueryManager queryManager = session.getWorkspace().getQueryManager();
			Query query = queryManager.createQuery(sql2Query,Query.JCR_SQL2);
			QueryResult result = query.execute();

	        RowIterator rowIterator = result.getRows();
	        
	        while(rowIterator.hasNext()) {
	           Row row = rowIterator.nextRow();                   
	           Page asset = resourceResolver.getResource(row.getNode("parent").getPath()).adaptTo(Page.class);
	           listItems.add( asset);
	        }
		 } catch (ItemNotFoundException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
             log.error("executePageQuery ItemNotFoundException :" , e.getMessage());
             System.out.println(e.getMessage());
         } catch (AccessDeniedException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
             log.error("executePageQuery AccessDeniedException :" , e.getMessage());
             System.out.println(e.getMessage());
         } catch (RepositoryException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
             log.error("executePageQuery RepositoryException :" , e.getMessage());
             System.out.println(e.getMessage());
         }
        return listItems;
	}
	/**
	 * 
	 * @param resourceResolver
	 * @param sql2Query
	 * @return
	 */
    public static List<Resource> executeResourceQuery(ResourceResolver resourceResolver, String sql2Query){
		
		java.util.List<Resource> listItems = new ArrayList<Resource>();
		try {
			Session session = resourceResolver.adaptTo(Session.class);
			//QueryResult result = JcrResourceUtil.query(session, sql2Query,Query.JCR_SQL2);
			QueryManager queryManager = session.getWorkspace().getQueryManager();
			Query query = queryManager.createQuery(sql2Query,Query.JCR_SQL2);
			QueryResult result = query.execute();

	        RowIterator rowIterator = result.getRows();
	        
	        while(rowIterator.hasNext()) {
	           Row row = rowIterator.nextRow();                   
	           Resource asset = resourceResolver.getResource(row.getPath());
	           listItems.add( asset);
	        }
		 } catch (ItemNotFoundException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
             log.error("executePageQuery ItemNotFoundException :" , e.getMessage());
             System.out.println(e.getMessage());
         } catch (AccessDeniedException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
             log.error("executePageQuery AccessDeniedException :" , e.getMessage());
             System.out.println(e.getMessage());
         } catch (RepositoryException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
             log.error("executePageQuery RepositoryException :" , e.getMessage());
             System.out.println(e.getMessage());
         }
        return listItems;
	}
	
	
}