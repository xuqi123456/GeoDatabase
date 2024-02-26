package cn.edu.whu.gds.service;

import cn.edu.whu.gds.bean.entity.InferenceResult;
import cn.edu.whu.gds.bean.vo.Response;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Service;

@Service
public interface InferenceResultService {
    Response getInferenceResult(String name);

    Response addInferenceResult(InferenceResult inferenceResult);

    Response updateInferenceResult();
}
