package com.sandy.ecp.mybatis.config;

/**
 * 重写SqlSessionFactoryBean就的修改下Spring的MyBatis配置部分：
 *	最后附加上属性配置文件：mybatis-refresh.properties
 *	#是否开启刷新线程
 *	enabled=true
 *	#延迟启动刷新程序的秒数
 *	delaySeconds=60
 *	#刷新扫描间隔的时长秒数
 *	sleepSeconds=3
 *	#扫描Mapper文件的资源路径
 *	mappingPath=mappings
 */
public class MybatisConfig {

	
	
	
	
}
