package pl.web.dsys.core.models;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = { Resource.class,
		SlingHttpServletRequest.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class AssetListModel {

	private static final Logger log = LoggerFactory.getLogger(AssetListModel.class);

	@SlingObject
	Resource resource;

	@SlingObject
	SlingHttpServletRequest request;

	@SlingObject
	ResourceResolver resolver;

	List<ListItem> assetsList;

	@Self
	private EnhancedListModel enhancedListModel;

	@ValueMapValue
	@Default(intValues = 0)
	private int maxItems;

	@ValueMapValue
	private String autopauseDisabled;

	@ValueMapValue
	private String autoplay;

	@ValueMapValue
	private String delay;

	@ValueMapValue
	private String infiniteLoop;

	@ValueMapValue
	private String animation;

	@PostConstruct
	protected void init() {
		log.debug("Inside Post Construct of Assets List Model.. {}");
	}

	public List<ListItem> getAssetsList() {
		List<ListItem> assetList = enhancedListModel.getEnhancedListItems();
		if (null != assetList && !assetList.isEmpty()) {
			return assetList;
		}
		return new ArrayList<ListItem>();
	}

	public EnhancedListModel getEnhancedListModel() {
		return enhancedListModel;
	}

	public String getTitle() {
		return enhancedListModel.getTitle();
	}

	public String getAutopauseDisabled() {
		return autopauseDisabled;
	}

	public String getAutoplay() {
		return autoplay;
	}

	public String getDelay() {
		return delay;
	}

	public String getInfiniteLoop() {
		return infiniteLoop;
	}

	public String getAnimation() {
		return animation;
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
