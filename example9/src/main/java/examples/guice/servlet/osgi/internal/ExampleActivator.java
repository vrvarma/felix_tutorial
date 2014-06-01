package examples.guice.servlet.osgi.internal;

import org.apache.felix.http.api.ExtHttpService;
import org.ops4j.peaberry.Peaberry;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

import com.google.inject.Guice;
import com.google.inject.servlet.GuiceFilter;

/*
 * See http://svn.apache.org/repos/asf/felix/trunk/http/samples/filter/src/main/java/org/apache/felix/http/samples/filter/Activator.java
 */
public class ExampleActivator implements BundleActivator {
	private ServiceTracker tracker;

	private GuiceFilter guiceFilter;

	public void start(BundleContext context) throws Exception {
		
		guiceFilter = Guice.createInjector( Peaberry.osgiModule( context ), new ExampleServletModule() ).getInstance( GuiceFilter.class );

		this.tracker = new ServiceTracker(context,
				ExtHttpService.class.getName(), null) {
			
			
			@Override
			public Object addingService(ServiceReference ref) {
				Object service = super.addingService(ref);
				try {
					((ExtHttpService) service).registerFilter(guiceFilter,
							".*", null, 0, null);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return service;
			}

			@Override
			public void removedService(ServiceReference ref, Object service) {
				((ExtHttpService) service).unregisterFilter(guiceFilter);
				super.removedService(ref, service);
			}
		};
		

		this.tracker.open();
	}

	public void stop(BundleContext context) throws Exception {
		this.tracker.close();
	}

	void serviceRemoved(ExtHttpService service) {
		service.unregisterFilter(guiceFilter);
	}
}
