package com.wleiy.upload.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * <一句话功能简述><p>
 * MultipartFile类常用的一些方法：
 * String getContentType()          //获取文件MIME类型
 * InputStream getInputStream()     //后去文件流
 * String getName()                 //获取表单中文件组件的名字
 * String getOriginalFilename()     //获取上传文件的原名
 * long getSize()                   //获取文件的字节大小，单位byte
 * boolean isEmpty()                //是否为空
 * void transferTo(File dest)       //保存到一个目标文件中。
 *
 * @param <E>
 * @author wangxy
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Controller
public class FileUpDownCtrl<E> {

    /**
     * 上传文件
     *
     * @ param file
     * @ return
     */
    @RequestMapping(value = "/upload.do")
    public String fileUpload(@RequestParam("files") List<CommonsMultipartFile> files, HttpServletRequest request) {
//        直接使用集合，不使用数组，若使用数组，不选文件点击上传和选择一个文件进行上传效果相同，无容易进行非空判断
        if (!files.isEmpty()) {
            //循环获取file数组中得文件
            for (int i = 0; i < files.size(); i++) {
                CommonsMultipartFile file = files.get(i);
                try {
                    //获取存取路径
                    String filePath = request.getServletContext().getRealPath("/") + "upload/" + file.getOriginalFilename();
                    // 转存文件
                    File file1 = new File(filePath);
                    if (!file1.exists()) {
                        file1.mkdirs();
                    }
                    file.transferTo(file1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // 重定向
            return "redirect:/list.do";
        }
        // 重定向
        return "redirect:/fail.html";
    }
    @RequestMapping(value = "/list.do")
    @ResponseBody
    public String list(HttpServletRequest request, HttpServletResponse response) {
        String filePath = request.getSession().getServletContext().getRealPath("/") + "upload";
        File[] files = new File(filePath).listFiles();
        StringBuilder fileNames = new StringBuilder();
        System.out.println("开始打印");
        for (File f : files) {
            System.out.println(f.getName());
            fileNames.append(f.getName() + "\n");
        }
        return fileNames.toString();
    }

    /**
     * 配置了 <property name="resolveLazily" value="true" />
     * 才有效
     *
     * @param ex
     * @author wangxy
     */
    @ExceptionHandler
    public void doExcepiton(Exception ex) {
        if (ex instanceof MaxUploadSizeExceededException) {
            System.out.println("文件太大");
        }
    }

}
