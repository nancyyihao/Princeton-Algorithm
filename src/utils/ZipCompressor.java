package utils;

import java.io.*;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipCompressor {
    static final int BUFFER = 8192;

    private File zipFile;

    public ZipCompressor(String absoluteOutputPath) {
        zipFile = new File(absoluteOutputPath);
    }

    public void compress(String... absoluteInputPath) {
        ZipOutputStream out = null;
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(zipFile);
            CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream,
                    new CRC32());
            out = new ZipOutputStream(cos);
            String basedir = "";
            for (int i = 0; i < absoluteInputPath.length; i++) {
                compress(new File(absoluteInputPath[i]), out, basedir);
            }
            out.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void compress(File file, ZipOutputStream out, String basedir) throws Exception {
        if (file.isDirectory()) {
            System.out.println("压缩：" + basedir + file.getName());
            this.compressDirectory(file, out, basedir);
        } else {
            System.out.println("压缩：" + basedir + file.getName());
            this.compressFile(file, out, basedir);
        }
    }

    /**
     * 压缩一个目录
     */
    private void compressDirectory(File dir, ZipOutputStream out, String basedir) throws Exception {
        if (!dir.exists())
            return;

        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files)
                compress(file, out, basedir + dir.getName() + "/");
        }
    }

    /**
     * 压缩一个文件
     */
    private void compressFile(File file, ZipOutputStream out, String basedir) throws Exception {
        if (!file.exists()) {
            file.createNewFile();
        }

        // 移除头部 package
        String txt = getFileText(file.getAbsolutePath());
        txt = txt.replaceAll("package .*;", "");
        txt = txt.replaceAll("import part2_week4.test.BoggleBoard;", "");

        File tmpFile = new File(file.getParent() + "/test/" + file.getName());
        if (!tmpFile.exists()) {
            tmpFile.createNewFile();
        }
        writeTextToFile(tmpFile.getAbsolutePath(), txt);

        // 把文件添加到压缩包中
        BufferedInputStream bis = new BufferedInputStream(
                new FileInputStream(tmpFile));
        ZipEntry entry = new ZipEntry(basedir + tmpFile.getName());
        out.putNextEntry(entry);
        int count;
        byte data[] = new byte[BUFFER];
        while ((count = bis.read(data, 0, BUFFER)) != -1) {
            out.write(data, 0, count);
        }
        bis.close();

        // 删除临时文件
        tmpFile.delete();
    }

    private String getFileText(String path) throws Exception {
        StringBuilder strBuf = new StringBuilder();
        BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(path), "utf8"));
        while (br.ready()) {
            String brStr = br.readLine();
            strBuf.append(brStr).append("\n");
        }
        br.close();
        return strBuf.toString();
    }

    private void writeTextToFile(String path, String txt) throws Exception {
        BufferedWriter br = new BufferedWriter(new FileWriter(path));
        br.write(txt);
        br.close();
    }

    public static void main(String[] args) {
        ZipCompressor zc = new ZipCompressor("/Users/jasonkent27/Desktop/boggle.zip");
        zc.compress(
                "/Users/jasonkent27/Desktop/Princeton-Algorithm/src/part2_week4/BoggleSolver.java");
    }
}