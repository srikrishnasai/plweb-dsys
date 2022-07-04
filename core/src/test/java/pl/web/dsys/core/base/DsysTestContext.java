package pl.web.dsys.core.base;

import org.apache.sling.models.impl.ResourceTypeBasedResourcePicker;
import org.apache.sling.models.spi.ImplementationPicker;
import org.apache.sling.testing.mock.sling.ResourceResolverType;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextBuilder;
import io.wcm.testing.mock.aem.junit5.AemContextCallback;

public final class DsysTestContext {

	public static final String TEST_CONTEXT_JSON = "/test-content.json";

	private DsysTestContext() {

	}

	public static AemContext newAemContext() {
		return new AemContextBuilder().resourceResolverType(ResourceResolverType.JCR_MOCK)
				.<AemContext>afterSetUp(SETUP_CALLBACK).afterTearDown(TEAR_DOWN_CALLBACK).build();
	}

	private static final AemContextCallback TEAR_DOWN_CALLBACK = context -> {
		context = null;
	};

	private static final AemContextCallback SETUP_CALLBACK = context -> {
		context.addModelsForClasses("pl.web.dsys.core.models");
		context.registerService(ImplementationPicker.class, new ResourceTypeBasedResourcePicker());
	};
}
