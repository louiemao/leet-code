package org.example.other;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

/**
 * 大文件写入类
 * @author 苍韧
 * @date 2023/10/23
 */
public class MappedBiggerFileWriter {

    /**
     * 对外接口：以指定编码字符串对应的编码，向目标文件写入内容（字符串），如果没有则创建
     * @param to 目标文件全路径
     * @param content 待写入内容
     * @param charsetString 编码字符串
     * @throws IOException UnsupportedEncodingException异常
     */
    public static void write(String to, String content, String charsetString) throws IOException {
        byte[] bs;
        if (charsetString != null && charsetString.length() > 0){
            bs = content.getBytes(charsetString);
        }else {
            bs = content.getBytes();
        }
        write(to, bs);
    }

    /**
     * 对外接口：以指定编码，向目标文件写入内容（字符串），如果没有则创建
     * @param to 目标文件全路径
     * @param content 待写入内容
     * @param charset 编码
     * @throws IOException IO异常
     */
    public static void write(String to, String content, Charset charset) throws IOException {
        byte[] bs;
        if (charset != null){
            bs = content.getBytes(charset);
        }else {
            bs = content.getBytes();
        }
        write(to, bs);
    }

    /**
     * 对外接口：以默认编码向目标文件写入内容（字符串），如果没有则创建
     * @param to 目标文件全路径
     * @param content 待写入内容
     * @throws IOException IO异常
     */
    public static void write(String to, String content) throws IOException {
        write(to, content, "");
    }

    /**
     * 对外接口：向目标文件写入内容（字节数组），如果没有则创建
     * @param to 目标文件全路径
     * @param bs 待写入内容字节数组
     * @throws IOException 异常
     */
    public static void write(String to, byte[] bs) throws IOException {
        File f = new File(to);
        // 文件/目录不存在则将文件/目录创建
        if (!f.exists()){
            if (!f.getParentFile().exists()){
                f.getParentFile().mkdirs();
            }
            f.createNewFile();
        }
        // 通过RandomAccessFile拿到channel，然后利用MappedByteBuffer写文件
        RandomAccessFile acf = null;
        FileChannel fc;
        try {
            acf = new RandomAccessFile(to, "rw");
            fc = acf.getChannel();
            long offset = f.length();
            MappedByteBuffer mbuf = fc.map(FileChannel.MapMode.READ_WRITE, offset, bs.length);
            mbuf.put(bs);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (acf != null){
                acf.close();
            }
        }
    }

}

