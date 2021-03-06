/*
 ***************************************************************************************
 *  Copyright (C) 2006 EsperTech, Inc. All rights reserved.                            *
 *  http://www.espertech.com/esper                                                     *
 *  http://www.espertech.com                                                           *
 *  ---------------------------------------------------------------------------------- *
 *  The software in this package is published under the terms of the GPL license       *
 *  a copy of which has been included with this distribution in the license.txt file.  *
 ***************************************************************************************
 */
package com.espertech.esper.common.internal.epl.expression.funcs;

import com.espertech.esper.common.client.type.EPTypeClass;
import com.espertech.esper.common.internal.bytecodemodel.base.CodegenClassScope;
import com.espertech.esper.common.internal.bytecodemodel.base.CodegenMethodScope;
import com.espertech.esper.common.internal.bytecodemodel.model.expression.CodegenExpression;
import com.espertech.esper.common.internal.collection.UniformPair;
import com.espertech.esper.common.internal.epl.expression.codegen.ExprForgeCodegenSymbol;
import com.espertech.esper.common.internal.epl.expression.core.*;
import com.espertech.esper.common.internal.metrics.instrumentation.InstrumentationBuilderExpr;
import com.espertech.esper.common.internal.util.SimpleNumberCoercer;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static com.espertech.esper.common.internal.bytecodemodel.model.expression.CodegenExpressionBuilder.constantNull;

public class ExprCaseNodeForge implements ExprTypableReturnForge, ExprForgeInstrumentable {
    private final ExprCaseNode parent;
    private final EPTypeClass resultType;
    protected final LinkedHashMap<String, Object> mapResultType;
    private final boolean isNumericResult;
    private final boolean mustCoerce;
    private final SimpleNumberCoercer coercer;
    private final List<UniformPair<ExprNode>> whenThenNodeList;
    private final ExprNode optionalCompareExprNode;
    private final ExprNode optionalElseExprNode;

    ExprCaseNodeForge(ExprCaseNode parent, EPTypeClass resultType, LinkedHashMap<String, Object> mapResultType, boolean isNumericResult, boolean mustCoerce, SimpleNumberCoercer coercer, List<UniformPair<ExprNode>> whenThenNodeList, ExprNode optionalCompareExprNode, ExprNode optionalElseExprNode) {
        if (resultType == null) {
            throw new IllegalArgumentException("Null result");
        }
        this.parent = parent;
        this.resultType = resultType;
        this.mapResultType = mapResultType;
        this.isNumericResult = isNumericResult;
        this.mustCoerce = mustCoerce;
        this.coercer = coercer;
        this.whenThenNodeList = whenThenNodeList;
        this.optionalCompareExprNode = optionalCompareExprNode;
        this.optionalElseExprNode = optionalElseExprNode;
    }

    public List<UniformPair<ExprNode>> getWhenThenNodeList() {
        return whenThenNodeList;
    }

    public ExprNode getOptionalCompareExprNode() {
        return optionalCompareExprNode;
    }

    public ExprNode getOptionalElseExprNode() {
        return optionalElseExprNode;
    }

    public ExprCaseNode getForgeRenderable() {
        return parent;
    }

    public EPTypeClass getEvaluationType() {
        return resultType;
    }

    boolean isNumericResult() {
        return isNumericResult;
    }

    public boolean isMustCoerce() {
        return mustCoerce;
    }

    public SimpleNumberCoercer getCoercer() {
        return coercer;
    }

    public ExprEvaluator getExprEvaluator() {
        List<UniformPair<ExprEvaluator>> evals = new ArrayList<>();
        for (UniformPair<ExprNode> pair : whenThenNodeList) {
            evals.add(new UniformPair<>(pair.getFirst().getForge().getExprEvaluator(), pair.getSecond().getForge().getExprEvaluator()));
        }
        if (!parent.isCase2()) {
            return new ExprCaseNodeForgeEvalSyntax1(this, evals, optionalElseExprNode == null ? null : optionalElseExprNode.getForge().getExprEvaluator());
        } else {
            return new ExprCaseNodeForgeEvalSyntax2(this, evals, optionalCompareExprNode.getForge().getExprEvaluator(), optionalElseExprNode == null ? null : optionalElseExprNode.getForge().getExprEvaluator());
        }
    }

    public ExprTypableReturnEval getTypableReturnEvaluator() {
        return new ExprCaseNodeForgeEvalTypable(this);
    }

    public CodegenExpression evaluateCodegenUninstrumented(EPTypeClass requiredType, CodegenMethodScope codegenMethodScope, ExprForgeCodegenSymbol exprSymbol, CodegenClassScope codegenClassScope) {
        if (!parent.isCase2()) {
            return ExprCaseNodeForgeEvalSyntax1.codegen(this, codegenMethodScope, exprSymbol, codegenClassScope);
        } else {
            return ExprCaseNodeForgeEvalSyntax2.codegen(this, codegenMethodScope, exprSymbol, codegenClassScope);
        }
    }

    public CodegenExpression evaluateCodegen(EPTypeClass requiredType, CodegenMethodScope codegenMethodScope, ExprForgeCodegenSymbol exprSymbol, CodegenClassScope codegenClassScope) {
        return new InstrumentationBuilderExpr(this.getClass(), this, "ExprCase", requiredType, codegenMethodScope, exprSymbol, codegenClassScope).build();
    }

    public CodegenExpression evaluateTypableSingleCodegen(CodegenMethodScope codegenMethodScope, ExprForgeCodegenSymbol exprSymbol, CodegenClassScope codegenClassScope) {
        return ExprCaseNodeForgeEvalTypable.codegenTypeableSingle(this, codegenMethodScope, exprSymbol, codegenClassScope);
    }

    public CodegenExpression evaluateTypableMultiCodegen(CodegenMethodScope codegenMethodScope, ExprForgeCodegenSymbol exprSymbol, CodegenClassScope codegenClassScope) {
        return constantNull();
    }

    public ExprForgeConstantType getForgeConstantType() {
        return ExprForgeConstantType.NONCONST;
    }

    public Boolean isMultirow() {
        return mapResultType == null ? null : false;
    }

    public LinkedHashMap<String, Object> getRowProperties() throws ExprValidationException {
        return mapResultType;
    }
}
