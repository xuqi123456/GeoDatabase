package cn.edu.whu.gds.mapper;

import cn.edu.whu.gds.bean.entity.InferenceResult;
import org.apache.ibatis.annotations.*;

@Mapper
public interface InferenceResultMapper {
    @Select("select id, name, task_id, ST_AsGeoJson(geom) geom, create_time from gd_inference_result where name = #{name}")
    InferenceResult getInferenceResult(@Param("name") String name);

    @Insert("insert into gd_inference_result (name, task_id, geom, create_time) values (#{name}, #{taskId}, ST_GeomFromGeoJSON(#{geom}), now())")
    Boolean addInferenceResult(InferenceResult inferenceResult);

    @Update("update gd_inference_task set state = #{state} where id = #{id}")
    Boolean updateInferenceResult();

    Boolean deleteInferenceResult();
}
