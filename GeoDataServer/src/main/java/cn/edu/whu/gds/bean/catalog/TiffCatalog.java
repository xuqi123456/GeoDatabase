package cn.edu.whu.gds.bean.catalog;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TiffCatalog {
    private Integer id;
    private String label;
    private Integer tiffId;
    private String path;
    private List<TiffCatalog> children;

    public TiffCatalog(Integer id, String label, List<TiffCatalog> children) {
        this.id = id;
        this.label = label;
        this.children = children;
    }

    public TiffCatalog(Integer id, String label, Integer tiffId) {
        this.id = id;
        this.label = label;
        this.tiffId = tiffId;
    }

    public TiffCatalog(Integer id, String label, Integer tiffId, String path) {
        this.id = id;
        this.label = label;
        this.tiffId = tiffId;
        this.path = path;
    }
}
