package com.kalvin.kvf.common.config;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * 作用：生成图片验证码配置<br>
 * 说明：(无)
 *
 * @author Kalvin
 * @Date 2019年05月05日 17:13
 */
@Configuration
public class KaptchaConfig {

    @Bean
    public Producer kaptcha() {
        Properties properties = new Properties();
        properties.setProperty("kaptcha.border", "no");
        properties.setProperty("kaptcha.border.color", "105,179,90");
        properties.setProperty("kaptcha.textproducer.font.color", "1,148,134");
        properties.setProperty("kaptcha.background.clear.from", "103,153,228");
        properties.setProperty("kaptcha.background.clear.to", "white");
        properties.setProperty("kaptcha.image.width", "250");
        properties.setProperty("kaptcha.image.height", "90");
        properties.setProperty("kaptcha.session.key", "code");
        properties.setProperty("kaptcha.textproducer.char.length", "5");
        properties.setProperty("kaptcha.textproducer.font.names", "宋体,楷体,微软雅黑");
        Config config = new Config(properties);
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}
