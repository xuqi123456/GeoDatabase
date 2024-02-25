package cn.edu.whu.gds.mapper;

import cn.edu.whu.gds.bean.entity.InferenceTask;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.web.bind.annotation.PathVariable;

@Mapper
public interface InferenceTaskMapper {
    Boolean getInferenceTask();

    @Insert("insert into gd_inference_task (name, state, start_time, end_time) values (#{name}, #{state}, now(), now())")
    Boolean addInferenceTask(InferenceTask inferenceTask);

    @Update("update gd_inference_task set state = #{state} where id = #{id}")
    Boolean updateInferenceTask(@Param("id") Integer id, @Param("state") Short state);

    Boolean deleteInferenceTask();
}
