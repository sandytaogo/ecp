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
package com.sandy.ecp.media; 
 
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import ws.schild.jave.process.ProcessWrapper;
import ws.schild.jave.process.ffmpeg.DefaultFFMPEGLocator;
/**  
 * Description:  
 * @author Sandy
 * @Date 2024年3月23日 下午6:54:55
 * @since 1.0.0
 */
public class FfmpegTransformM3u8 {
    // 输入文件夹
    private static String inputDirectory = "C:\\Users\\Sandy\\Downloads";
    // 输出m3u8文件
    private static String outputM3u8FileName = "index.m3u8";
    // 判断是否是视频文件正则
    private static String isVideoFileRegex = ".*\\.(mp4|avi|mkv)$";
    // 选择切片时间
    private static Integer HlsTime = 1;

    /**
     * 定义目录所在位置
     * 循环遍历这个目录文件，找到子目录的视频文件
     * 判断当前的文件是否是视频文件
     * 如果是文件夹则继续遍历
     * @param args main program arguments
     */
    public static void main(String[] args) {
        File directory = new File(inputDirectory);
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            assert files != null;
            for (File file : files) {
                if (file.isFile() && isVideoFile(file.getName())) {
                    String inputFilePath = file.getAbsolutePath();
                    new Thread(() -> convertToTs(inputFilePath)).start();
                }
            }
        }
    }

    /**
     * 如果文件名 后缀符合将文件先转成ts类型文件
     * 有实验表名，先转成ts会快一点
     *
     * @param inputFilePath 当前文件的路径
     */
    private static void convertToTs(String inputFilePath) {
        String outputDirectory = inputFilePath.replaceFirst("\\.[^.]+$", "");
        File directory = new File(outputDirectory);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String outputFilePath = outputDirectory + File.separator + "output.ts";
        String command = "ffmpeg -i " + inputFilePath + " -c copy -bsf:v h264_mp4toannexb -force_key_frames expr:gte(t,n_forced*" + HlsTime + ") -f mpegts " + outputFilePath;
        executeFfmpegCommandUnLib(command);
        convertToM3U8(outputDirectory);
    }

    /**
     * 将文件转成hls类型，并且m3u8命名为index
     * 切片的ts以output.ts转换
     *
     * @param inputDirectory 输入的文件夹
     */
    private static void convertToM3U8(String inputDirectory) {
        // 使用FFmpeg将ts文件转换成m3u8文件
        String outputFilePath = inputDirectory + File.separator + outputM3u8FileName;
        String command = "ffmpeg -i " + inputDirectory + File.separator + "output.ts" + " -c:v libx264 -c:a aac -force_key_frames expr:gte(t,n_forced*" + HlsTime + ") -hls_time " + HlsTime + " -hls_list_size 0 -f hls " + outputFilePath;
        executeFfmpegCommandUnLib(command);
    }
    
    /**
     * 执行将文件转成m3u8 hls格式
     * @param command Ffmpeg 命令
     * @throws IOException 
     */
    private static void executeFfmpegCommandUnLib(String command) {
		ProcessWrapper ffmpeg = new DefaultFFMPEGLocator().createExecutor();
		String[] args = command.split(" ");
		for (String arg : args) {
			if ("ffmpeg".equals(arg)) {
				continue;
			}
			ffmpeg.addArgument(arg);
		}
		try {
			ffmpeg.execute();
			try (BufferedReader br = new BufferedReader(new InputStreamReader(ffmpeg.getErrorStream()))) {
				blockFfmpeg(br);
			}
			File file = new File(inputDirectory + "\\finish.txt");
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    private static void blockFfmpeg(BufferedReader br) throws IOException {
		String line;
		// 该方法阻塞线程，直至合成成功
		while ((line = br.readLine()) != null) {
			doNothing(line);
		}
	}

	private static void doNothing(String line) {
		System.out.println(line);
	}

    /**
     * 执行将文件转成m3u8 hls格式
     *
     * @param command Ffmpeg 命令
     */
    protected static void executeFfmpegCommand(String command) {
        ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
        processBuilder.redirectErrorStream(true);
        try {
            Process process = processBuilder.start();
            // 将ffmpeg执行内容输出在控制台中
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            // 等待转换完成
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 判断是否符合想要的格式
     * mp4|avi|mkv
     * 通过文件后缀名哦安短
     *
     * @param fileName 文件名称
     * @return 布尔值
     */
    private static boolean isVideoFile(String fileName) {
        return fileName.matches(isVideoFileRegex);
    }
}

