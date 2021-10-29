package com.hg.hyy.utils;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.hg.hyy.entity.User;

public class UserSerializationUtil {

    private static RuntimeSchema<User> schema = RuntimeSchema.createFrom(User.class);

    /**
     * 序列化方法，将User对象序列化为字节数组
     * 
     * @param user
     * @return
     */
    public static byte[] serialize(User user) {
        // Serializes the {@code message} into a byte array using the given schema
        return ProtostuffIOUtil.toByteArray(user, schema, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
    }

    /**
     * 反序列化方法，将字节数组反序列化为User对象
     * 
     * @param array
     * @return
     */
    public static User deserialize(byte[] array) {
        User user = schema.newMessage();
        // Merges the {@code message} with the byte array using the given {@code schema}
        ProtostuffIOUtil.mergeFrom(array, user, schema);
        return user;
    }
}
