package cn.edu.whu.gds.bean.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InferenceTask {
    private Integer id;
    private String name;
    private Short state;
    private Timestamp startTime;
    private Timestamp endTime;
    private String type;
    private String modelName;
    private String modelVersion;
}
