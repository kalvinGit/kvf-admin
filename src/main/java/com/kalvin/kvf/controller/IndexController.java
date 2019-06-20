package com.kalvin.kvf.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.kalvin.kvf.common.utils.ShiroKit;
import com.kalvin.kvf.dto.R;
import com.kalvin.kvf.entity.sys.User;
import com.kalvin.kvf.service.sys.IMenuService;
import com.kalvin.kvf.service.sys.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;

@RestController
public class IndexController extends BaseController {

    @Autowired
    private IMenuService menuService;

    @Autowired
    private IUserService userService;

    @GetMapping(value = "/")
    public ModelAndView index() {
        User user = userService.getById(ShiroKit.getUserId());
        return new ModelAndView("index").addObject("authUserInfo", user);
    }

    @GetMapping(value = "home")
    public ModelAndView home() {
        return new ModelAndView("home");
    }

    @GetMapping(value = "test")
    public ModelAndView test() {
        return new ModelAndView("test");
    }

    @GetMapping(value = "test2")
    public ModelAndView testEasyui() {
        return new ModelAndView("test-easyui");
    }

    @GetMapping(value = "index/menus")
    public R menus() {
        return R.ok(menuService.listUserPermissionMenuWithSubByUserId(ShiroKit.getUserId()));
    }

    @GetMapping(value = "index/navMenus")
    public R navMenus() {
        return R.ok(menuService.listUserPermissionNavMenuByUserId(ShiroKit.getUserId()));
    }

    @GetMapping(value = "tree/list")
    public R treegridData(String id) {
        JSONObject jsonObject = JSONUtil.parseObj("{\"total\":5,\"rows\":[{\"POPEDOM_NAME\":\"系统审计\",\"TARGET\":null,\"IS_LOG\":2,\"PARENT_ID\":\"0\",\"XH\":541,\"CREATER_ID\":\"9DE5260188E2451F82FEED173E464A1B\",\"NAV_ICON_HEIGHT\":null,\"ENTRY_TIME\":\"2019-05-30 22:00:15\",\"NAV_ICON_WIDTH\":null,\"FORWARD_ACTION\":null,\"ID\":\"42380957052241518e05d9e93a18ba5b\",\"NAV_ICON\":null,\"REMARK\":null,\"TYPE\":1},{\"POPEDOM_NAME\":\"操作日志\",\"TARGET\":null,\"IS_LOG\":2,\"PARENT_ID\":\"42380957052241518e05d9e93a18ba5b\",\"XH\":577,\"CREATER_ID\":\"9DE5260188E2451F82FEED173E464A1B\",\"NAV_ICON_HEIGHT\":null,\"ENTRY_TIME\":\"2018-04-24 21:35:26\",\"NAV_ICON_WIDTH\":null,\"FORWARD_ACTION\":\"/admin/log\",\"ID\":\"0e15bc482d5e4b1b92b8aeee03c3eab2\",\"NAV_ICON\":null,\"REMARK\":null,\"TYPE\":1}]}\n");
        HashMap hashMap = jsonObject.toBean(HashMap.class);
        hashMap.forEach((k, v) -> LOGGER.info("has={},{}", k, v));
        HashMap<String, Object> h = new HashMap<>();
//        h.put("row", "row");
//        h.put("data", "{\"11\", \"22\"}");
        h.putAll(hashMap);

        if (StrUtil.isBlank(id)) {
            return R.ok("{\"total\":5,\"rows\":[{\"POPEDOM_NAME\":\"系统审计\",\"TARGET\":null,\"IS_LOG\":2,\"PARENT_ID\":\"0\",\"XH\":541,\"CREATER_ID\":\"9DE5260188E2451F82FEED173E464A1B\",\"NAV_ICON_HEIGHT\":null,\"ENTRY_TIME\":\"2019-05-30 22:00:15\",\"NAV_ICON_WIDTH\":null,\"FORWARD_ACTION\":null,\"ID\":\"42380957052241518e05d9e93a18ba5b\",\"NAV_ICON\":null,\"REMARK\":null,\"TYPE\":1}]}");
        } else {
            return R.ok("[{\"POPEDOM_NAME\":\"操作日志\",\"TARGET\":null,\"IS_LOG\":2,\"_parentId\":\"42380957052241518e05d9e93a18ba5b\",\"XH\":577,\"CREATER_ID\":\"9DE5260188E2451F82FEED173E464A1B\",\"NAV_ICON_HEIGHT\":null,\"ENTRY_TIME\":\"2018-04-24 21:35:26\",\"NAV_ICON_WIDTH\":null,\"FORWARD_ACTION\":\"/admin/log\",\"ID\":\"0e15bc482d5e4b1b92b8aeee03c3eab2\",\"NAV_ICON\":null,\"REMARK\":null,\"TYPE\":1}]");
        }
    }
}
