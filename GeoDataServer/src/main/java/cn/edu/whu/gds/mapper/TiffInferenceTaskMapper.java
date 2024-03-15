package cn.edu.whu.gds.mapper;

import cn.edu.whu.gds.bean.entity.TiffInferenceTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.List;

@Mapper
public interface TiffInferenceTaskMapper {
    @Select("select * from " + Table.TIFF_INFERENCE_TASK)
    List<TiffInferenceTask> getAllTiffInferenceTask();
    @Select("select tiff_id from " + Table.TIFF_INFERENCE_TASK+ " where task_id = #{taskId}")
    List<Integer> getImageIdByTaskId(@Param("taskId") Integer id);
    @Select("select task_id from " + Table.TIFF_INFERENCE_TASK+ " where tiff_id = #{tiffId}")
    List<Integer> getTaskIdByTiffId(@Param("tiffId") Integer id);

    @Select("select name from " + Table.TIFF+ " where id = #{tiffId}")
    String getImageNameById(@Param("tiffId") Integer id);
}
