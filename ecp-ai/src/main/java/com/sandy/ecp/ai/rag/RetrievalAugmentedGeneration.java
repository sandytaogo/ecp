/*
 * Copyright 2023-2040 the original author or authors.
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
package com.sandy.ecp.ai.rag;

import com.sandy.ecp.ai.context.AbstractGeneration;

/**
 *RAG 的全称是 Retrieval-Augmented Generation，检索增强生成。
 *结合前面的分析，问题可以总结为：
 *大模型懂语义，但没有企业私有知识；
 *传统搜索有数据，但不懂自然语言表达；
 *企业应用既需要理解用户问题，又需要基于可靠资料回答。
 *RAG 的思路就是把两者结合起来：先从知识库中检索相关资料，再把检索结果交给大模型生成回答。
 *为 LLM 提供了从某些数据源检索到的信息，并基于此修正生成的答案。RAG 基本上是 Search + LLM 提示，可以通过大模型回答查询，并将搜索算法所找到的信息作为大模型的上下文。查询和检索到的上下文都会被注入到发送到 LLM 的提示语中。
 *嵌入式搜索引擎可以通过 Faiss 来实现，向量搜索领域成为了RAG的一个助力。像pinecone 这样的向量数据库可以构建开源搜索索引，为输入文本增加了额外的存储空间，还增加了一些其他工具。关于向量数据库，可以参考解读向量数据库。
 *面向RAG的开发框架，对于基于 LLM 的流水线和应用程序，有两个最著名的开源工具—— LangChain 和 LlamaIndex，分别是在2022年10月和11月创建的，随着 ChatGPT 爆发，也在2023年获得了大量采用。LlamaIndex 和 LangChain 都是令人惊叹的开源项目，它们的发展速度非常快。
 *plain
 *ingest → chunk → embed → index → retrieve → answer
 */
public class RetrievalAugmentedGeneration extends AbstractGeneration {
	
	
	
	
	
}
