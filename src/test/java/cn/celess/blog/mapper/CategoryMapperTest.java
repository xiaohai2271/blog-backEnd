package cn.celess.blog.mapper;

import cn.celess.blog.BaseTest;
import cn.celess.blog.entity.Category;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class CategoryMapperTest extends BaseTest {

    @Autowired
    CategoryMapper categoryMapper;

    @Test
    public void insert() {
        Category category = generateCategory();
        assertNotNull(category.getId());
    }

    @Test
    public void delete() {
        Category category = generateCategory();
        assertFalse(category.getDeleted());
        int lines = categoryMapper.delete(category.getId());
        assertNotEquals(0, lines);
        Category categoryById = categoryMapper.findCategoryById(category.getId());
        assertTrue(categoryById.getDeleted());
    }

    @Test
    public void update() {
        Category category = generateCategory();
        category.setName(randomStr(4));
        int update = categoryMapper.update(category);
        assertEquals(1, update);
    }

    @Test
    public void existsByName() {
        Category category = generateCategory();
        assertTrue(categoryMapper.existsByName(category.getName()));
        assertFalse(categoryMapper.existsByName(randomStr(8)));
    }

    @Test
    public void existsById() {
        Category category = generateCategory();
        assertTrue(categoryMapper.existsById(category.getId()));
    }

    @Test
    public void findCategoryByName() {
        Category suibi = categoryMapper.findCategoryByName("随笔");
        assertNotNull(suibi);

        // tag 数据
        Category shiro = categoryMapper.findCategoryByName("shiro");
        assertNull(shiro);
    }

    @Test
    public void findCategoryById() {
        Category suibi = categoryMapper.findCategoryByName("随笔");

        Category categoryById = categoryMapper.findCategoryById(suibi.getId());

        assertEquals(suibi.getName(), categoryById.getName());
    }

    @Test
    public void findAll() {
        List<Category> all = categoryMapper.findAll();
        assertNotEquals(0, all);
        all.forEach(category -> assertTrue(category.getCategory()));
    }

    @Test
    public void getAllName() {
        List<String> allName = categoryMapper.getAllName();
        assertNotEquals(0, allName.size());
    }

    @Test
    public void getNameById() {
        Category category = generateCategory();
        assertEquals(category.getName(), categoryMapper.getNameById(category.getId()));
    }

    @Test
    public void getIdByName() {
        Category category = generateCategory();
        assertEquals(category.getId(), categoryMapper.getIdByName(category.getName()));
    }

    @Test
    public void getLastestCategory() {
        List<Category> all = categoryMapper.findAll();
        all.sort((o1, o2) -> (int) (o2.getId() - o1.getId()));
        assertEquals(all.get(0).getId(), categoryMapper.getLastestCategory().getId());
    }

    @Test
    public void count() {
        List<Category> all = categoryMapper.findAll();
        List<Category> collect = all.stream().filter(category -> !category.getDeleted()).collect(Collectors.toList());
        assertEquals(collect.size(), categoryMapper.count());
    }

    private Category generateCategory() {
        Category category = new Category(randomStr(4));

        categoryMapper.insert(category);
        category.setDeleted(false);
        return category;
    }
}