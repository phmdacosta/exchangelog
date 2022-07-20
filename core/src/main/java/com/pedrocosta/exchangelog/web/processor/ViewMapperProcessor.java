package com.pedrocosta.exchangelog.web.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedrocosta.exchangelog.annotation.View;
import com.pedrocosta.springutils.viewmapper.ViewMapper;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;

public class ViewMapperProcessor extends RequestResponseBodyMethodProcessor {

    private final ViewMapper mapper;

    public ViewMapperProcessor(ObjectMapper objectMapper, ViewMapper mapper) {
        super(Collections.singletonList(new MappingJackson2HttpMessageConverter(objectMapper)));
        this.mapper = mapper;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(View.class);
    }

    @Override
    protected Object readWithMessageConverters(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType) throws IOException, HttpMediaTypeNotSupportedException, HttpMessageNotReadableException {
        for (Annotation ann : parameter.getParameterAnnotations()) {
            View viewType = AnnotationUtils.getAnnotation(ann, View.class);
            if (viewType != null) {
                return super.readWithMessageConverters(inputMessage, parameter, viewType.value());
            }
        }
        throw new RuntimeException();
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Object view = super.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
        return this.mapper.map(view, parameter.getParameterType());
    }
}
