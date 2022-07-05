package pl.web.dsys.core.pojos;

//java util imports
import java.util.Date;
import java.util.Objects;

public class AssetlistBean {
	private String name;
	/** The heading text. */
	private String title;
	/** The description. */
	private String description;

	/** The path. */
	private String path;

	private String emailValue;

	/** The formNumber. */
	private String formNumber;

	/** The lastModified. */
	private Date lastModified;

	private Date publishedDate;
	private String icon;
	private long size;

	private String related;
	/** State App Disclosure. */
	private String formtype;
	private String disclosure;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRelated() {
		return related;
	}

	public void setRelated(String related) {
		this.related = related;
	}

	public void setEmailValue(String emailValue) {
		this.emailValue = emailValue;
	}

	public Date getPublishedDate() {
		return publishedDate;
	}

	public void setPublishedDate(Date publishedDate) {
		this.publishedDate = publishedDate;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getIcon() {
		return icon;
	}

	/**
	 * 
	 * @return
	 */
	public String getFormNumber() {
		return formNumber;
	}

	/**
	 * 
	 * @param formNumber
	 */
	public void setFormNumber(String formNumber) {
		this.formNumber = formNumber;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	/**
	 * @return the headingText
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param headingText the headingText to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return the path
	 */
	public Date getLastModified() {
		return lastModified;
	}

	/**
	 * @param path the path to set
	 */
	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	/**
	 * 
	 * @return
	 */
	public String getEmailValue() {

		return formNumber + "|" + org.apache.commons.lang.StringEscapeUtils.escapeHtml(title) + "|" + path + "|" + size;

	}

	/**
	 * @return the formtype
	 */
	public String getFormType() {
		return formtype;
	}

	/**
	 * @param formtype the formtype to set
	 */
	public void setFormType(String formtype) {
		this.formtype = formtype;
	}

	/**
	 * @return the disclosure
	 */
	public String getDisclosure() {
		return disclosure;
	}

	/**
	 * @param disclosure the disclosure to set
	 */
	public void setDisclosure(String disclosure) {
		this.disclosure = disclosure;
	}

	/**
	 * Overrides equals and hashcode method for checking duplicates objects.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof AssetlistBean))
			return false;
		AssetlistBean that = (AssetlistBean) obj;
		return Objects.equals(getPath(), that.getPath());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getPath());
	}

}
