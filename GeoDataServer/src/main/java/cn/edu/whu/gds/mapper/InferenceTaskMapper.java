package cn.edu.whu.gds.mapper;

import cn.edu.whu.gds.bean.entity.InferenceTask;
import org.apache.ibatis.annotations.*;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Mapper
public interface InferenceTaskMapper {
    @Select("select max(id) from " + Table.INFERENCE_TASK)
    Integer getMaxId();

    @Select("select * from " + Table.INFERENCE_TASK)
    List<InferenceTask> getAllInferenceTask();

    Boolean getInferenceTask();

    @Insert("insert into " + Table.INFERENCE_TASK + " (name, state, start_time, end_time, type) values (#{name}, 0, now(), now(), #{type})")
    Boolean addInferenceTask(InferenceTask inferenceTask);

    @Insert("insert into " + Table.TIFF_INFERENCE_TASK + " (tiff_id, task_id) values (#{tiffId}, #{taskId})")
    Boolean addTiffInferenceTask(@Param("tiffId") Integer tiffId, @Param("taskId") Integer taskId);

    @Update("update " + Table.INFERENCE_TASK + " set state = #{state} where id = #{id}")
    Boolean updateInferenceTask(@Param("id") Integer id, @Param("state") Short state);

    @Update("update " + Table.INFERENCE_TASK + " set end_time = now() where id = #{id}")
    Boolean setInferenceTaskEndTime(@Param("id") Integer id);

    Boolean deleteInferenceTask();
}
