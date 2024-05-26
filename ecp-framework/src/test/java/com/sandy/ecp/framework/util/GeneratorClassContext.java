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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.springframework.cglib.beans.BeanGenerator;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.ArrayMemberValue;
import javassist.bytecode.annotation.StringMemberValue;

public class GeneratorClassContext<ID> {

	public void add() {
		BeanGenerator generator = new BeanGenerator();
//		generator.setSuperclass(AbstractDateVO.class);
		generator.setUseCache(true);
		generator.addProperty("clas", String.class);
		System.out.println(generator.create());
	}
	
	public void test () throws CannotCompileException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
		
		ClassPool pool = ClassPool.getDefault();
		CtClass cc = pool.makeClass("com.sandy.workflow.vo.MyClass");
		CtField field = CtField.make("private String name;", cc);
		cc.addField(field);
		
		field = new CtField(CtClass.intType, "myField", cc);
		cc.addField(field);
		
		field = new CtField(CtClass.charType, "myFie", cc);
		cc.addField(field);
		
		CtMethod method = CtNewMethod.make("public void sayHello(){ System.out.println(\"Hello, world!\"); }", cc);
		MethodInfo methodInfo = method.getMethodInfo();
		ConstPool constPool = methodInfo.getConstPool();

		//要添加的注解
        AnnotationsAttribute methodAttr = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
        Annotation methodAnnot = new Annotation("org.springframework.kafka.annotation.KafkaListener", constPool);

        // 添加方法注解
        StringMemberValue[] elements = {new StringMemberValue(String.join("-", "elk-event", "env"), constPool)};
        ArrayMemberValue amv = new ArrayMemberValue(constPool);
        amv.setValue(elements);
        methodAnnot.addMemberValue("topics", amv);
        methodAnnot.addMemberValue("groupId", new StringMemberValue("bury-point-fat", constPool));
        methodAttr.addAnnotation(methodAnnot);
        methodInfo.addAttribute(methodAttr);

		cc.addMethod(method);
		Class<?> clazz = cc.toClass();
		// 创建对象并调用方法
		Object obj = clazz.newInstance();
		System.out.println(obj);
		Method m = clazz.getDeclaredMethod("sayHello");
		m.invoke(obj);
	}
	
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException, CannotCompileException {
		new GeneratorClassContext<>().test();
	}
}
