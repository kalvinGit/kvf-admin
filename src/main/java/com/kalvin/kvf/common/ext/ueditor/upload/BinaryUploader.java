package com.kalvin.kvf.common.ext.ueditor.upload;

import com.kalvin.kvf.common.ext.ueditor.PathFormat;
import com.kalvin.kvf.common.ext.ueditor.define.AppInfo;
import com.kalvin.kvf.common.ext.ueditor.define.BaseState;
import com.kalvin.kvf.common.ext.ueditor.define.FileType;
import com.kalvin.kvf.common.ext.ueditor.define.State;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class BinaryUploader {

    private IStorageManager storage;

    public BinaryUploader(IStorageManager storage) {
        this.storage = storage;
    }

    public State save(HttpServletRequest request, Map<String, Object> conf) {
        if (request instanceof MultipartRequest) {
            return doSave((MultipartRequest) request, conf);
        } else {
            return doSave(request, conf);
        }
    }

    protected State doSave(MultipartRequest request, Map<String, Object> conf) {
        Map<String, MultipartFile> map = request.getFileMap();

        try {
            MultipartFile file = null;
            for (MultipartFile temp : map.values()) {
                if (!temp.isEmpty()) {
                    file = temp;
                    break;
                }
            }

            if (file == null) {
                return new BaseState(false, AppInfo.NOTFOUND_UPLOAD_DATA);
            }

            String savePath = (String) conf.get("savePath");
            String originFileName = file.getOriginalFilename();
            String suffix = FileType.getSuffixByFilename(originFileName);

            originFileName = originFileName.substring(0, originFileName.length() - suffix.length());
            savePath = savePath + suffix;

            long maxSize = ((Long) conf.get("maxSize")).longValue();

            if (!validType(suffix, (String[]) conf.get("allowFiles"))) {
                return new BaseState(false, AppInfo.NOT_ALLOW_FILE_TYPE);
            }

            savePath = PathFormat.parse(savePath, originFileName);

            String rootPath = (String) conf.get("rootPath");

            InputStream is = file.getInputStream();
            State storageState = storage.saveFileByInputStream(is, rootPath, savePath, maxSize);
            is.close();

            if (storageState.isSuccess()) {
                storageState.putInfo("type", suffix);
                storageState.putInfo("original", originFileName + suffix);
            }

            return storageState;
        } catch (IOException e) {
            return new BaseState(false, AppInfo.IO_ERROR);
        }
    }

    protected State doSave(HttpServletRequest request, Map<String, Object> conf) {
        FileItemStream fileStream = null;
        boolean isAjaxUpload = request.getHeader( "X_Requested_With" ) != null;

        if (!ServletFileUpload.isMultipartContent(request)) {
            return new BaseState(false, AppInfo.NOT_MULTIPART_CONTENT);
        }

        ServletFileUpload upload = new ServletFileUpload(
            new DiskFileItemFactory());

        if ( isAjaxUpload ) {
            upload.setHeaderEncoding( "UTF-8" );
        }

        try {
            FileItemIterator iterator = upload.getItemIterator(request);

            while (iterator.hasNext()) {
                fileStream = iterator.next();

                if (!fileStream.isFormField()) {
                    break;
                }
                fileStream = null;
            }

            if (fileStream == null) {
                return new BaseState(false, AppInfo.NOTFOUND_UPLOAD_DATA);
            }

            String savePath = (String) conf.get("savePath");
            String originFileName = fileStream.getName();
            String suffix = FileType.getSuffixByFilename(originFileName);

            originFileName = originFileName.substring(0, originFileName.length() - suffix.length());
            savePath = savePath + suffix;

            long maxSize = ((Long) conf.get("maxSize")).longValue();

            if (!validType(suffix, (String[]) conf.get("allowFiles"))) {
                return new BaseState(false, AppInfo.NOT_ALLOW_FILE_TYPE);
            }

            savePath = PathFormat.parse(savePath, originFileName);

            String rootPath = (String) conf.get("rootPath");

            InputStream is = fileStream.openStream();
            State storageState = storage.saveFileByInputStream(is, rootPath, savePath, maxSize);
            is.close();

            if (storageState.isSuccess()) {
                storageState.putInfo("type", suffix);
                storageState.putInfo("original", originFileName + suffix);
            }

            return storageState;
        } catch (FileUploadException e) {
            return new BaseState(false, AppInfo.PARSE_REQUEST_ERROR);
        } catch (IOException e) {
        }
        return new BaseState(false, AppInfo.IO_ERROR);
    }

    private static boolean validType(String type, String[] allowTypes) {
        List<String> list = Arrays.asList(allowTypes);

        return list.contains(type);
    }
}
