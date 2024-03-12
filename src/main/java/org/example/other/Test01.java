//package org.example.other;
//
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.nio.channels.FileChannel;
//
///**
// * @author 苍韧
// * @date 2023/10/20
// */
//public class Test01 {
//    public static void main(String[] args) {
//
//        long time0 = System.currentTimeMillis();
//
//        //List<String> list = new ArrayList<>();
//        Boolean[] ids = new Boolean[2000000001];
//        try {
//            FileInputStream fileIn = new FileInputStream("/Users/maoliang/Downloads/1.txt");
//            String charsetName = "UTF-8";
//            FileChannel fileChannel = fileIn.getChannel();
//            long fileLength = fileChannel.size();
//            int number = (int)Math.ceil((double)fileLength / (double)Integer.MAX_VALUE);
//            // 内存文件映射数组
//            MappedByteBuffer[] mappedBufArray = new MappedByteBuffer[number];
//            long preLength = 0;
//            // 映射区域的大小
//            long regionSize = Integer.MAX_VALUE;
//            // 将文件的连续区域映射到内存文件映射数组中
//            for (int i = 0; i < number; i++) {
//                if (fileLength - preLength < (long)Integer.MAX_VALUE) {
//                    // 最后一片区域的大小
//                    regionSize = fileLength - preLength;
//                }
//                mappedBufArray[i] = fileChannel.map(FileChannel.MapMode.READ_ONLY, preLength, regionSize);
//                // 下一片区域的开始
//                preLength += regionSize;
//            }
//            int arraySize = 65536;
//            int count = 0;
//            byte[] tempArray = null;
//            byte[] array = null;
//            while (count < number) {
//                //System.out.println("按若干行读取文件，本次读取到的内容为：");
//                //System.out.println("↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓下面为本次读取到的内容↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓");
//                //System.out.println(new String(reader.getArray()));
//                //System.out.println("↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑上面为本次读取到的内容↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑");
//                int limit = mappedBufArray[count].limit();
//                int position = mappedBufArray[count].position();
//                int tempArrayLength = 0;
//                if (tempArray != null) {
//                    tempArrayLength = tempArray.length;
//                }
//                if (limit - position + tempArrayLength > arraySize) {
//                    array = new byte[arraySize];
//                    if (tempArrayLength != 0) {
//                        System.arraycopy(tempArray, 0, array, 0, tempArrayLength);
//                        mappedBufArray[count].get(array, tempArrayLength, arraySize - tempArrayLength);
//                        tempArray = null;
//                    } else {
//                        mappedBufArray[count].get(array);
//                    }
//                    // 读取完之后解析一下然后截取到最后一个换行符
//                    int[] lastIndexOfNextLine = firstLineEndIndex(array);
//                    while (lastIndexOfNextLine[0] >= 0) {
//                        byte[] tempArr = new byte[arraySize];
//                        System.arraycopy(array, 0, tempArr, 0, arraySize);
//
//                        array = new byte[lastIndexOfNextLine[0]];
//                        System.arraycopy(tempArr, 0, array, 0, lastIndexOfNextLine[0]);
//                        int id = bytesToIntBigEndian(array);
//                        System.out.println(id);
//                        ids[id] = true;
//
//                        int length = lastIndexOfNextLine[0] + lastIndexOfNextLine[1];
//                        tempArray = new byte[arraySize - length];
//                        System.arraycopy(tempArr, length, tempArray, 0, arraySize - length);
//                        lastIndexOfNextLine = firstLineEndIndex(tempArray);
//                    }
//                } else {// 本内存文件映射最后一次读取数据
//                    array = new byte[limit - position + tempArrayLength];
//                    if (tempArrayLength != 0) {
//                        System.arraycopy(tempArray, 0, array, 0, tempArrayLength);
//                        mappedBufArray[count].get(array, tempArrayLength, limit - position);
//                        tempArray = null;
//                    } else {
//                        mappedBufArray[count].get(array);
//                    }
//                    // 如果正好以换行符结尾，则正常读取，否则把当前array作为tempArray继续下次读取
//                    if (endWitEndSymbol(array) || !mappedBufArray[count].hasRemaining()) {
//                        if (count < number) {
//                            count++;// 转换到下一个内存文件映射
//                        }
//                        return limit - position + tempArrayLength;
//                    } else {
//                        if (count < number) {
//                            count++;// 转换到下一个内存文件映射
//                        }
//                        tempArray = new byte[limit - position + tempArrayLength];
//                        System.arraycopy(array, 0, tempArray, 0, limit - position + tempArrayLength);
//                        return readLines();
//                    }
//                }
//            }
//
//            //// 读取文件
//            //File file = new File("/Users/maoliang/Downloads/1.txt");
//            //// 判断文件是否存在
//            //if (file.isFile() && file.exists()) {
//            //    InputStreamReader reader = new InputStreamReader(Files.newInputStream(file.toPath()),
//            //        StandardCharsets.UTF_8);
//            //    BufferedReader bufferedReader = new BufferedReader(reader);
//            //    try {
//            //        String str;
//            //        //循环判断是否文件扫描完
//            //        while ((str = bufferedReader.readLine()) != null) {
//            //            //将每一行添加到list集合里
//            //            list.add(str);
//            //        }
//            //    } catch (IOException e) {
//            //        e.printStackTrace();
//            //    }
//            //    bufferedReader.close();
//            //    reader.close();
//            //} else {
//            //    System.out.println("找不到指定的文件");
//            //}
//        } catch (IOException e) {
//            System.out.println("读取文件内容出错");
//            e.printStackTrace();
//        }
//        long time1 = System.currentTimeMillis();
//        System.out.println(time1 - time0);
//    }
//
//    /**
//     * 获取当前字符数组第一个换行符的下标，同时记录换行符占位数lineBreakLength("\r\n"=2,"\n"=1)
//     *
//     * @param array
//     * @return
//     */
//    private static int[] firstLineEndIndex(byte[] array) {
//
//        if (array == null || array.length == 0) {
//            return new int[] {-1, 0};
//        }
//        // 第一个字符即为换行符的情况
//        if (array[0] == '\n') {
//            return new int[] {0, 1};
//        }
//        // 其他情况
//        for (int i = 0; i < array.length - 1; i++) {
//            if (array[i + 1] == '\n') {
//                if (array[i] == '\r') {
//                    return new int[] {i, 2};
//                } else {
//                    return new int[] {i + 1, 1};
//                }
//            }
//        }
//        return new int[] {-1, 0};
//    }
//
//    /*小端，低字节在后*/
//    public static int bytesToIntLittleEndian(byte[] bytes) {
//        // byte数组中序号小的在右边
//        return bytes[0] & 0xFF |
//            (bytes[1] & 0xFF) << 8 |
//            (bytes[2] & 0xFF) << 16 |
//            (bytes[3] & 0xFF) << 24;
//    }
//
//    /*大端，高字节在后*/
//    public static int bytesToIntBigEndian(byte[] bytes) {
//        // byte数组中序号大的在右边
//        return bytes[3] & 0xFF |
//            (bytes[2] & 0xFF) << 8 |
//            (bytes[1] & 0xFF) << 16 |
//            (bytes[0] & 0xFF) << 24;
//    }
//}
