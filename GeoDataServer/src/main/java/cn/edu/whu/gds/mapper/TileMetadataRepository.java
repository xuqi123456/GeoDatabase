package cn.edu.whu.gds.mapper;

import cn.edu.whu.gds.bean.entity.TileMetadata;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface TileMetadataRepository extends CrudRepository<TileMetadata, Long> {

    // 根据name属性查询数据
    @Query("SELECT t FROM TileMetadata t WHERE t.name = ?1")
    TileMetadata findBySql(String name);
}
