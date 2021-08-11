package xyz.xionglei.main.utils.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * 文件相关工具类
 */
public class FileUtils {

    /**
     * 压缩文件夹下的所有文件
     *
     * @param srcPath          源文件(夹)
     * @param outPath          输出路径(需为文件路径)
     * @param keepDirStructure 压缩时时候保存源文件结构
     * @throws IOException IOException
     */
    public static void toZip(String srcPath, String outPath, boolean keepDirStructure) throws IOException {
        assertEmpty(srcPath);
        assertEmpty(outPath);
        File srcFile = new File(srcPath);
        File outFile = new File(outPath);
        if (!srcFile.exists()) {
            throw new IllegalArgumentException("需要压缩的文件不存在");
        }
        assertOutFile(outFile);

        try (FileOutputStream fileOutputStream = new FileOutputStream(outFile)) {
            ZipUtils.toZip(srcPath, fileOutputStream, keepDirStructure);
        }
    }

    /**
     * 压缩文件夹下的所有文件(保存原目录结构)
     *
     * @param srcPath 源文件(夹)
     * @param outPath 输出路径(需为文件夹路径)
     * @throws IOException IOException
     */
    public static void toZip(String srcPath, String outPath) throws IOException {
        toZip(srcPath, outPath, true);
    }

    /**
     * 枷缩文件列表下的所有的文件到指定文件夹
     *
     * @param fileList 待压缩文件
     * @param outPath  输出文件就
     * @throws IOException IOException
     */
    public static void toZip(List<File> fileList, String outPath) throws IOException {
        assertEmpty(fileList);
        assertEmpty(outPath);
        File outFile = new File(outPath);
        assertOutFile(outFile);
        try (FileOutputStream outputStream = new FileOutputStream(outFile)) {
            ZipUtils.toZip(fileList, outputStream);
        }
    }


    private static void assertEmpty(String str) throws IllegalArgumentException {
        if (str == null || str.isEmpty()) {
            throw new IllegalArgumentException("参数不能为空");
        }
    }

    private static void assertEmpty(Collection<?> collection) {
        if (collection == null || collection.isEmpty()) {
            throw new IllegalArgumentException("参数不能为空");
        }
    }

    private static void assertOutFile(File outFile) throws IOException {
        if (outFile.exists()) {
            throw new IOException("输出的文件已经存在");
        }
        if (!outFile.createNewFile()) {
            throw new IOException("创建输出文件失败");
        }
    }

}
