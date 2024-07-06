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
package com.sandy.ecp.rule.test;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.sandy.ecp.rule.context.EcpRuleService;

/**
 * ECP Book Discount Test
 * @author Sandy
 * 
 */
@RunWith(MockitoJUnitRunner.class)
public class EcpBookDiscountTest {
	
	private final KieServices kieServices = KieServices.Factory.get();
	
	@Mock
	private EcpRuleService ecpRuleService;
	
	@Before
	public void brfore() {
		if (this.ecpRuleService == null) {
			this.ecpRuleService = new EcpRuleService();
		}
		System.out.println("before rule engine book discount test");
	}

    @Test
    @org.junit.Test
    public void buyBookTest() {
        // 获取Kie容器对象（默认容器对象
        KieContainer kieContainer = kieServices.newKieClasspathContainer();
        // 从Kie容器对象中获取会话对象（默认session对象
        KieSession kieSession = kieContainer.newKieSession();
        OrderTestRule order = new OrderTestRule();
        order.setOriginalPrice(160d);
 
        // 将order对象插入工作内存
        kieSession.insert(order);
        System.out.println("匹配规则前优惠后价格：" + order.getRealPrice());
        // 匹配对象
        // 激活规则，由drools框架自动进行规则匹配。若匹配成功，则执行
        kieSession.fireAllRules();
        // 关闭会话
        kieSession.dispose();
 
        System.out.println("优惠前价格：" + order.getOriginalPrice() + "\n优惠后价格：" + order.getRealPrice());
    }
}
