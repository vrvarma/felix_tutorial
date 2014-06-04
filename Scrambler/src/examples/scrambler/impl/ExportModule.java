package examples.scrambler.impl;

import static org.ops4j.peaberry.Peaberry.service;
import static org.ops4j.peaberry.util.TypeLiterals.export;

import com.google.inject.AbstractModule;

import examples.scrambler.Scramble;

// here's where we bind the exported service...
public class ExportModule extends AbstractModule {

	@Override
	protected void configure() {

		// note: the service is exported to the registry at injection time
		bind(export(Scramble.class)).toProvider(service(ScrambleImpl.class).export());

	}
}
