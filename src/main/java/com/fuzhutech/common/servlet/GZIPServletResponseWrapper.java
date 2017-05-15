package com.fuzhutech.common.servlet;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.zip.GZIPOutputStream;

public class GZIPServletResponseWrapper extends HttpServletResponseWrapper {

    // 默认的 response
    private HttpServletResponse response;

    // 自定义的 outputStream, 执行close()的时候对数据压缩，并输出
    GZIPServletOutputStream gzipServletOutputStream;

    // 自定义 printWriter，将内容输出到 GZipOutputStream 中
    private PrintWriter printWriter = null;

    public GZIPServletResponseWrapper(HttpServletResponse response) {
        super(response);
        this.response = response;
    }

    //以流的方式获取输出——重写getOutputStream() //返回一般为打印流，其底层是对ServletOutputStream引用。
    @Override
    public ServletOutputStream getOutputStream() throws IOException {

        if (printWriter != null) {
            throw new IllegalStateException();
        }

        if (gzipServletOutputStream == null) {
            gzipServletOutputStream = new GZIPServletOutputStream(response);
        }

        return gzipServletOutputStream;
    }

    //以字符方式获取输出——重写getWriter() //直接对ServletOutputStream的引用
    @Override
    public PrintWriter getWriter() throws IOException {
        if (gzipServletOutputStream != null) {
            throw new IllegalStateException();
        }

        if (printWriter == null) {
            gzipServletOutputStream = new GZIPServletOutputStream(response);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(gzipServletOutputStream, getResponse().getCharacterEncoding());
            printWriter = new PrintWriter(outputStreamWriter);

            //printWriter = new PrintWriter(new OutputStreamWriter(new GZIPServletOutputStream(response), "UTF-8"));
        }

        return printWriter;
    }

    @Override
    public void setContentLength(int len) {
        //压缩后数据长度会发生变化 因此将该方法内容置空
    }

    //刷新流——重写flushBuffer()
    @Override
    public void flushBuffer() throws IOException {
        if (gzipServletOutputStream != null) {
            gzipServletOutputStream.flush();
        }
        if (printWriter != null) {
            printWriter.flush();
        }
    }

    //刷新流——重写flushBuffer()
    @Override
    public void reset() {
        if (gzipServletOutputStream != null) {
            gzipServletOutputStream.reset();
        }
    }

    public void finishResponse() throws IOException {
        if (gzipServletOutputStream != null)
            gzipServletOutputStream.close();

        if (printWriter != null)
            printWriter.close();
    }

}
