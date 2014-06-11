package com.extensiblejava.hello.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HelloWorldServlet extends HttpServlet {

        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                resp.setContentType("text/html");
                resp.getWriter().write("<html><body><p>Hello OSGI World -- Welcome to Jetty embedded in Felix.</p><p>Say <a href=\"./html/hello.html\"/>hello.</a></p><p>Say <a href=\"./html/goodbye.html\"/>goodbye.</a></p><p>Serve me <a href=\"./jsp/hello.jsp\"/>a JSP please.</a></p></body></html>"); //$NON-NLS-1$
        }

}