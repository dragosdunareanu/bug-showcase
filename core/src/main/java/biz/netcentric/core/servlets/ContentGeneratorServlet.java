package biz.netcentric.core.servlets;


import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_PATHS;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.apache.sling.engine.SlingRequestProcessor;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.day.cq.contentsync.handler.util.RequestResponseFactory;

@Component(
        service = { Servlet.class },
        property = {
                SLING_SERVLET_PATHS + "=/bin/private/generate-html"
        }
)
public class ContentGeneratorServlet extends SlingSafeMethodsServlet {

    @Reference
    private SlingRequestProcessor slingRequestProcessor;
    @Reference
    private RequestResponseFactory requestResponseFactory;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {

        ResourceResolver resourceResolver = request.getResourceResolver();

        String page1Path = request.getParameter("path1");
        String page2Path = request.getParameter("path2");

        HttpServletRequest req1 = new HttpServletRequestWrapper(requestResponseFactory.createRequest(HttpConstants.METHOD_GET, page1Path));
        HttpServletRequest req2 = new HttpServletRequestWrapper(requestResponseFactory.createRequest(HttpConstants.METHOD_GET, page2Path));

        ByteArrayOutputStream byteStreamRes1 = new ByteArrayOutputStream();
        ByteArrayOutputStream byteStreamRes2 = new ByteArrayOutputStream();

        HttpServletResponse res1 = requestResponseFactory.createResponse(byteStreamRes1);
        HttpServletResponse res2 = requestResponseFactory.createResponse(byteStreamRes2);

        slingRequestProcessor.processRequest(req1, res1, resourceResolver);
        res1.getWriter().flush();
        slingRequestProcessor.processRequest(req2, res2, resourceResolver);
        res2.getWriter().flush();


        response.getWriter().write("Page 1:");
        response.getWriter().write("<pre>" + byteStreamRes1.toString("UTF-8") + "</pre>");
        response.getWriter().write("Page 2:");
        response.getWriter().write("<pre>" + byteStreamRes2.toString("UTF-8")  + "</pre>");
    }
}
