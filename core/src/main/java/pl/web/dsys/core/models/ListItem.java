package pl.web.dsys.core.models;

import java.util.Date;

import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface ListItem {

	String getTitle();

	String getDescription();

	String getContentType();

	String getPath();

	String getUrl();

	Date getLastModified();

	Date getPublishedDate();

	String getName();

}
