package pl.web.dsys.core.helpers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.RequestAttribute;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = { Resource.class,
		SlingHttpServletRequest.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class MultifieldHelperModel {

	Logger logger = LoggerFactory.getLogger(MultifieldHelperModel.class);

	@RequestAttribute
	private String nodeName = null;

	@RequestAttribute
	private String nodePath = null;

	@SlingObject
	public Resource resource;

	@SlingObject
	public ResourceResolver resolver;

	public Resource childResource;
	int length = 0;

	@PostConstruct
	protected void init() {
		if (StringUtils.isNotBlank(nodeName)) {
			childResource = resource.getChild(nodeName);
		}

		if (StringUtils.isNotBlank(nodePath)) {
			childResource = resolver.getResource(nodePath);
		}
	}

	public Resource getChildResource() {
		return childResource;
	}

	public int getLength() {
		List<Resource> items = new ArrayList<>();
		if (null != childResource && childResource.hasChildren()) {
			Iterator<Resource> children = childResource.listChildren();
			while (children.hasNext()) {
				items.add(children.next());
			}
			logger.debug("Items ::{}", items);
			return items.size();
		}
		return length;
	}
}
