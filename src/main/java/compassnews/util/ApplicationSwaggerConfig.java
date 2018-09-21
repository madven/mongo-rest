package compassnews.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Java config for Springfox swagger documentation plugin
 *
 * @author Vitaliy Fedoriv
 */

@Configuration
@EnableSwagger2
@ComponentScan(basePackages = "com.selcukc.mongo_rest.controllers")
public class ApplicationSwaggerConfig {

	@Bean
	public Docket customDocket() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any())
				.build()
				.apiInfo(getApiInfo());
	}

	private ApiInfo getApiInfo() {
		return new ApiInfoBuilder()
				.title("Admin Dashboard backend Api Documentation")
				.description("This is REST API documentation of the Admin Dashboard backend.")
				.version("1.0")
				.contact(new Contact("Selcuk",
						"https://github.com/compass-news/Service/tree/master/java/services",
						"selcuk@compassnews.co.uk"))
				.build();
	}


}
