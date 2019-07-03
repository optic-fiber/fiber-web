package com.cheroliv.fiber.config

import com.cheroliv.fiber.web.http.CachingHttpHeadersFilter
import com.cheroliv.fiber.web.rest.InterController
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.boot.web.server.MimeMappings
import org.springframework.boot.web.server.WebServerFactory
import org.springframework.boot.web.server.WebServerFactoryCustomizer
import org.springframework.boot.web.servlet.ServletContextInitializer
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

import javax.servlet.DispatcherType
import javax.servlet.FilterRegistration
import javax.servlet.ServletContext
import javax.servlet.ServletException
import java.nio.charset.StandardCharsets
import java.nio.file.Paths

import static java.net.URLDecoder.decode

@Slf4j
@CompileStatic
@Configuration
@EnableCaching
@EnableSwagger2
class WebConfigurer implements ServletContextInitializer, WebServerFactoryCustomizer<WebServerFactory>, WebMvcConfigurer {

    final FiberProperties properties = FiberProperties.instance

    @Override
     void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Bean
    CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource()
        CorsConfiguration config = properties.cors
        if (config.getAllowedOrigins() != null && !config.getAllowedOrigins().isEmpty()) {
            log.debug("Registering CORS filter")
            source.registerCorsConfiguration("/api/**", config)
            source.registerCorsConfiguration("/management/**", config)
            source.registerCorsConfiguration("/v2/api-docs", config)
        }
        new CorsFilter(source)
    }
    @Bean
    Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage(InterController.package.toString()))
                .paths(PathSelectors.any())
        //                .paths(PathSelectors.ant("/fiber-web/*"))
                .build()
    }
    @Override
    void onStartup(ServletContext servletContext) throws ServletException {


    }


    @Override
    void customize(WebServerFactory server) {
        setMimeMappings(server)
        setLocationForStaticAssets(server)
    }

    private void setMimeMappings(WebServerFactory server) {
        if (server instanceof ConfigurableServletWebServerFactory) {
            MimeMappings mappings = new MimeMappings(MimeMappings.DEFAULT)
            mappings.add("html", MediaType.TEXT_HTML_VALUE + ";charset=" + StandardCharsets.UTF_8.name().toLowerCase())
            mappings.add("json", MediaType.TEXT_HTML_VALUE + ";charset=" + StandardCharsets.UTF_8.name().toLowerCase())
            ConfigurableServletWebServerFactory servletWebServer = (ConfigurableServletWebServerFactory) server
            servletWebServer.setMimeMappings(mappings)
        }
    }

    private void setLocationForStaticAssets(WebServerFactory server) {
        if (server instanceof ConfigurableServletWebServerFactory) {
            ConfigurableServletWebServerFactory servletWebServer =
                    server as ConfigurableServletWebServerFactory
            File root = new File(resolvePathPrefix()
                    + "build/resources/main/static/")
            if (root.exists() && root.directory) {
                servletWebServer.documentRoot = root
            }
        }
    }

    /**
     * Resolve path prefix to static resources.
     */
    private String resolvePathPrefix() {
        String fullExecutablePath
        try {
            fullExecutablePath = decode(this.class.getResource("").path, StandardCharsets.UTF_8.name())
        } catch (UnsupportedEncodingException e) {
            /* try without decoding if this ever happens */
            fullExecutablePath = this.class.getResource("").path
        }
        String rootPath = Paths.get(".").toUri().normalize().getPath()
        String extractedPath = fullExecutablePath.replace(rootPath, "")
        int extractionEndIndex = extractedPath.indexOf("build/")
        if (extractionEndIndex <= 0) {
            return ""
        }
        return extractedPath.substring(0, extractionEndIndex)
    }

    /**
     * Initializes the caching HTTP Headers Filter.
     */
    private void initCachingHttpHeadersFilter(ServletContext servletContext,
                                              EnumSet<DispatcherType> disps) {
        log.debug("Registering Caching HTTP Headers Filter")
        FilterRegistration.Dynamic cachingHttpHeadersFilter =
                servletContext.addFilter("cachingHttpHeadersFilter",
                        new CachingHttpHeadersFilter(properties: properties))

        cachingHttpHeadersFilter.addMappingForUrlPatterns(disps, true, "/i18n/*")
        cachingHttpHeadersFilter.addMappingForUrlPatterns(disps, true, "/content/*")
        cachingHttpHeadersFilter.addMappingForUrlPatterns(disps, true, "/app/*")
        cachingHttpHeadersFilter.setAsyncSupported(true)
    }
}
