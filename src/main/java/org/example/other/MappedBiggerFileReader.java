package org.example.other;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import sun.misc.Cleaner;
import sun.misc.Unsafe;
import sun.nio.ch.DirectBuffer;

/**
 * 大文件读取类
 * @author 苍韧
 * @date 2023/10/23
 */
public class MappedBiggerFileReader {
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

    public MappedBiggerFileReader(String fileName) throws IOException {
        this(fileName, "UTF-8", 65536);
    }

    public MappedBiggerFileReader(String fileName, int arraySize) throws IOException {
        this(fileName, "UTF-8", arraySize);
    }

    public MappedBiggerFileReader(String fileName, String charsetName) throws IOException {
        this(fileName, charsetName, 65536);
    }

    /**
     * 核心构造方法，读取整个文件到缓存数组mappedBufArray里
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
        this.number = (int) Math.ceil((double) fileLength / (double) Integer.MAX_VALUE);
        // 内存文件映射数组
        this.mappedBufArray = new MappedByteBuffer[number];
        long preLength = 0;
        // 映射区域的大小
        long regionSize = Integer.MAX_VALUE;
        // 将文件的连续区域映射到内存文件映射数组中
        for (int i = 0; i < number; i++) {
            if (fileLength - preLength < (long) Integer.MAX_VALUE) {
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
     * 获取文件的第一行，返回其字符串，可以多次获取，不影响当前读取位置
     * @return
     * @throws IOException
     */
    public String getFirstLine() throws IOException {
        if (number < 1){
            return "";
        }
        MappedByteBuffer mappedByteBuffer = fileIn.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, arraySize);
        int limit = mappedByteBuffer.limit();
        int size = Math.min(limit, arraySize);
        byte[] tBytes = new byte[size];
        mappedByteBuffer.get(tBytes,0,size);
        int firstLineEndIndex = firstLineEndIndex(tBytes);
        byte[] tempArr = new byte[0];
        if (firstLineEndIndex > 0){
            tempArr = new byte[firstLineEndIndex];
            System.arraycopy(tBytes, 0, tempArr, 0,firstLineEndIndex);
        }
        return new String(tempArr, charsetName);
    }

    /**
     * 对外接口：读取第一行到缓存数组array里（只能读取一次，会将读取位置向后移动，必须在调用其他read方法前调用才有效）
     * 注：现在\r\n和\n都视为换行符
     * @return
     * @throws IOException
     */
    public int readFirstLine() throws Exception {
        // 该方法需保证该文件是第一次被调用readLines或readFirstLine
        if (count >= number || count > 0 || tempArray != null) {
            return -1;
        }
        int position = mappedBufArray[count].position();
        if (position != 0){
            return -1;
        }
        int limit = mappedBufArray[count].limit();
        int res = -1;
        if (limit - position > arraySize) {
            array = new byte[arraySize];
            mappedBufArray[count].get(array);
            // 读取完之后解析一下然后截取到第一个行结尾换行符
            int firstLineEndIndex = firstLineEndIndex(array);
            if (firstLineEndIndex == 0) {
                // 这种情况代表整个文件只有一行，且第一行为空串
                array = new byte[0];
                mappedBufArray[count].position(0);
                res = 0;
            }else if (firstLineEndIndex > 0){
                // 正常情况，截取出第1个换行符前的内容
                byte[] tArr = new byte[firstLineEndIndex];
                System.arraycopy(array, 0, tArr, 0, firstLineEndIndex);
                array = new byte[firstLineEndIndex];
                System.arraycopy(tArr, 0, array, 0, firstLineEndIndex);
                mappedBufArray[count].position(firstLineEndIndex+lineBreakLength);
                res = firstLineEndIndex;
            }else {
                throw new Exception("单行超过长度了");
            }
        } else {
            // 整个文件的内容没有arraySize大则把整个文件的内容先放进array里再进行判断和处理
            array = new byte[limit - position];
            mappedBufArray[count].get(array);
            int firstLineEndIndex = firstLineEndIndex(array);
            if (firstLineEndIndex == -1){
                // 如果没有换行符代表整个文件只有一行
                res = limit - position;
                if (count < number) {
                    count++;// 转换到下一个内存文件映射
                }
            }else if(firstLineEndIndex == 0){
                // 这种情况代表整个文件只有一行，且第一行为空串
                array = new byte[0];
                mappedBufArray[count].position(0);
                res = 0;
                if (count < number) {
                    count++;// 转换到下一个内存文件映射
                }
            }else {
                // 正常情况，截取出第1个换行符前的内容
                byte[] tArr = new byte[limit - position];
                System.arraycopy(array, 0, tArr, 0, firstLineEndIndex);
                array = new byte[firstLineEndIndex];
                System.arraycopy(tArr, 0, array, 0, firstLineEndIndex);
                mappedBufArray[count].position(firstLineEndIndex+lineBreakLength);
                res = firstLineEndIndex;
            }
        }
        return res;
    }

    /**
     * 对外接口：按若干行读到缓存数组array里（只能读取小于预设的arraySize的句子，大于会抛异常）
     * 注：现在\r\n和\n都视为换行符
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
        if (tempArray != null){
            tempArrayLength = tempArray.length;
        }
        if (limit - position + tempArrayLength > arraySize) {
            array = new byte[arraySize];
            if (tempArrayLength != 0){
                System.arraycopy(tempArray,0,array,0, tempArrayLength);
                mappedBufArray[count].get(array, tempArrayLength, arraySize - tempArrayLength);
                tempArray = null;
            }else {
                mappedBufArray[count].get(array);
            }
            // 读取完之后解析一下然后截取到最后一个换行符
            int lastIndexOfNextLine = lastLineEndIndex(array);
            if (lastIndexOfNextLine >= 0){
                byte[] tempArr = new byte[arraySize];
                System.arraycopy(array, 0, tempArr, 0, arraySize);
                array = new byte[lastIndexOfNextLine];
                System.arraycopy(tempArr, 0, array, 0,lastIndexOfNextLine);
                int length = lastIndexOfNextLine+lineBreakLength;
                tempArray = new byte[arraySize - length];
                System.arraycopy(tempArr, length, tempArray, 0, arraySize - length);
                return length;
            }else {
                throw new Exception("单行超过长度了");
            }
        } else {// 本内存文件映射最后一次读取数据
            array = new byte[limit - position + tempArrayLength];
            if (tempArrayLength != 0){
                System.arraycopy(tempArray,0,array,0, tempArrayLength);
                mappedBufArray[count].get(array, tempArrayLength, limit - position);
                tempArray = null;
            }else {
                mappedBufArray[count].get(array);
            }
            // 如果正好以换行符结尾，则正常读取，否则把当前array作为tempArray继续下次读取
            if (endWitEndSymbol(array) || !mappedBufArray[count].hasRemaining()){
                if (count < number) {
                    count++;// 转换到下一个内存文件映射
                }
                return limit - position + tempArrayLength;
            }else {
                if (count < number) {
                    count++;// 转换到下一个内存文件映射
                }
                tempArray = new byte[limit - position + tempArrayLength];
                System.arraycopy(array,0,tempArray,0,limit - position + tempArrayLength);
                return readLines();
            }
        }
    }


    /**
     * 对外接口：按字节读取内容至缓存数组array里
     * @return
     * @throws IOException
     */
    public int read() throws IOException {
        if (count >= number) {
            return -1;
        }
        int limit = mappedBufArray[count].limit();
        int position = mappedBufArray[count].position();
        if (limit - position > arraySize) {
            array = new byte[arraySize];
            mappedBufArray[count].get(array);
            return arraySize;
        } else {// 本内存文件映射最后一次读取数据
            array = new byte[limit - position];
            mappedBufArray[count].get(array);
            if (count < number) {
                count++;// 转换到下一个内存文件映射
            }
            return limit - position;
        }
    }

    /**
     * 对外接口：释放资源，读取完文件后请调用该方法。否则会影响删除之类的操作
     * 注：sun.misc.Unsafe该类为sun包下的，根据各人jdk不同可能不需要用反射来释放
     * @throws IOException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public void close() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (fileIn != null){
            fileIn.close();
        }
        array = null;
        tempArray = null;
        if (mappedBufArray != null){
            //Unsafe unsafe = createUnsafe();
            //Method invokeCleaner = Unsafe.class.getDeclaredMethod("invokeCleaner", ByteBuffer.class);
            for (MappedByteBuffer mappedByteBuffer : mappedBufArray) {
                // 在关闭资源时执行以下代码释放内存
                //invokeCleaner.setAccessible(true);
                //invokeCleaner.invoke(unsafe, mappedByteBuffer);
                unmap(mappedByteBuffer);
            }
        }
    }

    private static void unmap(MappedByteBuffer bb) {
        Cleaner cl = ((DirectBuffer)bb).cleaner();
        if (cl != null) {
            cl.clean();
        }
    }

    private static Unsafe createUnsafe() {
        try {
            Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
            Field field = unsafeClass.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            return (Unsafe) field.get(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 对外接口，获取当前字符数组缓存（即正读取到的内容）
     * @return
     */
    public byte[] getArray() {
        return array;
    }

    /**
     * 对外接口，获取当前读到的内容，返回的结果是以每一行的字符串列表（这里将\r\n和\n都视为换行符）
     * @return
     * @throws UnsupportedEncodingException
     */
    public List<String> getLineStringList() throws UnsupportedEncodingException {
        String data = new String(array, charsetName);
        if(StringUtils.isNotEmpty(data)){
            String dataT= data.replaceAll("\r\n","\n");
            return Arrays.asList(dataT.split("\n"));
        }
        return new ArrayList<>();
    }

    public String[] getLineStringArray() throws UnsupportedEncodingException {
        String data = new String(array, charsetName);
        if(StringUtils.isNotEmpty(data)){
            String dataT= data.replaceAll("\r\n","\n");
            return dataT.split("\n");
        }
        return null;
    }

    public List<Integer> getLineIntegerList() throws UnsupportedEncodingException {
        String data = new String(array, charsetName);
        if(StringUtils.isNotEmpty(data)){
            String dataT= data.replaceAll("\r\n","\n");
            String[] split = dataT.split("\n");
            List<Integer> list=new ArrayList<>(split.length);
            for (String s : split) {
                list.add(Integer.valueOf(s));
            }
        }
        return new ArrayList<>();
    }

    /**
     * 对外接口，获取当前文件长度
     * @return
     */
    public long getFileLength() {
        return fileLength;
    }

    /**
     * 判断当前字符数组是否以\n为结尾
     * @param array
     * @return
     */
    private boolean endWitEndSymbol(byte[] array){
        if (array == null || array.length == 0){
            return false;
        }
        return array[array.length - 1] == '\n';
    }

    /**
     * 获取当前字符数组最后一个换行符的下标，同时记录换行符占位数lineBreakLength("\r\n"=2,"\n"=1)
     * @param array
     * @return
     */
    private int lastLineEndIndex(byte[] array){
        if (array == null || array.length == 0){
            return -1;
        }
        for (int i = array.length-1; i > 0; i--){
            if (array[i] == '\n'){
                if (array[i-1] == '\r'){
                    lineBreakLength = 2;
                    return i-1;
                }else {
                    lineBreakLength = 1;
                    return i;
                }
            }
        }
        // 第一个字符为换行符的情况
        if (array[0] == '\n'){
            lineBreakLength = 1;
            return 0;
        }
        return -1;
    }

    /**
     * 获取当前字符数组第一个换行符的下标，同时记录换行符占位数lineBreakLength("\r\n"=2,"\n"=1)
     * @param array
     * @return
     */
    private int firstLineEndIndex(byte[] array){
        if (array == null || array.length == 0){
            return -1;
        }
        // 第一个字符即为换行符的情况
        if (array[0] == '\n'){
            lineBreakLength = 1;
            return 0;
        }
        // 其他情况
        for (int i = 0; i < array.length-1; i++){
            if (array[i+1] == '\n'){
                if (array[i] == '\r'){
                    lineBreakLength = 2;
                    return i;
                }else {
                    lineBreakLength = 1;
                    return i+1;
                }
            }
        }
        return -1;
    }
}
