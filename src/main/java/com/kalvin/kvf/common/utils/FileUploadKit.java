package com.kalvin.kvf.common.utils;

import cn.hutool.core.date.DateUtil;
import com.kalvin.kvf.common.constant.Constants;
import com.kalvin.kvf.common.constant.UploadPathEnum;
import com.kalvin.kvf.common.dto.UploadFileInfo;
import com.kalvin.kvf.common.exception.KvfException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * 文件上传工具
 * @author Kalvin
 */
public class FileUploadKit {

    private final static Logger LOGGER = LoggerFactory.getLogger(FileUploadKit.class);

    public static UploadFileInfo upload(MultipartFile multipartFile, UploadPathEnum pathEnum) {
        String basePath = System.getProperty("user.dir") + File.separator + Constants.BASE_PATH;
        String filename = multipartFile.getOriginalFilename();
        String path = pathEnum.getPath() + "/" + DateUtil.format(DateUtil.date(), "yyyyMMdd");
        String filePath = basePath + "/" + path + "/" + filename;
        LOGGER.debug("upload filename:{}", filename);
        LOGGER.debug("upload filePath:{}", filePath);
        File file = new File(filePath);
        try {
            File parentFile = file.getParentFile();
            if (parentFile != null && !parentFile.exists()) {
                parentFile.mkdirs();
            }
            if (file.exists()) {
                file.delete();
            }
            if (file.createNewFile()) {
                multipartFile.transferTo(file);
            } else {
                throw new KvfException("文件上传失败");
            }
        } catch (IOException e) {
            throw new KvfException("文件上传失败：" + e.getMessage() + filePath);
        }

        return new UploadFileInfo(filename, path + "/" + filename, filePath.replaceAll("\\\\", "\\/"));
    }

}
