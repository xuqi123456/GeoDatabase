package cn.edu.whu.gds.mapper;

import cn.edu.whu.gds.bean.entity.InferenceTask;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InferenceTaskMapper {
    Boolean getInferenceTask();

    @Insert("insert into gd_inference_task (name, state, start_time, end_time) values (#{name}, #{state}, now(), now())")
    Boolean addInferenceTask(InferenceTask inferenceTask);

    Boolean updateInferenceTask();

    Boolean deleteInferenceTask();
}
