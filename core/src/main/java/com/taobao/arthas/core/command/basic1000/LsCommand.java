package com.taobao.arthas.core.command.basic1000;

import com.taobao.arthas.core.shell.command.AnnotatedCommand;
import com.taobao.arthas.core.shell.command.CommandProcess;
import com.taobao.arthas.core.util.ExceptionUtils;
import com.taobao.arthas.core.util.IOUtils;
import com.taobao.middleware.cli.annotations.Name;
import com.taobao.middleware.cli.annotations.Summary;

/**
 * @author linlan.zcj@alibaba-inc.com
 * @date 2019/01/08
 */
@Name("ls")
@Summary("same as linux ls")
public class LsCommand extends AnnotatedCommand {

    @Override
    public void process(CommandProcess process) {
        try {
            Process p = Runtime.getRuntime().exec(buildCommand(process));
            p.waitFor();
            final int value = p.exitValue();
            if (value == 0) {
                process.write(IOUtils.toString(p.getInputStream()));
            } else {
                process.write(IOUtils.toString(p.getErrorStream()));
            }
        } catch (Exception e) {
            process.write(ExceptionUtils.getStackTrace(e));
        } finally {
            process.end();
        }
    }

    private String[] buildCommand(CommandProcess process) {
        StringBuilder sb = new StringBuilder("ls ");

        for (String s : process.args()) {
            sb.append(s).append(" ");
        }

        return new String[] {"/bin/bash", "-c", sb.toString()};
    }
}
