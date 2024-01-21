package cn.edu.whu.gds.util;

import cn.edu.whu.gds.bean.vo.Response;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.ServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class HttpResponseUtil {
    public Response responseRender(HttpStatus httpStatus, String msg) {
        return new Response(httpStatus.value(), msg);
    }

    public Response responseRender(HttpStatus httpStatus, String msg, Object data) {
        return new Response(httpStatus.value(), msg, data);
    }

    public void responseRender(ServletResponse servletResponse, HttpStatus status, String msg) {
        try {
            servletResponse.setCharacterEncoding(StandardCharsets.UTF_8.name());
            servletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
            servletResponse.getWriter().print(JSON.toJSONString(responseRender(status, msg)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void responseRender(ServletResponse servletResponse, String jsonString) {
        JSONObject jsonObject = JSON.parseObject(jsonString);
        responseRender(servletResponse,
                HttpStatus.valueOf(jsonObject.getString("code")),
                jsonObject.getString("msg"));
    }

    public Response continuing(String msg) {
        return responseRender(HttpStatus.CONTINUE, msg);
    }

    public Response switchingProtocols(String msg) {
        return responseRender(HttpStatus.SWITCHING_PROTOCOLS, msg);
    }

    public Response ok(String msg) {
        return responseRender(HttpStatus.OK, msg);
    }

    public Response ok(String msg, String data) {
        return responseRender(HttpStatus.OK, msg, data);
    }

    public Response ok(String msg, Object data) {
        return responseRender(HttpStatus.OK, msg, data);
    }

    public Response created(String msg) {
        return responseRender(HttpStatus.CREATED, msg);
    }

    public Response created(String msg, String data) {
        return responseRender(HttpStatus.CREATED, msg, data);
    }

    public Response noContent(String msg) {
        return responseRender(HttpStatus.NO_CONTENT, msg);
    }

    public Response failure(String msg) {
        return responseRender(HttpStatus.MULTI_STATUS, msg);
    }

    public Response badRequest(String msg) {
        return responseRender(HttpStatus.BAD_REQUEST, msg);
    }

    public void badRequest(ServletResponse servletResponse, String msg) {
        responseRender(servletResponse, HttpStatus.BAD_REQUEST, msg);
    }

    public Response unauthorized(String msg) {
        return responseRender(HttpStatus.UNAUTHORIZED, msg);
    }

    public Response forbidden(String msg) {
        return responseRender(HttpStatus.FORBIDDEN, msg);
    }

    public Response payloadTooLarge(String msg) {
        return responseRender(HttpStatus.PAYLOAD_TOO_LARGE, msg);
    }

    public Response internalServerError(String msg) {
        return responseRender(HttpStatus.INTERNAL_SERVER_ERROR, msg);
    }
}
