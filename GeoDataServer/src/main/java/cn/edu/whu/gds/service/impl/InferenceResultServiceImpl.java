package cn.edu.whu.gds.service.impl;

import cn.edu.whu.gds.bean.entity.InferenceResult;
import cn.edu.whu.gds.bean.vo.Response;
import cn.edu.whu.gds.mapper.InferenceResultMapper;
import cn.edu.whu.gds.mapper.InferenceTaskMapper;
import cn.edu.whu.gds.service.InferenceResultService;
import cn.edu.whu.gds.util.Bucket;
import cn.edu.whu.gds.util.HttpResponseUtil;
import cn.edu.whu.gds.util.MinioUtil;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class InferenceResultServiceImpl implements InferenceResultService {
    private final static Bucket BUCKET = Bucket.TILES;
    @Autowired
    private InferenceResultMapper inferenceResultMapper;
    @Autowired
    private InferenceTaskMapper inferenceTaskMapper;
    @Autowired
    private HttpResponseUtil httpResponseUtil;
    @Autowired
    private MinioUtil minioUtil;

    @Override
    public void getInferenceResult(Integer id, HttpServletResponse response) {
        InferenceResult inferenceResult = inferenceResultMapper.getInferenceResult(id);
        minioUtil.download(response, BUCKET,
                "result/" + inferenceResult.getPath() + inferenceResult.getName() + ".geojson");
    }

    @Override
    @Transactional
    public Response addInferenceResult(InferenceResult inferenceResult) {
//        try {
//            minioUtil.put(geoJson, BUCKET,
//                    "result/" + inferenceResult.getPath() + inferenceResult.getName() + ".geojson");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        if (!inferenceResultMapper.addInferenceResult(inferenceResult)) {
            return httpResponseUtil.failure("推理结果添加失败");
        }
        Integer taskId = inferenceResult.getTaskId();
        inferenceTaskMapper.updateInferenceTask(taskId, (short) 1);
        inferenceTaskMapper.setInferenceTaskEndTime(taskId);
        Integer resultId = inferenceResultMapper.getMaxId();
        Map<String, Integer> ans = new HashMap<>();
        ans.put("id", resultId);
        return httpResponseUtil.ok("已添加推理结果", ans);
    }

    @Override
    public Response updateInferenceResult() {
        return null;
    }
}
