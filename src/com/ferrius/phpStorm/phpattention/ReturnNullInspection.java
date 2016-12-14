package com.ferrius.phpStorm.phpattention;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElementVisitor;
import com.jetbrains.php.lang.inspections.PhpInspection;
import com.jetbrains.php.lang.psi.elements.FunctionReference;
import com.jetbrains.php.lang.psi.elements.MethodReference;
import com.jetbrains.php.lang.psi.resolve.types.PhpType;
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class ReturnNullInspection extends PhpInspection {


    private static final String[] EMPTY_ARRAY_FAIL = {
            "array_pop",
            "array_shift",
            "array_rand",
    };

    private static final String[] NULL_RETURN = {
            "array_reduce",
            "constant",
            "curl_multi_strerror",
            "curl_strerror",
            "error_get_last",
            "filter_input",
            "filter_input_array",
            "money_format",
    };



    @Override
    @NotNull
    public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
        return new PhpElementVisitor() {

            public void visitPhpFunctionCall(FunctionReference reference) {
                PhpType funcType = reference.resolveLocalType();


                if (StringUtils.contains(funcType.toString(), "null")) {
                    holder.registerProblem(reference, "Null returns", ProblemHighlightType.GENERIC_ERROR_OR_WARNING);
                }

                final String strFunctionName = reference.getName();
                if (StringUtil.isEmpty(strFunctionName)) {
                    return;
                }

                if (Arrays.asList(EMPTY_ARRAY_FAIL).contains(strFunctionName)) {
                    holder.registerProblem(reference, "Null if empty array input", ProblemHighlightType.GENERIC_ERROR_OR_WARNING);
                }

                if (Arrays.asList(NULL_RETURN).contains(strFunctionName)) {
                    holder.registerProblem(reference, "Null returns", ProblemHighlightType.GENERIC_ERROR_OR_WARNING);
                }

                if (strFunctionName.equals("json_decode")) {
                    holder.registerProblem(reference, "User input may return null (check is_array on json_decode return value)", ProblemHighlightType.GENERIC_ERROR_OR_WARNING);
                }
            }

            public void visitPhpMethodReference(MethodReference reference) {
                PhpType funcType = reference.resolveLocalType();

                if (StringUtils.contains(funcType.toString(), "null")) {
                    holder.registerProblem(reference, "Null returns", ProblemHighlightType.GENERIC_ERROR_OR_WARNING);
                }
            }
        };
    }
}
