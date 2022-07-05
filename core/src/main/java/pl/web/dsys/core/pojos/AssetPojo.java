package pl.web.dsys.core.pojos;

import java.util.ArrayList;
//java imports
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
//sling model annotations imports
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Default;
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
//wcm api imports
import com.adobe.granite.asset.api.Asset;
import com.adobe.granite.asset.api.AssetManager;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.designer.Style;

import pl.web.dsys.core.utils.AuthUtil;
import pl.web.dsys.core.utils.Enums;
import pl.web.dsys.core.utils.JcrQueryUtils;
import pl.web.dsys.core.utils.ListSort;

@Model(adaptables = SlingHttpServletRequest.class, adapters = List.class)
public class AssetPojo {
	private static final Logger LOGGER = LoggerFactory.getLogger(AssetPojo.class);
	private static final String TAGS_MATCH_ANY_VALUE = "any";
	private static final String ASSETS = "assets";
	private static final String QUERY = "query";

	@ScriptVariable
	private ValueMap properties;

	@ScriptVariable
	private Style currentStyle;

	@ScriptVariable
	private Page currentPage;

	@ScriptVariable(name = "wcmmode")
	private SightlyWCMMode mode;

	@SlingObject
	private ResourceResolver resourceResolver;

	@SlingObject
	private Resource resource;

	@Self
	private SlingHttpServletRequest request;

	@ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
	@Default(values = StringUtils.EMPTY)
	private String query;

	@ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
	@Default(intValues = 0)
	private int maxItems;

	private Enums.SortOrder sortOrder;
	private Enums.OrderBy orderBy;

	private AssetManager assetManager;
	private java.util.List<AssetlistBean> listItems;

	@PostConstruct
	protected void init() {
		// read edit config properties
		sortOrder = Enums.SortOrder.fromString(
				properties.get(com.adobe.cq.wcm.core.components.models.List.PN_SORT_ORDER, Enums.SortOrder.ASC.value));
		orderBy = Enums.OrderBy.fromString(
				properties.get(com.adobe.cq.wcm.core.components.models.List.PN_ORDER_BY, StringUtils.EMPTY));

	}

	public Collection<AssetlistBean> getItems() {
		if (listItems == null) {
			Enums.Source listType = getListType();
			populateListItems(listType);
		}

		return listItems;
	}

	public AssetlistBean getAssetBean(Resource assetRes) {
		return (convert((Asset) assetRes.adaptTo(Asset.class)));
	}

	public boolean showAssetList() {
		getItems();
		return mode.isEdit() || listItems.size() > 0;
	}

	private Enums.Source getListType() {
		String listFromValue = properties.get(com.adobe.cq.wcm.core.components.models.List.PN_SOURCE,
				currentStyle.get(com.adobe.cq.wcm.core.components.models.List.PN_SOURCE, StringUtils.EMPTY));

		return Enums.Source.fromString(listFromValue);
	}

	private void populateListItems(Enums.Source listType) {

		switch (listType) {
		case STATIC:
			populateStaticListItems();
			break;
		case CHILDREN:
			populateChildListItems();
			break;
		case TAGS:
			populateTagListItems();
			break;
		case SEARCH:
			populateSearchListItems();
			break;

		default:
			listItems = new ArrayList<AssetlistBean>();
			break;
		}

		sortListItems();
		setMaxItems();

	}

	private void populateStaticListItems() {
		listItems = new ArrayList<AssetlistBean>();
		this.assetManager = resourceResolver.adaptTo(AssetManager.class);
		String[] assetPaths = properties.get(ASSETS, new String[0]);
		for (String path : assetPaths) {
			if (this.assetManager.assetExists(path) && AuthUtil.isResourceAuthorized(path, request, resourceResolver)) {
				listItems.add(convert(this.assetManager.getAsset(path)));
			}
		}
	}

	private void populateChildListItems() {
		listItems = new ArrayList<AssetlistBean>();
		String parent = properties.get(com.adobe.cq.wcm.core.components.models.List.PN_PARENT_PAGE, String.class);
		if (StringUtils.isNotBlank(parent)) {
			Iterator<Resource> children = resourceResolver.getResource(parent).listChildren();
			if (children != null) {
				while (children.hasNext()) {
					Resource child = children.next();
					if (ResourceUtil.isA(child, com.day.cq.dam.api.DamConstants.NT_DAM_ASSET)
							&& AuthUtil.isResourceAuthorized(child.getPath(), request, resourceResolver))
						listItems.add(convert((Asset) child.adaptTo(Asset.class)));
				}
			}
		}

	}

