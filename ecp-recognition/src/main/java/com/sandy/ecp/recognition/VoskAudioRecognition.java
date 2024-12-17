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
package com.sandy.ecp.recognition;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.vosk.Model;
import org.vosk.Recognizer;

/**
 * Vosk is a speech recognition toolkit.
 * @author Sandy
 * @since 1.0.0 2024-12-12 17:12:12
 * @see https://alphacephei.com/
 */
public class VoskAudioRecognition {

	@Autowired(required = false)
	private Environment environment;
	
	private Model model;
	
	/**
	 * 初始化音频转换模型.
	 * @throws IOException
	 */
	public void init() throws IOException {
		this.model = new Model(environment.getProperty("vosk.model.path"));
	}
	
	/**
	 * 音频文件内容 转换文本.
	 * @param audio
	 * @param sampleRate
	 * @return text.
	 * @throws IOException
	 * @throws UnsupportedAudioFileException
	 */
	public String audioToText(String audio, Float sampleRate) throws IOException, UnsupportedAudioFileException {
		InputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(new FileInputStream(audio)));
		if (sampleRate == null) {
			sampleRate = 8000F;
		}
		try (Recognizer recognizer = new Recognizer(model, sampleRate)) {
			int bytes;
			byte[] b = new byte[2048];
			while ((bytes = ais.read(b)) >= 0) {
				recognizer.acceptWaveForm(b, bytes);
			}
			return recognizer.getFinalResult();
		} finally {
			if (ais != null) {
				ais.close();
			}
		}
	}
}
