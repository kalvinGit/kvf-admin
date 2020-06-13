package com.kalvin.kvf.common.ext.ueditor.upload;

import com.kalvin.kvf.common.ext.ueditor.PathFormat;
import com.kalvin.kvf.common.ext.ueditor.define.AppInfo;
import com.kalvin.kvf.common.ext.ueditor.define.BaseState;
import com.kalvin.kvf.common.ext.ueditor.define.State;
import org.apache.commons.io.FileUtils;

import java.io.*;

public class StorageManager implements IStorageManager {
    public static final int BUFFER_SIZE = 8192;

    public StorageManager() {
    }

    @Override
    public State saveBinaryFile(byte[] data, String rootPath, String savePath) {
        File file = new File(rootPath + savePath);

        State state = valid(file);

        if (!state.isSuccess()) {
            return state;
        }

        try {
            BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(file));
            bos.write(data);
            bos.flush();
            bos.close();
        } catch (IOException ioe) {
            return new BaseState(false, AppInfo.IO_ERROR);
        }

        state = new BaseState(true, file.getAbsolutePath());
        state.putInfo("url", PathFormat.format(savePath));
        state.putInfo( "size", data.length );
        state.putInfo( "title", file.getName() );
        return state;
    }

    @Override
    public State saveFileByInputStream(InputStream is, String rootPath, String savePath,
            long maxSize) {
        State state = null;

        File tmpFile = getTmpFile();

        byte[] dataBuf = new byte[ 2048 ];
        BufferedInputStream bis = new BufferedInputStream(is, StorageManager.BUFFER_SIZE);

        try {
            BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(tmpFile), StorageManager.BUFFER_SIZE);

            int count = 0;
            while ((count = bis.read(dataBuf)) != -1) {
                bos.write(dataBuf, 0, count);
            }
            bos.flush();
            bos.close();

            if (tmpFile.length() > maxSize) {
                tmpFile.delete();
                return new BaseState(false, AppInfo.MAX_SIZE);
            }

            state = saveTmpFile(tmpFile, rootPath + savePath);
            state.putInfo("url", PathFormat.format(savePath));

            if (!state.isSuccess()) {
                tmpFile.delete();
            }

            return state;

        } catch (IOException e) {
        }
        return new BaseState(false, AppInfo.IO_ERROR);
    }

    @Override
    public State saveFileByInputStream(InputStream is, String rootPath, String savePath) {
        State state = null;

        File tmpFile = getTmpFile();

        byte[] dataBuf = new byte[ 2048 ];
        BufferedInputStream bis = new BufferedInputStream(is, StorageManager.BUFFER_SIZE);

        try {
            BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(tmpFile), StorageManager.BUFFER_SIZE);

            int count = 0;
            while ((count = bis.read(dataBuf)) != -1) {
                bos.write(dataBuf, 0, count);
            }
            bos.flush();
            bos.close();

            state = saveTmpFile(tmpFile, rootPath + savePath);
            state.putInfo("url", PathFormat.format(savePath));

            if (!state.isSuccess()) {
                tmpFile.delete();
            }

            return state;
        } catch (IOException e) {
        }
        return new BaseState(false, AppInfo.IO_ERROR);
    }

    private static File getTmpFile() {
        File tmpDir = FileUtils.getTempDirectory();
        String tmpFileName = (Math.random() * 10000 + "").replace(".", "");
        return new File(tmpDir, tmpFileName);
    }

    private static State saveTmpFile(File tmpFile, String path) {
        State state = null;
        File targetFile = new File(path);

        if (targetFile.canWrite()) {
            return new BaseState(false, AppInfo.PERMISSION_DENIED);
        }
        try {
            FileUtils.moveFile(tmpFile, targetFile);
        } catch (IOException e) {
            return new BaseState(false, AppInfo.IO_ERROR);
        }

        state = new BaseState(true);
        state.putInfo( "size", targetFile.length() );
        state.putInfo( "title", targetFile.getName() );

        return state;
    }

    private static State valid(File file) {
        File parentPath = file.getParentFile();

        if ((!parentPath.exists()) && (!parentPath.mkdirs())) {
            return new BaseState(false, AppInfo.FAILED_CREATE_FILE);
        }

        if (!parentPath.canWrite()) {
            return new BaseState(false, AppInfo.PERMISSION_DENIED);
        }

        return new BaseState(true);
    }
}
