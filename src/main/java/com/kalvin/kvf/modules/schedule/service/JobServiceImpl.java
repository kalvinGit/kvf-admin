package com.kalvin.kvf.modules.schedule.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kalvin.kvf.common.exception.KvfException;
import com.kalvin.kvf.modules.schedule.constant.JobConstant;
import com.kalvin.kvf.modules.schedule.utils.ScheduleKit;
import org.springframework.stereotype.Service;
import com.kalvin.kvf.modules.schedule.entity.Job;
import com.kalvin.kvf.modules.schedule.mapper.JobMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 定时任务表 服务实现类
 * </p>
 * @since 2019-08-17 17:06:12
 */
@Service
public class JobServiceImpl extends ServiceImpl<JobMapper, Job> implements JobService {

    @Override
    public Page<Job> listJobPage(Job job) {
        Page<Job> page = new Page<>(job.getCurrent(), job.getSize());
        List<Job> jobs = baseMapper.selectJobList(job, page);
        return page.setRecords(jobs);
    }

    @Transactional
    @Override
    public void createJob(Job job) {
        job.setStatus(JobConstant.JOB_STATUS_PAUSE);
        super.save(job);
        ScheduleKit.create(job.getId(), job);
    }

    @Transactional
    @Override
    public void pauseJob(Long id) {
        Job job = this.checkJobExist(id);
        job.setStatus(JobConstant.JOB_STATUS_PAUSE);
        super.updateById(job);
        ScheduleKit.pause(id);
    }

    @Transactional
    @Override
    public void resumeJob(Long id) {
        Job job = this.checkJobExist(id);
        job.setStatus(JobConstant.JOB_STATUS_RUNNING);
        super.updateById(job);
        ScheduleKit.resume(id);
    }

    @Transactional
    @Override
    public void updateJob(Job job) {
        super.updateById(job);
        ScheduleKit.update(job.getId(), job);
    }

    @Transactional
    @Override
    public void deleteJob(Long id) {
        super.removeById(id);
        ScheduleKit.delete(id);
    }

    @Override
    public Job checkJobExist(Long id) {
        Job job = super.getById(id);
        if (job == null) {
            throw new KvfException("不存在的任务ID");
        }
        return job;
    }

}
