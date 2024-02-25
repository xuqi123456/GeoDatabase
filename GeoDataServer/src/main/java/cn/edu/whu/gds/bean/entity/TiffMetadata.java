package cn.edu.whu.gds.bean.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "gd_tiff")
public class TiffMetadata {
    @Id
    private Integer id;
    private String name;
    private String path;
    private String bbox;
    private Timestamp createTime;

    public String getObjectKey() {
        return path + name;
    }
}

