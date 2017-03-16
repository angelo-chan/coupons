package com.chainz.coupon.admin.swagger;

import io.swagger.annotations.Api;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

/** swagger configuration. */
@Configuration
@ConditionalOnProperty("swagger.enable")
@Import(BeanValidatorPluginsConfiguration.class)
@EnableSwagger2
public class SwaggerConfiguration {

  /**
   * Api info.
   *
   * @return api info.
   */
  @Bean
  ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title("Coupons API")
        .description("Coupons API description")
        .version("1.0")
        .build();
  }

  /**
   * coupons docket.
   *
   * @param apiInfo api info.
   * @return coupons docket.
   */
  @Bean
  public Docket couponsApi(ApiInfo apiInfo) {
    Docket docket =
        new Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
            .paths(PathSelectors.any())
            .build()
            .pathMapping("/")
            .directModelSubstitute(LocalDate.class, String.class)
            .directModelSubstitute(ZonedDateTime.class, String.class)
            .genericModelSubstitutes(ResponseEntity.class)
            .useDefaultResponseMessages(false)
            .globalOperationParameters(globalOperationParameters())
            .apiInfo(apiInfo);
    return docket;
  }

  /**
   * build global operation parameters.
   *
   * @return parameter list.
   */
  private List<Parameter> globalOperationParameters() {
    return Arrays.asList(
        new ParameterBuilder()
            .name("Account_Type")
            .parameterType("header")
            .modelRef(new ModelRef("string"))
            .required(true)
            .build(),
        new ParameterBuilder()
            .name("Vendor_Id")
            .parameterType("header")
            .modelRef(new ModelRef("string"))
            .build(),
        new ParameterBuilder()
            .name("Store_Id")
            .parameterType("header")
            .modelRef(new ModelRef("string"))
            .build(),
        new ParameterBuilder()
            .name("Open_Id")
            .parameterType("header")
            .modelRef(new ModelRef("string"))
            .build());
  }
}
