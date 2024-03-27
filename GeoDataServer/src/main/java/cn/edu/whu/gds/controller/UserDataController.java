package cn.edu.whu.gds.controller;

import cn.edu.whu.gds.bean.vo.Response;
import cn.edu.whu.gds.service.UserDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/user-data")
public class UserDataController {
    @Autowired
    private UserDataService userDataService;

    @RequestMapping(value = "/upload", method = RequestMethod.POST, produces = "application/json")
    public Response addUserTiff(@RequestBody List<MultipartFile> files) throws URISyntaxException, IOException {
        return userDataService.addUserTiff(files);
    }

}
