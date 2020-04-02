package com.kalvin.kvf.common.controller;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.json.JSONUtil;
import com.baidu.ueditor.ActionEnter;
import com.kalvin.kvf.KvfAdminApplication;
import com.kalvin.kvf.common.exception.KvfException;
import com.kalvin.kvf.common.utils.HttpServletContextKit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * Create by Kalvin on 2020/4/2.
 */
@Slf4j
@RestController
@RequestMapping(value = "ueditor")
public class UeditorController {

    /**
     * ueditor服务端统一接口
     * @param action 动作参数
     * @return obj
     */
    @RequestMapping(value = "api")
    public Object api(String action) throws Exception {
        switch (action) {
            case "ueditor.config":
                InputStream is = KvfAdminApplication.class.getResourceAsStream("/ueditor/config.json");
                byte[] bytes;
                try {
                    int available = is.available();
                    bytes = new byte[available];
                    is.read(bytes);
                } catch (IOException e) {
                    throw new KvfException("读取ueditor.config文件异常:" +  e.getMessage());
                }
                String s = new String(bytes, StandardCharsets.UTF_8);
                return JSONUtil.parseObj(s);
            case "uploadimage":
            case "uploadscrawl":
            case "catchimage":
            case "uploadvideo":
            case "uploadfile":
                this.handle();

        }
        return null;
    }

    public void handle() throws Exception {
        HttpServletRequest request = HttpServletContextKit.getHttpServletRequest();
        HttpServletResponse response = HttpServletContextKit.getHttpServletResponse();
        PrintWriter out = response.getWriter();
        request.setCharacterEncoding( "utf-8" );
        response.setHeader("Content-Type" , "text/html");
//        String rootPath0 = request.getSession().getServletContext().getRealPath( "/" );
//        log.debug("jar realPath={}", rootPath0);
        String rootPath = ClassUtil.getClassPath();
        log.debug("ueditor path={}", rootPath);
        out.write(new ActionEnter(request, rootPath).exec());
    }
}
