package pl.web.dsys.core.models;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = { Resource.class,
		SlingHttpServletRequest.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PageListModel {

	private static final Logger log = LoggerFactory.getLogger(PageListModel.class);

	@SlingObject
	Resource resource;

	@SlingObject
	SlingHttpServletRequest request;

	@SlingObject
	ResourceResolver resolver;

	List<ListItem> pagesList;

	@ValueMapValue
	private String listColumn;

	@ValueMapValue
	private String removeBullets;

	@ValueMapValue
	private String removePadding;

	@Self
	private EnhancedListModel enhancedListModel;

	@PostConstruct
	protected void init() {
		log.debug("Inside Post Construct of Page List Model.. {}");
	}

	public List<ListItem> getPagesList() {
		List<ListItem> pageList = enhancedListModel.getEnhancedListItems();
		if (null != pageList && !pageList.isEmpty()) {
			return pageList;
		}
		return new ArrayList<ListItem>();
	}

	public String getListColumn() {
		return listColumn;
	}

	public String getRemoveBullets() {
		return removeBullets;
	}

	public String getRemovePadding() {
		return removePadding;
	}

	public EnhancedListModel getEnhancedListModel() {
		return enhancedListModel;
	}

	public String getTitle() {
		return enhancedListModel.getTitle();
	}

	public String getDescription() {
		return enhancedListModel.getDescription();
	}

	public String getLength() {
		if (enhancedListModel.getLimit() != 0) {
			return String.valueOf(enhancedListModel.getLimit());
		}
		return enhancedListModel.getLength();
	}

}
