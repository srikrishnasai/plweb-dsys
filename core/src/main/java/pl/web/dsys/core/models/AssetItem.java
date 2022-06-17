package pl.web.dsys.core.models;

import java.util.Optional;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import com.day.cq.dam.api.Asset;
import com.day.cq.dam.commons.util.DamUtil;

import pl.web.dsys.core.search.SearchResult;

@Model(adaptables = Resource.class, adapters = SearchResult.class, resourceType = "dam:Asset")
public class AssetItem implements ListItem {

	@Self
	private Resource resource;

	@SlingObject
	private ResourceResolver resourceResolver;

	private Optional<Asset> asset;

	@PostConstruct
	protected void initModel() {
		this.asset = Optional.ofNullable(DamUtil.resolveToAsset(resource));
	}

	@Override
	public String getTitle() {
		return asset.map(a -> a.getMetadataValue("dc:title") + " from AssetItem Bean").filter(StringUtils::isNotEmpty)
				.orElse(asset.map(Asset::getName).filter(StringUtils::isNotEmpty).orElse(StringUtils.EMPTY));
	}

}
