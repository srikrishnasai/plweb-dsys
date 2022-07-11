package pl.web.dsys.core.pojos;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;

import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
class LinkPojoTest {

	@InjectMocks
	LinkPojo linkPojo;

	@Test
	void doTestLinkPojo() {
		linkPojo = new LinkPojo();
		linkPojo.setLinkText("Link Text");
		linkPojo.setLinkUrl("/content/home");
		linkPojo.setTarget(false);
		assertEquals("Link Text", linkPojo.getLinkText());
		assertEquals("/content/home", linkPojo.getLinkUrl());
		assertEquals(false, linkPojo.isTarget());
	}

}
