package pl.web.dsys.core.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.RequestAttribute;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.dam.api.DamConstants;
import com.day.cq.wcm.api.NameConstants;
import com.day.cq.wcm.api.Page;
import com.drew.lang.annotations.NotNull;

@Model(adaptables = { Resource.class,
		SlingHttpServletRequest.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class EnhancedListModel {

	private static final Logger log = LoggerFactory.getLogger(EnhancedListModel.class);

	public static final String LIST_ROOT_PAGE = "listRootPath";

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

	@ChildResource
	private List<Resource> listItems;

	@ValueMapValue
	private String listRootPath;

	@RequestAttribute
	private String listType;
	
	@ValueMapValue
	private String title;
	
	@ValueMapValue
	private String description;

	/**
	 * The current page.
	 */
	@ScriptVariable
	private Page currentPage;

	/**
	 * Component properties.
	 */
	@ScriptVariable
	protected ValueMap properties;

	List<Resource> resourcesList = new ArrayList<Resource>();
	List<ListItem> enhancedListItems = null;
	List<PageItem> enhancedPageItems = new ArrayList<PageItem>();
	List<AssetItem> enhancedAssetItems = new ArrayList<AssetItem>();

	@PostConstruct
	protected void init() {
		log.debug("Inside Post Construct of Enhanced List Model.. {}");

		if (StringUtils.isNotEmpty(listFrom) && StringUtils.equalsIgnoreCase(listFrom, "children")) {
			if (StringUtils.isNotEmpty(listRootPath) && StringUtils.equalsIgnoreCase(listType, "assets")) {
				Resource rootResource = resolver.getResource(listRootPath);
				Iterator<Resource> children = rootResource.listChildren();
				while (children.hasNext()) {
					resourcesList.add(children.next());
				}
			} else if (StringUtils.isNotEmpty(listRootPath) && StringUtils.equalsIgnoreCase(listType, "pages")) {
				Stream<Page> pagesList = getChildListItems();
				Iterator<Page> pageItr = pagesList.iterator();
				while (pageItr.hasNext()) {
					Page page = pageItr.next();
					resourcesList.add(page.adaptTo(Resource.class));
				}
			}
		} else if (StringUtils.isNotEmpty(listFrom) && StringUtils.equalsIgnoreCase(listFrom, "static")) {
			if (!listItems.isEmpty()) {
				for (Resource res : listItems) {
					ValueMap vm = res.getValueMap();
					String listItemPath = vm.get("listItems", String.class);
					if (StringUtils.isNotEmpty(listItemPath)) {
						Resource listItemRes = resolver.getResource(listItemPath);
						resourcesList.add(listItemRes);
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

	public List<Resource> getListItems() {
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

	private Optional<Page> getRootPage(String fieldName) {
		Optional<String> parentPath = Optional.ofNullable(this.properties.get(fieldName, String.class))
				.filter(StringUtils::isNotBlank);

		// no path is specified, use current page
		if (!parentPath.isPresent()) {
			return Optional.of(this.currentPage);
		}

		// a path is specified, get that page or return null
		return parentPath.map(resource.getResourceResolver()::getResource)
				.map(currentPage.getPageManager()::getContainingPage);
	}

	/**
	 * Get the list items if using children source.
	 *
	 * @return The child list items.
	 */
	private Stream<Page> getChildListItems() {
		return getRootPage("listRootPath").map(rootPage -> collectChildren(Integer.parseInt(childDepth), rootPage))
				.orElseGet(Stream::empty);
	}

	/**
	 * Get a stream of all children of the specified parent page not deeper than the
	 * specified max depth. This call is recursive and expects that the caller uses
	 * startLevel = parent.getDepth().
	 *
	 * @param depth  The number of levels under the root page to be included.
	 * @param parent The root page.
	 * @return Stream of all children of the specified parent that are not deeper
	 *         than the end level.
	 */
	private static Stream<Page> collectChildren(int depth, @NotNull Page parent) {
		if (depth <= 0) {
			return Stream.empty();
		}
		Iterator<Page> childIterator = parent.listChildren();
		return StreamSupport.stream(((Iterable<Page>) () -> childIterator).spliterator(), false)
				.flatMap(child -> Stream.concat(Stream.of(child), collectChildren(depth - 1, child)));
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

}
