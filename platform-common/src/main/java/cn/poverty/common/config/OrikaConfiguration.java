package cn.poverty.common.config;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @author
 * @projectName poverty-help-api
 * @Description: Orika的配置
 * @date 2020-07-22
 */
@Configuration
public class OrikaConfiguration {

    @Bean
    public ExtendOrikaMapperFactory loadFactory(){
        return new ExtendOrikaMapperFactory();
    }

    @Resource
    private MapperFactory mapperFactory;

    @Bean
    public MapperFacade loadMapperFacade(){
        return mapperFactory.getMapperFacade();
    }



    /**
     * 解决orika映射LocalDateTime报错问题
     */
    @PostConstruct
    public void init() {
        mapperFactory.getConverterFactory().registerConverter(new LocalDateTimeConverter());
        mapperFactory.getConverterFactory().registerConverter(new LocalDateConverter());
        mapperFactory.getConverterFactory().registerConverter(new LocalTimeConverter());
    }


    private class LocalDateTimeConverter extends BidirectionalConverter<LocalDateTime, LocalDateTime> {

        @Override
        public LocalDateTime convertTo(LocalDateTime localDateTime, Type<LocalDateTime> type, MappingContext mappingContext) {
            return LocalDateTime.from(localDateTime);
        }

        @Override
        public LocalDateTime convertFrom(LocalDateTime localDateTime, Type<LocalDateTime> type, MappingContext mappingContext) {
            return LocalDateTime.from(localDateTime);
        }
    }


    private class LocalDateConverter extends BidirectionalConverter<LocalDate, LocalDate> {

        @Override
        public LocalDate convertTo(LocalDate localDate, Type<LocalDate> type, MappingContext mappingContext) {
            return LocalDate.from(localDate);
        }

        @Override
        public LocalDate convertFrom(LocalDate localDate, Type<LocalDate> type, MappingContext mappingContext) {
            return LocalDate.from(localDate);
        }
    }


    private class LocalTimeConverter extends BidirectionalConverter<LocalTime, LocalTime> {

        @Override
        public LocalTime convertTo(LocalTime localTime, Type<LocalTime> type, MappingContext mappingContext) {
            return LocalTime.from(localTime);
        }

        @Override
        public LocalTime convertFrom(LocalTime localTime, Type<LocalTime> type, MappingContext mappingContext) {
            return LocalTime.from(localTime);
        }
    }


}
