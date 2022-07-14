package pl.web.dsys.core.models;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;

import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import pl.web.dsys.core.pojos.AssetlistBean;

@ExtendWith(AemContextExtension.class)
class AssetlistBeanTest {

	@InjectMocks
	AssetlistBean bean;

	@Test
	void doTestAssetlistBean() {
		bean = new AssetlistBean();
		bean.setDescription("description");
		bean.setDisclosure("disclouser");
		bean.setEmailValue("email");
		bean.setFormNumber("formnumber");
		bean.setFormType("formtype");
		bean.setIcon("icon");
		bean.setLastModified(new Date());
		bean.setName("name");
		bean.setPath("path");
		bean.setPublishedDate(new Date());
		bean.setRelated("related");
		bean.setSize(1);
		bean.setTitle("title");
		assertNotNull(bean.getDescription());
		assertNotNull(bean.getDisclosure());
		assertNotNull(bean.getEmailValue());
		assertNotNull(bean.getFormNumber());
		assertNotNull(bean.getFormType());
		assertNotNull(bean.getIcon());
		assertNotNull(bean.getLastModified());
		assertNotNull(bean.getName());
		assertNotNull(bean.getPath());
		assertNotNull(bean.getPublishedDate());
		assertNotNull(bean.getRelated());
		assertNotNull(bean.getSize());
		assertNotNull(bean.getTitle());
	}

}
