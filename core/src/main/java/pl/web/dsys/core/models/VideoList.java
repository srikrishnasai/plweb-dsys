package pl.web.dsys.core.models;

//java util imports
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//javax imports
import javax.annotation.PostConstruct;
import javax.jcr.Session;

import org.apache.commons.lang3.StringUtils;
//sling model apache imports
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
//logger imports
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.sightly.SightlyWCMMode;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
//search api imports
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;

//sling model for video list component
@Model(adaptables = { Resource.class,
		SlingHttpServletRequest.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = "jackson", extensions = "json")
public class VideoList {
	private static final Logger LOGGER = LoggerFactory.getLogger(VideoList.class);

	@ScriptVariable(name = "wcmmode")
	private SightlyWCMMode mode;

	@SlingObject
	private ResourceResolver resourceResolver;

	@SlingObject
	private Resource resource;

	@ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
	@Default(intValues = 10)
	private int limit;

	@ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
	@Default(values = StringUtils.EMPTY)
	private String videoClass;

	@ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
	@Default(values = StringUtils.EMPTY)
	private String controls;

	@ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
	@Default(values = "item.html")
	private String view;

	@ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
	@Default(values = StringUtils.EMPTY)
	private String campaignParamName;

	@ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
	@Default(values = StringUtils.EMPTY)
	private String autoplay;

	@ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
	@Default(values = StringUtils.EMPTY)
	private String loop;

	@ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
	@Default(values = StringUtils.EMPTY)
	private String preload;

	@ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
	@Default(values = StringUtils.EMPTY)
	private String download;

	@ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
	@Default(values = StringUtils.EMPTY)
	private String featuredVideo;

	@ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
	@Default(values = StringUtils.EMPTY)
	private String video;

	@ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
	@Default(values = StringUtils.EMPTY)
	private String id;

	@SlingObject
	private SlingHttpServletRequest request;

	@Self
	private EnhancedListModel enhancedListModel;

	private String mediaName;
	private String resourceType;
	private ArrayList<AssetItem> featuredVideoList = new ArrayList<>();
	AssetItem featuredAssetlistBean;
	private String requestParam = "";
	private String multifiedItemNodeName = "/campaignParamValues";

	@PostConstruct
	protected void init() {

		if (!StringUtils.isEmpty(autoplay) && autoplay.equals("true")) {
			autoplay = "autoplay";
		}
		if (!StringUtils.isEmpty(loop) && autoplay.equals("true")) {
			loop = "loop";
		}
		if (StringUtils.isEmpty(download) || StringUtils.equals(download, "false")) {
			download = "nodownload";
		} else {
			download = null;
		}

		// Gets campaign parameter values and add campaing video as 0th position if it
		// is available.
		if (null != campaignParamName && !campaignParamName.isEmpty()
				&& null != request.getParameter(campaignParamName)) {
			String queryParamValue = request.getParameter(campaignParamName).trim();
			Resource multifieldNode = resourceResolver.getResource(resource.getPath() + multifiedItemNodeName);

			if (null != multifieldNode) {
				Iterator<Resource> children = multifieldNode.listChildren();
				while (children.hasNext()) {
					Resource item = children.next();
					if (null != item) {
						String paramValue = item.getValueMap().get("paramValue", String.class);

						if (null != paramValue && !StringUtils.isEmpty(paramValue)
								&& paramValue.trim().equalsIgnoreCase(queryParamValue)) {

							String featuredVideoCampaign = item.getValueMap().get("featuredVideoCampaign",
									String.class);

							if (null != featuredVideoCampaign && !StringUtils.isEmpty(featuredVideoCampaign)) {
								Resource assetRes = resourceResolver.getResource(featuredVideoCampaign);

								if (assetRes != null) {
									AssetItem campaignAssetlistBean = getAssetBean(assetRes);
									featuredVideoList.add(campaignAssetlistBean);

								}
							}
							break;

						}

					}
				}
			}

		}

		// Get tagged video if it is available and add it at 0th position in list
		if (null != request.getParameter("tags") && !StringUtils.isEmpty(this.request.getParameter("tags"))) {
			requestParam = this.request.getParameter("tags");
			ArrayList<String> tagsVideoList = setupQueryBuilder();
			if (null != tagsVideoList && tagsVideoList.size() > 0) {
				for (int i = 0; i < tagsVideoList.size(); i++) {
					Resource assetRes = resourceResolver.getResource(tagsVideoList.get(i));
					if (assetRes != null) {
						AssetItem tagAssetlistBean = getAssetBean(assetRes);
						LOGGER.info("TagAssetlistBean::" + tagAssetlistBean);
						featuredVideoList.add(tagAssetlistBean);

					}

				}

			}
		}

		// getAssetBean object for featuredVideoPath

		if (null != featuredVideo && !StringUtils.isEmpty(featuredVideo)) {
			Resource assetRes = resourceResolver.getResource(featuredVideo);
			if (assetRes != null) {
				featuredAssetlistBean = getAssetBean(assetRes);

			}
		}
		// append featuredVideo list after tagged video if it is available
		Collection<AssetItem> listVideoItems = getAssetsList();
		LOGGER.info("enhancedList::" + listVideoItems);

		if (listVideoItems != null && listVideoItems.size() > 0) {
			ArrayList<AssetItem> list = new ArrayList(listVideoItems);
			Boolean isItemMatches = false;
			if (featuredAssetlistBean != null) {
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getPath().equals(featuredAssetlistBean.getPath())) {
						list.remove(i);
						list.add(0, featuredAssetlistBean);
						isItemMatches = true;
						break;
					}
				}
				if (!isItemMatches) {
					list.add(0, featuredAssetlistBean);

				}

			}
			// add all list items into featuredVideoList
			featuredVideoList.addAll(list);

		} else {
			if (featuredAssetlistBean != null) {
				featuredVideoList.add(featuredAssetlistBean);
			}
		}

		new LinkedHashSet<>(featuredVideoList);
		List<AssetItem> list = featuredVideoList.stream().distinct().collect(Collectors.toList());
		featuredVideoList = new ArrayList<>(list);

		// prepare data for analytics based on selected view for a list
		if (null != view && !StringUtils.isEmpty(view)) {
			ListItem assetlistBean = null;
			if (!view.equals("sidebar") && !view.equals("carousel")) {
				if (listVideoItems != null && listVideoItems.size() > 0) {
					assetlistBean = listVideoItems.iterator().next();
				}
			} else {
				if (featuredVideoList != null && featuredVideoList.size() > 0) {
					assetlistBean = featuredVideoList.iterator().next();

				}
			}
			if (null != assetlistBean) {
				video = assetlistBean.getPath();
				id = assetlistBean.getName().replaceAll("[^\\w\\s]", "").replaceAll(" ", "_").toLowerCase();
				if (StringUtils.isNotEmpty(video)) {
					com.day.cq.dam.api.Asset asset = null;
					Resource assetRes = resourceResolver.getResource(video);
					if (assetRes != null) {
						asset = assetRes.adaptTo(com.day.cq.dam.api.Asset.class);
						mediaName = StringUtils.isNotBlank(asset.getMetadataValue("dc:title"))
								? asset.getMetadataValue("dc:title")
								: asset.getName();
						resourceType = assetRes.getResourceType();
					}
				}
			}
			LOGGER.info("listSize::" + featuredVideoList.size());

		}

	}

	private ArrayList<String> setupQueryBuilder() {
		Map<String, String> predicateMap = new HashMap<>();
		predicateMap.put("path", "/content/dam");
		predicateMap.put("type", "dam:Asset");
		predicateMap.put("property", "jcr:content/metadata/cq:tags");
		predicateMap.put("property.value", requestParam);
		Session session = resourceResolver.adaptTo(Session.class);
		QueryBuilder queryBuilder = resourceResolver.adaptTo(QueryBuilder.class);
		ArrayList<String> pathList = new ArrayList<String>();

		try {
			Query query = queryBuilder.createQuery(PredicateGroup.create(predicateMap), session);
			SearchResult result = query.getResult();
			Iterator<Resource> resources = result.getResources();
			/* Video paths with given tag */
			while (resources.hasNext()) {
				Resource resource = resources.next();
				pathList.add(resource.getPath());

			}

		} catch (Exception e) {
		}
		return pathList;

	}
    /**
     * return Asset list
     * @return List of AssetItem
     */
	public List<AssetItem> getAssetsList() {
        enhancedListModel.getEnhancedListItems();
		List<AssetItem> assetList = enhancedListModel.getEnhancedAssetItems();
		if (null != assetList && !assetList.isEmpty()) {
			return assetList;
		}
		return new ArrayList<AssetItem>();
	}

	public AssetItem getAssetBean(Resource assetRes) {
		return assetRes.adaptTo(AssetItem.class);
	}

	public String getAutoplay() {
		return autoplay;
	}

	public String getVideoPath() {
		return video;
	}

	public String getId() {
		return id;
	}

	public boolean showVideo() {
    if (mode.isEdit())
			return true;
		return StringUtils.isNotEmpty(video);

	}

	public String getDownload() {
		return download;
	}

	public String getVideoClass() {
		return videoClass;
	}

	public String getControls() {
		return controls;
	}

	public String getLoop() {
		return loop;
	}

	public String getPreload() {
		return preload;
	}

	public String getMediaName() {
		return mediaName;
	}

	public String getResourceType() {
		return resourceType;
	}

	public ArrayList<AssetItem> getFeaturedVideoList() {
		return featuredVideoList;
	}

	public String getView() {
		return view;
	}

}