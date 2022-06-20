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

@Model(adaptables = Resource.class, adapters = ListItem.class, resourceType = "dam:Asset")
public class AssetItem implements ListItem {

	@Self
	private Resource resource;

	@SlingObject
	private ResourceResolver resourceResolver;

	private Optional<Asset> asset;

	String thumbnail = StringUtils.EMPTY;

	String assetFormat = StringUtils.EMPTY;

	@PostConstruct
	protected void initModel() {
		this.asset = Optional.ofNullable(DamUtil.resolveToAsset(resource));
		this.thumbnail = this.asset.map(a -> a.getRendition("cq5dam.thumbnail.319.319.png").getPath())
				.orElse(StringUtils.EMPTY);
		this.assetFormat = this.asset.map(a -> a.getMetadataValue("dc:format")).orElse(StringUtils.EMPTY);
	}

	@Override
	public String getTitle() {
		return asset.map(a -> a.getMetadataValue("dc:title")).filter(StringUtils::isNotEmpty)
				.orElse(asset.map(Asset::getName).filter(StringUtils::isNotEmpty).orElse(StringUtils.EMPTY));
	}

	public String getThumbnail() {
		return thumbnail;
	}

	@Override
	public String getDescription() {

		return asset.map(a -> a.getMetadataValue("dc:description")).filter(StringUtils::isNotEmpty)
				.orElse(StringUtils.EMPTY);
	}

	@Override
	public String getContentType() {
		if (StringUtils.isNotEmpty(assetFormat) && StringUtils.startsWithIgnoreCase(assetFormat, "video")) {
			return "video";
		}
		return "asset";
	}

	@Override
	public String getPath() {

		return asset.map(Asset::getPath).orElse(StringUtils.EMPTY);
	}

	@Override
	public String getUrl() {

		return getPath();
	}
}
