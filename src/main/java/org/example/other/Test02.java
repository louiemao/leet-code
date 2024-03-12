package org.example.other;

/**
 * @author 苍韧
 * @date 2023/10/23
 */
public class Test02 {
    public static void main(String[] args) {
        try {
            long time0 = System.currentTimeMillis();
            byte[] ids = new byte[2000000001];


            // 大文件读取用法
            String readFilePath = "/Users/maoliang/Downloads/1.txt";
            MappedBiggerFileReader reader = new MappedBiggerFileReader(readFilePath);
            //// 读取第一行，返回值为实际读取到的位数，-1为读取有误
            //if (reader.readFirstLine() != -1) {
            //    System.out.println("该文件第一行内容为:\n" + new String(reader.getArray()));
            //}
            // reader.readLines()是以若干行为单位，读取剩下的所有内容
            // 用reader.read()读取是以字节为单位，效率更高但不方便比较和处理，如果要处理的就是字节流用该方法更合适
            while (reader.readLines() != -1) {
                String[] lineStringArray = reader.getLineStringArray();
                if(lineStringArray!=null){
                    for (String s : lineStringArray) {
                        Integer i=Integer.valueOf(s);
                        ids[i]=1;
                    }
                }
            }
            // 读取完文件后要释放资源
            reader.close();

            long time1 = System.currentTimeMillis();
            System.out.println("读取文件耗时：" + (time1 - time0));

            // 大文件写入用法，因用法更简单一些所以直接写成了工具类的形式。释放资源等在方法里已经做过了直接调用即可。
            String writeFilePath = "/Users/maoliang/Downloads/5.txt";
            StringBuffer stringBuffer=new StringBuffer();
            for (int i = 0; i < ids.length; i++) {
                if(ids[i]!=1){
                    continue;
                }
                if(i==ids.length-1){
                    stringBuffer.append(i);
                    MappedBiggerFileWriter.write(writeFilePath,stringBuffer.toString());
                    stringBuffer.delete(0,stringBuffer.length());
                    continue;
                }else {
                    stringBuffer.append(i + System.lineSeparator());
                }
                if(i%6000==0){
                    MappedBiggerFileWriter.write(writeFilePath,stringBuffer.toString());
                    stringBuffer.delete(0,stringBuffer.length());
                }
            }


            long time2 = System.currentTimeMillis();
            System.out.println("写文件耗时：" + (time2 - time1));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    /*小端，低字节在后*/
    public static int bytesToIntLittleEndian(byte[] bytes) {
        // byte数组中序号小的在右边
        return bytes[0] & 0xFF |
            (bytes[1] & 0xFF) << 8 |
            (bytes[2] & 0xFF) << 16 |
            (bytes[3] & 0xFF) << 24;
    }

    /*大端，高字节在后*/
    public static int bytesToIntBigEndian(byte[] bytes) {
        // byte数组中序号大的在右边
        return bytes[3] & 0xFF |
            (bytes[2] & 0xFF) << 8 |
            (bytes[1] & 0xFF) << 16 |
            (bytes[0] & 0xFF) << 24;
    }
}
