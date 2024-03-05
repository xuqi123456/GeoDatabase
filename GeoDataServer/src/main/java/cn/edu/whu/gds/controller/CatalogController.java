package cn.edu.whu.gds.controller;

import cn.edu.whu.gds.bean.vo.Response;
import cn.edu.whu.gds.bean.vo.Road;
import cn.edu.whu.gds.util.HttpResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/catalog")
public class CatalogController {
    @Autowired
    private HttpResponseUtil httpResponseUtil;

    @RequestMapping(value = "", method = RequestMethod.POST, produces = "application/json")
    public Response addInferenceResult(@RequestBody Road road) {
        System.out.println(road.getUri());
        System.out.println(road.getBand_list());
        System.out.println(road.getRegion());
        return httpResponseUtil.ok("测试接口");
    }
}
