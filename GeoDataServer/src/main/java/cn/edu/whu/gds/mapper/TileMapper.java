package cn.edu.whu.gds.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TileMapper {
    @Select("select concat(path, name) from ea_tile where name = #{tilesName}")
    String getObjectKey(@Param("tilesName") String tilesName);
}
