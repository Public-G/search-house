package com.github.common.utils;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 利用HttpServletResponseWrapper封装HttpServletResponse
 * 使HttpServletResponse采用自定义的打印流
 *
 * @author ZEALER
 * @date 2018-11-6
 */
public class CustomResponseWrapper extends HttpServletResponseWrapper {

    private CustomPrintWriter     printWriter;
    private ByteArrayOutputStream bos;

    public CustomResponseWrapper(HttpServletResponse response) {
        super(response);
        this.bos = new ByteArrayOutputStream();
        this.printWriter = new CustomPrintWriter(bos);
    }

    /**
     * 获取数据的输出流转成String
     *
     * @return
     * @throws IOException
     */
    public String getContent() throws IOException {
        try {
            // flush 到ByteArrayOutputStream
            this.printWriter.flush();
            return new String(this.printWriter.getByteArrayOutputStream().toByteArray(), "utf-8");
        } finally {
            if (this.bos != null) {
                this.bos.close();
            }
            if (this.printWriter != null) {
                this.printWriter.close();
            }
        }
    }

    /**
     * 使用自定义的PrintWriter
     *
     * @return
     * @throws IOException
     */
    @Override
    public PrintWriter getWriter() throws IOException {
        return this.printWriter;
    }
}
