package pl.web.dsys.core.models;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.commons.jcr.JcrConstants;
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

	private Date lastModified;

	private Date publishedDate;

	private String name;

	private String icon;
	ValueMap vm;
	private static final Logger log = LoggerFactory.getLogger(AssetItem.class);

	@PostConstruct
	protected void initModel() {
		this.asset = Optional.ofNullable(DamUtil.resolveToAsset(resource));
		this.thumbnail = this.asset.map(a -> a.getRendition("original").getPath()).orElse(StringUtils.EMPTY);
		this.assetFormat = this.asset.map(a -> a.getMetadataValue("dc:format")).orElse(StringUtils.EMPTY);
		this.name = this.asset.map(a -> a.getName()).orElse(StringUtils.EMPTY);
		if (DamUtil.resolveToAsset(resource) != null) {
			Resource metaDataRes = resourceResolver
					.getResource(resource.getPath() + "/" + JcrConstants.JCR_CONTENT + "/metadata");
			if (metaDataRes != null) {
				this.vm = metaDataRes.getValueMap();
				GregorianCalendar cal = this.vm.get("publishedDate", GregorianCalendar.class);
				if (null == cal) {
					cal = this.vm.get("jcr:lastModified", GregorianCalendar.class);
				}
				if (cal != null)
					this.lastModified = cal.getTime();
			}

		}
		if (StringUtils.isNotEmpty(name)) {
			if (StringUtils.endsWithIgnoreCase(name, ".mp4")) {
				this.icon = "fa fa-play fa-2x";
			} else if (StringUtils.endsWithIgnoreCase(name, ".pdf")) {
				this.icon = "fa fa-file-pdf-o fa-2x";
			} else {
				this.icon = "fa fa-file-word-o fa-2x";
			}
		}
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
		return name;
	}

	public String getIcon() {
		return icon;
	}

	/**
	 * Overrides equals and hashcode method for checking duplicates objects.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof AssetItem))
			return false;
		AssetItem that = (AssetItem) obj;
		return Objects.equals(getPath(), that.getPath());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getPath());
	}
}
