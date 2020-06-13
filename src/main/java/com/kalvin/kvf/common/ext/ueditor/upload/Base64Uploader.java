package com.kalvin.kvf.common.ext.ueditor.upload;

import com.kalvin.kvf.common.ext.ueditor.PathFormat;
import com.kalvin.kvf.common.ext.ueditor.define.AppInfo;
import com.kalvin.kvf.common.ext.ueditor.define.BaseState;
import com.kalvin.kvf.common.ext.ueditor.define.FileType;
import com.kalvin.kvf.common.ext.ueditor.define.State;
import org.apache.commons.codec.binary.Base64;

import java.util.Map;

public final class Base64Uploader {

    private IStorageManager storage;

    public Base64Uploader(IStorageManager storage) {
        this.storage = storage;
    }

    public State save(String content, Map<String, Object> conf) {

        byte[] data = decode(content);

        long maxSize = ((Long) conf.get("maxSize")).longValue();

        if (!validSize(data, maxSize)) {
            return new BaseState(false, AppInfo.MAX_SIZE);
        }

        String suffix = FileType.getSuffix("JPG");

        String savePath = PathFormat.parse((String) conf.get("savePath"),
            (String) conf.get("filename"));

        savePath = savePath + suffix;
        String rootPath = (String) conf.get("rootPath");

        State storageState = storage.saveBinaryFile(data, rootPath, savePath);

        if (storageState.isSuccess()) {
            storageState.putInfo("type", suffix);
            storageState.putInfo("original", "");
        }

        return storageState;
    }

    private static byte[] decode(String content) {
        return Base64.decodeBase64(content);
    }

    private static boolean validSize(byte[] data, long length) {
        return data.length <= length;
    }

}