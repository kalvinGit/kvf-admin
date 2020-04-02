package com.kalvin.kvf.common;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;


/**
 * 【作用】代码生成器<br>
 * 【说明】（无）
 * @author Kalvin
 * @Date 2019/4/16 15:11
 */
public class CodeGenerator {

    private static String packageName = "com.kalvin.kvf"; // 生成的包名
    private static String[] tableNames = {"sys_log"};   // 表名
    private static String tablePrefix = "sys_";    // 配置了会自动去掉表名的前缀
    private static boolean serviceNameStartWithI = true;  //user -> UserService, 设置成true: user -> IUserService
    private static String author = "Kalvin";    // 作者
    private static String outputDir = "D://genCode";   // 代码生成的路径目录
    private static String dbUrl = "jdbc:mysql://localhost:3306/activiti_k?allowMultiQueries=true&useUnicode=true&characterEncoding=UTF-8&useSSL=false&tinyInt1isBit=false&serverTimezone=GMT%2B8";
    private static String dbUsername = "root";
    private static String dbPassword = "root";

    public static void main(String[] args) {
        generateByTables(serviceNameStartWithI, packageName, tableNames);
    }

    private static void generateByTables(boolean serviceNameStartWithI, String packageName, String... tableNames) {
        GlobalConfig config = new GlobalConfig();
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDbType(DbType.MYSQL).setUrl(dbUrl).setUsername(dbUsername).setPassword(dbPassword)
                .setDriverName("com.mysql.jdbc.Driver").setTypeConvert((globalConfig, s) -> {
            if (s.contains("tinyint(1)")) { // 自定义数据库表字段类型转换
                return DbColumnType.INTEGER;
            }
            return new MySqlTypeConvert().processTypeConvert(globalConfig, s);
        });
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig
                .setCapitalMode(true)
                .setEntityLombokModel(true)
//                .setDbColumnUnderline(true)
                .setTablePrefix(tablePrefix)
                .setNaming(NamingStrategy.underline_to_camel)
                .setInclude(tableNames)//修改替换成你需要的表名，多个表名传数组
                .setRestControllerStyle(true)
                .setSuperEntityClass("com.kalvin.kvf.common.entity.BaseEntity")
                .setSuperControllerClass("com.kalvin.kvf.common.controller.BaseController");

        config.setActiveRecord(false)
                .setAuthor(author)
                .setOutputDir(outputDir)
                .setFileOverride(true)
                .setEnableCache(false)
                .setBaseResultMap(true)
                .setBaseColumnList(true);
        TemplateConfig tc = new TemplateConfig();
        if (!serviceNameStartWithI) {
            config.setServiceName("%sService");
            config.setMapperName("%sMapper");
        }
        new AutoGenerator().setGlobalConfig(config)
                .setTemplate(tc)
                .setDataSource(dataSourceConfig)
                .setStrategy(strategyConfig)
                .setPackageInfo(
                        new PackageConfig()
                                .setParent(packageName)
                                .setEntity("entity.sys")
                                .setMapper("mapper.sys")
                                .setController("controller.sys")
                                .setService("service.sys")
                                .setServiceImpl("service.sys")
                                .setXml("xml.sys")
                ).execute();
    }
}