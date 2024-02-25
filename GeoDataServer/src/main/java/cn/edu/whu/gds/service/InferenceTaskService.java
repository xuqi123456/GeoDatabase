package cn.edu.whu.gds.service;

import cn.edu.whu.gds.bean.entity.InferenceTask;
import cn.edu.whu.gds.bean.vo.Response;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Service;

@Service
public interface InferenceTaskService {
    Response getInferenceTask();

    Response addInferenceTask(InferenceTask inferenceTask);

    Response updateInferenceTask();

    Response deleteInferenceTask();
}
