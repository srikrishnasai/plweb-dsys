package pl.web.dsys.core.pojos;

/**
 * PageInfo class is a pojo to be used in category list for nav bar component.
 */
public class PageInfo {
	private String pageTile;
	private String resourceType;
	private String path;

	public String getPageTile() {
		return pageTile;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setPageTile(String pageTile) {
		this.pageTile = pageTile;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}
}