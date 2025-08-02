package io.diddda.lib.common.client;

import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

public class ResultMap extends HashMap<String, Object>{

    public ResultMap() {
        super();
    }

    public ResultMap(Map<String, Object> map) {
        super();
        this.putAll(map);
    }

    public String getString(String key){
        if( !containsKey(key) ) return null;
        return (String) get(key);
    }
    public Integer getInteger(String key){
        if( !containsKey(key) ) return null;
        return (Integer) get(key);
    }

}
