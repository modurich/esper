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
package com.espertech.esper.common.internal.supportunit.bean;

import com.espertech.esper.common.internal.support.SupportBeanBase;

import java.io.Serializable;

public class SupportBean_C extends SupportBeanBase implements Serializable {
    public SupportBean_C(String id) {
        super(id);
    }
}
