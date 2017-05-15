package com.fuzhutech.common.servlet;


import java.io.IOException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.StringUtils;
import org.springframework.web.context.support.ServletContextResource;
import org.springframework.web.servlet.resource.AbstractResourceResolver;
import org.springframework.web.servlet.resource.ResourceResolverChain;

public class PathResourceResolver extends AbstractResourceResolver {

    private static Logger logger = LoggerFactory.getLogger(PathResourceResolver.class);

    private Resource[] allowedLocations;

    public PathResourceResolver() {
        logger.info("PathResourceResolver is created");
    }

    public void setAllowedLocations(Resource... locations) {
        this.allowedLocations = locations;
    }

    public Resource[] getAllowedLocations() {
        return this.allowedLocations;
    }

    protected Resource resolveResourceInternal(HttpServletRequest request, String requestPath, List<? extends Resource> locations, ResourceResolverChain chain) {
        logger.info("PathResourceResolver resolveResourceInternal");
        return this.getResource(requestPath, locations);
    }

    protected String resolveUrlPathInternal(String resourcePath, List<? extends Resource> locations, ResourceResolverChain chain) {
        logger.info("PathResourceResolver resolveUrlPathInternal");
        return StringUtils.hasText(resourcePath) && this.getResource(resourcePath, locations) != null?resourcePath:null;
    }

    private Resource getResource(String resourcePath, List<? extends Resource> locations) {
        Iterator var3 = locations.iterator();

        while(var3.hasNext()) {
            Resource location = (Resource)var3.next();

            try {
                if(this.logger.isTraceEnabled()) {
                    this.logger.trace("Checking location: " + location);
                }

                Resource resource = this.getResource(resourcePath, location);
                if(resource != null) {
                    if(this.logger.isTraceEnabled()) {
                        this.logger.trace("Found match: " + resource);
                    }

                    return resource;
                }

                if(this.logger.isTraceEnabled()) {
                    this.logger.trace("No match for location: " + location);
                }
            } catch (IOException var6) {
                this.logger.trace("Failure checking for relative resource - trying next location", var6);
            }
        }

        return null;
    }

    protected Resource getResource(String resourcePath, Resource location) throws IOException {
        Resource resource = location.createRelative(resourcePath);
        if(resource.exists() && resource.isReadable()) {
            if(this.checkResource(resource, location)) {
                return resource;
            }

            if(this.logger.isTraceEnabled()) {
                this.logger.trace("Resource path=\"" + resourcePath + "\" was successfully resolved but resource=\"" + resource.getURL() + "\" is neither under the current location=\"" + location.getURL() + "\" nor under any of the allowed locations=" + Arrays.asList(this.getAllowedLocations()));
            }
        }

        return null;
    }

    protected boolean checkResource(Resource resource, Resource location) throws IOException {
        if(this.isResourceUnderLocation(resource, location)) {
            return true;
        } else {
            if(this.getAllowedLocations() != null) {
                Resource[] var3 = this.getAllowedLocations();
                int var4 = var3.length;

                for(int var5 = 0; var5 < var4; ++var5) {
                    Resource current = var3[var5];
                    if(this.isResourceUnderLocation(resource, current)) {
                        return true;
                    }
                }
            }

            return false;
        }
    }

    private boolean isResourceUnderLocation(Resource resource, Resource location) throws IOException {
        if(resource.getClass() != location.getClass()) {
            return false;
        } else {
            String resourcePath;
            String locationPath;
            if(resource instanceof UrlResource) {
                resourcePath = resource.getURL().toExternalForm();
                locationPath = StringUtils.cleanPath(location.getURL().toString());
            } else if(resource instanceof ClassPathResource) {
                resourcePath = ((ClassPathResource)resource).getPath();
                locationPath = StringUtils.cleanPath(((ClassPathResource)location).getPath());
            } else if(resource instanceof ServletContextResource) {
                resourcePath = ((ServletContextResource)resource).getPath();
                locationPath = StringUtils.cleanPath(((ServletContextResource)location).getPath());
            } else {
                resourcePath = resource.getURL().getPath();
                locationPath = StringUtils.cleanPath(location.getURL().getPath());
            }

            if(locationPath.equals(resourcePath)) {
                return true;
            } else {
                locationPath = !locationPath.endsWith("/") && !locationPath.isEmpty()?locationPath + "/":locationPath;
                if(!resourcePath.startsWith(locationPath)) {
                    return false;
                } else if(resourcePath.contains("%") && URLDecoder.decode(resourcePath, "UTF-8").contains("../")) {
                    if(this.logger.isTraceEnabled()) {
                        this.logger.trace("Resolved resource path contains \"../\" after decoding: " + resourcePath);
                    }

                    return false;
                } else {
                    return true;
                }
            }
        }
    }
}

