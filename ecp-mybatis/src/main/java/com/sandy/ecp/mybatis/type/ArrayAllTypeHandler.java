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
package com.sandy.ecp.mybatis.type; 

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.Calendar;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeException;
import org.springframework.util.StringUtils;

/**
 * mybatis 数据处理自定义实现.
 * @author Sandy
 * @date 2013-07-23 15:04:23
 */
@MappedTypes({Object.class})
@MappedJdbcTypes({JdbcType.ARRAY})
public class ArrayAllTypeHandler extends BaseTypeHandler<Object[]> {
    
	private static final ConcurrentHashMap<Class<?>, String> STANDARD_MAPPING = new ConcurrentHashMap<Class<?>, String>();

    public ArrayAllTypeHandler() {
    	super();
    }

    public void setNonNullParameter(PreparedStatement ps, int i, Object[] parameter, JdbcType jdbcType) throws SQLException {
        Class<?> componentType = parameter.getClass().getComponentType();
        String arrayTypeName = this.resolveTypeName(componentType);
        if (StringUtils.isEmpty(arrayTypeName)) {
            throw new TypeException("ArrayType Handler requires SQL array or java array parameter and does not support type " + parameter.getClass());
        } else {
        	if (JdbcType.VARCHAR.equals(jdbcType)) {
        		StringBuilder sb = new StringBuilder();
        		if (parameter != null && 0 < parameter.length) {
        			for (Object item : parameter) {
        				sb.append(item).append(",");
        			}
        			sb.delete(sb.length() - 1, sb.length());
        		}
        		ps.setObject(i, sb.toString(), java.sql.Types.VARCHAR, 255);
        	} else {
              Array array = ps.getConnection().createArrayOf(arrayTypeName, parameter);
              ps.setArray(i, array);
              array.free();
        	}
        }
    }

    protected String resolveTypeName(Class<?> type) {
        return (String)STANDARD_MAPPING.getOrDefault(type, JdbcType.JAVA_OBJECT.name());
    }

    public Object[] getNullableResult(ResultSet rs, String columnName) throws SQLException {
    	if ("SYMBOLS".equals(columnName)) {
    		String value = rs.getString(columnName);
    		if (value == null) {
    			return new String []{};
    		}
    		return value.split(",");
    	}
        return this.extractArray(rs.getArray(columnName));
    }

    public Object[] getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return this.extractArray(rs.getArray(columnIndex));
    }

    public Object[] getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return this.extractArray(cs.getArray(columnIndex));
    }

    protected Object[] extractArray(Array array) throws SQLException {
        if (array == null) {
            return null;
        } else {
            try {
                return (Object[])((Object[])array.getArray());
            } catch (Exception var3) {
                return null;
            }
        }
    }

    static {
        STANDARD_MAPPING.put(BigDecimal.class, JdbcType.NUMERIC.name());
        STANDARD_MAPPING.put(BigInteger.class, JdbcType.BIGINT.name());
        STANDARD_MAPPING.put(Boolean.TYPE, JdbcType.BOOLEAN.name());
        STANDARD_MAPPING.put(Boolean.class, JdbcType.BOOLEAN.name());
        STANDARD_MAPPING.put(byte[].class, JdbcType.VARBINARY.name());
        STANDARD_MAPPING.put(Byte.TYPE, JdbcType.TINYINT.name());
        STANDARD_MAPPING.put(Byte.class, JdbcType.TINYINT.name());
        STANDARD_MAPPING.put(Calendar.class, JdbcType.TIMESTAMP.name());
        STANDARD_MAPPING.put(Date.class, JdbcType.DATE.name());
        STANDARD_MAPPING.put(java.util.Date.class, JdbcType.TIMESTAMP.name());
        STANDARD_MAPPING.put(Double.TYPE, JdbcType.DOUBLE.name());
        STANDARD_MAPPING.put(Double.class, JdbcType.DOUBLE.name());
        STANDARD_MAPPING.put(Float.TYPE, JdbcType.REAL.name());
        STANDARD_MAPPING.put(Float.class, JdbcType.REAL.name());
        STANDARD_MAPPING.put(Integer.TYPE, JdbcType.INTEGER.name());
        STANDARD_MAPPING.put(Integer.class, JdbcType.INTEGER.name());
        STANDARD_MAPPING.put(LocalDate.class, JdbcType.DATE.name());
        STANDARD_MAPPING.put(LocalDateTime.class, JdbcType.TIMESTAMP.name());
        STANDARD_MAPPING.put(LocalTime.class, JdbcType.TIME.name());
        STANDARD_MAPPING.put(Long.TYPE, JdbcType.BIGINT.name());
        STANDARD_MAPPING.put(Long.class, JdbcType.BIGINT.name());
        STANDARD_MAPPING.put(OffsetDateTime.class, JDBCType.TIMESTAMP_WITH_TIMEZONE.name());
        STANDARD_MAPPING.put(OffsetTime.class, JDBCType.TIME_WITH_TIMEZONE.name());
        STANDARD_MAPPING.put(Short.class, JdbcType.SMALLINT.name());
        STANDARD_MAPPING.put(String.class, JdbcType.VARCHAR.name());
        STANDARD_MAPPING.put(Time.class, JdbcType.TIME.name());
        STANDARD_MAPPING.put(Timestamp.class, JdbcType.TIMESTAMP.name());
        STANDARD_MAPPING.put(URL.class, JdbcType.DATALINK.name());
    }
}

