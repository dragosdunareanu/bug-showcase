package biz.netcentric.core.servlets;

import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_EXTENSIONS;
import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_METHODS;
import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_RESOURCE_TYPES;
import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_SELECTORS;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.adobe.cq.mcm.campaign.ContentGenerator;
import com.adobe.cq.mcm.campaign.NewsletterException;
@Component(
        service = { Servlet.class },
        property = {
                SLING_SERVLET_RESOURCE_TYPES + "=bug-showcase/components/page",
                SLING_SERVLET_METHODS + "=GET",
                SLING_SERVLET_EXTENSIONS + "=html",
                SLING_SERVLET_SELECTORS + "=campaign.content",
        }
)
public class NewsletterContentServlet extends SlingAllMethodsServlet {

    @Reference
    private ContentGenerator contentGenerator;

    @Override
    protected void doGet( SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {
        String contentHTML = null;
        try {
            contentHTML = contentGenerator.createHTML(request, response);
        } catch (NewsletterException e) {
            throw new RuntimeException(e);
        }
        response.getWriter().write(contentHTML);
    }
}
