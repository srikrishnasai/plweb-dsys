package pl.web.dsys.core.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.RequestAttribute;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.dam.api.DamConstants;
import com.day.cq.wcm.api.NameConstants;

@Model(adaptables = { Resource.class,
		SlingHttpServletRequest.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class EnhancedListModel {

	private static final Logger log = LoggerFactory.getLogger(EnhancedListModel.class);

	@SlingObject
	Resource resource;

	@SlingObject
	SlingHttpServletRequest request;

	@SlingObject
	ResourceResolver resolver;

	@ValueMapValue
	private String childDepth;

	@ValueMapValue
	private String listFrom;

	@ValueMapValue
	private String listItems;

	@ValueMapValue
	private String listRootPath;
	
	@RequestAttribute
	private String listType;

	List<Resource> resourcesList = new ArrayList<Resource>();
	List<ListItem> enhancedListItems = null;
    List<PageItem> enhancedPageItems = new ArrayList<PageItem>();
    List<AssetItem> enhancedAssetItems = new ArrayList<AssetItem>();
    
	@PostConstruct
	protected void init() {
		log.debug("Inside Post Construct of Enhanced List Model.. {}");

		if (StringUtils.isNotEmpty(listFrom) && StringUtils.equalsIgnoreCase(listFrom, "children")) {
			if (StringUtils.isNotEmpty(listRootPath)) {
				Resource rootResource = resolver.getResource(listRootPath);
				Iterator<Resource> children = rootResource.listChildren();
				while (children.hasNext()) {
					resourcesList.add(children.next());
				}
			}
		} else if (StringUtils.isNotEmpty(listFrom) && StringUtils.equalsIgnoreCase(listFrom, "static")) {
			if (StringUtils.isNotEmpty(listItems)) {
				String[] listItemsPaths = listItems.split(",");
				if (ArrayUtils.isNotEmpty(listItemsPaths)) {
					for (String path : listItemsPaths) {
						Resource pathResource = resolver.getResource(path);
						resourcesList.add(pathResource);
					}
				}
			}
		}

	}

	public List<ListItem> getEnhancedListItems() {
		if (!resourcesList.isEmpty()) {
			return getEnhancedList(resourcesList);
		}
		return enhancedListItems;
	}

	private List<ListItem> getEnhancedList(List<Resource> resourcesList) {
		List<ListItem> enhancedItems = new ArrayList<ListItem>();
		for (Resource res : resourcesList) {
			if (resolver.isResourceType(res, DamConstants.NT_DAM_ASSET)) {
				log.debug("Asset Resource ::{}", res.getPath());
				AssetItem item = res.adaptTo(AssetItem.class);
				enhancedItems.add(item);
				enhancedAssetItems.add(item);

			} else if (resolver.isResourceType(res, NameConstants.NT_PAGE)) {
				log.debug("Page Resource ::{}", res.getPath());
				PageItem item = res.adaptTo(PageItem.class);
				enhancedItems.add(item);
				enhancedPageItems.add(item);
			}
		}

		return enhancedItems;
	}

	public String getChildDepth() {
		return childDepth;
	}

	public String getListFrom() {
		return listFrom;
	}

	public String getListItems() {
		return listItems;
	}

	public String getListRootPath() {
		return listRootPath;
	}

	public String getListType() {
		return listType;
	}

	public List<Resource> getResourcesList() {
		return resourcesList;
	}

	public List<PageItem> getEnhancedPageItems() {
		return enhancedPageItems;
	}

	public List<AssetItem> getEnhancedAssetItems() {
		return enhancedAssetItems;
	}

	
}
