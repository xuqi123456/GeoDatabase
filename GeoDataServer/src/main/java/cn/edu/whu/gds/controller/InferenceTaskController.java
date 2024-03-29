package cn.edu.whu.gds.controller;

import cn.edu.whu.gds.bean.entity.InferenceResult;
import cn.edu.whu.gds.bean.entity.InferenceTask;
import cn.edu.whu.gds.bean.request.InferenceTaskRequest;
import cn.edu.whu.gds.bean.vo.Response;
import cn.edu.whu.gds.service.InferenceResultService;
import cn.edu.whu.gds.service.InferenceTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/inference-task")
public class InferenceTaskController {
    @Autowired
    private InferenceTaskService inferenceTaskService;

    @RequestMapping(value = "/catalog", method = RequestMethod.GET, produces = "application/json")
    public Response getInferenceTaskCatalog() {
        return inferenceTaskService.getInferenceTaskCatalog();
    }

    @RequestMapping(value = "", method = RequestMethod.POST, produces = "application/json")
    public Response addInferenceResult(@RequestBody InferenceTaskRequest inferenceTaskRequest) throws URISyntaxException {
        return inferenceTaskService.addInferenceTask(inferenceTaskRequest);
    }

    @RequestMapping(value = "", method = RequestMethod.PUT, produces = "application/json")
    public Response updateInferenceResult(@RequestParam("id") Integer id,
                                          @RequestParam("state") Short state) {
        return inferenceTaskService.updateInferenceTask(id, state);
    }
}
