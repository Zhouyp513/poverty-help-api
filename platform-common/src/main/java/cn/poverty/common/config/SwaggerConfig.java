package cn.poverty.common.config;

//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**

 * @projectName poverty-help-api
 * @Description: Swagger的配置
 * @date 2020-07-23
 */
//@Configuration
//@EnableSwagger2
public class SwaggerConfig {


    /*@Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                // 选择那些路径和api会生成document
                .select()
                // 对所有api进行监控
                .apis(RequestHandlerSelectors.basePackage("poverty"))
                // 对所有路径进行监控
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }*/

    /*private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("poverty-help-api-平台")
                .description("poverty-help-api-平台接口文档")
                .termsOfServiceUrl("https://www.singer-coder.com")
                .version("1.0.0")
                .build();
    }*/
}
