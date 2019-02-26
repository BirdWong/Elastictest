package cn.jijiking51.elastictest.controller;

import cn.jijiking51.elastictest.pojo.GoodsInfo;
import cn.jijiking51.elastictest.util.GoodsRepository;
import org.elasticsearch.common.lucene.search.function.FiltersFunctionScoreQuery;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * @author h4795
 * @className GoodsController
 * @description TODD
 * @date 2019/2/26 0026 16:21
 */
@RestController
public class GoodsController {

	@Autowired
	private GoodsRepository goodsRepository;

	@GetMapping(value = "/save")
	public String save(){
		GoodsInfo goodsInfo = new GoodsInfo(System.currentTimeMillis(),
		    "商品"+System.currentTimeMillis(),"这是一个测试商品");
		goodsRepository.save(goodsInfo);
		ArrayList<GoodsInfo> list = new ArrayList<>();
		list.add(new GoodsInfo(1L, "测试 1", "测试一"));
		list.add(new GoodsInfo(2L, "测试 2", "测试二"));
		list.add(new GoodsInfo(3L, "测试 3", "测试三"));
		list.add(new GoodsInfo(4L, "测试 4", "测试四"));
		goodsRepository.saveAll(list);
		return "success";
	}

	//http://localhost:8888/delete?id=1525415333329
	@GetMapping("delete")
	public String delete(long id){
		goodsRepository.deleteById(id);
		return "success";
	}

	@GetMapping(value = "deleteAll")
	public String deleteAll(){
		goodsRepository.deleteAll();
		return "sucees";
	}

	//http://localhost:8888/update?id=1525417362754&name=修改&description=修改
	@GetMapping("update")
	public String update(long id,String name,String description){
		GoodsInfo goodsInfo = new GoodsInfo(id, name, description);
		goodsRepository.save(goodsInfo);
		return "success";
	}

	//http://localhost:8888/getOne?id=1525417362754
	@GetMapping("getOne")
	public GoodsInfo getOne(long id){
		Optional<GoodsInfo> repositoryById = goodsRepository.findById(id);
		GoodsInfo goodsInfo = repositoryById.get();
		return goodsInfo;
	}


	//每页数量
	private Integer PAGESIZE=10;

	//http://localhost:8888/getGoodsList?query=商品
	//http://localhost:8888/getGoodsList?query=商品&pageNumber=1
	//根据关键字"商品"去查询列表，name或者description包含的都查询
	@GetMapping("getGoodsList")
	public List<GoodsInfo> getList(Integer pageNumber, String query){
		if(pageNumber==null){
			pageNumber = 0;
		}
		//es搜索默认第一页页码是0
		SearchQuery searchQuery=getEntitySearchQuery(pageNumber,PAGESIZE,query);
		Page<GoodsInfo> goodsPage = goodsRepository.search(searchQuery);
		return goodsPage.getContent();
	}

	/**
	 * 综合排序
	 * 参考：https://blog.csdn.net/wwd0501/article/details/78652850
	 * @param pageNumber
	 * @param pageSize
	 * @param searchContent
	 * @return
	 */
	private SearchQuery getEntitySearchQuery(int pageNumber, int pageSize, String searchContent) {
		FunctionScoreQueryBuilder.FilterFunctionBuilder[] functions = {
		    new FunctionScoreQueryBuilder.FilterFunctionBuilder(
			QueryBuilders.matchQuery("name", searchContent),
			ScoreFunctionBuilders. weightFactorFunction(100)),
		    new FunctionScoreQueryBuilder.FilterFunctionBuilder(
			QueryBuilders.matchQuery("description", searchContent ),
			ScoreFunctionBuilders.weightFactorFunction(100))
		};
		FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery(functions)

		    //设置权重分 求和模式
		    .scoreMode(FiltersFunctionScoreQuery.ScoreMode.SUM)
		    //设置权重分最低分
		    .setMinScore(10);

		// 设置分页
		Pageable pageable = new PageRequest(pageNumber, pageSize);
		return new NativeSearchQueryBuilder()
		    .withPageable(pageable)
		    .withQuery(functionScoreQueryBuilder).build();
	}

}
