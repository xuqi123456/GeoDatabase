package cn.edu.whu.gds.service;

import cn.edu.whu.gds.bean.entity.InferenceTask;
import cn.edu.whu.gds.bean.request.InferenceTaskRequest;
import cn.edu.whu.gds.bean.vo.Response;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;

@Service
public interface InferenceTaskService {
    Response getInferenceTaskCatalog();

    Response getInferenceTask();

    Response addInferenceTask(InferenceTaskRequest inferenceTaskRequest) throws URISyntaxException;

    Response updateInferenceTask(Integer id, Short state);

    Response deleteInferenceTask();
}
