package com.kalvin.kvf.gen.utils;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.File;
import java.io.FileWriter;

/**
 * create by Kalvin on 2019/06/13 21:41
 */
public class VelocityKit {

    private final static String PRE_TEMP_PATH = "templates/gen/vm/";

    private static VelocityEngine velocityEngine = null;

    private static void newEngine() {
        System.out.println("newEngine");
        VelocityEngine ve = new VelocityEngine();
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        ve.init();
        velocityEngine = ve;
    }

    public static Template getTemplate(String templateName) {
        if (velocityEngine == null) {
            VelocityKit.newEngine();
        }
        return velocityEngine.getTemplate(PRE_TEMP_PATH + templateName);
    }

    public static VelocityContext getContext() {
        return new VelocityContext();
    }

    public static void toFile(String templateName, VelocityContext ctx, String destPath) {
        try {
            File file = new File(destPath);
            if (file.getParentFile() != null && !file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            Template template = VelocityKit.getTemplate(templateName);
            FileWriter fw = new FileWriter(destPath);
            template.merge(ctx, fw);
            fw.flush();
        } catch (Exception e) {
            throw new RuntimeException("生成代码模板时出错：" + e.getMessage());
        }
    }
}
