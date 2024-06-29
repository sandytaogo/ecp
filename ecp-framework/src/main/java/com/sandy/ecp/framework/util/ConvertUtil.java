/*
 * Copyright 2018-2030 the original author or authors.
 *
 * Licensed under the company, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.company.com/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sandy.ecp.framework.util;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialClob;

import com.sandy.ecp.framework.exception.FindException;

/**
 * 
 * @author Sandy
 * @date 2023-05-23 12:12:12
 */
public final class ConvertUtil {
	
    public static final int UNASSIGNED_NULL = 0;
    public static final int ASSIGNED_NULL = 1;
    public static final int BYTE = 2;
    public static final int SHORT = 3;
    public static final int INT = 4;
    public static final int LONG = 5;
    public static final int FLOAT = 6;
    public static final int DOUBLE = 7;
    public static final int BIGINTEGER = 8;
    public static final int BIGDECIMAL = 10;
    public static final int BOOLEAN = 11;
    public static final int INPUTSTREAM = 12;
    public static final int DATE = 13;
    public static final int TIME = 14;
    public static final int TIMESTAMP = 15;
    public static final int STRING = 16;
    public static final int OBJECT = 17;
    public static final int BYTE_ARRAY = 18;
    public static final int BLOB = 19;
    public static final int CLOB = 20;
    public static final int ENUM = 21;
    public static final String ASSIGNEDNULL_S = "ASSIGNED_NULL";
    public static final String UNASSIGNEDNULL_S = "UNASSIGNED_NULL";
    public static final String BYTETYPE_S = "BYTE";
    public static final String SHORTTYPE_S = "SHORT";
    public static final String INTTYPE_S = "INT";
    public static final String INTEGERTYPE_S = "INTEGER";
    public static final String BIGINTEGERTYPE_S = "BIGINTEGER";
    public static final String LONGTYPE_S = "LONG";
    public static final String FLOATTYPE_S = "FLOAT";
    public static final String DOUBLETYPE_S = "DOUBLE";
    public static final String BIGDECIMALTYPE_S = "BIGDECIMAL";
    public static final String BOOLEANTYPE_S = "BOOLEAN";
    public static final String INPUTSTREAMTYPE_S = "INPUTSTREAM";
    public static final String DATETYPE_S = "DATE";
    public static final String TIMETYPE_S = "TIME";
    public static final String TIMESTAMPTYPE_S = "TIMESTAMP";
    public static final String BYTEARRAYTYPE_S = "[B";
    public static final String STRINGTYPE_S = "STRING";
    public static final String OBJECTTYPE_S = "OBJECT";
    public static final String BLOB_S = "SERIALBLOB";
    public static final String BLOB_P = "BLOB";
    public static final String CLOB_S = "SERIALCLOB";
    public static final String CLOB_P = "CLOB";
    public static final Double ZERODOUBLE;
    public static final Float ZEROFLOAT;
    public static final String ZEROSTRING = "0";
    public static final String FALSESTRING = "false";
    public static final String NOTSUPPORTTYPECAST = "\u4e0d\u652f\u6301\u7684\u7c7b\u578b\u8f6c\u6362";
    public static final char PACKAGESEP = '.';
    public static final BigDecimal ZEROBIGDECIMAL;
    public static final byte[] EMPTYBYTES;
    public static final char[] EMPTYCHARS;
    private static Map<String, String> types;
    static Map<Object, Object> typeMap;
    
