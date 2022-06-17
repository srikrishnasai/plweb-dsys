package pl.web.dsys.core.models;

import javax.annotation.PostConstruct;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

@Model(adaptables = Resource.class, adapters = ListItem.class, resourceType = "cq:Page")
public class PageItem implements ListItem {

	@Self
	private Resource resource;

	@SlingObject
	private ResourceResolver resourceResolver;

	private Page page;
	ValueMap vm;

	@PostConstruct
	protected void initModel() {
		if (resourceResolver.adaptTo(PageManager.class) != null
				&& resourceResolver.adaptTo(PageManager.class).getContainingPage(resource) != null) {
			this.page = resourceResolver.adaptTo(PageManager.class).getContainingPage(resource);
			this.vm = page.getContentResource().getValueMap();
		}
	}

	@Override
	public String getTitle() {
		return page.getTitle() + " From PageItem Bean";
	}

}
