package com.ferrius.phpStorm.phpattention;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElementVisitor;
import com.jetbrains.php.lang.inspections.PhpInspection;
import com.jetbrains.php.lang.psi.elements.FunctionReference;
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class ReturnFalseInspection extends PhpInspection {

    private static final String[] IO_CONNECT_FAIL = {
            "curl_exec",
            "dir",
            "file",
            "fopen",
            "fsockopen",
            "popen",
            "opendir",
            "tmpfile",
            "socket_create",
            "socket_create_listen",
            "socket_import_stream",
            "ssh2_connect",
            "stream_socket_accept",
            "stream_socket_client",
            "stream_socket_server",
    };

    private static final String[] PERMISSIONS_FAIL = {
            "fileatime",
            "filectime",
            "filegroup",
            "fileinode",
            "filemtime",
            "fileowner",
            "filesize",
            "filetype",
            "file_get_contents",
            "readfile",
            "readgzfile",
            "readlink",
            "realpath",
            "scandir",
    };

    private static final String[] USER_INPUT_FAIL = {
            "base64_decode",
            "date",
            "date_parse",
            "ip2long",
            "parse_url",
            "mktime",
    };

    private static final String[] EMPTY_ARRAY_FAIL = {
            "current",
            "end",
            "reset",
            "max",
            "min",
    };

    private static final String[] FALSE_NORMAL_RETURN = {
            "fgetc",
            "fgetcsv",
            "fgets",
            "filter_input",
            "filter_input_array",
            "filter_var",
            "filter_var_array",
            "getenv",
            "getopt",
            "ini_get",
            "get_parent_class",
            "mb_stripos",
            "mb_stristr",
            "mb_strpos",
            "mb_strrchr",
            "mb_strrichr",
            "mb_strripos",
            "mb_strrpos",
            "mb_strstr",
            "next",
            "prev",
            "stripos",
            "stristr",
            "strpbrk",
            "strpos",
            "strptime",
            "strrchr",
            "strripos",
            "strrpos",
            "strstr",
            "array_search",
    };

    private static final String[] FALSE_RETURN = {
            "array_combine",
            "eval",
            "mt_rand"
    };

    @Override
    @NotNull
    public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {

        return new PhpElementVisitor() {
            public void visitPhpFunctionCall(FunctionReference reference) {
                final String strFunctionName = reference.getName();

                if (StringUtil.isEmpty(strFunctionName)) {
                    return;
                }

                if (Arrays.asList(IO_CONNECT_FAIL).contains(strFunctionName)) {
                    holder.registerProblem(reference, "IO error may occur (should check false return)", ProblemHighlightType.GENERIC_ERROR_OR_WARNING);
                }

                if (Arrays.asList(PERMISSIONS_FAIL).contains(strFunctionName)) {
                    holder.registerProblem(reference, "Permission error may occur (should check permissions before and file existence)", ProblemHighlightType.GENERIC_ERROR_OR_WARNING);
                }

                if (Arrays.asList(USER_INPUT_FAIL).contains(strFunctionName)) {
                    holder.registerProblem(reference, "Should check false on user input data", ProblemHighlightType.GENERIC_ERROR_OR_WARNING);
                }

                if (Arrays.asList(EMPTY_ARRAY_FAIL).contains(strFunctionName)) {
                    holder.registerProblem(reference, "On empty array false", ProblemHighlightType.GENERIC_ERROR_OR_WARNING);
                }

                if (Arrays.asList(FALSE_NORMAL_RETURN).contains(strFunctionName)) {
                    holder.registerProblem(reference, "Normal false return, don't forget check", ProblemHighlightType.GENERIC_ERROR_OR_WARNING);
                }

                if (Arrays.asList(FALSE_RETURN).contains(strFunctionName)) {
                    holder.registerProblem(reference, "Return false", ProblemHighlightType.GENERIC_ERROR_OR_WARNING);
                }

                if (strFunctionName.equals("file_put_contents")) {
                    holder.registerProblem(reference, "IO error may occur (if (false == @file_put_contents(...)){})", ProblemHighlightType.GENERIC_ERROR_OR_WARNING);
                }
            }
        };
    }
}