    static {
        ZERODOUBLE = 0.0;
        ZEROFLOAT = 0.0f;
        ZEROBIGDECIMAL = new BigDecimal(0);
        EMPTYBYTES = new byte[0];
        EMPTYCHARS = new char[0];
        (ConvertUtil.types = new HashMap<String, String>()).put("BIGDECIMAL", String.valueOf(10));
        ConvertUtil.types.put("BOOLEAN", String.valueOf(11));
        ConvertUtil.types.put("[B", String.valueOf(18));
        ConvertUtil.types.put("BYTE", String.valueOf(2));
        ConvertUtil.types.put("DATE", String.valueOf(13));
        ConvertUtil.types.put("DOUBLE", String.valueOf(7));
        ConvertUtil.types.put("FLOAT", String.valueOf(6));
        ConvertUtil.types.put("INPUTSTREAM", String.valueOf(12));
        ConvertUtil.types.put("INT", String.valueOf(4));
        ConvertUtil.types.put("INTEGER", String.valueOf(4));
        ConvertUtil.types.put("BIGINTEGER", String.valueOf(8));
        ConvertUtil.types.put("LONG", String.valueOf(5));
        ConvertUtil.types.put("OBJECT", String.valueOf(17));
        ConvertUtil.types.put("SHORT", String.valueOf(3));
        ConvertUtil.types.put("STRING", String.valueOf(16));
        ConvertUtil.types.put("TIMESTAMP", String.valueOf(15));
        ConvertUtil.types.put("TIME", String.valueOf(14));
        ConvertUtil.types.put("SERIALBLOB", String.valueOf(19));
        ConvertUtil.types.put("BLOB", String.valueOf(19));
        ConvertUtil.types.put("SERIALCLOB", String.valueOf(20));
        ConvertUtil.types.put("CLOB", String.valueOf(20));
        (ConvertUtil.typeMap = new HashMap<Object, Object>()).put(String.class, 12);
        ConvertUtil.typeMap.put(BigInteger.class, -5);
        ConvertUtil.typeMap.put(SerialBlob.class, 2004);
        ConvertUtil.typeMap.put(SerialClob.class, 2005);
        ConvertUtil.typeMap.put(Boolean.class, 16);
        ConvertUtil.typeMap.put(Boolean.TYPE, 16);
        final Integer number = 2;
        ConvertUtil.typeMap.put(Float.class, number);
        ConvertUtil.typeMap.put(Float.TYPE, number);
        ConvertUtil.typeMap.put(Double.class, number);
        ConvertUtil.typeMap.put(Double.TYPE, number);
        ConvertUtil.typeMap.put(BigDecimal.class, number);
        ConvertUtil.typeMap.put(Long.TYPE, number);
        ConvertUtil.typeMap.put(Long.class, number);
        ConvertUtil.typeMap.put(Integer.class, 4);
        ConvertUtil.typeMap.put(Integer.TYPE, 4);
        ConvertUtil.typeMap.put(java.sql.Date.class, 91);
        ConvertUtil.typeMap.put(Date.class, 91);
        ConvertUtil.typeMap.put(Object.class, 93);
        ConvertUtil.typeMap.put(Time.class, 92);
        ConvertUtil.typeMap.put(Timestamp.class, 93);
    }
    
    private ConvertUtil() {
    	super();
    }
    
    public static int getType(final Object obj) {
        if (obj == null) {
            return 1;
        }
        final Class<?> cls = obj.getClass();
        if (cls.isEnum()) {
            return 21;
        }
        return getType(obj.getClass());
    }
    
    public static String getClassSimpleName(final Class<?> cls) {
        String name = cls.getName();
        final int index = name.lastIndexOf(46);
        if (index > 0) {
            name = name.substring(index + 1);
        }
        return name;
    }
    
    public static int getType(final Class<?> cls) {
        if (cls == null) {
            return 1;
        }
        if (cls.isEnum()) {
            return 21;
        }
        final String typeValue = ConvertUtil.types.get(getClassSimpleName(cls).toUpperCase());
        if (typeValue != null) {
            return Integer.parseInt(typeValue);
        }
        return 17;
    }
    
    private static void typeProblem(final int unexpectedType, final int expectedType, final boolean getter) {
        throw new RuntimeException("E0208.0009");
    }
    
    public static BigDecimal toBigDecimal(final Object object) {
        final int type = getType(object);
        switch (type) {
            case 21: {
                return new BigDecimal(((Enum<?>)object).ordinal());
            }
            case 11: {
                return new BigDecimal(((boolean)object) ? 1 : 0);
            }
            case 2: {
                return new BigDecimal((long)object);
            }
            case 3: {
                return new BigDecimal((long)object);
            }
            case 4: {
                return new BigDecimal(String.valueOf(object));
            }
            case 8: {
                return BigDecimal.valueOf(((BigInteger)object).longValue(), 0);
            }
            case 5: {
                return BigDecimal.valueOf((long)object, 0);
            }
            case 6: {
                return new BigDecimal(String.valueOf(object));
            }
            case 7: {
                return new BigDecimal(String.valueOf(object));
            }
            case 10: {
                return (BigDecimal)object;
            }
            case 14: {
                return BigDecimal.valueOf(((Time)object).getTime());
            }
            case 13: {
                return BigDecimal.valueOf(((Date)object).getTime());
            }
            case 15: {
                return BigDecimal.valueOf(((Timestamp)object).getTime());
            }
            case 0:
            case 1: {
                return new BigDecimal(0);
            }
            case 16: {
                final String stringVal = (String)object;
                return (stringVal == null || stringVal.length() == 0) ? ConvertUtil.ZEROBIGDECIMAL : new BigDecimal(stringVal);
            }
            default: {
                typeProblem(type, 10, true);
                return null;
            }
        }
    }
    
