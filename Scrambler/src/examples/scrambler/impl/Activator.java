package examples.scrambler.impl;

import static com.google.inject.Guice.createInjector;
import static org.ops4j.peaberry.Peaberry.osgiModule;

import org.ops4j.peaberry.Export;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import examples.scrambler.Scramble;

public class Activator implements BundleActivator {

	@Inject
	Export<Scramble> handle;

	public void start(final BundleContext ctx) throws Exception {

		// export service and inject handle into the activator
		createInjector(osgiModule(ctx), new ExportModule()).injectMembers(this);
	}

	public void stop(final BundleContext ctx) throws Exception {
		handle.unput();
	}
}
