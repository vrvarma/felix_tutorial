package com.extensiblejava.hello.web;

import com.google.inject.servlet.ServletModule;

public class ExampleServletModule extends ServletModule {
	@Override
	protected void configureServlets() {
		serve("/webjsp/*").with(HelloWorldServlet.class);
		
		
	}
}
