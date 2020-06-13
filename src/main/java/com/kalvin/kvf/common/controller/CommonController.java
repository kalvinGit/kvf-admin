package com.kalvin.kvf.common.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.kalvin.kvf.common.constant.Constants;
import com.kalvin.kvf.common.constant.UploadPathEnum;
import com.kalvin.kvf.common.dto.R;
import com.kalvin.kvf.common.exception.KvfException;
import com.kalvin.kvf.common.utils.FileUploadKit;
import com.kalvin.kvf.common.utils.HttpServletContextKit;
import com.kalvin.kvf.modules.sys.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * 【作用】通用控制层<br>
 * 【说明】（无）
 * @author Kalvin
 * @Date 2019/5/9 20:20
 */
@Slf4j
@RestController
@RequestMapping(value = "common")
public class CommonController {

    @Autowired
    private IUserService userService;

    @GetMapping(value = "select/user")
    public ModelAndView selectUser() {
        return new ModelAndView("common/select_user");
    }

    @PostMapping(value = "fileUpload")
    public R fileUpload(@RequestParam(value = "file") MultipartFile file, String type) {
        if (file == null) {
            return R.fail("没有可上传的文件");
        }
        UploadPathEnum uploadPathEnum = UploadPathEnum.get(type);
        if (uploadPathEnum == null) {
            return R.fail("不存在的上传路径类型：" + type);
        }
        return R.ok(FileUploadKit.upload(file, uploadPathEnum));
    }

    @GetMapping(value = "download")
    public void download(String filePath) {
        if (StrUtil.isBlank(filePath)) {
            throw new KvfException("不存在的文件：" + filePath);
        }
        HttpServletResponse response = HttpServletContextKit.getHttpServletResponse();
        ServletUtil.write(response, new File(filePath));
    }

    /**
     * 访问项目外部静态图片
     */
    @GetMapping(value = "static/{fileType}/{yyyyMMdd}/{filename}")
    public void staticImage(@PathVariable String fileType, @PathVariable String yyyyMMdd, @PathVariable String filename) {
        String basePath = System.getProperty("user.dir") + File.separator + Constants.BASE_USER_FILE_PATH;
        String fileUrl = basePath + "/" + fileType + "/" + yyyyMMdd + "/" + filename;
        String suffix = filename.substring(filename.lastIndexOf(".") + 1);
        try {
            HttpServletResponse response = HttpServletContextKit.getHttpServletResponse();
            ServletUtil.write(response, new FileInputStream(new File(fileUrl)), "image/" + suffix);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new KvfException("访问静态图片【" + fileUrl + "】出错：" + e.getMessage());
        }
    }

    /**
     * 使用流访问项目外部的静态文件
     */
    @GetMapping(value = "static/{uploadType}/{fileType}/{yyyyMMdd}/{filename}")
    public void staticFile(@PathVariable String uploadType, @PathVariable String fileType, @PathVariable String yyyyMMdd, @PathVariable String filename) {
        String basePath = System.getProperty("user.dir") + File.separator + Constants.BASE_USER_FILE_PATH;
        String fileUrl = basePath + "/" + uploadType + "/" + fileType + "/" + yyyyMMdd + "/" + filename;
        HttpServletResponse response = HttpServletContextKit.getHttpServletResponse();
        ServletUtil.write(response, new File(fileUrl));
    }

    @GetMapping(value = "search/user")
    public R searchUsers(String keyword) {
        return R.ok(userService.search(keyword));
    }

}
