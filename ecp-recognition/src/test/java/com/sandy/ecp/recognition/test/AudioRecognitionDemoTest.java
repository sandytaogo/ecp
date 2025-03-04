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
package com.sandy.ecp.recognition.test;

import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.vosk.LogLevel;
import org.vosk.Recognizer; 
import org.vosk.LibVosk;
import org.vosk.Model;

public class AudioRecognitionDemoTest {
	
	public static void main(String[] argv) throws IOException, UnsupportedAudioFileException {
		int counter = 0;
		LibVosk.setLogLevel(LogLevel.DEBUG);
		Model model = new Model("G:\\model\\vosk-model-cn-0.22");
		do {
			counter++;
			long start  = System.currentTimeMillis();
			InputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(new FileInputStream("F:\\AudioFile\\1732988773646.wav")));
			Recognizer recognizer = new Recognizer(model, 8000);
			int bytes;
			byte[] b = new byte[4096];
			while ((bytes = ais.read(b)) >= 0) {
				recognizer.acceptWaveForm(b, bytes);
			}
			System.out.println(recognizer.getFinalResult() + System.lineSeparator());
			System.out.println(String.format("TimeMillis=%s毫秒", System.currentTimeMillis() - start));
			recognizer.close();
		} while (counter < 4);
	}

}
