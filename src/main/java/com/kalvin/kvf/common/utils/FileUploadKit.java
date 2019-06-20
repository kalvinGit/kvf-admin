package com.kalvin.kvf.common.utils;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import com.kalvin.kvf.common.constant.UploadPathEnum;
import com.kalvin.kvf.common.dto.UploadFileInfo;
import com.kalvin.kvf.common.exception.KvfException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * 文件上传工具
 * @author Kalvin
 */
public class FileUploadKit {

    private final static Logger LOGGER = LoggerFactory.getLogger(FileUploadKit.class);

    public static UploadFileInfo upload(MultipartFile multipartFile, UploadPathEnum pathEnum, String toPath) {
        String path;
        String uploadPath;
        String filename;
        try {
            if (multipartFile == null) {
                throw new KvfException("没有可上传的文件");
            }

            filename = multipartFile.getOriginalFilename();

            if (pathEnum == null && StrUtil.isBlank(toPath)) {
                throw new KvfException("上传路径不存在");
            } else if (pathEnum != null) {
                path = pathEnum.getPath() + filename;
                uploadPath = ClassUtil.getClassPath() +  path;
            } else {
                path = toPath + filename;
                uploadPath = path;
            }
            LOGGER.info("uploadPath={}", uploadPath);

            File file = new File(uploadPath);
            // 判断父目录是否存在，如果不存在，则创建
            if (file.getParentFile() != null && !file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
            multipartFile.transferTo(file);
            return new UploadFileInfo(filename, path, uploadPath);
        } catch (Exception e) {
            throw new KvfException("上传文件失败：" + e.getMessage());
        }

    }

    public static UploadFileInfo uploadRelative(MultipartFile multipartFile, UploadPathEnum pathEnum) {
        return FileUploadKit.upload(multipartFile, pathEnum, null);
    }

    public static UploadFileInfo uploadAbsolute(MultipartFile multipartFile, String toPath) {
        return FileUploadKit.upload(multipartFile, null, toPath);
    }
}
