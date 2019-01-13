package com.taobao.arthas.core.command.klass100.redefine;

import com.taobao.arthas.core.shell.command.impl.AnnotatedCommandImpl;
import com.taobao.arthas.core.util.IOUtils;
import com.taobao.arthas.core.util.LogUtil;
import com.taobao.arthas.core.util.StringUtils;
import com.taobao.middleware.cli.*;
import com.taobao.middleware.cli.impl.DefaultCommandLine;
import com.taobao.middleware.logger.Logger;

import java.io.IOException;
import java.util.List;

/**
 * @author linlan.zcj@alibaba-inc.com
 * @date 2019/01/08
 */
public class RedefineAnnotatedCommandImpl extends AnnotatedCommandImpl {

    private CliWrapper cli = new CliWrapper();

    private static final Logger logger = LogUtil.getArthasLogger();

    public RedefineAnnotatedCommandImpl() {
        super(RedefineCommand.class);
        cli.setReal(super.cli());
    }

    @Override
    public CLI cli() {
        return cli;
    }

    private class CliWrapper implements CLI {

        private CLI real;

        public void setReal(CLI real) {
            this.real = real;
        }

        @Override
        public CommandLine parse(List<String> arguments) {
            return parseLinuxCommand(real.parse(arguments));
        }

        @Override
        public CommandLine parse(List<String> arguments, boolean validate) {
            return parseLinuxCommand(real.parse(arguments, validate));
        }

        private CommandLine parseLinuxCommand(CommandLine commandLine) {
            if (commandLine instanceof DefaultCommandLine) {
                DefaultCommandLine tmp = (DefaultCommandLine)commandLine;
                DefaultCommandLine ret1 = new DefaultCommandLine(cli);
                for (Option s : real.getOptions()) {
                    if (s.isMultiValued()) {
                        List<String> optionValue = tmp.getRawValuesForOption(s);
                        for (String t : optionValue) {
                            if (isLinuxCommand(t)) {
                                String[] commandRet = runLinuxCommand(t);
                                if (commandRet != null) {
                                    for (String a : commandRet) {
                                        if (!StringUtils.isBlank(a)) {
                                            ret1.addRawValue(s, a.trim());
                                        }
                                    }
                                }
                            } else {
                                ret1.addRawValue(s, t);
                            }
                        }
                    } else {
                        String optionValue = tmp.getRawValueForOption(s);
                        if (!StringUtils.isBlank(optionValue)) {
                            ret1.addRawValue(s, optionValue);
                        }
                    }
                }
                return ret1;
            }
            return commandLine;
        }

        private boolean isLinuxCommand(String tmp) {
            return !StringUtils.isBlank(tmp) && tmp.startsWith("`")
                && tmp.endsWith("`");
        }

        private String[] runLinuxCommand(String tmp) {
            String cmd = tmp.substring(1, tmp.length() - 1);
            try {
                Process process = Runtime.getRuntime().exec(new String[] {"/bin/bash", "-c", cmd});
                String ret = IOUtils.toString(process.getInputStream());
                return ret.split("[\\s+|\\r?\\n]");

            } catch (IOException e) {
                logger.warn(" error when run linux command", e);
            }
            return null;
        }

        @Override
        public String getName() {
            return real.getName();
        }

        @Override
        public CLI setName(String name) {
            return real.setName(name);
        }

        @Override
        public String getDescription() {
            return real.getDescription();
        }

        @Override
        public CLI setDescription(String desc) {
            return real.setDescription(desc);
        }

        @Override
        public String getSummary() {
            return real.getSummary();
        }

        @Override
        public CLI setSummary(String summary) {
            return real.setSummary(summary);
        }

        @Override
        public boolean isHidden() {
            return real.isHidden();
        }

        @Override
        public CLI setHidden(boolean hidden) {
            return real.setHidden(hidden);
        }

        @Override
        public List<Option> getOptions() {
            return real.getOptions();
        }

        @Override
        public CLI addOption(Option option) {
            return real.addOption(option);
        }

        @Override
        public CLI addOptions(List<Option> options) {
            return real.addOptions(options);
        }

        @Override
        public CLI setOptions(List<Option> options) {
            return real.setOptions(options);
        }

        @Override
        public List<Argument> getArguments() {
            return real.getArguments();
        }

        @Override
        public CLI addArgument(Argument arg) {
            return real.addArgument(arg);
        }

        @Override
        public CLI addArguments(List<Argument> args) {
            return real.addArguments(args);
        }

        @Override
        public CLI setArguments(List<Argument> args) {
            return real.setArguments(args);
        }

        @Override
        public Option getOption(String name) {
            return real.getOption(name);
        }

        @Override
        public Argument getArgument(String name) {
            return real.getArgument(name);
        }

        @Override
        public Argument getArgument(int index) {
            return real.getArgument(index);
        }

        @Override
        public CLI removeOption(String name) {
            return real.removeOption(name);
        }

        @Override
        public CLI removeArgument(int index) {
            return real.removeArgument(index);
        }

        @Override
        public CLI usage(StringBuilder builder) {
            return real.usage(builder);
        }

        @Override
        public CLI usage(StringBuilder builder, String prefix) {
            return real.usage(builder, prefix);
        }

        @Override
        public CLI usage(StringBuilder builder, UsageMessageFormatter formatter) {
            return real.usage(builder, formatter);
        }

        @Override
        public boolean isCaseSensitive() {
            return real.isCaseSensitive();
        }

        @Override
        public void setCaseSensitive(boolean caseSensitive) {
            real.setCaseSensitive(caseSensitive);
        }
    }
}


