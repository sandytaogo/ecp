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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Date;
import java.util.function.Function;

import javax.sql.rowset.serial.SerialClob;
import javax.sql.rowset.serial.SerialException;

import org.junit.Test;

import com.sandy.ecp.framework.func.FunCentent;
import com.sandy.ecp.framework.session.SessionVO;

/**  
 * Description: 
 * @author Sandy
 * @Date 2024年4月3日 下午6:44:23
 * @since 1.0.0
 */
public class ConvertUtilTest {

	@Test
	public void toIntTest() {
		ConvertUtil.toInt(12);
		ConvertUtil.toInt(12D);
		ConvertUtil.toInt("12");
	}
	
	@Test
	public void integerTest() {
		ConvertUtil.toInteger(12);
		ConvertUtil.toInteger(12D);
		ConvertUtil.toInteger("12");
	}
	
	@Test
	public void toBooleanTest() {
		ConvertUtil.toBoolean(0);
		ConvertUtil.toBoolean("true");
		ConvertUtil.toBoolean("false");
		ConvertUtil.toBoolean(false);
		ConvertUtil.toBoolean(true);
		
		ConvertUtil.toBoolean(Boolean.FALSE);
		ConvertUtil.toBoolean(new Float(1));
		ConvertUtil.toBoolean(new Integer(1));
		ConvertUtil.toBoolean(new Double(1));
		ConvertUtil.toBoolean(new BigInteger("1"));
		ConvertUtil.toBoolean(new BigDecimal("1"));
	}
	
	@Test
	public void bigDecimalTest() {
		ConvertUtil.toBigDecimal(12.4F);
		ConvertUtil.toBigDecimal(12.4D);
		ConvertUtil.toBigDecimal((long)12.4);
		ConvertUtil.toBigDecimal(12.4);
		ConvertUtil.toBigDecimal(new BigDecimal("12.4"));
		ConvertUtil.toBigDecimal("12.4");
	}
	
	@Test
	public void objectTest() {
		ConvertUtil.toClassObject(Function.class, FunCentent.class);
		ConvertUtil.toBigDecimal(12.4D);
		ConvertUtil.toBigDecimal((long)12.4);
		ConvertUtil.toBigDecimal(12.4);
		ConvertUtil.toBigDecimal("12.4");
		ConvertUtil.toBigDecimal(new Time(System.currentTimeMillis()));
	}
	@Test
	public void toObjectTest() {
		ConvertUtil.getObjectToString(new SessionVO());
	}
	
	@Test
	public void toFloatTest() {
		ConvertUtil.toFloat(23.f);
		ConvertUtil.toFloat(new BigDecimal("34"));
		ConvertUtil.toFloat(new BigInteger("3"));
		ConvertUtil.toFloat(new Double(4));
	}
	
	@Test
	public void toLongTest() {
		ConvertUtil.toLong(34);
		ConvertUtil.toLong(new BigDecimal("34"));
		ConvertUtil.toLong(new BigInteger("3"));
		ConvertUtil.toLong(new Integer(4));
		ConvertUtil.toLong(new Double(4));
		ConvertUtil.toLong(new Long(4));
	}
	
	@Test
	public void toDoubleTest() {
		ConvertUtil.toDouble(new BigDecimal("34"));
		ConvertUtil.toDouble(new BigInteger("3"));
		ConvertUtil.toDouble(new Double(4));
	}
	
	@Test
	public void toDateTest() {
		ConvertUtil.toDate(new Date());
	}
	
	@Test
	public void toByteTest() {
		ConvertUtil.toByte(new BigDecimal("34"));
		ConvertUtil.toByte(new BigInteger("3"));
		ConvertUtil.toByte(new Integer(4));
		ConvertUtil.toByte(new Short((short) 4));
		ConvertUtil.toByte(new Double(4));
		ConvertUtil.toByte(new Long(4));
	}
	
	@Test
	public void toByteArrayTest() {
		byte [] b = "test".getBytes();
		ConvertUtil.toByteArray(b);
	}
	
	@Test
	public void getObjectTest() {
		ConvertUtil.getObject("Boolean", "true");
		ConvertUtil.getObject("Integer", "1");
		ConvertUtil.getObject("Date", System.currentTimeMillis() + "");
		ConvertUtil.getObject("String", "true");
	}
	
	@Test
	public void serialClobTest() throws SerialException, SQLException {
		char [] chars = "test".toCharArray();
		ConvertUtil.toClob(new SerialClob(chars));
	}
	
	
	 
}
