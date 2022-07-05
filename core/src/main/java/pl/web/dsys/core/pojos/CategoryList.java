package pl.web.dsys.core.pojos;

import java.util.List;

/**
 * PageInfo class is a pojo for getting pageInfo to be used in navigation bar
 * component.
 */
public class CategoryList {
	private String category;
	private String path;
	private List<PageInfo> pageInfolist;

	public List<PageInfo> getPageInfolist() {
		return pageInfolist;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setPageInfolist(List<PageInfo> pageInfolist) {
		this.pageInfolist = pageInfolist;
	}

}