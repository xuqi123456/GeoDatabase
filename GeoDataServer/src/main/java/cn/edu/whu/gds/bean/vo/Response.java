package cn.edu.whu.gds.bean.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
    private Integer code;
    private String msg;
    private Object data;

    public Response(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
