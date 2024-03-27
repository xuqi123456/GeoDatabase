package cn.edu.whu.gds.mapper;

import cn.edu.whu.gds.bean.entity.InferenceResult;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface InferenceResultMapper {
    @Select("select max(id) from " + Table.INFERENCE_RESULT)
    Integer getMaxId();

    @Select("select id, name from " + Table.INFERENCE_RESULT + " where task_id = #{taskId}")
    List<InferenceResult> getInferenceResultByTaskId(@Param("taskId") Integer id);

    @Select("select id, name, task_id, path, create_time from " + Table.INFERENCE_RESULT + " where id = #{id}")
    InferenceResult getInferenceResult(@Param("id") Integer id);

    @Insert("insert into " + Table.INFERENCE_RESULT + " (name, task_id, path, create_time) values (#{name}, #{taskId}, #{path}, now())")
    Boolean addInferenceResult(InferenceResult inferenceResult);

    Boolean updateInferenceResult();

    Boolean deleteInferenceResult();
}
