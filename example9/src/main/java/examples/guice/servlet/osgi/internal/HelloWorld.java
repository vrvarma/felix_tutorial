package examples.guice.servlet.osgi.internal;

import com.google.inject.ImplementedBy;

@ImplementedBy(HelloWorldImpl.class)

public interface HelloWorld {

	String getHello();

}
