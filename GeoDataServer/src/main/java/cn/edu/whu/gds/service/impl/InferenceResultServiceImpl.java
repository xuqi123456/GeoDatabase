package cn.edu.whu.gds.service.impl;

import cn.edu.whu.gds.bean.entity.InferenceResult;
import cn.edu.whu.gds.bean.vo.Response;
import cn.edu.whu.gds.mapper.InferenceResultMapper;
import cn.edu.whu.gds.service.InferenceResultService;
import cn.edu.whu.gds.util.HttpResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InferenceResultServiceImpl implements InferenceResultService {
    @Autowired
    private InferenceResultMapper inferenceResultMapper;
    @Autowired
    private HttpResponseUtil httpResponseUtil;

    @Override
    public Response getInferenceResult(String name) {
        return httpResponseUtil.ok("获得推理结果", inferenceResultMapper.getInferenceResult(name));
    }

    @Override
    public Response addInferenceResult(InferenceResult inferenceResult) {
        if (!inferenceResultMapper.addInferenceResult(inferenceResult)) {
            return httpResponseUtil.failure("推理结果添加失败");
        }
        return httpResponseUtil.ok("已添加推理结果");
    }

    @Override
    public Response updateInferenceResult() {
        return null;
    }
}
