package cn.edu.whu.gds.mapper;

import cn.edu.whu.gds.bean.entity.TiffMetadata;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TiffRepository extends CrudRepository<TiffMetadata, Long> {
    @Query(value = "SELECT * FROM gd_tiff p", nativeQuery = true)
    List<TiffMetadata> getAllTiffMetadata();

    // 根据name属性查询数据
    @Query(value = "SELECT p.path FROM gd_tiff p WHERE ST_Intersects(p.bbox, ?1)", nativeQuery = true)
    List<String> getPathByWkt(String wkt);
}
