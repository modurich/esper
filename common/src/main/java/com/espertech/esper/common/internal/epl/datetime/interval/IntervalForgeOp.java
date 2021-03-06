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
package com.espertech.esper.common.internal.epl.datetime.interval;

import com.espertech.esper.common.client.EventBean;
import com.espertech.esper.common.client.type.EPTypeClass;
import com.espertech.esper.common.client.type.EPTypePremade;
import com.espertech.esper.common.internal.bytecodemodel.base.CodegenBlock;
import com.espertech.esper.common.internal.bytecodemodel.base.CodegenClassScope;
import com.espertech.esper.common.internal.bytecodemodel.base.CodegenMethod;
import com.espertech.esper.common.internal.bytecodemodel.base.CodegenMethodScope;
import com.espertech.esper.common.internal.bytecodemodel.model.expression.CodegenExpression;
import com.espertech.esper.common.internal.epl.expression.codegen.ExprForgeCodegenSymbol;
import com.espertech.esper.common.internal.epl.expression.core.ExprEvaluator;
import com.espertech.esper.common.internal.epl.expression.core.ExprEvaluatorContext;

import static com.espertech.esper.common.internal.bytecodemodel.model.expression.CodegenExpressionBuilder.localMethod;
import static com.espertech.esper.common.internal.bytecodemodel.model.expression.CodegenExpressionBuilder.ref;

public class IntervalForgeOp implements IntervalOp {

    private final ExprEvaluator evaluatorTimestamp;
    private final IntervalForgeImpl.IntervalOpEval intervalOpEval;

    public IntervalForgeOp(ExprEvaluator evaluatorTimestamp, IntervalForgeImpl.IntervalOpEval intervalOpEval) {
        this.evaluatorTimestamp = evaluatorTimestamp;
        this.intervalOpEval = intervalOpEval;
    }

    public Object evaluate(long startTs, long endTs, EventBean[] eventsPerStream, boolean isNewData, ExprEvaluatorContext context) {
        Object parameter = evaluatorTimestamp.evaluate(eventsPerStream, isNewData, context);
        if (parameter == null) {
            return parameter;
        }

        return intervalOpEval.evaluate(startTs, endTs, parameter, eventsPerStream, isNewData, context);
    }

    public static CodegenExpression codegen(IntervalForgeImpl forge, CodegenExpression start, CodegenExpression end, CodegenMethodScope codegenMethodScope, ExprForgeCodegenSymbol exprSymbol, CodegenClassScope codegenClassScope) {
        CodegenMethod methodNode = codegenMethodScope.makeChild(EPTypePremade.BOOLEANBOXED.getEPType(), IntervalForgeOp.class, codegenClassScope).addParam(EPTypePremade.LONGPRIMITIVE.getEPType(), "startTs").addParam(EPTypePremade.LONGPRIMITIVE.getEPType(), "endTs");

        EPTypeClass evaluationType = (EPTypeClass) forge.getForgeTimestamp().getEvaluationType();
        CodegenBlock block = methodNode.getBlock()
            .declareVar(evaluationType, "parameter", forge.getForgeTimestamp().evaluateCodegen(evaluationType, methodNode, exprSymbol, codegenClassScope));
        if (!evaluationType.getType().isPrimitive()) {
            block.ifRefNullReturnNull("parameter");
        }
        block.methodReturn(forge.getIntervalOpForge().codegen(ref("startTs"), ref("endTs"), ref("parameter"), evaluationType, methodNode, exprSymbol, codegenClassScope));
        return localMethod(methodNode, start, end);
    }
}
