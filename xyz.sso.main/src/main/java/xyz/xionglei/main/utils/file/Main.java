package xyz.xionglei.main.utils.file;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws IOException {
//        testListFileZip();
        testSrcPathZip();

    }

    public static void testListFileZip() throws IOException {
        String outPath = "C:\\Users\\lei\\Desktop\\MultiDir.zip";
        long start = System.currentTimeMillis();
        List<File> fileList = new LinkedList<>();
        String srcPath1 = "C:\\Users\\lei\\Desktop\\TODO";
        String srcPath2 = "C:\\Users\\lei\\Desktop\\bak";
        String srcPath3 = "C:\\Users\\lei\\Desktop\\TODO\\突击班面经";
        fileList.add(new File(srcPath1));
        fileList.add(new File(srcPath2));
        fileList.add(new File(srcPath3));

        FileUtils.toZip(fileList, outPath);
        long end = System.currentTimeMillis();
        System.out.println("压缩完成, 耗时: " + (end - start) + " ms");
    }

    public static void testSrcPathZip() throws IOException {

        String srcPath = "C:\\Users\\lei\\Desktop\\todZipFIle";
        String outPath = "C:\\Users\\lei\\Desktop\\todZipFIle-Todo.zip";
        File file = new File(outPath);
        file.deleteOnExit();

        long start = System.currentTimeMillis();
        FileUtils.toZip(srcPath, outPath);
        long end = System.currentTimeMillis();
        System.out.println("压缩完成, 耗时: " + (end - start) + " ms");

        try {
            TimeUnit.SECONDS.sleep(7);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        file.deleteOnExit();
    }
}
