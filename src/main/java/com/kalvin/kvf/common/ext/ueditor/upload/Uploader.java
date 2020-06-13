package com.kalvin.kvf.common.ext.ueditor.upload;

import com.kalvin.kvf.common.ext.ueditor.define.State;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class Uploader {

    private IStorageManager storage;
	private HttpServletRequest request = null;
	private Map<String, Object> conf = null;

	public Uploader(HttpServletRequest request, Map<String, Object> conf) {
		this(new StorageManager(), request, conf);
	}

    public Uploader(IStorageManager storage, HttpServletRequest request, Map<String, Object> conf) {
        this.storage = storage;
        this.request = request;
        this.conf = conf;
    }

	public final State doExec() {
		String filedName = (String) this.conf.get("fieldName");
		State state = null;

		if ("true".equals(this.conf.get("isBase64"))) {
			state = new Base64Uploader(storage).save(this.request.getParameter(filedName), this.conf);
		} else {
			state = new BinaryUploader(storage).save(this.request, this.conf);
		}

		return state;
	}
}
