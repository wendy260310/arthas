package com.taobao.arthas.core.command.basic1000;

import com.taobao.arthas.core.shell.command.AnnotatedCommand;
import com.taobao.arthas.core.shell.command.CommandProcess;
import com.taobao.arthas.core.util.ExceptionUtils;
import com.taobao.arthas.core.util.IOUtils;
import com.taobao.middleware.cli.annotations.Name;
import com.taobao.middleware.cli.annotations.Summary;

import java.io.IOException;

/**
 * @author linlan.zcj@alibaba-inc.com
 * @date 2019/01/08
 */
@Name("ls")
@Summary("same as linux ls")
public class LsCommand extends AnnotatedCommand {

    private static final String LS = "ls";

    @Override
    public void process(CommandProcess process) {
        try {
            Process p = Runtime.getRuntime().exec(concatCmdFromArgs(process));
            process.write(IOUtils.toString(p.getInputStream()));
        } catch (IOException e) {
            process.write(ExceptionUtils.getStackTrace(e));
        } finally {
            process.end();
        }
    }

    private String concatCmdFromArgs(CommandProcess process) {
        StringBuilder sb = new StringBuilder(LS);

        for (String s : process.args()) {
            sb.append(" ").append(s);
        }
        return sb.toString();
    }

}
