package com.cherry.cm.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Compays on 2016/8/3.
 */
public class MapBuilder
{
    private Map map= new HashMap();
    public static MapBuilder newInstance(){
        return new MapBuilder();
    }

    public MapBuilder put(Object key,Object value){
        map.put(key,value);
        return this;
    }

    public Map build(){
        return map;
    }

    /**
     * 实例
     * @param args
     */
    public static void main(String[] args) {
        MapBuilder.newInstance().put("ada","ada").put("cc","").build();
    }

}
