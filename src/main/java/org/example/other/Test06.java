package org.example.other;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import sun.misc.Cleaner;
import sun.nio.ch.DirectBuffer;

/**
 * @author 苍韧
 * @date 2023/10/23
 */
public class Test06 {
    public static void main(String[] args) {
        try {
            byte[] ids = new byte[2000000001];

            // 读取文件
            Thread t01 = new Thread(new MyRunnable("/online_data/1.txt", ids), "线程01");
            Thread t02 = new Thread(new MyRunnable("/online_data/2.txt", ids), "线程02");
            Thread t03 = new Thread(new MyRunnable("/online_data/3.txt", ids), "线程03");
            Thread t04 = new Thread(new MyRunnable("/online_data/4.txt", ids), "线程04");
            t01.start();
            t02.start();
            t03.start();
            t04.start();
            t01.join();
            t02.join();
            t03.join();
            t04.join();

            // 写文件
            String writeFilePath = "/tctmp/result.txt";
            File f = new File(writeFilePath);
            if (f.exists()){
                f.delete();
            }
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < ids.length; i++) {
                if (ids[i] != 1) {
                    continue;
                }
                if (i == ids.length - 1) {
                    stringBuffer.append(i);
                    write(writeFilePath, stringBuffer.toString());
                    stringBuffer.delete(0, stringBuffer.length());
                    continue;
                } else {
                    stringBuffer.append(i + System.lineSeparator());
                }
                if (i % 200000 == 0) {
                    write(writeFilePath, stringBuffer.toString());
                    stringBuffer.delete(0, stringBuffer.length());
                }
            }

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public static void write(String to, String content, String charsetString) throws IOException {
        byte[] bs;
        if (charsetString != null && charsetString.length() > 0){
            bs = content.getBytes(charsetString);
        }else {
            bs = content.getBytes();
        }
        write(to, bs);
    }

    public static void write(String to, String content) throws IOException {
        write(to, content, "");
    }

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

    static class MyRunnable implements Runnable {
        private String filePath;
        private byte[] ids;

        public MyRunnable(String filePath, byte[] ids) {
            this.filePath = filePath;
            this.ids = ids;
        }

        @Override
        public void run() {
            try {
                MappedBiggerFileReader reader = new MappedBiggerFileReader(filePath, "UTF-8", 65536);
                while (reader.readLines() != -1) {
                    String[] lineStringArray = reader.getLineStringArray();
                    if (lineStringArray != null) {
                        for (String s : lineStringArray) {
                            Integer i = Integer.valueOf(s);
                            ids[i] = 1;
                        }
                    }
                }
                // 读取完文件后要释放资源
                reader.close();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }

    public static class MappedBiggerFileReader {
        /**
         * 内存映射缓冲区数组
         */
        private MappedByteBuffer[] mappedBufArray;
        /**
         * 采取readLines方法时用到的缓存数组，为了保证每次读取都是某行开头作为开头，某行结尾作为结尾
         */
        private byte[] tempArray;
        /**
         * 内存映射缓冲区下标
         */
        private int count = 0;
        /**
         * 内存映射缓冲区个数
         */
        private int number;
        /**
         * 文件输入流
         */
        private FileInputStream fileIn;
        /**
         * 指定读取的编码，默认为GBK
         */
        private String charsetName;
        /**
         * 文件总长度
         */
        private long fileLength;
        /**
         * 缓存数组最大大小，默认大小65536
         */
        private int arraySize;
        /**
         * 缓存数组
         */
        private byte[] array;
        /**
         * 换行符长度，默认为\r\n，即2
         * 若为\n，则改为1
         */
        private int lineBreakLength = 2;

        /**
         * 核心构造方法，读取整个文件到缓存数组mappedBufArray里
         *
         * @param fileName
         * @param charsetName
         * @param arraySize
         * @throws IOException
         */
        public MappedBiggerFileReader(String fileName, String charsetName, int arraySize) throws IOException {
            this.fileIn = new FileInputStream(fileName);
            this.charsetName = charsetName;
            FileChannel fileChannel = fileIn.getChannel();
            this.fileLength = fileChannel.size();
            this.number = (int)Math.ceil((double)fileLength / (double)Integer.MAX_VALUE);
            // 内存文件映射数组
            this.mappedBufArray = new MappedByteBuffer[number];
            long preLength = 0;
            // 映射区域的大小
            long regionSize = Integer.MAX_VALUE;
            // 将文件的连续区域映射到内存文件映射数组中
            for (int i = 0; i < number; i++) {
                if (fileLength - preLength < (long)Integer.MAX_VALUE) {
                    // 最后一片区域的大小
                    regionSize = fileLength - preLength;
                }
                mappedBufArray[i] = fileChannel.map(FileChannel.MapMode.READ_ONLY, preLength, regionSize);
                // 下一片区域的开始
                preLength += regionSize;
            }
            this.arraySize = arraySize;
        }

        /**
         * 对外接口：按若干行读到缓存数组array里（只能读取小于预设的arraySize的句子，大于会抛异常）
         * 注：现在\r\n和\n都视为换行符
         *
         * @return
         * @throws IOException
         */
        public int readLines() throws Exception {
            if (count >= number) {
                return -1;
            }
            int limit = mappedBufArray[count].limit();
            int position = mappedBufArray[count].position();
            int tempArrayLength = 0;
            if (tempArray != null) {
                tempArrayLength = tempArray.length;
            }
            if (limit - position + tempArrayLength > arraySize) {
                array = new byte[arraySize];
                if (tempArrayLength != 0) {
                    System.arraycopy(tempArray, 0, array, 0, tempArrayLength);
                    mappedBufArray[count].get(array, tempArrayLength, arraySize - tempArrayLength);
                    tempArray = null;
                } else {
                    mappedBufArray[count].get(array);
                }
                // 读取完之后解析一下然后截取到最后一个换行符
                int lastIndexOfNextLine = lastLineEndIndex(array);
                if (lastIndexOfNextLine >= 0) {
                    byte[] tempArr = new byte[arraySize];
                    System.arraycopy(array, 0, tempArr, 0, arraySize);
                    array = new byte[lastIndexOfNextLine];
                    System.arraycopy(tempArr, 0, array, 0, lastIndexOfNextLine);
                    int length = lastIndexOfNextLine + lineBreakLength;
                    tempArray = new byte[arraySize - length];
                    System.arraycopy(tempArr, length, tempArray, 0, arraySize - length);
                    return length;
                } else {
                    throw new Exception("单行超过长度了");
                }
            } else {// 本内存文件映射最后一次读取数据
                array = new byte[limit - position + tempArrayLength];
                if (tempArrayLength != 0) {
                    System.arraycopy(tempArray, 0, array, 0, tempArrayLength);
                    mappedBufArray[count].get(array, tempArrayLength, limit - position);
                    tempArray = null;
                } else {
                    mappedBufArray[count].get(array);
                }
                // 如果正好以换行符结尾，则正常读取，否则把当前array作为tempArray继续下次读取
                if (endWitEndSymbol(array) || !mappedBufArray[count].hasRemaining()) {
                    if (count < number) {
                        count++;// 转换到下一个内存文件映射
                    }
                    return limit - position + tempArrayLength;
                } else {
                    if (count < number) {
                        count++;// 转换到下一个内存文件映射
                    }
                    tempArray = new byte[limit - position + tempArrayLength];
                    System.arraycopy(array, 0, tempArray, 0, limit - position + tempArrayLength);
                    return readLines();
                }
            }
        }

        /**
         * 对外接口：释放资源，读取完文件后请调用该方法。否则会影响删除之类的操作
         * 注：sun.misc.Unsafe该类为sun包下的，根据各人jdk不同可能不需要用反射来释放
         *
         * @throws IOException
         */
        public void close() throws IOException {
            if (fileIn != null) {
                fileIn.close();
            }
            array = null;
            tempArray = null;
            if (mappedBufArray != null) {
                for (MappedByteBuffer mappedByteBuffer : mappedBufArray) {
                    // 在关闭资源时执行以下代码释放内存
                    Cleaner cl = ((DirectBuffer)mappedByteBuffer).cleaner();
                    if (cl != null) {
                        cl.clean();
                    }
                }
            }
        }

        public String[] getLineStringArray() throws UnsupportedEncodingException {
            String data = new String(array, charsetName);
            if (data != null && data.length() > 0) {
                String dataT = data.replaceAll("\r\n", "\n");
                return dataT.split("\n");
            }
            return null;
        }

        /**
         * 判断当前字符数组是否以\n为结尾
         *
         * @param array
         * @return
         */
        private boolean endWitEndSymbol(byte[] array) {
            if (array == null || array.length == 0) {
                return false;
            }
            return array[array.length - 1] == '\n';
        }

        /**
         * 获取当前字符数组最后一个换行符的下标，同时记录换行符占位数lineBreakLength("\r\n"=2,"\n"=1)
         *
         * @param array
         * @return
         */
        private int lastLineEndIndex(byte[] array) {
            if (array == null || array.length == 0) {
                return -1;
            }
            for (int i = array.length - 1; i > 0; i--) {
                if (array[i] == '\n') {
                    if (array[i - 1] == '\r') {
                        lineBreakLength = 2;
                        return i - 1;
                    } else {
                        lineBreakLength = 1;
                        return i;
                    }
                }
            }
            // 第一个字符为换行符的情况
            if (array[0] == '\n') {
                lineBreakLength = 1;
                return 0;
            }
            return -1;
        }

    }
}
