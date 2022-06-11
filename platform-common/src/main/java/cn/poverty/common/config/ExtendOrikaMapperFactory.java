package cn.poverty.common.config;


import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.Mapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**

 * @projectName poverty-help-api
 * @Description: 自定义映射工厂类解决Orika的LocalDateTime转换异常
 * @date 2020-07-22
 */
@Component("extendOrikaMapperFactory")
public class ExtendOrikaMapperFactory  implements FactoryBean<MapperFactory>, ApplicationContextAware {

    ApplicationContext applicationContext;

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public MapperFactory getObject() throws Exception {
        DefaultMapperFactory build = new DefaultMapperFactory.Builder().build();
        for (CustomConverter converter : applicationContext.getBeansOfType(CustomConverter.class).values()) {
            build.getConverterFactory().registerConverter(converter);
        }
        for (Mapper<?, ?> mapper : applicationContext.getBeansOfType(Mapper.class).values()) {
            build.registerMapper(mapper);
        }
        for (ClassMapBuilder<?, ?> mapper : applicationContext.getBeansOfType(ClassMapBuilder.class).values()) {
            build.registerClassMap(mapper);
        }
        return build;
    }

    @Override
    public Class<?> getObjectType() {
        return MapperFactory.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


}

