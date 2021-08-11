package xyz.xionglei.main.utils.file;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 压缩文件相关工具类
 *
 * @author xionglei
 */
public class ZipUtils {

    private static final int BUFFER_SIZE = 1 << 12; // 4KB

    /**
     * 压缩方法一：
     * <p>
     * 将指定的文件路径下的所有文件压缩成zip
     *
     * @param srcDir           压缩文件(夹)路径
     * @param out              压缩文件输出流
     * @param keepDirStructure 压缩后的文件是否保留原来的目录结构。true保留 false不保留(不保留目录结构可能会出现同名文件,会压缩失败)
     * @throws IOException IOException
     */
    public static void toZip(String srcDir, OutputStream out, boolean keepDirStructure) throws IOException {

        try (ZipOutputStream zipOutputStream = new ZipOutputStream(out)) {
            File srcFile = new File(srcDir);
            compress(srcFile, zipOutputStream, srcFile.getName(), keepDirStructure);
        }

    }


    /**
     * 压缩方法二:
     * <p>
     * 将传入进来的所有文件压缩成一个压缩文件
     *
     * @param srcFiles     所有文件
     * @param outputStream 压缩文件输出流
     * @throws IOException IOException
     */
    public static void toZip(List<File> srcFiles, OutputStream outputStream) throws IOException {
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);) {
            for (File file : srcFiles) {
                compress(file, zipOutputStream, file.getName(), true);
            }
        }
    }

    /**
     * 递归的压缩文件
     *
     * @param srcFile          源文件
     * @param zipOutputStream  压缩输出流
     * @param name             文件名
     * @param keepDirStructure 是否保存原来的目录结果。true是 false否(不保留目录结构可能会出现同名文件, 压缩失败)
     * @throws IOException IOException
     */
    private static void compress(File srcFile, ZipOutputStream zipOutputStream, String name, boolean keepDirStructure) throws IOException {
        if (srcFile.isFile()) {
            // 向zip流中添加一个zip实体, 实体名字为name
            zipOutputStream.putNextEntry(new ZipEntry(name));
            // 复制文件到zips输出流中
            int len;
            byte[] buffer = new byte[BUFFER_SIZE];
            try (FileInputStream inputStream = new FileInputStream(srcFile);
                 BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream)) {
                while ((len = bufferedInputStream.read(buffer)) != -1) {
                    zipOutputStream.write(buffer, 0, len);
//                    当前zip实体完成, 需要关闭点前zip实体 tyr() 自动关闭
//                    zipOutputStream.closeEntry();
                }
            }
        } else {
            // src为一个文件夹
            File[] files = srcFile.listFiles();
            if (files == null || files.length == 0) {
                // 需要保存原来的文件结构, 需要对空文件夹处理
                if (keepDirStructure) {
                    // 空文件夹处理
                    zipOutputStream.putNextEntry(new ZipEntry(name + "/"));
                    // 没有文件需要复制, 直接关闭Entry
                    zipOutputStream.closeEntry();
                }
            } else {
                // 当前文件夹下存在文件 递归的调用此方法压缩
                for (File file : files) {
                    if (keepDirStructure) {
                        // 需要保存原目录
                        // 注意：file.getName()前面需要带上父文件夹的名字加一斜杠,
                        // 不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了
                        compress(file, zipOutputStream, name + "/" + file.getName(), true);
                    } else {
                        compress(file, zipOutputStream, file.getName(), false);
                    }
                }
            }
        }
    }
}
