package cn.jijiking51.elastictest.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

/**
 * @author h4795
 * @className GoodsInfo
 * @description TODD
 * @date 2019/2/26 0026 15:46
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "testgoods", type = "goods")
public class GoodsInfo implements Serializable {
	private Long id;
	private String name;
	private String description;


}
