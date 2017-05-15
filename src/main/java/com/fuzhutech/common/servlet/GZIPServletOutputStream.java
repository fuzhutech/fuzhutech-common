package com.fuzhutech.common.servlet;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

public class GZIPServletOutputStream extends ServletOutputStream {

    private HttpServletResponse response;  // 定义压缩输出流
    private GZIPOutputStream gzipOutputStream;
    private ByteArrayOutputStream byteArrayOutputStream;

    // 构造函数来创建压缩输出流
    public GZIPServletOutputStream(ServletOutputStream servletOutputStreams) throws IOException {
        gzipOutputStream = new GZIPOutputStream(servletOutputStreams);
    }

    public GZIPServletOutputStream(HttpServletResponse response) throws IOException {
        this.response = response;
        byteArrayOutputStream = new ByteArrayOutputStream();
        gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream);
    }

    @Override
    public boolean isReady() {
        return false;
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {

    }

    @Override
    public void write(int b) throws IOException {
        gzipOutputStream.write(b);
    }

    @Override
    public void close() throws IOException {
        gzipOutputStream.finish();
        byte[] content = byteArrayOutputStream.toByteArray();

        response.addHeader("Content-Encoding", "gzip");
        response.addHeader("Content-Length", Integer.toString(content.length));

        ServletOutputStream out = response.getOutputStream();
        out.write(content);
        out.close();
    }

    @Override
    public void flush() throws IOException {
        gzipOutputStream.flush();
    }

    public void reset(){
        byteArrayOutputStream.reset();
    }
}
