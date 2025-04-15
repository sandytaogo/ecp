/*
 * Copyright 2024-2030 the original author or authors.
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

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
 
/**
 * mybatis 数据处理boolean 转换 bit.
 * @author Sandy
 * @date 2024-07-23 15:04:23
 */
@MappedTypes(Boolean.class)
@MappedJdbcTypes(JdbcType.BIT)
public class BooleanToBitTypeHandler implements TypeHandler<Boolean> {
	
	public BooleanToBitTypeHandler() {
		super();
	}
	
    @Override
    public void setParameter(PreparedStatement ps, int i, Boolean parameter, JdbcType jdbcType) throws SQLException {
        ps.setByte(i, parameter ? (byte) 1 : (byte) 0); // true -> 1, false -> 0
    }
 
    @Override
    public Boolean getResult(ResultSet rs, String columnName) throws SQLException {
        byte val = rs.getByte(columnName); // 获取字节值并转换为Boolean
        return val == 1; // 1 -> true, 0 -> false
    }

	@Override
	public Boolean getResult(ResultSet rs, int columnIndex) throws SQLException {
		byte val = rs.getByte(columnIndex);
		return val == 1;
	}

	@Override
	public Boolean getResult(CallableStatement cs, int columnIndex) throws SQLException {
		byte val = cs.getByte(columnIndex);
		return val == 1;
	}
}
