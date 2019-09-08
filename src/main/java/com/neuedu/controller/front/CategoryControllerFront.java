package com.neuedu.controller.front;

import com.neuedu.pojo.Category;
import com.neuedu.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryControllerFront {
    @Autowired
    ICategoryService categoryService;
    /**
     * 查询所有类别
     */

    @RequestMapping("/findAll")
    public List<Category> findAllCategory() {

        return categoryService.findAllCategory();
    }

}


