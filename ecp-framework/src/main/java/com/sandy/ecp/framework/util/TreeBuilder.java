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

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

/**
 * Tree (view object)
 * @author Sandy 060219-062193
 * @Since 2.0.0 17th 06 2018  
 */
public class TreeBuilder {

	@SuppressWarnings("unchecked")
	public static <T> T bindTree(List<? extends Node> list) {
		List<Node> rootlist = loadRoot(list);
		list = subtract(list, rootlist); 
		for (Node item : rootlist) {
			item .setChildren(recursion(item, list));
		}
		return (T) rootlist;
	}
	
	protected static List<Node> loadRoot(List<? extends Node> list) {
		List<Node> rootlist = new ArrayList<Node>();
		List<?> tempList = list.stream().map(Node::getId).collect(Collectors.toList());
		for (Node item : list) {
			if (!tempList.contains(item.getPid())) {
				rootlist.add(item);
			}
		}
		return rootlist;
	}
	
	protected static List<Node> subtract(List<? extends Node> list, List<? extends Node> useList) { 
		List<Node> unUseList = new ArrayList<Node>(list);
		ListIterator<?> iterator = useList.listIterator();
		while (iterator.hasNext()) {
			unUseList.remove(iterator.next());
		}
		return unUseList;
	}
	
	protected static List<Node> recursion(Node rootNode, List<? extends Node> list) {
		List<Node> children = new ArrayList<Node>();
		for (Node item : list) {
			if (null != item.getPid() && item.getPid().equals(rootNode.getId())) {
				children.add(item);
			}
		}
		list = subtract(list, children);
		for (Node item : children) {
			item.setChildren(recursion(item, list));
		}
		return children;
	}
}
