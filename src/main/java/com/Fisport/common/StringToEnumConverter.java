package com.Fisport.common;


import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class StringToEnumConverter implements Converter<String, EFieldStatus> {
    @Override
    public EFieldStatus convert(String source) {
        return EFieldStatus.valueOf(source.toUpperCase());
    }
}
