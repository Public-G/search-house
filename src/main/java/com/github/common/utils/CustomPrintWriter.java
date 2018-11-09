package com.github.common.utils;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

/**
 * 自定义打印流
 *
 * @author ZEALER
 * @date 2018-11-6
 */
public class CustomPrintWriter extends PrintWriter {

    /**
     * 保存数据返回的结果
     */
    private ByteArrayOutputStream byteArrayOutputStream;

    public CustomPrintWriter(ByteArrayOutputStream out) {
        super(out);
        this.byteArrayOutputStream = out;
    }

    public ByteArrayOutputStream getByteArrayOutputStream() {
        return byteArrayOutputStream;
    }
}
