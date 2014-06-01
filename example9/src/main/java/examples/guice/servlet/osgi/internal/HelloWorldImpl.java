package examples.guice.servlet.osgi.internal;

import javax.inject.Singleton;

@Singleton
public class HelloWorldImpl implements HelloWorld {

	@Override
	public String getHello() {

		System.err.println("HelloWorld!!!!");
		return "Guice Hello New one";
	}

}
