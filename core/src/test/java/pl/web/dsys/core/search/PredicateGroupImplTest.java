package pl.web.dsys.core.search;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;

import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import pl.web.dsys.core.search.predicates.PredicateOption;
import pl.web.dsys.core.search.predicates.impl.PredicateGroupImpl;

@ExtendWith(AemContextExtension.class)
class PredicateGroupImplTest {

	@InjectMocks
	PredicateGroupImpl predicateGroupImpl;

	@Test
	void doTest() {
		predicateGroupImpl = new PredicateGroupImpl("title", "name", new ArrayList<PredicateOption>());
		assertNotNull(predicateGroupImpl.getName());
		assertNotNull(predicateGroupImpl.getOptions());
		assertNotNull(predicateGroupImpl.getTitle());
	}

}
