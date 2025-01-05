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
企业管理云平台（Enterprise Cloud Platform,缩写为：ECP）利用现代人工智能算法技术打造适合中国企业综合管理平台框架、采用云计算SAAS平台利用高速化的互联网传输能力，为软件开发商搭建一个高效、灵活的软件架构；同时根据需求智能分配计算资源，满足中小企业日益增长的信息化管理要求，也为大型集团企业提供高效、安全、稳定的一站式专业级软件应用服务。

#### 软件架构
软件架构说明
<p align="center">
	<img src="architecture.png" />
</p>


#### 基础设施参考模型 Reference model
参考模型是指被用于作为基准和对比的模型，在结构化信息标准促进组织的定义中，它被用于理解某些环境中实体之间的重要关系，以及用于开发支持该环境的一般标准或规范框架。

#### 概念

摘要：参考模型被用于提供有关某种环境的信息，以及描述这种环境下可能发生的实体类型或种类，而不是特定环境下实际发生的实体；
实体和关系：参考模型描述两种类型的实体及其关系；
同环境：参考模型不会试图描述「具体事物」，但会阐明「所处环境」或问题空间；
不确定性：参考模型通常被用于促进对问题的理解，而不是具体的解决方案。

##### 应用

创建模型中对象及相互关系的标准，这样可以让软件的制作、软件的扩展更为容易；
教育领域，软件开发领域的领导者可以将问题分解，以加快处理和改进的效率；
改善人与人之间的沟通能力，通过将问题分解为实体或「存在的事物」提升沟通效率；
创建明确的角色和职责，可以提升团队的整体效率；
用于比较不同的东西，在问题分解为基本概念后，可利用参考模型检查不同解决方案。

#### Abstract:

MQTT is a Client Server publish/subscribe messaging transport protocol. It is light weight, open,
simple, and designed so as to be easy to implement. These characteristics make it ideal for use
in many situations, including constrained environments such as for communication in Machine to
Machine (M2M) and Internet of Things (IoT) contexts where a small code footprint is required
and/or network bandwidth is at a premium.

#### Vosk Recognition 

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

There are five model sizes, four with English-only versions, offering speed and accuracy tradeoffs. Below are the names of the available models and their approximate memory requirements and inference speed relative to the large model; actual speed may vary depending on many factors including the available hardware.


|  Size  | Parameters | English-only model | Multilingual model | Required VRAM | Relative speed |
|:------:|:----------:|:------------------:|:------------------:|:-------------:|:--------------:|
|  tiny  |    39 M    |     `tiny.en`      |       `tiny`       |     ~1 GB     |      ~32x      |
|  base  |    74 M    |     `base.en`      |       `base`       |     ~1 GB     |      ~16x      |
| small  |   244 M    |     `small.en`     |      `small`       |     ~2 GB     |      ~6x       |
| medium |   769 M    |    `medium.en`     |      `medium`      |     ~5 GB     |      ~2x       |
| large  |   1550 M   |        N/A         |      `large`       |    ~10 GB     |       1x       |


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
