package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Product;
import com.neuedu.vo.ProductDetailVO;

import java.util.List;
public interface IProductService {
    public ServerResponse addOrUpdate(Product product);
    /**
     * 商品的搜索
     * @param productName
     * @param productId
     * @return
     */
    public List<Product> search(String productName,
                                String productId);

    /**
     * 商品详情
     */
    public ServerResponse<ProductDetailVO> detail(Integer productId);

    /**
     * 商品详情
     */
    public ServerResponse<Product> findProductByProductId(Integer productId);

    /**
     * 根据商品id查询商品信息（库存）
     */
    public ServerResponse<Product> findProductById(Integer productId);

    /**
     * 扣库存
     */
    public ServerResponse reduceStock(Integer productId,Integer stock);

    /**
     * 查询所有商品
     */
    public List<Product> findAll();

    /**
     * 根据类别ID查询类别下的所有商品
     */
    public List<Product> findProductByCategoryID(String categoryId);

    /**
     * 通过商品id获取商品对象
     */

    public Product findProductById(String pid);

}
