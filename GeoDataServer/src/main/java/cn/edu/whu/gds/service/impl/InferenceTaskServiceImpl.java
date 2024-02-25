package cn.edu.whu.gds.service.impl;

import cn.edu.whu.gds.bean.entity.InferenceTask;
import cn.edu.whu.gds.bean.vo.Response;
import cn.edu.whu.gds.mapper.InferenceTaskMapper;
import cn.edu.whu.gds.service.InferenceResultService;
import cn.edu.whu.gds.service.InferenceTaskService;
import cn.edu.whu.gds.util.HttpResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InferenceTaskServiceImpl implements InferenceTaskService {
    @Autowired
    private InferenceTaskMapper inferenceTaskMapper;
    @Autowired
    private HttpResponseUtil httpResponseUtil;

    @Override
    public Response getInferenceTask() {
        return null;
    }

    @Override
    public Response addInferenceTask(InferenceTask inferenceTask) {
        if (!inferenceTaskMapper.addInferenceTask(inferenceTask)) {
            return httpResponseUtil.failure("推理结果添加失败");
        }
        return httpResponseUtil.ok("已添加推理结果");
    }

    @Override
    public Response updateInferenceTask() {
        return null;
    }

    @Override
    public Response deleteInferenceTask() {
        return null;
    }
}
