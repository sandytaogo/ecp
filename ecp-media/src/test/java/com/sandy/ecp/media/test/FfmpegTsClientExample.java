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
package com.sandy.ecp.media.test;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class FfmpegTsClientExample {

	public static void main(String[] args) throws IOException {
		int counter = 0;
		do {
			counter ++;
			Socket socket = new Socket("127.0.0.1", 3000);
			InputStream is = socket.getInputStream();
			is.read();
			System.out.println("over");
			socket.close();
		} while (counter < 3);
		
	}
}
