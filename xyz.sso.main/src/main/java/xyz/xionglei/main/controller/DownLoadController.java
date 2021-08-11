package xyz.xionglei.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import xyz.xionglei.main.utils.file.FileUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/download")
public class DownLoadController {

    @RequestMapping("/zip")
    public void downLoadZip(HttpServletRequest request, HttpServletResponse response) {
        // 下载当前文件夹下的所有文件
        String filePath = "C:\\Users\\lei\\Desktop\\testZip";
        String zipFilePath = request.getSession().getServletContext().getRealPath("/") + "zip-temp-" + System.nanoTime() + ".zip";
        File srcFile = new File(filePath);
        if (!srcFile.exists() || srcFile.isFile()) {
//            return "500";
            return;
        }
        try{
            FileUtils.toZip(Arrays.asList(Objects.requireNonNull(srcFile.listFiles())), zipFilePath);
            try(FileInputStream inputStream = new FileInputStream(zipFilePath);
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                OutputStream outputStream = response.getOutputStream();
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream)
            ) {
                byte[] buffer = new byte[1024];
                int len;
                while ((len = bufferedInputStream.read(buffer)) != -1) {
                    bufferedOutputStream.write(buffer, 0, len);
                }
                bufferedOutputStream.flush();

                // 删除临时zip文件
                new Thread(() -> {
                    try {
                        TimeUnit.SECONDS.sleep(3);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    File zipFile = new File(zipFilePath);
                    System.out.println("zip文件路径：" + zipFile.getAbsolutePath());
                    if (zipFile.delete()) {
                        System.out.println("删除成功");
                    } else {
                        System.out.println("删除失败");
                    }
                }, "zip-temp-remove").start();
//                return "200";
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
//            return "500";
        }
    }
}
