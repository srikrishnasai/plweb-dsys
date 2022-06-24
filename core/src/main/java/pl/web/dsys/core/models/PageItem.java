package pl.web.dsys.core.models;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

import pl.web.dsys.core.utils.SharedContants;

@Model(adaptables = Resource.class, adapters = ListItem.class, resourceType = "cq:Page")
public class PageItem implements ListItem {

	@Self
	private Resource resource;

	@SlingObject
	private ResourceResolver resourceResolver;

	private Page page;
	ValueMap vm;

	Date lastModified;
	
	@PostConstruct
	protected void initModel() {
		if (resourceResolver.adaptTo(PageManager.class) != null
				&& resourceResolver.adaptTo(PageManager.class).getContainingPage(resource) != null) {
			this.page = resourceResolver.adaptTo(PageManager.class).getContainingPage(resource);
			this.vm = page.getContentResource().getValueMap();
			this.lastModified = this.page.getLastModified().getTime();
		}
	}

	@Override
	public String getTitle() {
		return StringUtils.defaultIfBlank(StringUtils.defaultIfBlank(page.getPageTitle(), page.getTitle()),
				page.getName());
	}

	@Override
	public String getDescription() {

		return page.getDescription();
	}

	@Override
	public String getContentType() {

		return "page";
	}

	@Override
	public String getPath() {

		return page.getPath();
	}

	@Override
	public String getUrl() {

		return getPath() + SharedContants.PAGE_EXTENSION;
	}

	@Override
	public Date getLastModified() {
		return lastModified;
	}

	@Override
	public Date getPublishedDate() {
		return lastModified;
	}

	@Override
	public String getName() {
		return StringUtils.defaultIfBlank(StringUtils.defaultIfBlank(page.getPageTitle(), page.getTitle()),
				page.getName());
	}

}
