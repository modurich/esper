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
package com.espertech.esper.common.internal.epl.expression.core;

import com.espertech.esper.common.client.EventBean;
import com.espertech.esper.common.client.hook.expr.EPLExpressionEvaluationContext;
import com.espertech.esper.common.client.type.EPTypeClass;
import com.espertech.esper.common.internal.bytecodemodel.base.CodegenClassScope;
import com.espertech.esper.common.internal.bytecodemodel.base.CodegenMethodScope;
import com.espertech.esper.common.internal.bytecodemodel.model.expression.CodegenExpression;
import com.espertech.esper.common.internal.bytecodemodel.model.expression.CodegenExpressionRef;
import com.espertech.esper.common.internal.epl.expression.codegen.ExprForgeCodegenSymbol;

import java.io.StringWriter;

import static com.espertech.esper.common.internal.bytecodemodel.model.expression.CodegenExpressionBuilder.staticMethod;

/**
 * Represents the "current_evaluation_context" function in an expression tree.
 */
public class ExprCurrentEvaluationContextNode extends ExprNodeBase implements ExprEvaluator, ExprForge {

    /**
     * Ctor.
     */
    public ExprCurrentEvaluationContextNode() {
    }

    public ExprEvaluator getExprEvaluator() {
        return this;
    }

    public ExprForge getForge() {
        return this;
    }

    public ExprNodeRenderable getForgeRenderable() {
        return this;
    }

    public CodegenExpression evaluateCodegen(EPTypeClass requiredType, CodegenMethodScope codegenMethodScope, ExprForgeCodegenSymbol exprSymbol, CodegenClassScope codegenClassScope) {
        CodegenExpressionRef refExprEvalCtx = exprSymbol.getAddExprEvalCtx(codegenMethodScope);
        return staticMethod(ExprCurrentEvaluationContextNode.class, "exprCurrentEvaluationContextMake", refExprEvalCtx);
    }

    public ExprForgeConstantType getForgeConstantType() {
        return ExprForgeConstantType.NONCONST;
    }

    public EPTypeClass getEvaluationType() {
        return EPLExpressionEvaluationContext.EPTYPE;
    }

    public ExprNode validate(ExprValidationContext validationContext) throws ExprValidationException {
        if (this.getChildNodes().length != 0) {
            throw new ExprValidationException("current_evaluation_context function node cannot have a child node");
        }
        return null;
    }

    public boolean isConstantResult() {
        return false;
    }

    public Object evaluate(EventBean[] eventsPerStream, boolean isNewData, ExprEvaluatorContext exprEvaluatorContext) {
        return exprCurrentEvaluationContextMake(exprEvaluatorContext);
    }

    /**
     * NOTE: Code-generation-invoked method, method name and parameter order matters
     *
     * @param exprEvaluatorContext ctx
     * @return wrapper
     */
    public static EPLExpressionEvaluationContext exprCurrentEvaluationContextMake(ExprEvaluatorContext exprEvaluatorContext) {
        return new EPLExpressionEvaluationContext(exprEvaluatorContext.getStatementName(), exprEvaluatorContext.getAgentInstanceId(), exprEvaluatorContext.getRuntimeURI(), exprEvaluatorContext.getUserObjectCompileTime());
    }

    public void toPrecedenceFreeEPL(StringWriter writer, ExprNodeRenderableFlags flags) {
        writer.append("current_evaluation_context()");
    }

    public ExprPrecedenceEnum getPrecedence() {
        return ExprPrecedenceEnum.UNARY;
    }

    public boolean equalsNode(ExprNode node, boolean ignoreStreamPrefix) {
        if (!(node instanceof ExprCurrentEvaluationContextNode)) {
            return false;
        }
        return true;
    }
}
