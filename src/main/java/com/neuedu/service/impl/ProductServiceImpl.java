package com.neuedu.service.impl;

import com.neuedu.common.ResponseCode;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.ProductMapper;
import com.neuedu.pojo.Category;
import com.neuedu.pojo.Product;
import com.neuedu.service.ICategoryService;
import com.neuedu.service.IProductService;
import com.neuedu.utils.DateUtils;
import com.neuedu.vo.ProductDetailVO;
import com.neuedu.vo.ProductListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductServiceImpl implements IProductService {
    @Autowired
    ICategoryService categoryService;

   @Autowired
    ProductMapper productMapper;

   /* @Value("${business.imageHost}")
    private String imageHost;

*/
    @Override
    public ServerResponse addOrUpdate(Product product) {
        if (product==null){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"参数必传");
        }

        //subimages 1.png,2.png,3.png
        //step2:设置商品的主图 sub_images->1.jps,2.jpg,3.png
        String subImages=product.getSubImages();
        if (subImages!=null&&!subImages.equals("")){
            String[] subImageArr=subImages.split(",");
            if (subImageArr.length>0){
                //设置商品的主图
                product.setMainImage(subImageArr[0]);
            }
        }


       Integer productId=product.getId();
        if (productId==null){
            //添加
       int result=productMapper.insert(product);
       if (result<=0){
           return ServerResponse.serverResponseByError(ResponseCode.ERROR,"添加失败");
       }else{
           return ServerResponse.serverResponseBySuccess();
       }
        }else{
            //更新
           int result=productMapper.updateByPrimaryKey(product);
            if (result<=0){
                return ServerResponse.serverResponseByError(ResponseCode.ERROR,"更新失败");
            }else{
                return ServerResponse.serverResponseBySuccess();
            }
        }
    }


    /**
     * 搜索商品
     * @param productName
     * @param productId
     * @return
     */
    @Override
    public List<Product> search(String productName, String productId) {
        Integer id = null;
        if(productId != null) id = Integer.valueOf(productId);
        List<Product> products = productMapper.findProductsByNameAndId(id, productName);

        return products;
    }


    /**
     * 商品详情
     * @param productId
     * @return
     */
    @Override
    public ServerResponse<ProductDetailVO> detail(Integer productId) {
        if (productId==null){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"参数必传");
        }
        Product product=productMapper.selectByPrimaryKey(productId);
        if (product==null){
            return ServerResponse.serverResponseBySuccess();
        }

        //product-productDetailVO

        ProductDetailVO productDetailVO=assembleProductDetailVO(product);
        return ServerResponse.serverResponseBySuccess(productDetailVO);
    }

    @Override
    public ServerResponse<Product> findProductByProductId(Integer productId) {
        if (productId==null){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"参数必传");
        }
        Product product=productMapper.selectByPrimaryKey(productId);
        if (product==null){
            return ServerResponse.serverResponseBySuccess();
        }
        return ServerResponse.serverResponseBySuccess(product);
    }

    @Override
    public ServerResponse<Product> findProductById(Integer productId) {
        if (productId==null){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"商品id必传");
        }
        Product product=productMapper.selectByPrimaryKey(productId);
        if (product==null){
            //商品不存在
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"商品不存在");

        }
        return ServerResponse.serverResponseBySuccess(product);
    }

    @Override
    public ServerResponse reduceStock(Integer productId, Integer stock) {
        if (productId==null){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"商品id必传");
        }
        Product product=productMapper.selectByPrimaryKey(productId);
        if (stock==null){
            //商品不存在
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"库存参数必传");
        }
        int result=productMapper.reduceProductStock(productId,stock);
        if (result<=0){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"扣库存失败");
        }
        return ServerResponse.serverResponseBySuccess();
    }

    @Override
    public List<Product> findAll() {
        return productMapper.selectAll();
    }

    @Override
    public List<Product> findProductByCategoryID(String categoryId) {
        return productMapper.findProductsByCategoryID(Integer.valueOf(categoryId));
    }

    @Override
    public Product findProductById(String pid) {
        return productMapper.selectByPrimaryKey(Integer.valueOf(pid));
    }

    private ProductDetailVO assembleProductDetailVO(Product product){
        ProductDetailVO productDetailVO=new ProductDetailVO();
        productDetailVO.setCategoryId(product.getCategoryId());
        productDetailVO.getCreateTime(DateUtils.dateToStr(product.getCreateTime()));
        productDetailVO.setDetail(product.getDetail());
       // productDetailVO.setImageHost(imageHost);
        productDetailVO.setName(product.getName());
        productDetailVO.setMainImage(product.getSubImages());
        productDetailVO.setId(product.getId());
        productDetailVO.setStatus(product.getStatus());
        productDetailVO.setStock(product.getStock());
        productDetailVO.setSubImages(product.getSubImages());
        productDetailVO.setSubtitle(product.getSubtitle());
        productDetailVO.setUpdateTime(DateUtils.dateToStr(product.getUpdateTime()));
       //Category category=categoryMapper.s
       ServerResponse<List<Category>> serverResponse=categoryService.getCategoryById(product.getCategoryId());
       Category category=serverResponse.getData().get(0);
       if(category!=null){
           productDetailVO.setParentCategoryId(category.getParentId());
       }
       return productDetailVO;
    }

    private ProductListVO assembleProductListVO(Product product){

        ProductListVO productListVO=new ProductListVO();
        productListVO.setId(product.getId());
        productListVO.setCategoryId(product.getCategoryId());
        productListVO.setMainImage(product.getMainImage());
        productListVO.setName(product.getName());
        productListVO.setPrice(product.getPrice());
        productListVO.setStatus(product.getStatus());
        productListVO.setSubtitle(product.getSubtitle());
        return productListVO;
    }


}
