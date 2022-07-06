package pl.web.dsys.core.helpers;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.RequestAttribute;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Model(adaptables = {Resource.class, SlingHttpServletRequest.class},
defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ChildNodeImageFileReference {

    private static final Logger log = LoggerFactory.getLogger(ChildNodeImageFileReference.class);

    
    @RequestAttribute
    String nodeName;

    @Inject
    Resource resource;

    @Inject
    ResourceResolver resourceResolver;

    private String imageFileReference;
    
    @PostConstruct 
    public void activate() throws Exception {   
        
    	if(null != nodeName) {
    		String imageNodePath = resource.getPath() + "/" + nodeName;
    		Resource imageResource = resourceResolver.getResource(imageNodePath);
    		if(null != imageResource) {
    			this.imageFileReference = imageResource.getValueMap().get("fileReference", StringUtils.EMPTY);
    		}
    	}
    }

    public String getImageFileReference() {
        return imageFileReference;
    }

    public void setImageFileReference(String imageFileReference) {
        this.imageFileReference = imageFileReference;
    }

}
