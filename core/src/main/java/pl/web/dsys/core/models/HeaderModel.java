package pl.web.dsys.core.models;

import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.wcm.api.Page;

/**
 * This Sling Model returns Header Component's authored dialog values and
 * exports them as json.
 * 
 * @author Krishna
 *
 */
@Model(adaptables = { Resource.class,
		SlingHttpServletRequest.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = HeaderModel.resourceType)
@Exporter(name = "jackson", extensions = "json")
public class HeaderModel {
	
	private static final Logger log = LoggerFactory.getLogger(HeaderModel.class);
	public static final String resourceType = "plweb-dsys/components/header/v1/header";

	@SlingObject
	Resource resource;

	@SlingObject
	SlingHttpServletRequest request;
	
	@SlingObject
	ResourceResolver resolver;
	
	@ScriptVariable
	Page currentPage;
	
	@ValueMapValue
	private String fileReference;
	
	@ValueMapValue
	private String searchTargetPath;
	
	@ChildResource
	private List<SecondaryNavLinksModel> primarylinks;
	
	@PostConstruct
	protected void init() {
		log.debug("Inside Post Construct of Header Model..");
	}

	public String getFileReference() {
		return fileReference;
	}

	public String getSearchTargetPath() {
		return searchTargetPath;
	}

	public List<SecondaryNavLinksModel> getPrimarylinks() {
		return primarylinks;
	}

}
