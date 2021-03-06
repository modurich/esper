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
package com.espertech.esper.common.internal.filterspec;

import com.espertech.esper.common.internal.bytecodemodel.base.CodegenClassScope;
import com.espertech.esper.common.internal.bytecodemodel.base.CodegenMethod;
import com.espertech.esper.common.internal.bytecodemodel.base.CodegenMethodScope;
import com.espertech.esper.common.internal.bytecodemodel.model.expression.CodegenExpression;
import com.espertech.esper.common.internal.context.aifactory.core.SAIFFInitializeSymbolWEventType;
import com.espertech.esper.common.internal.epl.expression.core.ExprFilterSpecLookupable;
import com.espertech.esper.common.internal.epl.expression.core.ExprFilterSpecLookupableForge;

import static com.espertech.esper.common.internal.bytecodemodel.model.expression.CodegenExpressionBuilder.*;
import static com.espertech.esper.common.internal.filterspec.FilterSpecParamFilterForEvalDoubleForge.makeAnonymous;

public final class FilterSpecParamAdvancedIndexQuadTreePointRegionForge extends FilterSpecParamForge {

    private FilterSpecParamFilterForEvalDoubleForge xEval;
    private FilterSpecParamFilterForEvalDoubleForge yEval;

    public FilterSpecParamAdvancedIndexQuadTreePointRegionForge(ExprFilterSpecLookupableForge lookupable, FilterOperator filterOperator, FilterSpecParamFilterForEvalDoubleForge xEval, FilterSpecParamFilterForEvalDoubleForge yEval) {
        super(lookupable, filterOperator);
        this.xEval = xEval;
        this.yEval = yEval;
    }

    public CodegenExpression makeCodegen(CodegenClassScope classScope, CodegenMethodScope parent, SAIFFInitializeSymbolWEventType symbols) {
        CodegenMethod method = parent.makeChild(FilterSpecParamAdvancedIndexQuadTreePointRegion.EPTYPE, this.getClass(), classScope);
        method.getBlock()
                .declareVar(ExprFilterSpecLookupable.EPTYPE, "lookupable", localMethod(lookupable.makeCodegen(method, symbols, classScope)))
                .declareVar(ExprFilterSpecLookupable.EPTYPE_FILTEROPERATOR, "op", enumValue(FilterOperator.class, filterOperator.name()))
                .declareVar(FilterSpecParamAdvancedIndexQuadTreePointRegion.EPTYPE, "fpai", newInstance(FilterSpecParamAdvancedIndexQuadTreePointRegion.EPTYPE, ref("lookupable"), ref("op")))
                .exprDotMethod(ref("fpai"), "setxEval", makeAnonymous(xEval, this.getClass(), classScope, method))
                .exprDotMethod(ref("fpai"), "setyEval", makeAnonymous(yEval, this.getClass(), classScope, method))
                .methodReturn(ref("fpai"));
        return localMethod(method);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof FilterSpecParamAdvancedIndexQuadTreePointRegionForge)) {
            return false;
        }

        FilterSpecParamAdvancedIndexQuadTreePointRegionForge other = (FilterSpecParamAdvancedIndexQuadTreePointRegionForge) obj;
        if (!super.equals(other)) {
            return false;
        }
        return this.xEval.equals(other.xEval) &&
                (this.yEval.equals(other.yEval));
    }

    public int hashCode() {
        return super.hashCode();
    }

    public void valueExprToString(StringBuilder out, int i) {
        out.append("Point-Region ");
        out.append("x ");
        xEval.valueToString(out);
        out.append("y ");
        yEval.valueToString(out);
    }
}
