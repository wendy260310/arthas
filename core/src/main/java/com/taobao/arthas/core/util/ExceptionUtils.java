package com.taobao.arthas.core.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author linlan.zcj@alibaba-inc.com
 * @date 2019/01/08
 */
public class ExceptionUtils {
    public static String getStackTrace(final Throwable throwable) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }
}
