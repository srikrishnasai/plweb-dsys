package pl.web.dsys.core.pojos;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;

import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
class TabListTest {

	@InjectMocks
	TabList tabList;

	@Test
	void doTestTabList() {
		tabList = new TabList();
		tabList.setTagList(new ArrayList<TagInfo>());
		assertNotNull(tabList.getTagList());
	}

}