	private void populateTagListItems() {

		listItems = new ArrayList<AssetlistBean>();
		boolean doTagQuery = true;
		String[] contenttags = properties.get(com.adobe.cq.wcm.core.components.models.List.PN_TAGS, new String[0]);

		boolean matchAny = properties
				.get(com.adobe.cq.wcm.core.components.models.List.PN_TAGS_MATCH, TAGS_MATCH_ANY_VALUE)
				.equals(TAGS_MATCH_ANY_VALUE);

		String[] parent = properties.get(com.adobe.cq.wcm.core.components.models.List.PN_TAGS_PARENT_PAGE,
				new String[0]);

		if (doTagQuery && ArrayUtils.isNotEmpty(contenttags) && ArrayUtils.isNotEmpty(parent)) {

			String clause = JcrQueryUtils.buildSQL2QueryCriteria(contenttags, null, null, matchAny, false, false);
			String sql2query = JcrQueryUtils.getSQL2QueryString(JcrQueryUtils.QUERY_ASSETS, parent, clause, null, null);

			listItems = convertList(JcrQueryUtils.executeQuery(resourceResolver, sql2query));
		}
	}

	private void populateSearchListItems() {
		listItems = new ArrayList<AssetlistBean>();
		String parent = properties.get("searchIn", String.class);
		String query = properties.get(QUERY, String.class);

		if (!StringUtils.isBlank(parent)) {
			String sql2query = " SELECT parent.* FROM [dam:Asset] AS parent "
					+ " INNER JOIN [nt:unstructured] AS child ON ISCHILDNODE(child,parent)  "
					+ " WHERE ISDESCENDANTNODE(parent,'" + parent + "')" + StringUtils.stripToEmpty(query);

			listItems = convertList(JcrQueryUtils.executeQuery(resourceResolver, sql2query));
		}
	}

	private void sortListItems() {
		if (orderBy != null) {
			listItems.sort(new ListSort(orderBy, sortOrder));
		}
	}

	/**
	 * 
	 * @param assetList
	 * @return
	 */
	private List<AssetlistBean> convertList(List<Asset> assetList) {
		List<AssetlistBean> listAssetItems = new ArrayList<AssetlistBean>();
		for (Asset item : assetList) {
			if (AuthUtil.isResourceAuthorized(item.getPath(), request, resourceResolver))
				listAssetItems.add(convert(item));
		}
		return listAssetItems;
	}

	/**
	 * 
	 * @param asset
	 * @return
	 */
	private AssetlistBean convert(Asset asset) {
		AssetlistBean assetBean = new AssetlistBean();

		assetBean.setPath(asset.getPath());

		Resource curAsset = asset.getResourceResolver()
				.getResource(asset.getPath() + "/" + com.day.cq.commons.jcr.JcrConstants.JCR_CONTENT + "/metadata");

		ValueMap sitevm = curAsset.adaptTo(ValueMap.class);
		assetBean.setTitle(sitevm.get("dc:title", String.class));
		assetBean.setDescription(sitevm.get("dc:description", String.class));

		assetBean.setFormNumber(sitevm.get("formNumber", String.class));

		GregorianCalendar cal = sitevm.get("publishedDate", GregorianCalendar.class);
		if (cal == null)
			cal = sitevm.get("jcr:lastModified", GregorianCalendar.class);
		if (cal != null)
			assetBean.setPublishedDate(cal.getTime());

		cal = sitevm.get("jcr:lastModified", GregorianCalendar.class);
		if (cal != null)
			assetBean.setLastModified(cal.getTime());

		if (StringUtils.endsWith(asset.getName(), ".mp4")) {
			assetBean.setIcon("fa fa-play fa-2x");
		} else {
			assetBean.setIcon(StringUtils.endsWith(asset.getName(), ".pdf") ? "fa fa-file-pdf-o fa-2x"
					: "fa fa-file-word-o fa-2x");
		}
		assetBean.setName(asset.getName());
		if (asset.getRendition("original") != null)
			assetBean.setSize(asset.getRendition("original").getSize());

		try {
			Resource relatedAssetRes = resourceResolver.getResource(asset.getPath() + "/"
					+ com.day.cq.commons.jcr.JcrConstants.JCR_CONTENT + "/related/sources/sling:members");
			if (relatedAssetRes != null) {
				if (null != relatedAssetRes.getChildren()) {
					assetBean.setRelated(relatedAssetRes.getChildren().iterator().next().getValueMap()
							.get("sling:resource", String.class));
					LOGGER.debug(" related image: " + assetBean.getRelated());
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			LOGGER.debug("convert getting related image: " + ex.getMessage());
		}

		return assetBean;
	}

	private void setMaxItems() {
		if (maxItems != 0) {
			java.util.List<AssetlistBean> tmpListItems = new ArrayList<AssetlistBean>();
			for (AssetlistBean item : listItems) {
				if (tmpListItems.size() < maxItems) {
					tmpListItems.add(item);
				} else {
					break;
				}
			}
			listItems = tmpListItems;
		}
	}

}