    public static Boolean toBoolean(final Object object) {
        final int type = getType(object);
        switch (type) {
            case 11: {
                return (Boolean)object;
            }
            case 2: {
                return (long)object != 0L;
            }
            case 3: {
                return (long)object != 0L;
            }
            case 4: {
                return (int)object != 0;
            }
            case 8: {
                return ((BigInteger)object).intValue() != 0;
            }
            case 5: {
                return (long)object != 0L;
            }
            case 6: {
                return ((Float)object).compareTo(ConvertUtil.ZEROFLOAT) != 0;
            }
            case 7: {
                return ((Double)object).compareTo(ConvertUtil.ZERODOUBLE) != 0;
            }
            case 10: {
                return ((BigDecimal)object).longValue() != 0L;
            }
            case 14: {
                return object != null;
            }
            case 13: {
                return object != null;
            }
            case 15: {
                return object != null;
            }
            case 0:
            case 1: {
                return Boolean.FALSE;
            }
            case 16: {
                final String stringVal = (String)object;
                final boolean tmpValue = stringVal == null || stringVal.length() == 0 || "0".equals(stringVal) || "false".equalsIgnoreCase(stringVal);
                return !tmpValue;
            }
            default: {
                typeProblem(type, 11, true);
                return Boolean.FALSE;
            }
        }
    }
    
    public static Byte toByte(final Object object) {
        final int type = getType(object);
        switch (type) {
            case 11: {
                return Integer.valueOf(((boolean)object) ? 1 : 0).byteValue();
            }
            case 2: {
                return (Byte)object;
            }
            case 3: {
                return ((Short)object).byteValue();
            }
            case 4: {
                return ((Integer)object).byteValue();
            }
            case 8: {
                return ((BigInteger)object).byteValue();
            }
            case 5: {
                return ((Long)object).byteValue();
            }
            case 6: {
                return ((Float)object).byteValue();
            }
            case 7: {
                return ((Double)object).byteValue();
            }
            case 10: {
                return ((BigDecimal)object).byteValue();
            }
            case 14: {
            }
            case 13: {
            }
            case 15: {
            }
            case 0:
            case 1: {
                return null;
            }
            case 16: {
                final String stringVal = (String)object;
                if (StringUtil.isNotEmptyString(stringVal)) {
                    try {
                        return stringVal.getBytes("UTF-8")[0];
                    }
                    catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                }
                return null;
            }
            default: {
                typeProblem(type, 2, true);
                return null;
            }
        }
    }
    
    public static byte[] toByteArray(final Object object) {
        return (byte[])object;
    }
    
    public static Date toDate(final Object value) {
        if (value == null) {
            return null;
        }
        long time = 0L;
        if (value instanceof Timestamp) {
            time = ((Timestamp)value).getTime();
        } else {
            if (value instanceof Time) {
                return (Time)value;
            }
            if (value instanceof Date) {
                return (Date)value;
            }
            if (value instanceof Long) {
                time = (long)value;
            }
        }
        return new Date(time);
    }
    
    public static Date toSqlDate(final Object value) {
        if (value == null) {
            return null;
        }
        long time = 0L;
        if (value instanceof Timestamp) {
            time = ((Timestamp)value).getTime();
        }
        else if (value instanceof Time) {
            time = ((Time)value).getTime();
        }
        else if (value instanceof Date) {
            time = ((Date)value).getTime();
        }
        return new java.util.Date(time);
    }
    
