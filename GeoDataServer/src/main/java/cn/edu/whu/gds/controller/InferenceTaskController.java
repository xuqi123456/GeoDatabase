package cn.edu.whu.gds.controller;

import cn.edu.whu.gds.bean.entity.InferenceResult;
import cn.edu.whu.gds.bean.entity.InferenceTask;
import cn.edu.whu.gds.bean.vo.Response;
import cn.edu.whu.gds.service.InferenceResultService;
import cn.edu.whu.gds.service.InferenceTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/inference-task")
public class InferenceTaskController {
    @Autowired
    private InferenceTaskService inferenceTaskService;

    @RequestMapping(value = "", method = RequestMethod.POST, produces = "application/json")
    public Response addInferenceResult(@RequestBody InferenceTask inferenceTask) {
        return inferenceTaskService.addInferenceTask(inferenceTask);
    }
}
