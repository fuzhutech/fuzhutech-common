package com.fuzhutech.common.web.servlet.gzip;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.zip.GZIPOutputStream;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.AbstractResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.resource.AbstractResourceResolver;
import org.springframework.web.servlet.resource.EncodedResource;
import org.springframework.web.servlet.resource.ResourceResolverChain;

public class GzipResourceResolver extends AbstractResourceResolver {

    private static Logger logger = LoggerFactory.getLogger(GzipResourceResolver.class);

    public GzipResourceResolver() {
        logger.info("GzipResourceResolver is created");
    }

    protected Resource resolveResourceInternal(HttpServletRequest request, String requestPath, List<? extends Resource> locations, ResourceResolverChain chain) {
        logger.info("GzipResourceResolver resolveResourceInternal requestPath{}",requestPath);
        Resource resource = chain.resolveResource(request, requestPath, locations);
        if (resource == null || request != null && !this.isGzipAccepted(request)) {
            logger.info("GzipResourceResolver resolveResourceInternal resource == null");
            return resource;
        }

        try {
            //获取对应的.gz文件

            Resource gzipped = new GzippedResource(resource);
            if (gzipped.exists()) {
                logger.info("gzipped.lastModified() :{};resource.lastModified():{}",gzipped.lastModified(),resource.lastModified() );
                if (gzipped.lastModified() > resource.lastModified())
                    return gzipped;
                else
                    gzipped.getFile().delete();
            }

            String uri = request.getRequestURI();
            String ext = FilenameUtils.getExtension(uri);
            String gzippPattern=",.js,.css,";
            //满足返回gzip文件要求
            if(true){
            //if ((resource.getFile().length() > 0) && (StringUtils.indexOf(gzippPattern, ",."+ext+",")!=-1)) {
                //不存在且满足需要创建对应的.gz文件条件
                //性能监控
                long startTime = System.currentTimeMillis();

                GzipResourceUtil.compress(resource);

                long time = System.currentTimeMillis() - startTime;
                logger.info("压缩["+resource.getFilename()+"]，共计耗时:" + time );


                //返回创建gzip文件
                Resource gzipped1 = new GzippedResource(resource);
                if (gzipped1.exists()) {
                    return gzipped1;
                }
            }
        } catch (IOException var7) {
            this.logger.trace("No gzipped resource for [" + resource.getFilename() + "]", var7);
        } catch (Exception e) {
            this.logger.trace("create gzipped resource for [" + resource.getFilename() + "] failed", e);
        }

        logger.info("GzipResourceResolver return resource");
        return resource;

    }

    private boolean isGzipAccepted(HttpServletRequest request) {
        String value = request.getHeader("Accept-Encoding");
        return value != null && value.toLowerCase().contains("gzip");
    }

    protected String resolveUrlPathInternal(String resourceUrlPath, List<? extends Resource> locations, ResourceResolverChain chain) {
        logger.info("resolveUrlPathInternal");
        return chain.resolveUrlPath(resourceUrlPath, locations);
    }

    private static final class GzippedResource extends AbstractResource implements EncodedResource {
        private final Resource original;
        private final Resource gzipped;

        public GzippedResource(Resource original) throws IOException {
            this.original = original;
            this.gzipped = original.createRelative(original.getFilename() + ".gz");
        }

        public InputStream getInputStream() throws IOException {
            return this.gzipped.getInputStream();
        }

        public boolean exists() {
            return this.gzipped.exists();
        }

        public boolean isReadable() {
            return this.gzipped.isReadable();
        }

        public boolean isOpen() {
            return this.gzipped.isOpen();
        }

        public URL getURL() throws IOException {
            return this.gzipped.getURL();
        }

        public URI getURI() throws IOException {
            return this.gzipped.getURI();
        }

        public File getFile() throws IOException {
            return this.gzipped.getFile();
        }

        public long contentLength() throws IOException {
            return this.gzipped.contentLength();
        }

        public long lastModified() throws IOException {
            return this.gzipped.lastModified();
        }

        public Resource createRelative(String relativePath) throws IOException {
            return this.gzipped.createRelative(relativePath);
        }

        public String getFilename() {
            return this.original.getFilename();
        }

        public String getDescription() {
            return this.gzipped.getDescription();
        }

        public String getContentEncoding() {
            return "gzip";
        }
    }

    private static final class GzipResourceUtil {

        static void compress(Resource original) throws Exception {
            InputStream is = original.getInputStream();
            FileOutputStream fos = new FileOutputStream(original.getURI().getRawPath() + ".gz");
            logger.info("开始压缩:" + original.getURI().getRawPath() + ".gz");

            compress(is, fos);

            is.close();
            fos.flush();
            fos.close();
        }

        static void compress(InputStream is, OutputStream os)
                throws Exception {

            GZIPOutputStream gos = new GZIPOutputStream(os);

            int count;
            byte data[] = new byte[1024];
            while ((count = is.read(data, 0, 1024)) != -1) {
                gos.write(data, 0, count);
            }

            gos.finish();

            gos.flush();
            gos.close();
        }
    }
}
