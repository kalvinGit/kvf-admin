package com.kalvin.kvf.modules.workflow.dto;

import cn.hutool.core.bean.BeanUtil;
import com.kalvin.kvf.common.dto.R;
import org.activiti.engine.query.Query;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Create by Kalvin on 2020/4/20.
 */
public class ProcessQuery<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private T data;

    private Long count;

    public ProcessQuery() {

    }

    public ProcessQuery(T data) {
        this.data = data;
    }

    public ProcessQuery(T data, Long count) {
        this.data = data;
        this.count = count;
    }

    public ProcessQuery<T> listPage(Query query, int current, int size) {
        current--;
        this.count = query.count();
        this.data = (T) query.listPage(current * size, size);
        return this;
    }

    public R convertAndToR() {
        // 由于activiti一些实体类是懒加载的，无法直接返回给前端。这里做一下转化
        if (this.data instanceof List) {
            final List<HashMap<String, Object>> list = (List<HashMap<String, Object>>) ((List) this.data).stream().map(BeanUtil::beanToMap).collect(Collectors.toList());
            return R.ok(list).setTotal(this.count);
        }
        return this.toR();
    }

    public R toR() {
        return R.ok(this.data).setTotal(this.count);
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
