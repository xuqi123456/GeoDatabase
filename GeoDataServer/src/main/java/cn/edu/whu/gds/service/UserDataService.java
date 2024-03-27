package cn.edu.whu.gds.service;

import cn.edu.whu.gds.bean.vo.Response;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@Service
public interface UserDataService {
    Response addUserTiff(List<MultipartFile> file) throws URISyntaxException, IOException;


}
