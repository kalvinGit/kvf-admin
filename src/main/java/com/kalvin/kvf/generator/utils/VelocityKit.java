package com.kalvin.kvf.generator.utils;

import cn.hutool.core.date.DateUtil;
import com.kalvin.kvf.generator.constant.ConfigConstant;
import com.kalvin.kvf.generator.constant.TemplateTypeEnum;
import com.kalvin.kvf.generator.vo.GenConfigVO;
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
            File parentFile = file.getParentFile();
            if (parentFile != null && (!parentFile.exists() || (parentFile.exists() && !parentFile.isDirectory()))) {
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

    /**
     * 所有模板统一生成
     * @param config 代码生成配置数据
     */
    public static void allToFile(GenConfigVO config) {
        VelocityContext ctx = VelocityKit.getContext();
        ctx.put("config", config);
        ctx.put("pack", new ConfigConstant.PackageConfig());
        ctx.put("createTime", DateUtil.now());
        ctx.put("author", ConfigConstant.AUTHOR);
        for (TemplateTypeEnum typeEnum : TemplateTypeEnum.values()) {
            if ("TABLE".equals(typeEnum.getType()) || "TREE_GRID".equals(typeEnum.getType())) {
                if (config.getTableType().equalsIgnoreCase(typeEnum.getType())) {
                    VelocityKit.toFile(typeEnum.getName(), ctx,
                            AuxiliaryKit.getGenerateCodePath(typeEnum, config.getModuleName(), config.getFunName()));
                }
            } else {
                VelocityKit.toFile(typeEnum.getName(), ctx,
                        AuxiliaryKit.getGenerateCodePath(typeEnum, config.getModuleName(), config.getFunName()));
            }
        }
    }
}
