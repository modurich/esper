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
package com.espertech.esper.common.internal.epl.datetime.eval;

import com.espertech.esper.common.internal.epl.expression.dot.core.ExprDotForge;
import com.espertech.esper.common.internal.epl.join.analyze.FilterExprAnalyzerAffector;
import com.espertech.esper.common.internal.rettype.EPChainableType;

public class ExprDotDTMethodDesc {

    private final ExprDotForge forge;
    private final EPChainableType returnType;
    private final FilterExprAnalyzerAffector intervalFilterDesc;

    public ExprDotDTMethodDesc(ExprDotForge forge, EPChainableType returnType, FilterExprAnalyzerAffector intervalFilterDesc) {
        this.forge = forge;
        this.returnType = returnType;
        this.intervalFilterDesc = intervalFilterDesc;
    }

    public ExprDotForge getForge() {
        return forge;
    }

    public EPChainableType getReturnType() {
        return returnType;
    }

    public FilterExprAnalyzerAffector getIntervalFilterDesc() {
        return intervalFilterDesc;
    }
}
