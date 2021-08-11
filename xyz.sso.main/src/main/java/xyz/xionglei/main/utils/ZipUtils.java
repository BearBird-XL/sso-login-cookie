package xyz.xionglei.main.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtils {

    /**
     * 把接受的全部文件打成压缩包
     *
     * @param fileList 文件;
     * @param outputStream 输出流
     */
    public static void zipFile(List<File> fileList, ZipOutputStream outputStream) {
        if (fileList == null || fileList.isEmpty()) {
            return;
        }
        fileList.forEach(file -> zipFile(file, outputStream));
    }

    /**
     * 根据输入的文件与输出流文件进行打包
     * @param inputFile file
     * @param outputStream 输出流
     */
    public static void zipFile(File inputFile, ZipOutputStream outputStream) {
        if (inputFile == null || outputStream == null) {
            return;
        }
        if (inputFile.isFile()) {
            try (FileInputStream in = new FileInputStream(inputFile);
                 BufferedInputStream bf = new BufferedInputStream(in, 1024);
            ) {
                ZipEntry entry = new ZipEntry(inputFile.getName());
                outputStream.putNextEntry(entry);
                // 向压缩文件中输出数据
                int len;
                byte[] buffer = new byte[1024];
                while ((len = bf.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, len);
                }
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            File[] files = inputFile.listFiles();
            if (files != null && files.length > 0) {
                for (File f: files) {
                    zipFile(f, outputStream);
                }
            }
        }
    }

    public static HttpServletResponse downloadZip(File file, HttpServletResponse response) {
        if (!file.exists()) {
            System.out.println("待压缩的文件不存在");
        } else {
            // 以流的形式下载文件
            try(FileInputStream in = new FileInputStream(file);
                BufferedInputStream bfi = new BufferedInputStream(in);
                OutputStream toClientStream = new BufferedOutputStream(response.getOutputStream())
            ) {

                // 清空response
                response.reset();
                response.setContentType("application/octet-stream");
                // 如果输出的是中文名的文件， 在此出就要用URLEncoder.encode方法进行处理
                response.setHeader("Content-Disposition", "attachment;filename="
                        + new String(file.getName().getBytes("GB2312"), "ISO8859-1"));
                byte[] buffer = new byte[bfi.available()];
                int read;
                while ((read = bfi.read(buffer)) != -1) {
                    toClientStream.write(buffer);
                }
                toClientStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return response;
    }


    //应用场景 打包
//    @RequestMapping(value = "downloadZip")
    public HttpServletResponse downloadZip(HttpServletRequest request, HttpServletResponse response)throws Exception {
        List<File> files = new ArrayList<File>();
        //打包凭证.zip与EXCEL一起打包
        try {
            String zipFilenameA =  request.getSession().getServletContext().getRealPath("/") + "tempFileA.zip" ;
            File zipFile = new File(zipFilenameA);
            if (!zipFile.exists()){
                zipFile.createNewFile();
            }
            response.reset();
            //response.getWriter()
            //创建文件输出流
            FileOutputStream fousa = new FileOutputStream(zipFile);
            ZipOutputStream zipOutA = new ZipOutputStream(fousa);
            ZipUtils.zipFile(files, zipOutA);
            zipOutA.close();
            fousa.close();
            return ZipUtils.downloadZip(zipFile,response);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return response ;
    }
}
