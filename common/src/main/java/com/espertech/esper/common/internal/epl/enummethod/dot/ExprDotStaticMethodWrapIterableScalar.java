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
package com.espertech.esper.common.internal.epl.enummethod.dot;

import com.espertech.esper.common.client.type.EPTypeClass;
import com.espertech.esper.common.client.type.EPTypePremade;
import com.espertech.esper.common.internal.bytecodemodel.base.CodegenClassScope;
import com.espertech.esper.common.internal.bytecodemodel.base.CodegenMethodScope;
import com.espertech.esper.common.internal.bytecodemodel.model.expression.CodegenExpression;
import com.espertech.esper.common.internal.rettype.EPChainableType;
import com.espertech.esper.common.internal.rettype.EPChainableTypeHelper;
import com.espertech.esper.common.internal.util.CollectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

import static com.espertech.esper.common.internal.bytecodemodel.model.expression.CodegenExpressionBuilder.cast;
import static com.espertech.esper.common.internal.bytecodemodel.model.expression.CodegenExpressionBuilder.staticMethod;

public class ExprDotStaticMethodWrapIterableScalar implements ExprDotStaticMethodWrap {

    private static final Logger log = LoggerFactory.getLogger(ExprDotStaticMethodWrapArrayScalar.class);

    private final String methodName;
    private final EPTypeClass componentType;

    public ExprDotStaticMethodWrapIterableScalar(String methodName, EPTypeClass componentType) {
        this.methodName = methodName;
        this.componentType = componentType;
    }

    public EPChainableType getTypeInfo() {
        return EPChainableTypeHelper.collectionOfSingleValue(componentType);
    }

    public Collection convertNonNull(Object result) {
        if (!(result instanceof Iterable)) {
            log.warn("Expected iterable-type input from method '" + methodName + "' but received " + result.getClass());
            return null;
        }
        return CollectionUtil.iterableToCollection((Iterable) result);
    }

    public CodegenExpression codegenConvertNonNull(CodegenExpression result, CodegenMethodScope codegenMethodScope, CodegenClassScope codegenClassScope) {
        return staticMethod(CollectionUtil.class, "iterableToCollection", cast(EPTypePremade.ITERABLE.getEPType(), result));
    }
}