    public static Double toDouble(final Object object) {
        final int type = getType(object);
        switch (type) {
            case 21: {
                return (double)((Enum<?>)object).ordinal();
            }
            case 11: {
//                return object ? 1.0 : 0.0;
            }
            case 2: {
                return (double)object;
            }
            case 3: {
                return (double)object;
            }
            case 4: {
                return (double)object;
            }
            case 8: {
                return ((BigInteger)object).doubleValue();
            }
            case 5: {
                return (double)object;
            }
            case 6: {
                return (double)object;
            }
            case 7: {
                return (Double)object;
            }
            case 10: {
                return ((BigDecimal)object).doubleValue();
            }
            case 14: {
                return Double.valueOf(((Time)object).getTime());
            }
            case 13: {
                return Double.valueOf(((Date)object).getTime());
            }
            case 15: {
                return Double.valueOf(((Timestamp)object).getTime());
            }
            case 0:
            case 1: {
                return null;
            }
            case 16: {
                final String stringVal = (String)object;
                if (StringUtil.isNotEmptyString(stringVal)) {
                    return Double.valueOf(stringVal);
                }
                return null;
            }
            default: {
                typeProblem(type, 7, true);
                return null;
            }
        }
    }
    
    public static Float toFloat(final Object object) {
        final int type = getType(object);
        switch (type) {
            case 21: {
                return Float.valueOf(((Enum<?>)object).ordinal());
            }
            case 11: {
//                return object ? 1.0f : 0.0f;
            }
            case 2: {
                return (float)object;
            }
            case 3: {
                return (float)object;
            }
            case 8: {
                return ((BigInteger)object).floatValue();
            }
            case 4: {
                return (float)object;
            }
            case 5: {
                return (float)object;
            }
            case 6: {
                return (Float)object;
            }
            case 7: {
                return ((Double)object).floatValue();
            }
            case 10: {
                return ((BigDecimal)object).floatValue();
            }
            case 14: {
                return Float.valueOf(((Time)object).getTime());
            }
            case 13: {
                return Float.valueOf(((Date)object).getTime());
            }
            case 15: {
                return Float.valueOf(((Timestamp)object).getTime());
            }
            case 0:
            case 1: {
                return null;
            }
            case 16: {
                final String stringVal = (String)object;
                if (StringUtil.isNotEmptyString(stringVal)) {
                    return Float.valueOf(stringVal);
                }
                return null;
            }
            default: {
                typeProblem(type, 6, true);
                return null;
            }
        }
    }
    
    public static InputStream toInputStream(final Object object) {
        return (InputStream)object;
    }
    
    public static Integer toInteger(final Object object) {
        final int type = getType(object);
        switch (type) {
            case 21: {
                return ((Enum<?>)object).ordinal();
            }
            case 11: {
                return ((boolean)object) ? 1 : 0;
            }
            case 2: {
                return (int)object;
            }
            case 3: {
                return (int)object;
            }
            case 4: {
                return (Integer)object;
            }
            case 8: {
                return ((BigInteger)object).intValue();
            }
            case 5: {
                return ((Long)object).intValue();
            }
            case 6: {
                return ((Float)object).intValue();
            }
            case 7: {
                return ((Double)object).intValue();
            }
            case 10: {
                return ((BigDecimal)object).intValue();
            }
            case 14: {
                return (int)((Time)object).getTime();
            }
            case 13: {
                return (int)((Date)object).getTime();
            }
            case 15: {
                return (int)((Timestamp)object).getTime();
            }
            case 0:
            case 1: {
                return null;
            }
            case 16: {
                final String stringVal = (String)object;
                if (StringUtil.isNotEmptyString(stringVal)) {
                    return Integer.valueOf(stringVal);
                }
                return 0;
            }
            default: {
                typeProblem(type, 4, true);
                return null;
            }
        }
    }
    
