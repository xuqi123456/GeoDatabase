package cn.edu.whu.gds.controller;

import cn.edu.whu.gds.bean.entity.InferenceResult;
import cn.edu.whu.gds.bean.vo.Response;
import cn.edu.whu.gds.service.InferenceResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/inference-result")
public class InferenceResultController {
    @Autowired
    private InferenceResultService inferenceResultService;

    @RequestMapping(value = "/{name}", method = RequestMethod.GET, produces = "application/json")
    public Response addInferenceResult(@PathVariable("name") String name) {
        return inferenceResultService.getInferenceResult(name);
    }

    @RequestMapping(value = "", method = RequestMethod.POST, produces = "application/json")
    public Response addInferenceResult(@RequestBody InferenceResult inferenceResult) {
        return inferenceResultService.addInferenceResult(inferenceResult);
    }
}
