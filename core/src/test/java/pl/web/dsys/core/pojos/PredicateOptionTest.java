package pl.web.dsys.core.pojos;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;

import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import pl.web.dsys.core.search.predicates.PredicateOption;

@ExtendWith(AemContextExtension.class)
class PredicateOptionTest {

	@InjectMocks
	PredicateOption option;

	@InjectMocks
	PredicateOption option1;

	@Test
	void doTestPredicateOption() {
		option = new PredicateOption("label", "value");
		option.setActive(true);
		option1 = new PredicateOption("begin", "value");
		option1.setActive(false);
		assertEquals("label", option.getLabel());
		assertEquals("value", option.getValue());
		assertEquals(true, option.isActive());
		List<PredicateOption> options = new ArrayList<PredicateOption>();
		options.add(option);
		options.add(option1);
		Collections.sort(options, new PredicateOption.AlphabeticalByLabel());
		assertEquals("begin", options.get(0).getLabel());
	}
}
