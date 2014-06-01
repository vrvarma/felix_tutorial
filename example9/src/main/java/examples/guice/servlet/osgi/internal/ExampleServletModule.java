package examples.guice.servlet.osgi.internal;

import com.google.inject.name.Names;
import com.google.inject.servlet.ServletModule;

public class ExampleServletModule extends ServletModule {
	@Override
	protected void configureServlets() {
		serve("/example/*").with(ExampleServlet.class);

		bindConstant().annotatedWith(Names.named("message"))
				.to("Hello, World!");

		bind(HelloWorld.class).to(HelloWorldImpl.class);
	}
}
