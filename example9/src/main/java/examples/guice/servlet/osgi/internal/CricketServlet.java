package examples.guice.servlet.osgi.internal;

import java.io.IOException;
import java.io.PrintWriter;

import javax.inject.Singleton;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Singleton
public class CricketServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8399408333501885374L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();
		out.println("<html>\n" + "<head><title>Welcome to Cricket World</title></head>\n"
				+ "<body>\n" + "	<h1>Interesting titbits...</h1>\n"
				+ "	<br />\n" + "	<table border=\"1\">\n" + "		<thead>\n"
				+ "			<tr>\n" + "				<th>Cricket 1</th>\n"
				+ "				<th>Cricket 2</th>\n" + "				<th>Cricket 3</th>\n"
				+ "				<th>Cricket 4</th>\n" + "				<th>Cricket 5</th>\n"
				+ "			</tr>\n" + "		</thead>\n" + "		<tbody>\n" + "			<tr>\n"
				+ "				<td>1</td>\n" + "				<td>1</td>\n" + "				<td>1</td>\n"
				+ "				<td>1</td>\n" + "				<td>1</td>\n" + "			</tr>\n"
				+ "			<tr>\n" + "				<td>2</td>\n" + "				<td>2</td>\n"
				+ "				<td>2</td>\n" + "				<td>2</td>\n" + "				<td>2</td>\n"
				+ "			</tr>\n" + "			<tr>\n" + "				<td>3</td>\n"
				+ "				<td>3</td>\n" + "				<td>3</td>\n" + "				<td>3</td>\n"
				+ "				<td>3</td>\n" + "			</tr>\n" + "			<tr>\n"
				+ "				<td>4</td>\n" + "				<td>4</td>\n" + "				<td>4</td>\n"
				+ "				<td>4</td>\n" + "				<td>4</td>\n" + "			</tr>\n"
				+ "		</tbody>\n" + "	</table>\n" + "</body>\n" + "</html>");
		out.close();
	}
}
