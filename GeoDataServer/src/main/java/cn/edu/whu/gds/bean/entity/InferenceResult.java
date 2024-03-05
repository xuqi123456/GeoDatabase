package cn.edu.whu.gds.bean.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InferenceResult {
    private Integer id;
    private Integer taskId;
    private String name;
    private String path;
    private Timestamp createTime;
}
