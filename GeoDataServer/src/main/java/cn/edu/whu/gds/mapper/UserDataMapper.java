package cn.edu.whu.gds.mapper;

import cn.edu.whu.gds.bean.entity.TiffMetadata;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserDataMapper {
    @Select("select max(id) from " + Table.USER_TIFF)
    Integer getMaxId();

    @Insert("insert into " + Table.USER_TIFF + " (name, path, bbox, create_time, province, city) values (#{name}, #{path}, ST_GeomFromText(#{bbox}), now(), #{province}, #{city})")
    Boolean addTiff(TiffMetadata tiffMetadata);
    @Select(value = "SELECT * FROM " + Table.USER_TIFF + " WHERE name = #{names}")
    List<TiffMetadata> getTiffMetadataByNames(@Param("names") String names);
}
