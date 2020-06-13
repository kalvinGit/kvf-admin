package com.kalvin.kvf.common.controller;

import com.kalvin.kvf.common.constant.Constants;
import com.kalvin.kvf.common.ext.ueditor.ActionEnter;
import com.kalvin.kvf.common.ext.ueditor.PathFormat;
import com.kalvin.kvf.common.ext.ueditor.define.BaseState;
import com.kalvin.kvf.common.ext.ueditor.define.State;
import com.kalvin.kvf.common.ext.ueditor.upload.IStorageManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Create by Kalvin on 2020/4/2.
 */
@Slf4j
@RestController
@RequestMapping(value = "ueditor")
public class UEditorController {

    @RequestMapping("upload")
    public void upload(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type", "text/html");

//        String rootPath = ClassUtil.getClassPath();
        String rootPath = Constants.BASE_USER_FILE_PATH + "/ueditor";
        response.getWriter().write(new ActionEnter(new FileStoreUEditorUploader(), request, rootPath).exec());
    }

    /**
     * 文件上传目录实现类
     */
    static class FileStoreUEditorUploader implements IStorageManager {

        private final static String BASE_UE_FILE_PATH = System.getProperty("user.dir") + File.separator + Constants.BASE_USER_FILE_PATH;

        @Override
        public State saveBinaryFile(byte[] data, String rootPath, String savePath) {
            return null;
        }

        @Override
        public State saveFileByInputStream(InputStream is, String rootPath, String savePath) {
            return null;
        }

        @Override
        public State saveFileByInputStream(InputStream is, String rootPath, String savePath, long maxSize) {
            String filePath = BASE_UE_FILE_PATH + File.separator + savePath;
            log.debug("rootPath={}", rootPath);
            log.debug("filePath={}", filePath);
            try {
                File file = new File(filePath);
                File parentFile = file.getParentFile();
                if (!parentFile.exists()) {
                    if (parentFile.mkdirs()) {
                        log.debug("has created path:{}", parentFile.getAbsolutePath());
                    }
                }
                FileOutputStream fos = new FileOutputStream(file);
                byte[] b = new byte[1024];
                while ((is.read(b)) != -1) {
                    fos.write(b);// 写入数据
                }
                fos.close();// 保存数据
                BaseState state = new BaseState(true);

                state.putInfo( "size", file.length());
                state.putInfo( "title", file.getName());
                state.putInfo("url", PathFormat.format(savePath));
                return state;
            } catch (IOException e) {
                log.error("ueditor上传文件失败：" + e.getMessage());
                return new BaseState(false);
            }
        }
    }
}
