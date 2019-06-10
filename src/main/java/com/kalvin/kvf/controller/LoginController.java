package com.kalvin.kvf.controller;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.kalvin.kvf.comm.annotation.Action;
import com.kalvin.kvf.comm.utils.HttpServletContextUtil;
import com.kalvin.kvf.comm.utils.ShiroUtils;
import com.kalvin.kvf.dto.R;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 作用：LayOA系统登录<br>
 * 说明：(无)
 *
 * @author Kalvin
 * @Date 2019年05月05日 15:14
 */
@RestController
public class LoginController extends BaseController {

    @Autowired
    private Producer producer;

    @GetMapping(value = "login")
    public ModelAndView login() {
        Subject subject = ShiroUtils.getSubject();
        if (subject.isAuthenticated()) {
            return new ModelAndView("redirect:");
        }
        return new ModelAndView("login");
    }

    @Action("登录")
    @PostMapping(value = "login")
    public R login(@RequestParam("username") String username, @RequestParam("password") String password, String vercode) {
        // todo 先不要验证码
        /*String kaptcha = ShiroUtils.getKaptcha(Constants.KAPTCHA_SESSION_KEY);
        if (!vercode.equalsIgnoreCase(kaptcha)) {
            return R.fail("验证码不正确");
        }*/

        try {
            Subject subject = ShiroUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            subject.login(token);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return R.fail(e.getMessage());
        }

        LOGGER.info("{}登陆", username);
        return R.ok();
    }

    @Action("退出")
    @GetMapping(value = "logout")
    public ModelAndView logout() {
        String username = ShiroUtils.getUser().getUsername();
        ShiroUtils.logout();
        LOGGER.info("{}退出登录", username);
        return new ModelAndView("redirect:/login");
    }

    /**
     * 获取图片验证码
     * @throws IOException
     */
    @GetMapping("captcha.jpg")
    public void captcha() throws IOException {
        HttpServletResponse response = HttpServletContextUtil.getHttpServletResponse();
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");

        // 生成文字验证码
        String text = producer.createText();
        // 生成图片验证码
        BufferedImage image = producer.createImage(text);
        // 保存到shiro session
        ShiroUtils.setSessionAttribute(Constants.KAPTCHA_SESSION_KEY, text);

        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);
    }


}