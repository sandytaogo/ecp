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
package com.sandy.ecp.recognition.util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 图像文件处理辅助工具类.
 * @author sandy
 * @date 2022-04-19 15:23:23
 */
public class FileUtil {

    static Lock lock = new ReentrantLock();
    
    public static boolean copyAndRename(String from, String to) throws IOException {
        Path sourcePath = Paths.get(from);
        Path destinationPath = Paths.get(to);
        Files.copy(sourcePath, destinationPath);
        return true;
    }

    public static boolean checkFile(final File file) {
        if(file.exists() && file.isFile()) {
            return true;
        }
        return false;
    }

    /**
     * 重命名文件
     * @param file
     * @param newName 可以是文件名，也可以是路径+文件名
     * @return
     */
    public static boolean renameFile(String filePath, String newName) {
        File file = new File(filePath);
        return renameFile(file, newName);
    }

    /**
     * 重命名文件
     * @param file
     * @param newName 可以是文件名，也可以是路径+文件名
     * @return
     */
    public static boolean renameFile(File file, String newName) {
        if(file.exists()) {
            String targetPath = null;
            if(newName.indexOf("/") >= 0 || newName.indexOf("\\\\") >= 0) {
                targetPath = newName;
            } else {
                targetPath = file.getParentFile().getAbsolutePath() + "/" + newName;
            }
            
            File targetFile = new File(targetPath);
            file.renameTo(targetFile);
            return true;
        }
        return false;
    }

    public static void createDir(String dir) {
        File file = new File(dir);
        if(file.exists() && file.isDirectory()) {
            return ;
        } else {
            file.mkdirs();
        }
    }

    /**
     * 删除并重新创建目录
     * @param dir
     */
    public static void recreateDir(final String dir) {
        new File(dir).delete();
        new File(dir).mkdir();
    }


    /**
     * 递归获取文件信息
     * @param path String类型
     * @param files
     */
    public static void getFiles(final String path, Vector<String> files) {
        getFiles(new File(path), files);
    }


    /**
     * 递归获取文件信息
     * @param dir FIle类型
     * @param files
     */
    private static void getFiles(final File dir, Vector<String> files) {
        File[] filelist = dir.listFiles();
        for (File file : filelist) {
            if (file.isDirectory()) {
                getFiles(file, files);
            } else {
                files.add(file.getAbsolutePath());
            }
        }
    }

    /**
     * @param dir
     * @param filename
     * @param recursive
     * @return
     */
    public static List<File> listFile(File dir, final String fileType, boolean recursive) {
        if (!dir.exists()) {
            throw new RuntimeException("目录：" + dir + "不存在");
        }
        if (!dir.isDirectory()) {
            throw new RuntimeException(dir + "不是目录");
        }
        FileFilter ff = null;
        if (fileType == null || fileType.length() == 0) {
            ff = new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return true;
                }
            };
        } else {
            ff = new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    if (pathname.isDirectory()) {
                        return true;
                    }
                    String name = pathname.getName().toLowerCase();
                    String format = name.substring(name.lastIndexOf(".") + 1);
                    if (fileType.contains(format)) {
                        return true;
                    } else {
                        return false;
                    }
                }
            };
        }
        return listFile(dir, ff, recursive);
    }

    /**
     * 
     * @param dir
     * @param ff
     * @param recursive 是否遍历子目录
     * @return
     */
    public static List<File> listFile(File dir, FileFilter ff, boolean recursive) {
        List<File> list = new ArrayList<File>();
        File[] files = dir.listFiles(ff);
        if (files != null && files.length > 0) {
            for (File f : files) {
                // 如果是文件,添加文件到list中
                if (f.isFile() || (f.isDirectory() && !f.getName().startsWith("."))) {
                    list.add(f);
                } else if (recursive) {
                    // 获取子目录中的文件,添加子目录中的经过过滤的所有文件添加到list
                    list.addAll(listFile(f, ff, true));
                }
            }
        }
        return list;
    }

}
