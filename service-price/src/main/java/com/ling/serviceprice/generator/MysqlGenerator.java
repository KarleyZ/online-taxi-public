package com.ling.serviceprice.generator;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

/**
 * 自动生成代码实体类
 */
public class MysqlGenerator {
    /*public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/service-driver-user?characterEncoding=utf-8&serverTimezone=GMT%2B8","root","123456")
                .globalConfig(builder -> {
                    builder.author("ling").fileOverride().outputDir("E:\\MSB\\online-taxi-public\\service-driver-user\\src\\main\\java");
                })
                .packageConfig(builder -> {
                    builder.parent("com.ling.servicedriveruser").pathInfo(Collections.singletonMap(OutputFile.mapperXml,
                            "E:\\MSB\\online-taxi-public\\service-driver-user\\src\\main\\java\\com\\ling\\servicedriveruser\\mapper"));
                }).strategyConfig(builder -> {
                    builder.addInclude("car");
                })
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }*/
    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/service-price?characterEncoding=utf-8&serverTimezone=GMT%2B8","root","123456")
                .globalConfig(builder -> {
                    builder.author("ling").fileOverride().outputDir("E:\\MSB\\online-taxi-public\\service-price\\src\\main\\java");
                })
                .packageConfig(builder -> {
                    builder.parent("com.ling.serviceprice").pathInfo(Collections.singletonMap(OutputFile.mapperXml,
                            "E:\\MSB\\online-taxi-public\\service-price\\src\\main\\java\\com\\ling\\serviceprice\\mapper"));
                }).strategyConfig(builder -> {
                    builder.addInclude("price_rule");
                })
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }
}
