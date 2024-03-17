package cn.edu.whu.gds.service.impl;

import cn.edu.whu.gds.bean.catalog.InferenceTaskCatalog;
import cn.edu.whu.gds.bean.entity.InferenceResult;
import cn.edu.whu.gds.bean.entity.InferenceTask;
import cn.edu.whu.gds.bean.request.InferenceTaskRequest;
import cn.edu.whu.gds.bean.vo.Response;
import cn.edu.whu.gds.bean.vo.Road;
import cn.edu.whu.gds.mapper.InferenceResultMapper;
import cn.edu.whu.gds.mapper.InferenceTaskMapper;
import cn.edu.whu.gds.mapper.TiffInferenceTaskMapper;
import cn.edu.whu.gds.mapper.TiffRepository;
import cn.edu.whu.gds.service.InferenceTaskService;
import cn.edu.whu.gds.util.HttpResponseUtil;
import com.alibaba.fastjson.JSONObject;
//import com.sun.org.apache.xpath.internal.operations.String;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.description.type.TypeList;
import org.apache.hadoop.yarn.webapp.hamlet.Hamlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.client.AsyncRestTemplate;

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
    private TiffInferenceTaskMapper tiffInferenceTaskMapper;
    @Autowired
    private TiffRepository tiffRepository;
    @Autowired
    private HttpResponseUtil httpResponseUtil;
    @Autowired
    private AsyncRestTemplate restTemplate;

    @Override
    public Response getInferenceTaskCatalog() {
        // TODO: 两表JOIN一次查完
        // 获取所有推理任务
        List<InferenceTask> inferenceTaskList = inferenceTaskMapper.getAllInferenceTask();
        // 创建任务目录
        List<InferenceTaskCatalog> ans = new ArrayList<>();
        Map<Integer, Map<String, Map<Integer, InferenceTaskCatalog>>> imageTaskMap = new HashMap<>();
        Integer typeId = 1;
        Integer imageCount = 1;
        List<String> typeList = new ArrayList<>();
        // 将任务放到对应的影像之下
        for (InferenceTask inferenceTask : inferenceTaskList) {
            Integer taskId = inferenceTask.getId();
            List<Integer> imageIdList = tiffInferenceTaskMapper.getImageIdByTaskId(taskId);
            String type = inferenceTask.getType();
            String taskName = inferenceTaskMapper.getTaskNameById(taskId);
            typeList.add(type);
            if (imageIdList != null) {
                for (Integer imageId : imageIdList) {
                    if (!imageTaskMap.containsKey(imageId)) {
                        imageTaskMap.put(imageId, new HashMap<>());
                    }
                    if (!imageTaskMap.get(imageId).containsKey(type)) {
                        imageTaskMap.get(imageId).put(type, new HashMap<>());
                    }
                    if (!imageTaskMap.get(imageId).get(type).containsKey(taskId)) {
                        imageTaskMap.get(imageId).get(type).put(taskId, new InferenceTaskCatalog(taskId, taskName, inferenceTask.getState()));
                    }
                }
            }
        }
        // 所有的任务类型
        Set<String> typeSet = new HashSet<>(typeList);
        // 影像层
        for (Map.Entry<Integer, Map<String, Map<Integer, InferenceTaskCatalog>>> firstMap : imageTaskMap.entrySet()) {
            if (imageCount <= 10) {
                String imageName = tiffInferenceTaskMapper.getImageNameById(firstMap.getKey());
                String bbox = tiffInferenceTaskMapper.getImageBboxById(firstMap.getKey());
                bbox = bbox.substring(bbox.indexOf('(') + 1, bbox.indexOf(')'));
                String[] extentList = bbox.split(",");
                List<Double> extent = new ArrayList<>(extentList.length);
                for (String s : extentList) {
                    String[] tmp = s.split(" ");
                    extent.add(Double.parseDouble(tmp[0]));
                    extent.add(Double.parseDouble(tmp[1]));
                }


                String[] imageNameList = imageName.split(",");
                StringBuilder sb = new StringBuilder();
                List<InferenceTaskCatalog> catalog = new ArrayList<>();
                List<InferenceTaskCatalog> children = new ArrayList<>();
                for (String type : typeSet) {
                    // 任务类型层
                    for (Map.Entry<String, Map<Integer, InferenceTaskCatalog>> secondMap : firstMap.getValue().entrySet()) {
                        // 任务类型
                        String taskType = secondMap.getKey();
                        if (taskType.equals(type)) {
                            // 任务层
                            for (Map.Entry<Integer, InferenceTaskCatalog> thirdMap : secondMap.getValue().entrySet()) {
                                Integer taskId = thirdMap.getKey();
                                InferenceTaskCatalog taskCatalog = thirdMap.getValue();
                                String name = taskCatalog.getLabel();
                                List<InferenceResult> inferenceResultList = inferenceResultMapper.getInferenceResultByTaskId(taskId);
                                if (!inferenceResultList.isEmpty()) {
                                    List<InferenceTaskCatalog> results = new ArrayList<>();
                                    // 任务结果层
                                    for (InferenceResult inferenceResult : inferenceResultList) {
                                        results.add(new InferenceTaskCatalog(
                                                inferenceResult.getId(), inferenceResult.getName(), true));
                                    }
                                    children.add(new InferenceTaskCatalog(taskId, name, taskCatalog.getState(), results));
                                } else {
                                    children.add(new InferenceTaskCatalog(taskId, name, taskCatalog.getState()));
                                }
                            }
                        }
                    }
                    // 同一个类型的任务放在一起
                    catalog.add(new InferenceTaskCatalog(typeId, type, children));
                    children = new ArrayList<>();
                    ++typeId;
                }
                for (int i = 0; i < Math.min(imageNameList.length, 10); i++) {
                    sb.append(imageNameList[i]);
                    if (i < 9 && imageNameList.length > i + 1) {
                        // 在每个元素后面添加逗号，除了最后一个元素
                        sb.append(",");
                    }
                }
                String path = "myspcoe:" + sb;
                ans.add(new InferenceTaskCatalog(firstMap.getKey(), String.valueOf(sb), extent, path, catalog));
                ++imageCount;
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
