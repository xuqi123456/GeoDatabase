package cn.edu.whu.gds.controller;

import cn.edu.whu.gds.bean.entity.InferenceResult;
import cn.edu.whu.gds.bean.vo.Response;
import cn.edu.whu.gds.service.InferenceResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/inference-result")
public class InferenceResultController {
    @Autowired
    private InferenceResultService inferenceResultService;

    @RequestMapping(value = "", method = RequestMethod.GET, produces = "application/json")
    public void getInferenceResult(@RequestParam("id") Integer id,
                                   HttpServletResponse response) {
        inferenceResultService.getInferenceResult(id, response);
    }

    @RequestMapping(value = "", method = RequestMethod.POST, produces = "application/json")
    public Response addInferenceResult(@RequestBody InferenceResult inferenceResult) throws IOException {
        return inferenceResultService.addInferenceResult(inferenceResult);
    }
}
