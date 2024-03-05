package cn.edu.whu.gds.bean.request;

import cn.edu.whu.gds.bean.entity.InferenceTask;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InferenceTaskRequest {
    private Integer id;
    private String name;
    private Short state;
    private String type;
    private JSONObject geom;
    private List<Integer> tiffIds;

    public InferenceTask getInferenceTask() {
        InferenceTask inferenceTask = new InferenceTask();
        if (id != null) {
            inferenceTask.setId(id);
        }
        inferenceTask.setName(name);
        if (state != null) {
            inferenceTask.setState(state);
        }
        if (type != null) {
            inferenceTask.setType(type);
        }
        return inferenceTask;
    }
}
