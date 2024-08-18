package com.wentry.wbpm.utils.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.wentry.wbpm.utils.json.DynamicFieldSerializerModifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: tangwc
 */
public class JsonUtils {


    static ObjectMapper mapper = new ObjectMapper();


    /**
     * json 动态忽略指定名的属性
     */
    public static String toJson(Object obj, String... withoutAttr) {
        try {
            // 创建一个 SimpleModule 并注册 SerializerModifier
            SimpleModule module = new SimpleModule();
            module.setSerializerModifier(new DynamicFieldSerializerModifier(Arrays.stream(withoutAttr).collect(Collectors.toList())));

            // 注册模块到 ObjectMapper
            mapper.registerModule(module);

            // 序列化对象
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String toJson(Object obj) {
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
