package cn.celess.blog.mapper;

import cn.celess.blog.BaseTest;
import cn.celess.blog.entity.Tag;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class TagMapperTest extends BaseTest {

    @Autowired
    TagMapper tagMapper;

    @Test
    public void insert() {
        Tag tag = generateTag();
        assertNotNull(tag.getId());
    }

    @Test
    public void update() {
        Tag tag = generateTag();
        tag.setName(randomStr(4));
        int update = tagMapper.update(tag);
        assertEquals(1, update);
    }

    @Test
    public void delete() {
        Tag tag = generateTag();
        assertFalse(tag.getDeleted());
        assertEquals(1, tagMapper.delete(tag.getId()));
        Tag tagById = tagMapper.findTagById(tag.getId());
        assertTrue(tagById.getDeleted());
    }

    @Test
    public void findTagById() {
        Tag tag = generateTag();
        Tag tagById = tagMapper.findTagById(tag.getId());
        assertEquals(tag.getName(), tagById.getName());
        assertEquals(tag.getId(), tagById.getId());
    }

    @Test
    public void findTagByName() {
        Tag tag = generateTag();
        Tag tagById = tagMapper.findTagByName(tag.getName());
        assertEquals(tag.getName(), tagById.getName());
        assertEquals(tag.getId(), tagById.getId());
    }

    @Test
    public void existsByName() {
        String s = randomStr(4);
        assertFalse(tagMapper.existsByName(s));
        Tag tag = new Tag(s);
        tagMapper.insert(tag);
        assertTrue(tagMapper.existsByName(s));
    }

    @Test
    public void getLastestTag() {
        List<Tag> all = tagMapper.findAll();
        all.sort(((o1, o2) -> (int) (o2.getId() - o1.getId())));
        assertEquals(all.get(0).getId(), tagMapper.getLastestTag().getId());
    }

    @Test
    public void findAll() {
        List<Tag> all = tagMapper.findAll();
        assertNotEquals(0, all.size());
    }

    @Test
    public void count() {
        assertNotEquals(0, tagMapper.count());
        List<Tag> all = tagMapper.findAll();
        List<Tag> collect = all.stream().filter(tag -> !tag.getDeleted()).collect(Collectors.toList());
        assertEquals(collect.size(), tagMapper.count());
    }

    private Tag generateTag() {
        Tag tag = new Tag(randomStr(4));
        tagMapper.insert(tag);
        tag.setDeleted(false);
        return tag;
    }
}