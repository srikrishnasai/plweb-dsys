package pl.web.dsys.core.pojos;

/**
 * 
 * This Link Pojo is used to bind objects with link text, link url and target.
 * 
 * @author Krishna
 *
 */
public class LinkPojo {

	public String linkText;

	public String linkUrl;

	public boolean target;

	public String getLinkText() {
		return linkText;
	}

	public void setLinkText(String linkText) {
		this.linkText = linkText;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public boolean isTarget() {
		return target;
	}

	public void setTarget(boolean target) {
		this.target = target;
	}

}
