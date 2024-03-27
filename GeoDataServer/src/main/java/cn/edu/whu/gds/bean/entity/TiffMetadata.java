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
    private String province;
    private String city;

    public TiffMetadata(String name, String path, String bbox, String province, String city) {
        this.name = name;
        this.path = path;
        this.bbox = bbox;
        this.province = province;
        this.city = city;
    }

    public String getObjectKey() {
        return path + name;
    }
}

