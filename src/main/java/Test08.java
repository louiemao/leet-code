import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * @author 苍韧
 * @date 2023/10/23
 */
public class Test08 {
    public static void main(String[] args) {
        BufferedWriter writer = null;
        try {
            byte[] ids = new byte[2000000001];

            // 读取文件
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

            //创建文件
            String writeFilePath = "/Users/maoliang/Downloads/5.txt";
            File f = new File(writeFilePath);
            if (f.exists()) {
                f.delete();
            } else {
                if (!f.getParentFile().exists()) {
                    f.getParentFile().mkdirs();
                }
                f.createNewFile();
            }

            // 写文件
            StringBuilder stringBuffer = new StringBuilder(6000000);
            writer = new BufferedWriter(new FileWriter(writeFilePath));
            for (int i = 0; i < ids.length; i++) {
                if (i == ids.length - 1) {
                    if (ids[i] == 1) {
                        stringBuffer.append(i);
                    } else if (stringBuffer.length() > 0) {
                        stringBuffer.deleteCharAt(stringBuffer.length() - 1);
                    }
                    if (stringBuffer.length() > 0) {
                        writer.write(stringBuffer.toString());
                    }
                    continue;
                }

                if (ids[i] != 1) {
                    continue;
                }

                stringBuffer.append(i + System.lineSeparator());

                if (stringBuffer.length() >= 5000000) {
                    writer.write(stringBuffer.toString());
                    stringBuffer = new StringBuilder();
                }
            }

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (Throwable e) {
                e.printStackTrace();
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
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(filePath));
                String line;

                while ((line = reader.readLine()) != null) {
                    int userId = Integer.parseInt(line);
                    ids[userId] = 1;
                }

                reader.close();

            } catch (Throwable throwable) {
                throwable.printStackTrace();
            } finally {
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
