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
public class InferenceTaskCatalog {
    private Integer id;
    private String label;
    private Short state;
    private Boolean isResult;
    private List<InferenceTaskCatalog> children;

    public InferenceTaskCatalog(Integer id, String label) {
        this.id = id;
        this.label = label;
    }

    public InferenceTaskCatalog(Integer id, String label, Boolean isResult) {
        this.id = id;
        this.label = label;
        this.isResult = isResult;
    }

    public InferenceTaskCatalog(Integer id, String label, Short state) {
        this.id = id;
        this.label = label;
        this.state = state;
    }

    public InferenceTaskCatalog(Integer id, String label, List<InferenceTaskCatalog> children) {
        this.id = id;
        this.label = label;
        this.children = children;
    }

    public InferenceTaskCatalog(Integer id, String label, Short state, List<InferenceTaskCatalog> children) {
        this.id = id;
        this.label = label;
        this.state = state;
        this.children = children;
    }
}
