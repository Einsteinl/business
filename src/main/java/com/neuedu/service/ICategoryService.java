package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.Category;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface ICategoryService {

    /**
     * 添加类别
     * categoryid
     * categoryName
     * categoryUrl
     */
    public ServerResponse addCategory(Category category);
    /**
     *修改类别
     * categoryid
     * categoryName
     * categoryUrl
     */
    public ServerResponse updateCategory(Category category);

    /**
     *查看类别
     * categoryid
     *
     */
    public ServerResponse getCategoryById(@PathVariable("categoryId") Integer categoryId);
    /**
     *
     */
    public ServerResponse deepCategory(@PathVariable("categoryId") Integer categoryId);

    /**
     * 根据id查询类别
     */
    public ServerResponse<Category> selectCategory(Integer categoryId);

    /**
     * 查看所有类别
     */
    public List<Category> findAllCategory();

}
