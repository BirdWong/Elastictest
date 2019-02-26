package cn.jijiking51.elastictest.util;

import cn.jijiking51.elastictest.pojo.GoodsInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

/**
 * @author h4795
 * @className GoodsRepository
 * @description TODD
 * @date 2019/2/26 0026 16:19
 */
@Component
public interface GoodsRepository extends ElasticsearchRepository<GoodsInfo, Long> {
}
