package examples.guice.servlet.osgi.internal;

import java.io.IOException;
import java.io.PrintWriter;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Singleton
public class ExampleServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8399408333501885374L;

	@Inject
	@Named("message")
	private String message;

	@Inject
	private HelloWorld helloWorld;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/html");
		PrintWriter w = resp.getWriter();
		w.write(message + " from " + req.getRequestURL());
		w.write("<br/>" + helloWorld.getHello());
		w.close();
	}
}
