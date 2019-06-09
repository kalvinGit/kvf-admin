package com.kalvin.kvf.controller;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kalvin.kvf.dto.R;
import com.kalvin.kvf.exception.LayOAException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

public class LoaController<T, S extends IService<T>> extends BaseController {

    @Autowired
    protected S service;

    public ModelAndView index(String viewName) {
        return new ModelAndView(viewName);
    }

    public ModelAndView edit(Long id, String viewName, Class<T> clazz) {
        ModelAndView mv = new ModelAndView(viewName);
        T entity;
        if (id == null) {
            try {
                entity = clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new LayOAException("发生异常:" + e.getMessage());
            }
        } else {
            entity = service.getById(id);
        }
        mv.addObject("editInfo", entity);
        return mv;
    }

    @RequiresPermissions(value = "sys:dept:edit")
    @PostMapping(value = "save")
    public R save(T entity) {
        service.saveOrUpdate(entity);
        return R.ok();
    }

    @PostMapping(value = "remove/{id}")
    public R remove(@PathVariable Long id) {
        service.removeById(id);
        return R.ok();
    }

    @PostMapping(value = "removeBatch")
    public R removeBatch(@RequestParam("ids") List<Long> ids) {
        service.removeByIds(ids);
        return R.ok();
    }

    @GetMapping(value = "get/{id}")
    public R getOne(@PathVariable Long id) {
        return R.ok(service.getById(id));
    }

}
