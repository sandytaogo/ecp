/*
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sandy.ecp.framework.util;

import java.lang.reflect.Field;

/**
 * 
 * @author Sandy
 * @date 2023-05-23 12:12:12
 */
public final class PrimitiveTypeDefault {
    protected static boolean BOOLEAN;
    protected static byte BYTE;
    protected static char CHAR;
    protected static short SHORT;
    protected static int INT;
    protected static long LONG;
    protected static float FLOAT;
    protected static double DOUBLE;
    
    private PrimitiveTypeDefault() {
    }
    
    public static Object getPrimitiveNullObject(final Class<?> cls) {
        final String name = ClassUtil.getClassSimpleName(cls).toUpperCase();
        final Field fld = ClassUtil.getClassField(PrimitiveTypeDefault.class, name);
        try {
            return fld.get(null);
        }
        catch (Exception e) {
            throw new RuntimeException("service.PrimitiveTypeError", e);
        }
    }
}