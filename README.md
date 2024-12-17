# 企业管理云平台

![Java](https://img.shields.io/badge/Java-8-blue.svg)
![IDE](https://img.shields.io/badge/IDE-IntelliJ%20IDEA-brightgreen.svg)
![License](https://img.shields.io/badge/License-Apache2-orange.svg)
[![CircleCI](https://circleci.com/gh/alibaba/spring-cloud-alibaba/tree/2023.x.svg?style=svg)](https://circleci.com/gh/alibaba/spring-cloud-alibaba/tree/2023.x)
[![Maven Central](https://img.shields.io/maven-central/v/com.alibaba.cloud/spring-cloud-alibaba-dependencies.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:com.alibaba.cloud%20AND%20a:spring-cloud-alibaba-dependencies)
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![actions](https://github.com/alibaba/spring-cloud-alibaba/workflows/Integration%20Testing/badge.svg)](https://github.com/alibaba/spring-cloud-alibaba/actions)

> System : Windows 10
>
> Intellij IDEA : Ultimate 2020.3.4
>
> Java : 1.8.0_171
>
> MySQL : 8.0.30

#### 介绍
企业管理云平台（Enterprise Cloud Platform,缩写为：ECP）打造适合中国企业综合管理平台框架、采用云计算SAAS平台利用高速化的互联网传输能力，为软件开发商搭建一个高效、灵活的软件架构；同时根据需求智能分配计算资源，满足中小企业日益增长的信息化管理要求，也为大型集团企业提供高效、安全、稳定的一站式专业级软件应用服务。

#### 软件架构
软件架构说明
<p align="center">
	<img src="architecture.png" />
</p>


#### Abstract:

MQTT is a Client Server publish/subscribe messaging transport protocol. It is light weight, open,
simple, and designed so as to be easy to implement. These characteristics make it ideal for use
in many situations, including constrained environments such as for communication in Machine to
Machine (M2M) and Internet of Things (IoT) contexts where a small code footprint is required
and/or network bandwidth is at a premium.

#### Vosk 

https://alphacephei.com/vosk/

Vosk is a speech recognition toolkit. The best things in Vosk are:

Supports 20+ languages and dialects - English, Indian English, German, French, Spanish, Portuguese, Chinese, Russian, Turkish, Vietnamese, Italian, Dutch, Catalan, Arabic, Greek, Farsi, Filipino, Ukrainian, Kazakh, Swedish, Japanese, Esperanto, Hindi, Czech, Polish, Uzbek, Korean, Breton, Gujarati, Tajik. More to come.
Works offline, even on lightweight devices - Raspberry Pi, Android, iOS
Installs with simple pip3 install vosk
Portable per-language models are only 50Mb each, but there are much bigger server models available.
Provides streaming API for the best user experience (unlike popular speech-recognition python packages)
There are bindings for different programming languages, too - java/csharp/javascript etc.
Allows quick reconfiguration of vocabulary for best accuracy.
Supports speaker identification beside simple speech recognition.

#### Models

We have two types of models - big and small, small models are ideal for some limited task on mobile applications. They can run on smartphones, Raspberry Pi’s. They are also recommended for desktop applications. Small model typically is around 50Mb in size and requires about 300Mb of memory in runtime. Big models are for the high-accuracy transcription on the server. Big models require up to 16Gb in memory since they apply advanced AI algorithms. Ideally you run them on some high-end servers like i7 or latest AMD Ryzen. On AWS you can take a look on c5a machines and similar machines in other clouds.

Most small model allow dynamic vocabulary reconfiguration. Big models are static the vocabulary can not be modified in runtime.

#### 安装教程

1.  基于  apache maven deploy 进行自行发布
2.  xxxx
3.  xxxx

#### 使用说明

1.  xxxx
2.  xxxx
3.  xxxx
基于 Spring cloud
采用  Drools（JBoss Rules Engine) , Activiti Flowable 工作流引擎

#### 参与贡献

1.  Fork 本仓库
2.  新建 Feat_xxx 分支
3.  提交代码
4.  新建 Pull Request


#### 特技

1.  使用 Readme\_XXX.md 来支持不同的语言，例如 Readme\_en.md, Readme\_zh.md
2.  Gitee 官方博客 [blog.gitee.com](https://blog.gitee.com)
3.  你可以 [https://gitee.com/explore](https://gitee.com/explore) 这个地址来了解 Gitee 上的优秀开源项目
4.  [GVP](https://gitee.com/gvp) 全称是 Gitee 最有价值开源项目，是综合评定出的优秀开源项目
5.  Gitee 官方提供的使用手册 [https://gitee.com/help](https://gitee.com/help)
6.  Gitee 封面人物是一档用来展示 Gitee 会员风采的栏目 [https://gitee.com/gitee-stars/](https://gitee.com/gitee-stars/)
