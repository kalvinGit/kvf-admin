package com.kalvin.kvf.modules.generator.constant;

import lombok.Getter;

/**
 * Create by Kalvin on 2019/6/15
 */
public class ConfigConstant {

    public final static String CODE_GEN_PATH = "D://genCode";  //  代码生成的路径目录，可根据实际情况修改，不要直接放根目录
    public final static String CODE_FOLDER_NAME = "code";
    public final static String CODE_ZIP_FILENAME = "code.zip";
    public final static String PRE_TEMP_PATH = "templates/generator/vm/";   // 模板文件路径
    public final static String BUTTON_JSON_REL_PATH = "templates/generator/defaultButtonConfig.json"; // 生成按钮配置文件相对路径
    public final static String AUTHOR = ""; // 作者（用于代码注释。可不填）

    public final static String HEAD_BUTTON_KEY = "headButtons";
    public final static String ROW_BUTTON_KEY = "rowButtons";

    /**
     * 包信息配置
     */
    @Getter
    public static class PackageConfig {
        public final String BASE_PACKAGE = "com.kalvin.kvf";   // 项目基包路径，可根据实际情况修改
        public final String ENTITY_PACKAGE = "entity";   // 实体类包，可根据实际情况修改
        public final String MAPPER_PACKAGE = "mapper";   // mapper/dao类包，可根据实际情况修改
        public final String SERVICE_PACKAGE = "service";   // 服务类接口包，可根据实际情况修改
        public final String SERVICE_IMPL_PACKAGE = "service";   // 服务接口实现类包，可根据实际情况修改
        public final String CONTROLLER_PACKAGE = "controller";   // 控制层类包，可根据实际情况修改
        public final String XML_PACKAGE = "xml";   // xml路径，可根据实际情况修改
    }

}
