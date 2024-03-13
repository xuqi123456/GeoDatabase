package cn.edu.whu.gds.service.impl;

import cn.edu.whu.gds.bean.catalog.InferenceTaskCatalog;
import cn.edu.whu.gds.bean.entity.InferenceResult;
import cn.edu.whu.gds.bean.entity.InferenceTask;
import cn.edu.whu.gds.bean.request.InferenceTaskRequest;
import cn.edu.whu.gds.bean.vo.Response;
import cn.edu.whu.gds.bean.vo.Road;
import cn.edu.whu.gds.mapper.InferenceResultMapper;
import cn.edu.whu.gds.mapper.InferenceTaskMapper;
import cn.edu.whu.gds.mapper.TiffRepository;
import cn.edu.whu.gds.service.InferenceResultService;
import cn.edu.whu.gds.service.InferenceTaskService;
import cn.edu.whu.gds.util.HttpResponseUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

@Slf4j
@Service
@Transactional
public class InferenceTaskServiceImpl implements InferenceTaskService {
    @Autowired
    private InferenceTaskMapper inferenceTaskMapper;
    @Autowired
    private InferenceResultMapper inferenceResultMapper;
    @Autowired
    private TiffRepository tiffRepository;
    @Autowired
    private HttpResponseUtil httpResponseUtil;
    @Autowired
    private AsyncRestTemplate restTemplate;

    @Override
    public Response getInferenceTaskCatalog() {
        // TODO: 两表JOIN一次查完
        List<InferenceTask> inferenceTaskList = inferenceTaskMapper.getAllInferenceTask();
        Map<String, InferenceTaskCatalog> inferenceTaskMap = new HashMap<>();
        List<InferenceTaskCatalog> ans = new ArrayList<>();

        Integer typeId = 1;
        for (InferenceTask inferenceTask : inferenceTaskList) {
            Integer id = inferenceTask.getId();
            String name = inferenceTask.getName();
            String type = inferenceTask.getType();
            List<InferenceResult> inferenceResultList = inferenceResultMapper.getInferenceResultByTaskId(id);
            if (!inferenceTaskMap.containsKey(type)) {
                List<InferenceTaskCatalog> children = new ArrayList<>();
                if (inferenceResultList.size() > 0) {
                    List<InferenceTaskCatalog> results = new ArrayList<>();
                    for (InferenceResult inferenceResult : inferenceResultList) {
                        results.add(new InferenceTaskCatalog(
                                inferenceResult.getId(), inferenceResult.getName(), true));
                    }
                    children.add(new InferenceTaskCatalog(id, name, inferenceTask.getState(), results));
                } else {
                    children.add(new InferenceTaskCatalog(id, name));
                }
                InferenceTaskCatalog catalog = new InferenceTaskCatalog(typeId, type, children);
                ans.add(catalog);
                inferenceTaskMap.put(type, catalog);
                ++typeId;
            } else {
                if (inferenceResultList.size() > 0) {
                    List<InferenceTaskCatalog> results = new ArrayList<>();
                    for (InferenceResult inferenceResult : inferenceResultList) {
                        results.add(new InferenceTaskCatalog(
                                inferenceResult.getId(), inferenceResult.getName(), true));
                    }
                    inferenceTaskMap.get(type).getChildren().add(
                            new InferenceTaskCatalog(id, name, inferenceTask.getState(), results));
                } else {
                    inferenceTaskMap.get(type).getChildren().add(
                            new InferenceTaskCatalog(id, name, inferenceTask.getState()));
                }
            }
        }

        return httpResponseUtil.ok("获取所有推理任务", ans);
    }

    @Override
    public Response getInferenceTask() {
        return null;
    }

    @Override
    public Response addInferenceTask(InferenceTaskRequest inferenceTaskRequest) throws URISyntaxException {
        if (!inferenceTaskMapper.addInferenceTask(inferenceTaskRequest.getInferenceTask())) {
            return httpResponseUtil.failure("推理任务添加失败");
        }
        Integer taskId = inferenceTaskMapper.getMaxId();
        List<Integer> tiffIds = inferenceTaskRequest.getTiffIds();
        for (Integer tiffId : tiffIds) {
            inferenceTaskMapper.addTiffInferenceTask(tiffId, taskId);
        }
        List<String> paths = tiffRepository.getPathsByIds(tiffIds);
        for (String path : paths) {
            System.out.println(path);
        }
        Road road = new Road(
                "s3://geodata" + paths.get(0) , new ArrayList<>(Arrays.asList(1, 2, 3)),
                inferenceTaskRequest.getGeom(), taskId
        );
        HttpEntity<Road> entity = new HttpEntity<>(road);
        ListenableFuture<ResponseEntity<JSONObject>> ett = restTemplate.postForEntity(
                new URI("http://192.168.30.170:18443/predictions/road"), entity, JSONObject.class);
        ett.addCallback(new ListenableFutureCallback<ResponseEntity<JSONObject>>() {
            //调用失败
            @Override
            public void onFailure(Throwable e) {
                e.printStackTrace();
                log.debug("async rest response failure");
            }
            //调用成功
            @Override
            public void onSuccess(ResponseEntity<JSONObject> result) {
                log.info("async rest response success");
            }
        });
        Map<String, Integer> ans = new HashMap<>();
        ans.put("id", taskId);
        return httpResponseUtil.ok("已添加推理任务", ans);
    }

    @Override
    public Response updateInferenceTask(Integer id, Short state) {
        if (state > 1 || state < -1) {
            return httpResponseUtil.badRequest("state输入有误");
        }
        if (!inferenceTaskMapper.updateInferenceTask(id, state)){
            return httpResponseUtil.failure("推理结果状态更新失败");
        }
        if (state == -1 || state == 1) {
            inferenceTaskMapper.setInferenceTaskEndTime(id);
        }
        return httpResponseUtil.ok("已更新推理结果状态");
    }

    @Override
    public Response deleteInferenceTask() {
        return null;
    }
}
