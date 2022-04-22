package pl.web.dsys.core.models;

import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This Sling Model returns Secondary Nav Component's authored dialog values and
 * exports them as json.
 * 
 * @author Krishna
 *
 */
@Model(adaptables = { Resource.class,
		SlingHttpServletRequest.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = SecondaryNavModel.resourceType)
@Exporter(name = "jackson", extensions = "json")
public class SecondaryNavModel {

	private static final Logger log = LoggerFactory.getLogger(SecondaryNavModel.class);
	public static final String resourceType = "plweb-dsys/components/secondary-nav/v1/secondary-nav";

	@SlingObject
	Resource resource;

	@SlingObject
	SlingHttpServletRequest request;

	@ChildResource
	private SecondaryNavColumnModel columnOne;
	
	@ChildResource
	private SecondaryNavColumnModel columnTwo;
	
	@ChildResource
	private SecondaryNavColumnModel columnThree;

	@PostConstruct
	protected void init() {
		log.debug("Inside Post Construct of Secondary Nav Model..");
	}

	public SecondaryNavColumnModel getColumnOne() {
		return columnOne;
	}

	public SecondaryNavColumnModel getColumnTwo() {
		return columnTwo;
	}

	public SecondaryNavColumnModel getColumnThree() {
		return columnThree;
	}
	
}
