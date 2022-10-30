package com.onlinetutorialspoint.repo;

import com.onlinetutorialspoint.model.Item;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class ItemRepository {

    public static final String KEY = "ITEM";

    private RedisTemplate<String,Item> redisTemplate;

    private HashOperations hashOperations;

    public ItemRepository(RedisTemplate<String, Item> redisTemplate) {
        this.redisTemplate = redisTemplate;
        hashOperations = redisTemplate.opsForHash();
    }

    /*Getting all Items from table (получает все элементы из таблицы*/
    public Map<Integer,Item> getAllItems(){
        return hashOperations.entries(KEY);
    }

    /*Getting a specific item by item id from table (получает определенные элементы из таблицы)*/
    public Item getItem(int itemId){
        return (Item) hashOperations.get(KEY,itemId);
    }

    /*Adding an item into redis database (добавляет внутрь базы данных redis, некотрый элемент)*/
    public void addItem(Item item){
        hashOperations.put(KEY,item.getId(),item);
    }

    /*delete an item from database (удаление элемента из базы)*/
    public void deleteItem(int id){
        hashOperations.delete(KEY,id);
    }

    /*update an item from database (обновление элемента из базы)*/
    public void updateItem(Item item){
        addItem(item);
    }
}
