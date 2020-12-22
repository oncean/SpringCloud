package com.wangsheng.springcloud.exception;


import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

@Configuration
public class ExceptionHandlerImportSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        Class<?> annotationType = EnableExceptionHandler.class;
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(
                annotationType.getName(),false
        ));
        boolean value = attributes.getBoolean("value");
        if(value){
            //开启注解
            return new String[]{GlobalExceptionHandler.class.getName()};
        }
        return new String[0];
    }

}
