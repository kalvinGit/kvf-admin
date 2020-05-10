package com.kalvin.kvf.modules.workflow.service.api;

/**
 * Create by Kalvin on 2020/4/30.
 */
public interface IProcessImage {

    byte[] getFlowImgByProcInstId(String procInstId) throws Exception;

}
