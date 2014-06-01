package examples.scrambler.test;

import static org.ops4j.peaberry.Peaberry.service;

import com.google.inject.AbstractModule;

import examples.scrambler.Scramble;

// here's where we bind the imported service...
public class ImportModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(Scramble.class).toProvider(service(Scramble.class).single());
	}
}
