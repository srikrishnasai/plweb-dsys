package pl.web.dsys.core.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.adobe.aemds.guide.utils.JcrResourceConstants;
import com.day.cq.wcm.api.NameConstants;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.web.dsys.core.pojos.PageInfo;

/**
 * The NavigationBarModel class is a sling model for returning back a list of child pages for NavigationBar component.
 */
@Model(adaptables = {Resource.class, SlingHttpServletRequest.class},
defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class NavigationBarModel {

	Logger logger = LoggerFactory.getLogger(NavigationBarModel.class);
		
    @Inject
	public Resource resource;
    
    @ValueMapValue
    public String linkTarget;

    @Inject
    private ResourceResolver resourceResolver;
	
	public Resource childResource;
	public List<PageInfo> pageInfoList;
    
	@PostConstruct
	protected void init() {
        if(linkTarget!=null){
        Resource requestResource = resourceResolver.getResource(linkTarget);
        if(requestResource != null){
        Iterator<Resource> resourceIterator = requestResource.listChildren();

		    //get child pages of current page path
			 pageInfoList = new ArrayList<>();
             resourceIterator.forEachRemaining(resource -> {
             PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
            if (pageManager != null) {
             Page page = pageManager.getContainingPage(resource);
            if (page != null) {
                
                //get current page properties
                PageInfo pageInfo = new PageInfo();
                final Resource contentResource = page.getContentResource();
                final ValueMap pageValueMap = contentResource.getValueMap();
                final String pageTitle = pageValueMap.get(NameConstants.PN_PAGE_TITLE) != null ? pageValueMap.get(NameConstants.PN_PAGE_TITLE).toString() : pageValueMap.get(NameConstants.PN_TITLE).toString();
                final String resourceType = pageValueMap.get(JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY) != null ? pageValueMap.get(JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY).toString() : StringUtils.EMPTY;
                final String isHideInNavigation = pageValueMap.get(NameConstants.PN_HIDE_IN_NAV)!= null? pageValueMap.get(NameConstants.PN_HIDE_IN_NAV).toString() : StringUtils.EMPTY ;
                pageInfo.setPageTile(pageTitle);
                pageInfo.setResourceType(resourceType);
                pageInfo.setPath(page.getPath());

                //add page into list only if its hide in navigation property is not set to true
                if(isHideInNavigation.isEmpty() || !isHideInNavigation.equalsIgnoreCase("true")){
                    pageInfoList.add(pageInfo);
                }

            }
        }
    });
}
}

	}
}
