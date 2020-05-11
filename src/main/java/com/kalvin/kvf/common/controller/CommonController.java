package com.kalvin.kvf.common.controller;

import com.kalvin.kvf.common.constant.UploadPathEnum;
import com.kalvin.kvf.common.dto.R;
import com.kalvin.kvf.common.utils.FileUploadKit;
import com.kalvin.kvf.modules.sys.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

/**
 * 【作用】通用控制层<br>
 * 【说明】（无）
 * @author Kalvin
 * @Date 2019/5/9 20:20
 */
@RestController
@RequestMapping(value = "common")
public class CommonController {

    @Autowired
    private IUserService userService;

    @GetMapping(value = "select/user")
    public ModelAndView selectUser() {
        return new ModelAndView("common/select_user");
    }

    @PostMapping(value = "fileUpload")
    public R fileUpload(@RequestParam(value = "file") MultipartFile file, int type) {
        return R.ok(FileUploadKit.uploadRelative(file, UploadPathEnum.get(type)));
    }

    @GetMapping(value = "search/user")
    public R searchUsers(String keyword) {
        return R.ok(userService.search(keyword));
    }

}
