package pl.web.dsys.core.pojos;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;

import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
class TagInfoTest {
	
	@InjectMocks
	TagInfo tagInfo;
	
	@Test
	void doTestTagInfo() {
		tagInfo = new TagInfo();
		tagInfo.setTagID("Tag Id");
		tagInfo.setTagTitle("Tag Title");
		assertEquals("Tag Id", tagInfo.getTagID());
		assertEquals("Tag Title", tagInfo.getTagTitle());
	}

}
