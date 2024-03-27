package cn.edu.whu.gds.service;

import cn.edu.whu.gds.bean.entity.InferenceResult;
import cn.edu.whu.gds.bean.vo.Response;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public interface InferenceResultService {
    Response getInferenceResult(Integer id, HttpServletResponse response);

    Response addInferenceResult(InferenceResult inferenceResult) throws IOException;

    Response updateInferenceResult();
}
