package cn.edu.whu.gds.bean.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TileMetadata {
    private Integer id;
    private String name;
    private String path;
    private String bbox;
    private Timestamp createTime;

    public String getObjectKey() {
        return path + name;
    }
}
