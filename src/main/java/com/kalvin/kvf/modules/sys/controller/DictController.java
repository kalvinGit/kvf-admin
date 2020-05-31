package com.kalvin.kvf.modules.sys.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kalvin.kvf.common.controller.BaseController;
import com.kalvin.kvf.common.dto.R;
import com.kalvin.kvf.modules.sys.entity.Dict;
import com.kalvin.kvf.modules.sys.service.DictService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


/**
 * <p>
 * 字典表 前端控制器
 * </p>
 * @since 2019-08-10 16:00:10
 */
@RestController
@RequestMapping("sys/dict")
public class DictController extends BaseController {

    @Autowired
    private DictService dictService;

    @RequiresPermissions("sys:dict:index")
    @GetMapping("index")
    public ModelAndView index() {
        return new ModelAndView("sys/dict");
    }

    @GetMapping(value = "edit")
    public ModelAndView edit(Long id) {
        ModelAndView mv = new ModelAndView("sys/dict_edit");
        Dict dict;
        if (id == null) {
            dict = new Dict();
        } else {
            dict = dictService.getById(id);
        }
        mv.addObject("editInfo", dict);
        return mv;
    }

    @GetMapping(value = "list/data")
    public R listData(Dict dict) {
        Page<Dict> page = dictService.listDictPage(dict);
        return R.ok(page);
    }

    @RequiresPermissions("sys:dict:add")
    @PostMapping(value = "add")
    public R add(Dict dict) {
        Dict d = dictService.getByCode(dict.getCode());
        if (d != null) {
            return R.fail("字典码({code})已被使用".replace("{code}", dict.getCode()));
        }
        dictService.save(dict);
        return R.ok();
    }

    @RequiresPermissions("sys:dict:del")
    @PostMapping(value = "batchdel")
    public R batchdel(@RequestParam("ids") List<Long> ids) {
        dictService.removeByIds(ids);
        return R.ok();
    }

    @RequiresPermissions("sys:dict:edit")
    @PostMapping(value = "edit")
    public R edit(Dict dict) {
        Dict d = dictService.getByCode(dict.getCode());
        if (d != null) {
            return R.fail("字典码({code})已被使用".replace("{code}", dict.getCode()));
        }
        dictService.updateById(dict);
        return R.ok();
    }

    @RequiresPermissions("sys:dict:del")
    @PostMapping(value = "del/{id}")
    public R del(@PathVariable Long id) {
        dictService.deleteWithChildren(id);
        return R.ok();
    }

    @GetMapping(value = "get/{id}")
    public R get(@PathVariable Long id) {
        return R.ok(dictService.getById(id));
    }

}

