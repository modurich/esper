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
package com.espertech.esper.common.internal.event.json.parser.forge;

import com.espertech.esper.common.client.type.EPTypeClass;
import com.espertech.esper.common.internal.bytecodemodel.base.CodegenClassScope;
import com.espertech.esper.common.internal.bytecodemodel.base.CodegenMethod;
import com.espertech.esper.common.internal.bytecodemodel.model.expression.CodegenExpression;
import com.espertech.esper.common.internal.event.json.parser.core.JsonDelegateBase;

import static com.espertech.esper.common.internal.bytecodemodel.model.expression.CodegenExpressionBuilder.localMethod;
import static com.espertech.esper.common.internal.bytecodemodel.model.expression.CodegenExpressionBuilder.newInstance;

public class JsonDelegateForgeWithDelegateFactorySelf implements JsonDelegateForge {

    private final String delegateClassName;
    private final EPTypeClass beanClass;

    public JsonDelegateForgeWithDelegateFactorySelf(String delegateClassName, EPTypeClass beanClass) {
        this.delegateClassName = delegateClassName;
        this.beanClass = beanClass;
    }

    public CodegenExpression newDelegate(JsonDelegateRefs fields, CodegenMethod parent, CodegenClassScope classScope) {
        CodegenMethod method = parent.makeChild(JsonDelegateBase.EPTYPE, JsonForgeFactoryEventTypeTyped.class, classScope);
        method.getBlock()
            .methodReturn(newInstance(delegateClassName, fields.getBaseHandler(), fields.getThis(), newInstance(beanClass)));
        return localMethod(method);
    }
}
