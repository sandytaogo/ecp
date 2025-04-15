/*
 * Copyright 2018-2030 the original author or authors.
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
package com.sandy.ecp.framework.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;

/**
 * validate code
 * @author Sandy
 * @since 1.0.0 15th 12 2016
 */
public class CodeUtil {

    private int width=85;//image width
    private int height = 30;//image height
    private int codeCount = 5;// 验证码字符个数
    private int lineCount = 150;// 验证码干扰线数
    private String code ;// validation code
    private BufferedImage buffImg; // validation image code
  
    private static final char[] codeSequence = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',  'k', 'l', 'm', 'n',  'p', 'q', 'r', 's', 't', 'u', 'v', 'w',  
            'x', 'y', 'z', '0','1', '2', '3', '4', '5', '6', '7', '8', '9' };  
    private static final char[] number_code_Sequence = {'0' ,'1', '2', '3', '4', '5', '6', '7', '8', '9' };
    
    public  CodeUtil() { super();  }  
    /** 
     * @param image width  
     * @param image height
     */  
    public  CodeUtil(int width,int height) {  
        this.width=width;  
        this.height=height;  
        this.createCodeAndImage();  
    }  
    
    /**
     * image Font Byte
     * 
     * @author Sandy
     * @since 1.0.0 01th 12 2016
     */
    class ImgFontByte {

    	public Font getFont(int fontHeight) {
    		try {
    			Font baseFont = Font.createFont(Font.TRUETYPE_FONT, new ByteArrayInputStream(hex2byte(getFontByteStr())));
    			return baseFont.deriveFont(Font.PLAIN, fontHeight);
    		} catch (Exception e) {
    			return new Font("Arial", Font.PLAIN, fontHeight);
    		}
    	}

    	private byte[] hex2byte(String str) {
    		if (str == null) {
    			return null;
    		}
    		str = str.trim();
    		int len = str.length();
    		if (len == 0 || len % 2 == 1) {
    			return null;
    		}
    		byte[] b = new byte[len / 2];
    		try {
    			for (int i = 0; i < str.length(); i += 2) {
    				b[i / 2] = (byte) Integer.decode("0x" + str.substring(i, i + 2)).intValue();
    			}
    			return b;
    		} catch (Exception e) {
    			return null;
    		}
    	}

    	private String getFontByteStr() {
    		return null;
    	}
    }
    
    /** 
     * @param width 图片宽 
     * @param height 图片高 
     * @param codeCount 字符个数 
     * @param lineCount 干扰线条数 
     */  
    public  CodeUtil(int width,int height,int codeCount,int lineCount) {  
        this.width=width;  
        this.height=height;  
        this.codeCount=codeCount;  
        this.lineCount=lineCount;  
        this.createCodeAndImage();  
    }  
    
    public static String createNumberCode(int codeCount){
   	 // 生成随机数  
       Random random = new Random();  
   	 // randomCode记录随机产生的验证码  
       StringBuilder randomCode = new StringBuilder();  
       // 随机产生codeCount个字符的验证码。  
       for (int i = 0; i < codeCount; i++) {  
           // 将产生的四个随机数组合在一起。  
           randomCode.append(String.valueOf(number_code_Sequence[random.nextInt(number_code_Sequence.length)]));  
       }  
       return randomCode.toString();    
   }
    
    public String createCode(int codeCount){
        Random random = new Random();// 生成随机数  
    	 // randomCode记录随机产生的验证码  
        StringBuilder randomCode = new StringBuilder();  
        // 随机产生codeCount个字符的验证码。  
        for (int i = 0; i < codeCount; i++) {  
            // 将产生的四个随机数组合在一起。  
            randomCode.append(String.valueOf(codeSequence[random.nextInt(codeSequence.length)]));  
        }  
        code=randomCode.toString();    
        return code;
    }
      
    public void createCodeAndImage() {  
        int x = 0,fontHeight=0,y=0;  
        int red = 0, green = 0, blue = 0;  
        x = width / (codeCount + 2);//每个字符的宽度  
        fontHeight = height - 2;//字体的高度  
        y = height - 4;// 图像buffer  
        buffImg = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);  
        Graphics2D g = buffImg.createGraphics();  
        Random random = new Random();// 生成随机数  
        // 将图像填充为白色  
        g.setColor(Color.WHITE);  
        g.fillRect(0, 0, width, height);  
        ImgFontByte imgFont=new ImgFontByte();   // 创建字体  
        Font font =imgFont.getFont(fontHeight);  
        g.setFont(font);  
        for (int i = 0; i < lineCount; i++) {  
            int xs = random.nextInt(width);  
            int ys = random.nextInt(height);  
            int xe = xs+random.nextInt(width/8);  
            int ye = ys+random.nextInt(height/8);  
            red = random.nextInt(255);  
            green = random.nextInt(255);  
            blue = random.nextInt(255);  
            g.setColor(new Color(red, green, blue));  
            g.drawLine(xs, ys, xe, ye);  
        }  
        // randomCode记录随机产生的验证码  
        StringBuffer randomCode = new StringBuffer();  
        // 随机产生codeCount个字符的验证码。  
        for (int i = 0; i < codeCount; i++) {  
            String strRand = String.valueOf(codeSequence[random.nextInt(codeSequence.length)]);  
            // 产生随机的颜色值，让输出的每个字符的颜色值都将不同。  
            red = random.nextInt(155);  
            green = random.nextInt(155);  
            blue = random.nextInt(155);  
            g.setColor(new Color(red, green, blue));
            g.drawString(strRand, 0 < i ? (1 + i ) * x : 5, y);  
            // 将产生的四个随机数组合在一起。  
            randomCode.append(strRand);  
        }  
        // 将四位数字的验证码保存到Session中。  
        code=randomCode.toString();       
    }  
    
    public static String generateCode(int length) {
    	Random r = new Random(System.nanoTime());
    	StringBuilder sb = new StringBuilder();
	    for (int i = 0; i < length; i++) {
	        int d =r.nextInt(codeSequence.length);
	        sb.append(codeSequence[d]);
	    }
	    return sb.toString();
    }
    
    public void write(String path) throws IOException {  
        OutputStream sos = new FileOutputStream(path);  
        this.write(sos);  
    }
    public void write(OutputStream sos) throws IOException {  
    	ImageIO.write(buffImg, "png", sos);
    	sos.close();  
    }
    public BufferedImage getBuffImg() {  
        return buffImg;  
    }  
    public String getCode() {  
        return code;  
    }
}
