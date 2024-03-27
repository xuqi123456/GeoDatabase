package cn.edu.whu.gds.mapper;

import cn.edu.whu.gds.bean.entity.TiffMetadata;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TiffRepository extends CrudRepository<TiffMetadata, Long> {
    @Query(value = "SELECT * FROM gd_tiff p ORDER BY p.province", nativeQuery = true)
    List<TiffMetadata> getAllTiffMetadata();

    // 根据name属性查询数据
    @Query(value = "SELECT p.path FROM gd_tiff p WHERE ST_Intersects(p.bbox, ?1)", nativeQuery = true)
    List<String> getPathByWkt(String wkt);

    @Query(value = "SELECT * FROM gd_tiff p WHERE ST_Intersects(p.bbox, ST_GeomFromGeoJSON(?1))", nativeQuery = true)
    List<TiffMetadata> getPathByGeoJson(String geoJson);

    @Query(value = "SELECT concat(p.path, '/', p.name) FROM gd_tiff p WHERE p.id in ?1", nativeQuery = true)
    List<String> getPathsByIds(List<Integer> ids);

}
