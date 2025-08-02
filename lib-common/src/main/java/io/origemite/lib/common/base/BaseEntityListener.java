package io.origemite.lib.common.base;

import io.origemite.lib.common.util.UuidUtils;
import io.origemite.lib.common.util.WebUtils;
import jakarta.persistence.PrePersist;

import java.util.UUID;

public class BaseEntityListener {

    @PrePersist
    public void prePersist(Object o) throws IllegalAccessException {
        if (o instanceof ServiceProviderble obj) {
            obj.setServiceId(WebUtils.getServiceId());
        }

        if (o instanceof IdGeneratable obj) {
            obj.setId(UuidUtils.uuidToBase64(UUID.randomUUID()));
        }

//        if (o instanceof Encryptable obj) {
//            handleEncryptable(obj);
//        }
    }

//    @PreUpdate
//    public void preUpdate(Object o) throws IllegalAccessException {
//        if (o instanceof Encryptable obj) {
//            handleEncryptable(obj);
//        }
//    }
//
//    @PostLoad
//    public void postLoad(Object o) throws IllegalAccessException {
//        if (o instanceof Encryptable obj) {
//            String cipherKeyId = EntityContextHolder.getCiperKey();
//
//            if (StringUtils.isNotEmpty(cipherKeyId)) {
//                Field[] fields = obj.getClass().getDeclaredFields();
//
//                for (Field field : fields) {
//                    if (field.isAnnotationPresent(Encrypt.class)) {
//                        field.setAccessible(true);
//                        Object value = field.get(obj);
//                        if (!ObjectUtils.isEmpty(value)) {
//                            String decryptValue = EncUtils.decKMS((String) value, cipherKeyId);
//                            field.set(obj, decryptValue);
//                        }
//                    }
//                }
//            }
//        }
//    }

//    private static void handleEncryptable(Encryptable obj) throws IllegalAccessException {
//        String cipherKeyId = EntityContextHolder.getCiperKey();
//
//        if (StringUtils.isEmpty(cipherKeyId)) {
//            throw new DidddaErrorException("cipherKeyId is null");
//        }
//
//        Field[] fields = obj.getClass().getDeclaredFields();
//
//        Map<String, Field> fieldsMap = Arrays.stream(obj.getClass().getDeclaredFields())
//                .collect(Collectors.toMap(Field::getName, Function.identity()));
//
//        for (Field field : fields) {
//
//            if (field.isAnnotationPresent(SecureHash.class)) {
//                field.setAccessible(true);
//                SecureHash annotation = field.getAnnotation(SecureHash.class);
//
//                Field targetField = fieldsMap.get(annotation.field());
//                targetField.setAccessible(true);
//                Object targetValue = targetField.get(obj);
//
//                if (!ObjectUtils.isEmpty(targetValue)) {
//                    String shaValue = EncUtils.encSHA512((String) targetValue);
//                    field.set(obj, shaValue);
//                }
//            }
//        }
//
//        for (Field field : fields) {
//
//            if (field.isAnnotationPresent(Encrypt.class)) {
//                field.setAccessible(true);
//                Object value = field.get(obj);
//                if (!ObjectUtils.isEmpty(value)) {
//                    String encValue = EncUtils.encKMS((String) value, cipherKeyId);
//                    field.set(obj, encValue);
//                }
//            }
//
//        }
//    }
}