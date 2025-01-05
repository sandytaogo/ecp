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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.sandy.ecp.media.FfmpegCmd;
import com.sandy.ecp.media.pool.MediaThreadPoolExecutor;

/**
 * 优点：各种浏览器，手机，小程序都能兼容，通用性很好。 缺点：公开的算法，还原也十分简单，有很多影音嗅探工具能直接下载还原，加密效果很弱，
 * ①新建一个记事本，取名enc.key（名字可以随便取），添加16个字节的自定义的AES128加密的密匙，如：hsjissmart123456
 * ②新建一个文件，enc.keyinfo，添加如下内容（注意：enc.keyinfo里面的enc.key路径绝对路径）
 * http://localhost/enc.key
 * ③这里需要把enc.key和enc.keyinfo放在同一目录下
 * @Description:(MP4 转码 HLS m3u8 AES128 加密)
 * @Copyright:
 * @author Sandy
 * @Date 2024年3月23日 下午10:21:53
 * @since 1.0.0
 */
public class FfmpegCmdHls4M3u8EncTest {
	
	private static String ENC_DIRECTORY = "C:\\Users\\Sandy\\Downloads\\lxtzaqbjp";
	
	private static ExecutorService pool = new MediaThreadPoolExecutor(MediaThreadPoolExecutor.core,//核心
			MediaThreadPoolExecutor.core,//最大
	        0L,//空闲立即退出
	        TimeUnit.MILLISECONDS,
	        new LinkedBlockingQueue<Runnable>(1024),//无边界阻塞队列
	        new ThreadPoolExecutor.AbortPolicy());
	
	//执行成功0,失败1
	private static int CODE_SUCCESS = 0;
	private static int CODE_FAIL = 1;
	
	/**
	 * ffmpeg -y -i 1.mp4 -c:v libx264 -c:a copy -f hls -hls_time 5 -hls_list_size 0 -hls_key_info_file aa.keyinfo -hls_playlist_type vod -hls_segment_filename part%d.ts part.m3u8
	 */
	private static String cmd_enc = " -y -i C:\\Users\\Sandy\\Downloads\\lxtzaqbjp.mp4 -hls_time 15 -hls_key_info_file $encInfoPath -hls_playlist_type vod -hls_segment_filename $encPath\\chunkfile_15s_%3d.ts $encPath\\index.m3u8";

	/**
	 * 第一步：创建enc.keyinfo文件
	 * 第二步：HLS m3u8 AES128 加密
	 * @param: @param args      
	 * @return: void      
	 * @throws
	 */
    public static void main(String[] args) {
	  
	    //异步执行
    	//第一步：创建enc.keyinfo文件等
    	CompletableFuture<String> completableFutureTask = CompletableFuture.supplyAsync(() ->{	
    		//创建enc.keyinfo文件,返回文件地址
		    String encKeyInfoFilePath = null;
		    //目录enc
			File encFilePathDir = new File(ENC_DIRECTORY);
            if (!encFilePathDir.exists()) {// 判断目录是否存在
            	encFilePathDir.mkdir();   
            }
		    //写入文件内容enc.key
            BufferedWriter bwkey = null;
		    //写入文件内容enc.keyinfo
            BufferedWriter bwkeyInfo = null;
            
            try{//文件
            	String encKeyFilePath = ENC_DIRECTORY + "\\media.key";
            	encKeyInfoFilePath = ENC_DIRECTORY + "\\media.keyinfo";
            	File fileKey = new File(encKeyFilePath);
            	File fileKeyInfo = new File(encKeyInfoFilePath);
            	
            	//初始化存在删除
            	if(fileKey.exists()) {
        			fileKey.delete();
        		}
            	if(fileKeyInfo.exists()) {
            		fileKeyInfo.delete();
        		}
            	bwkey = new BufferedWriter(new FileWriter(fileKey));
            	bwkeyInfo = new BufferedWriter(new FileWriter(fileKeyInfo));
            	
            	//写入key--自定义的AES128加密的密匙
            	bwkey.write(UUID.randomUUID().toString());
            	//写入keyInfo
            	//密匙URL地址，可以对该URL鉴权
//            	bwkeyInfo.write("http://127.0.0.1/media/lxtzaqbjp/media.key");
            	bwkeyInfo.write("https://xinxinji.cn/media/lxtzaqbjp/media.key");
            	bwkeyInfo.newLine();
            	//全路径，绝对路径
            	bwkeyInfo.write(encKeyFilePath);
            	
            	bwkeyInfo.newLine();
            	bwkeyInfo.write(UUID.randomUUID().toString().replace("-", ""));
            	
            	bwkey.flush();
            	bwkeyInfo.flush();
    		}catch(IOException e){
    			e.printStackTrace();
    			//恢复默认
    			encKeyInfoFilePath = null;
    		} finally{
    			try {
    				//一定要关闭文件
    				bwkey.close();
    				bwkeyInfo.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
    		}
	        return encKeyInfoFilePath;
	  }, pool);
      //异步执行
      //第二步：HLS m3u8 AES128 加密
	  CompletableFuture<Integer> completableFutureTaskHls = completableFutureTask.thenApplyAsync((String encKeyInfoFilePath) -> {
		  if(encKeyInfoFilePath == null || encKeyInfoFilePath.length() == 0) {return CODE_FAIL;}
		  System.out.println("第一步：创建enc.keyinfo文件,成功！");
		  Integer codeTmp =  cmdExecut(cmd_enc.replace("$encInfoPath", encKeyInfoFilePath).replace("$encPath", ENC_DIRECTORY));
		  if(CODE_SUCCESS != codeTmp) {return CODE_FAIL;}
		  System.out.println("第二步：HLS m3u8 AES128 加密,成功！");
		  return codeTmp;
	  }, pool);
	    //获取执行结果
	    //code=0表示正常
	    try {
		    System.out.println(String.format("获取最终执行结果:%s", completableFutureTaskHls.get() == CODE_SUCCESS ? "成功！" : "失败！"));
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	    pool.shutdown();
    }
    
    /**
     * 
     * @Description: (执行ffmpeg自定义命令)   
     * @param: @param cmdStr
     * @param: @return      
     * @return: Integer      
     * @throws
     */
    public static Integer cmdExecut(String cmdStr) {
		  //code=0表示正常
	      Integer code  = null;	   
		  FfmpegCmd ffmpegCmd = new FfmpegCmd();  			  
		  /**
		   * 错误流
		   */
		  InputStream errorStream = null;
		  try {
				//destroyOnRuntimeShutdown表示是否立即关闭Runtime
				//如果ffmpeg命令需要长时间执行，destroyOnRuntimeShutdown = false
				//openIOStreams表示是不是需要打开输入输出流:
				//	       inputStream = processWrapper.getInputStream();
				//	       outputStream = processWrapper.getOutputStream();
				//	       errorStream = processWrapper.getErrorStream();
			   ffmpegCmd.execute(false, true, cmdStr);
			   errorStream = ffmpegCmd.getErrorStream();
			    //打印过程
			    int len = 0;
		        while ((len=errorStream.read())!=-1){
		            System.out.print((char)len);
		        }
			   //code=0表示正常
			   code = ffmpegCmd.getProcessExitCode();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				//关闭资源
				ffmpegCmd.close();
			}
		  //返回
		  return code;
    }
}