    public static int toInt(final Object object) {
        final int type = getType(object);
        switch (type) {
            case 21: {
                return ((Enum<?>)object).ordinal();
            }
            case 11: {
                return ((boolean)object) ? 1 : 0;
            }
            case 2: {
                return (int)object;
            }
            case 3: {
                return (int)object;
            }
            case 4: {
                return (int)object;
            }
            case 8: {
                return ((BigInteger)object).intValue();
            }
            case 5: {
                return ((Long)object).intValue();
            }
            case 6: {
                return ((Float)object).intValue();
            }
            case 7: {
                return ((Double)object).intValue();
            }
            case 10: {
                return ((BigDecimal)object).intValue();
            }
            case 14: {
                return (int)((Time)object).getTime();
            }
            case 13: {
                return (int)((Date)object).getTime();
            }
            case 15: {
                return (int)((Timestamp)object).getTime();
            }
            case 0:
            case 1: {
                return 0;
            }
            case 16: {
                final String stringVal = (String) object;
                if (StringUtil.isEmpty(stringVal)) {
                    return 0;
                }
                return Integer.parseInt(stringVal);
            }
            default: {
                typeProblem(type, 4, true);
                return 0;
            }
        }
    }
    
    public static Long toLong(final Object object) {
        final int type = getType(object);
        switch (type) {
            case 2: {
                return (long)object;
            }
            case 3: {
                return (long)object;
            }
            case 4: {
                return ((Integer)object).longValue();
            }
            case 8: {
                return ((BigInteger)object).longValue();
            }
            case 5: {
                return (Long)object;
            }
            case 6: {
                return ((Float)object).longValue();
            }
            case 7: {
                return ((Double)object).longValue();
            }
            case 10: {
                return ((BigDecimal)object).longValue();
            }
            case 14: {
                return ((Time)object).getTime();
            }
            case 13: {
                return ((Date)object).getTime();
            }
            case 15: {
                return ((Timestamp)object).getTime();
            }
            case 0:
            case 1: {
                return null;
            }
            case 16: {
                final String stringVal = (String)object;
                if (StringUtil.isNotEmptyString(stringVal)) {
                    return Long.valueOf(stringVal);
                }
                return null;
            }
            default: {
                typeProblem(type, 5, true);
                return null;
            }
        }
    }
    
    public static Short toShort(final Object object) {
        final int type = getType(object);
        switch (type) {
            case 2: {
                return (short)object;
            }
            case 3: {
                return (Short)object;
            }
            case 4: {
                return ((Integer)object).shortValue();
            }
            case 8: {
                return ((BigInteger)object).shortValue();
            }
            case 5: {
                return ((Long)object).shortValue();
            }
            case 6: {
                return ((Float)object).shortValue();
            }
            case 7: {
                return ((Double)object).shortValue();
            }
            case 10: {
                return ((BigDecimal)object).shortValue();
            }
            case 14: {
                return (short)((Time)object).getTime();
            }
            case 13: {
                return (short)((Date)object).getTime();
            }
            case 15: {
                return (short)((Timestamp)object).getTime();
            }
            case 0:
            case 1: {
                return null;
            }
            case 16: {
                final String stringVal = (String)object;
                if (StringUtil.isNotEmptyString(stringVal)) {
                    return Short.valueOf(stringVal);
                }
                return null;
            }
            default: {
                typeProblem(type, 5, true);
                return null;
            }
        }
    }
    
    public static String toString(final Object object) {
        if (object == null) {
            return null;
        }
        return String.valueOf(object);
    }
    
    public static Timestamp toTimestamp(final Object value) {
        if (value == null) {
            return null;
        }
        long time = 0L;
        if (value instanceof Timestamp) {
            return (Timestamp)value;
        }
        if (value instanceof Time) {
            time = ((Time)value).getTime();
        }
        else if (value instanceof Date) {
            time = ((Date)value).getTime();
        }
        return new Timestamp(time);
    }
    
   
    
    public static SerialBlob toBlob(final Object value) {
        SerialBlob blob;
        try {
            if (value instanceof SerialBlob) {
                return (SerialBlob)value;
            }
            if (value instanceof byte[]) {
                return new SerialBlob((byte[])value);
            }
            if (value instanceof Blob) {
                return new SerialBlob((Blob)value);
            }
            if (value instanceof InputStream) {
                final InputStream stream = (InputStream)value;
                final byte[] b = new byte[stream.available()];
                stream.read(b);
                blob = new SerialBlob(b);
                return blob;
            } else if (value == null) {
            	 blob = new SerialBlob(ConvertUtil.EMPTYBYTES);
            } else {
            	 blob = new SerialBlob(value.toString().getBytes());
            }
        }
        catch (Exception e) {
            throw new RuntimeException("");
        }
        return blob;
    }
    
