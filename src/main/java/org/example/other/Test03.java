package org.example.other;

/**
 * @author 苍韧
 * @date 2023/10/23
 */
public class Test03 {
    public static void main(String[] args) {
        try {
            long time0 = System.currentTimeMillis();
            byte[] ids = new byte[2000000001];

            // 大文件读取用法
            Thread t01 = new Thread(new MyRunnable("/Users/maoliang/Downloads/1.txt", ids), "线程01");
            Thread t02 = new Thread(new MyRunnable("/Users/maoliang/Downloads/2.txt", ids), "线程02");
            Thread t03 = new Thread(new MyRunnable("/Users/maoliang/Downloads/3.txt", ids), "线程03");
            Thread t04 = new Thread(new MyRunnable("/Users/maoliang/Downloads/4.txt", ids), "线程04");
            t01.start();
            t02.start();
            t03.start();
            t04.start();
            t01.join();
            t02.join();
            t03.join();
            t04.join();

            long time1 = System.currentTimeMillis();
            System.out.println("读取文件耗时：" + (time1 - time0));

            // 大文件写入用法，因用法更简单一些所以直接写成了工具类的形式。释放资源等在方法里已经做过了直接调用即可。
            String writeFilePath = "/Users/maoliang/Downloads/5.txt";
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < ids.length; i++) {
                if (ids[i] != 1) {
                    continue;
                }
                if (i == ids.length - 1) {
                    stringBuffer.append(i);
                    MappedBiggerFileWriter.write(writeFilePath, stringBuffer.toString());
                    stringBuffer.delete(0, stringBuffer.length());
                    continue;
                } else {
                    stringBuffer.append(i + System.lineSeparator());
                }
                if (i % 10000 == 0) {
                    MappedBiggerFileWriter.write(writeFilePath, stringBuffer.toString());
                    stringBuffer.delete(0, stringBuffer.length());
                }
            }

            long time2 = System.currentTimeMillis();
            System.out.println("写文件耗时：" + (time2 - time1));
            System.out.println("总耗时：" + (time2 - time0));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
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
                MappedBiggerFileReader reader = new MappedBiggerFileReader(filePath);
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
}
