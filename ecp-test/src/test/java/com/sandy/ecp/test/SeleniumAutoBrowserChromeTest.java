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
package com.sandy.ecp.test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * 浏览器自动化测试单元用例.
 * @author Sandy
 * @since 1.0.0 2024-11-11 12:12:12
 */
public class SeleniumAutoBrowserChromeTest {
	
	
    public static void main(String[] args) {
        // 设置webdriver路径
//        System.setProperty("webdriver.gecko.driver", "H:\\program\\java\\jdk1.8.0_421\\bin\\chromedriver.exe");
    	ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");     // 允许所有的请求
        options.addArguments("--allowed-ips=172.18.64.1");
//        options.setExperimentalOption("debuggerAddress", "172.18.64.1");
        // 初始化一个新的Firefox浏览器实例
        WebDriver driver = new ChromeDriver(options);
     // specify the browser - no javascript support
//        WebDriver driver = new HtmlUnitDriver(BrowserVersion.CHROME);
        // 打开网页
        driver.get("https://www.baidu.com/");
        // 获取页面标题
        String title = driver.getTitle();
        System.out.println("Page title is: " + title);
       
        WebElement element = driver.findElement(By.id("kw"));
        element.sendKeys("测试");
        dely(3000);
        element = driver.findElement(By.id("su"));
        element.click();
        dely(8000);
        // 关闭浏览器
        driver.quit();
    }
    
    public static void dely(long millis) {
    	 try {
 			Thread.sleep(5000);
 		} catch (InterruptedException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}
    }
}