    public static SerialClob toClob(final Object value) {
        SerialClob clob;
        try {
            if (value instanceof SerialClob) {
                return (SerialClob)value;
            }
            if (value instanceof Clob) {
                clob = new SerialClob((Clob)value);
                return clob;
            } else if (value == null) {
            	clob = new SerialClob(ConvertUtil.EMPTYCHARS);
            } else {
            	 clob = new SerialClob(value.toString().toCharArray());
            }
        }
        catch (Exception e) {
            throw new RuntimeException("E0208.0009");
        }
        return clob;
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> T toEnum(final Object obj, final Class clazz) {
        try {
            if (obj instanceof String) {
                final String name = (String) obj;
                if (name.length() == 0) {
                    return null;
                }
                return (T) Enum.valueOf(clazz, name);
            } else if (obj instanceof Number) {
                final int ordinal = ((Number)obj).intValue();
                final Method method = clazz.getMethod("values", (Class<?>[])new Class[0]);
                final Object[] values = (Object[])method.invoke(null, new Object[0]);
                for (final Object value : values) {
                    final Enum<?> e = (Enum<?>) value;
                    if (e.ordinal() == ordinal) {
                        return (T) e;
                    }
                }
            }
        }
        catch (Exception e) {
            throw new RuntimeException("can not cast to : {0}", e);
        }
        throw new RuntimeException("can not cast to : {0}");
    }
    
    @SuppressWarnings("unchecked")
	public static Object toClassObject(final Object instance, final Class<?> targetClass) throws FindException {
        if (instance == null) {
            if (targetClass.isPrimitive()) {
                return ClassUtil.getPrimitiveTypeDefaultObject(targetClass);
            }
            return null;
        }
        else {
            final Class<?> srcClass = instance.getClass();
            if (srcClass == targetClass) {
                return instance;
            }
            if (targetClass == Class.class && instance instanceof String) {
                try {
                    return Class.forName(toString(instance));
                }
                catch (ClassNotFoundException e) {
                    throw new FindException("E0208.0023", e);
                }
            }
            final int type = getType(targetClass);
            switch (type) {
                case 21: {
                    return toEnum(instance, (Class<Object>)targetClass);
                }
                case 4: {
                    return toInteger(instance);
                }
                case 2: {
                    return toByte(instance);
                }
                case 3: {
                    return toShort(instance);
                }
                case 6: {
                    return toFloat(instance);
                }
                case 7: {
                    return toDouble(instance);
                }
                case 5: {
                    return toLong(instance);
                }
                case 10: {
                    return toBigDecimal(instance);
                }
                case 11: {
                    return toBoolean(instance);
                }
                case 16: {
                    return toString(instance);
                }
                case 14: {
                }
                case 13: {
                    return toDate(instance);
                }
                case 15: {
                    return toTimestamp(instance);
                }
                case 18: {
                    return toByteArray(instance);
                }
                case 17: {
                    return instance;
                }
                case 12: {
                    return toInputStream(instance);
                }
                case 19: {
                    return toBlob(instance);
                }
                case 20: {
                    return toClob(instance);
                }
                default: {
                    return instance;
                }
            }
        }
    }
    
    public static Class<?> sqlTypeToJavaType(final int sqlType) {
        Class<?> result = null;
        switch (sqlType) {
            case 12: {
                result = String.class;
                break;
            }
            case 1: {
                result = String.class;
                break;
            }
            case -5: {
                result = BigInteger.class;
                break;
            }
            case 2004: {
                result = SerialBlob.class;
                break;
            }
            case 2005: {
                result = SerialClob.class;
                break;
            }
            case 16: {
                result = Boolean.class;
                break;
            }
            case 2:
            case 3:
            case 6:
            case 8: {
                result = BigDecimal.class;
                break;
            }
            case 4: {
                result = Integer.class;
                break;
            }
            case 91: {
                result = Date.class;
                break;
            }
            case 92: {
                result = Time.class;
                break;
            }
            case 93: {
                result = Timestamp.class;
                break;
            }
            default: {
                result = String.class;
                break;
            }
        }
        return result;
    }
    
    public static String getObjectToString(final Object object) {
        if (object == null) {
            return null;
        }
        final Class<?> cls = object.getClass();
        if (cls == Short.TYPE || cls == Short.class || cls == Integer.TYPE || cls == Integer.class) {
            return String.valueOf(object);
        }
        if (cls == Long.TYPE || cls == Long.class || cls == Double.TYPE || cls == Double.class) {
            return String.valueOf(object);
        }
        if (cls == Float.TYPE || cls == Float.class || cls == BigDecimal.class) {
            return String.valueOf(object);
        }
        if (cls == BigInteger.class || cls == Boolean.TYPE || cls == Boolean.class) {
            return String.valueOf(object);
        }
        if (cls == Byte.TYPE || cls == Byte.class || cls == String.class) {
            return String.valueOf(object);
        }
        if (cls == Date.class) {
            return String.valueOf(((Date)object).getTime());
        }
        if (cls == java.sql.Date.class) {
            return String.valueOf(((java.sql.Date)object).getTime());
        }
        if (cls == Timestamp.class) {
            return String.valueOf(((Timestamp)object).getTime());
        }
        return null;
    }
    
    @SuppressWarnings("deprecation")
	public static Object getObject(final String type, final String value) {
        if (StringUtil.isNotEmptyString(type)) {
            if (Boolean.class.getName().toUpperCase().equals(type.toUpperCase())) {
                return Boolean.valueOf(value);
            }
            if (Integer.class.getName().toUpperCase().equals(type.toUpperCase())) {
                return Integer.valueOf(value);
            }
            if (BigDecimal.class.getName().toUpperCase().equals(type.toUpperCase())) {
                return new BigDecimal(value);
            }
            if (Date.class.getName().toUpperCase().equals(type.toUpperCase())) {
                return new Date(value);
            }
            if (java.sql.Date.class.getName().toUpperCase().equals(type.toUpperCase())) {
                return new java.sql.Date(new Date(value).getTime());
            }
            if (Timestamp.class.getName().toUpperCase().equals(type.toUpperCase())) {
                return Timestamp.valueOf(value);
            }
            if (String.class.getName().toUpperCase().equals(type.toUpperCase())) {
                return value;
            }
        }
        return null;
    }
    
    public static boolean checkComparable(final Object object) {
        return object != null && Comparable.class.isAssignableFrom(object.getClass());
    }
    
    public static boolean checkCollection(final Object object) {
        return object != null && Collection.class.isAssignableFrom(object.getClass());
    }
    
    public static int javaTypeToSqlType(final Class<?> cls) {
        if (cls.isEnum()) {
            return 4;
        }
        final Integer result = (Integer) ConvertUtil.typeMap.get(cls);
        if (result == null) {
            return 12;
        }
        return result;
    }
    
    public static <T> T mapToObject(final Map<String, Object> map, final Class<T> beanClass) {
        if (map == null) {
            return null;
        }
        T obj = null;
        try {
            obj = beanClass.newInstance();
//            final Class<?> clazz = (Class<?>)ClassUtil.getClass("org.apache.commons.beanutils.BeanUtils");
//            ClassUtil.callMethod(clazz, "populate", new Object[] { obj, map });
        }
        catch (Throwable e) {
            throw new RuntimeException("mapToObject");
        }
        return obj;
    }
    
    public static Map<?, ?> objectToMap(final Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            final Class<?> clazz = ClassUtil.getClass("org.apache.commons.beanutils.BeanMap");
            return (Map<?, ?>) clazz.getConstructor(Object.class).newInstance(obj);
        }
        catch (Throwable e) {
        }
		return typeMap;
    }
    
    /**
     * 转换为Long数组<br>
     * 
     * @param str 被转换的值
     * @return 结果
     */
    public static Long[] toLongArray(String str)
    {
        return toLongArray(str, ",");
    }
    
    /**
     * 转换为Long数组<br>
     * @param str 被转换的值
     * @param split 分隔符
     * @return 结果
     */
    public static Long[] toLongArray(String str, String split) {
        if (StringUtil.isEmpty(str))
        {
            return new Long[] {};
        }
        String[] arr = str.split(split);
        final Long[] longs = new Long[arr.length];
        for (int i = 0; i < arr.length; i++)
        {
            final Long v = toLong(arr[i]);
            longs[i] = v;
        }
        return longs;
    }
}

