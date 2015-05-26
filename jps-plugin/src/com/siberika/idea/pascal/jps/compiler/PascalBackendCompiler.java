package com.siberika.idea.pascal.jps.compiler;

import com.intellij.execution.process.ProcessAdapter;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.util.Processor;
import com.siberika.idea.pascal.jps.JpsPascalBundle;
import com.siberika.idea.pascal.jps.model.JpsPascalModuleType;
import com.siberika.idea.pascal.jps.util.ParamMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.PropertyKey;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Author: George Bakhtadze
 * Date: 15/05/2015
 */
public abstract class PascalBackendCompiler {

    protected final CompilerMessager compilerMessager;

    public PascalBackendCompiler(CompilerMessager compilerMessager) {
        this.compilerMessager = compilerMessager;
    }

    abstract protected void createStartupCommandImpl(String sdkHomePath, String moduleName, String outputDir,
                                          List<File> sdkFiles, List<File> moduleLibFiles, boolean isRebuild,
                                          @Nullable ParamMap pascalSdkData, ArrayList<String> commandLine);
    @NotNull
    public abstract String getId();

    public abstract ProcessAdapter getCompilerProcessAdapter(CompilerMessager messager);

    public abstract String getCompiledUnitExt();

    public String[] createStartupCommand(final String sdkHomePath, final String moduleName, final String outputDir,
                                         final List<File> sdkLibFiles, final List<File> moduleLibFiles,
                                         final List<File> files, @Nullable final ParamMap moduleData,
                                         final boolean isRebuild,
                                         @Nullable final ParamMap pascalSdkData) throws IOException, IllegalArgumentException {
        final ArrayList<String> commandLine = new ArrayList<String>();
        if (outputDir != null) {
            createStartupCommandImpl(sdkHomePath, moduleName, outputDir, sdkLibFiles, moduleLibFiles, isRebuild, pascalSdkData, commandLine);
            File mainFile = getMainFile(moduleData);
            if (files.size() == 1) {
                mainFile = files.get(0);
            }
            if (mainFile != null) {
                commandLine.add(mainFile.getAbsolutePath());
            } else {
                compilerMessager.error(getMessage(moduleName, "compile.noSource"), null, -1, -1);
            }

            StringBuilder sb = new StringBuilder();
            for (String cmd : commandLine) {
                sb.append(" ").append(cmd);
            }
            compilerMessager.info(getMessage(moduleName, "compile.commandLine", sb.toString()), null, -1, -1);
        } else {
            compilerMessager.error(getMessage(moduleName, "compile.noOutputDir"), null, -1, -1);
        }
        if (commandLine.size() == 0) {
            throw new IllegalArgumentException(getMessage(null, "compile.errorCall"));
        }
        return commandLine.toArray(new String[commandLine.size()]);
    }

    static File getMainFile(ParamMap moduleData) {
        String fileName = moduleData != null ? moduleData.get(JpsPascalModuleType.USERDATA_KEY_MAIN_FILE.toString()) : null;
        return fileName != null ? new File(fileName) : null;
    }

    protected static Set<File> retrievePaths(List<File> files) {
        Set<File> result = new HashSet<File>();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    result.add(file);
                } else {
                    result.add(file.getParentFile());
                }
            }
        }
        return result;
    }

    protected static void addLibPathToCmdLine(final ArrayList<String> commandLine, File sourceRoot,
                                              final String compilerSettingSrcpath, final String compilerSettingIncpath) {
        FileUtil.processFilesRecursively(sourceRoot, new Processor<File>() {
            @Override
            public boolean process(File file) {
                if (file.isDirectory()) {
                    commandLine.add(compilerSettingSrcpath + file.getAbsolutePath());
                    commandLine.add(compilerSettingIncpath + file.getAbsolutePath());
                }
                return true;
            }
        });
    }

    protected static String getMessage(String moduleName, @PropertyKey(resourceBundle = JpsPascalBundle.JPSBUNDLE)String msgId, String...args) {
        return JpsPascalBundle.message(msgId, args) + (moduleName != null ? " (" + JpsPascalBundle.message("general.module", moduleName) + ")" : "");
    }

    protected static File checkCompilerExe(String sdkHomePath, String moduleName, CompilerMessager compilerMessager, File executable) {
        if (sdkHomePath != null) {
            if (!executable.canExecute()) {
                compilerMessager.error(getMessage(moduleName, "compile.noCompiler", executable.getPath()), null, -1l, -1l);
                return null;
            }
        } else {
            compilerMessager.error(getMessage(moduleName, "compile.noSdkHomePath"), null, -1l, -1l);
            return null;
        }
        return executable;
    }

}