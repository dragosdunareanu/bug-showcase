package biz.netcentric.core.servlets;


import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_PATHS;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.engine.SlingRequestProcessor;
import org.apache.sling.servlethelpers.internalrequests.InternalRequest;
import org.apache.sling.servlethelpers.internalrequests.SlingInternalRequest;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.adobe.cq.mcm.campaign.ContentGenerator;
import com.adobe.cq.mcm.campaign.NewsletterException;
import com.day.cq.contentsync.handler.util.RequestResponseFactory;

@Component(
        service = { Servlet.class },
        property = {
                SLING_SERVLET_PATHS + "=/bin/private/generate-html"
        }
)
public class ContentGeneratorServlet extends SlingSafeMethodsServlet {

    @Reference
    private RequestResponseFactory requestResponseFactory;

    @Reference
    private ContentGenerator contentGenerator;

    @Reference
    private SlingRequestProcessor slingRequestProcessor;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {

        String page1Path = request.getParameter("path1");
        String page2Path = request.getParameter("path2");

        String page1 = new SlingInternalRequest(request.getResourceResolver(), slingRequestProcessor, page1Path)
                .withRequestMethod("GET")
                .withSelectors("campaign.content")
                .withExtension("html")
                .execute()
                .checkStatus(200)
                .getResponseAsString();

        String page2 = new SlingInternalRequest(request.getResourceResolver(), slingRequestProcessor, page2Path)
                .withRequestMethod("GET")
                .withSelectors("campaign.content")
                .withExtension("html")
                .execute()
                .checkStatus(200)
                .getResponseAsString();


        response.getWriter().write("Page 1:");
        response.getWriter().write("<pre>" + page1 + "</pre>");
        response.getWriter().write("Page 2:");
        response.getWriter().write("<pre>" + page2 + "</pre>");
    }
}
