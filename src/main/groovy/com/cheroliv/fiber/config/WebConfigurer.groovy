package com.cheroliv.fiber.config

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.boot.web.server.WebServerFactory
import org.springframework.boot.web.server.WebServerFactoryCustomizer
import org.springframework.boot.web.servlet.ServletContextInitializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

import javax.servlet.ServletContext
import javax.servlet.ServletException

@Slf4j
@CompileStatic
@Configuration
class WebConfigurer implements ServletContextInitializer, WebServerFactoryCustomizer<WebServerFactory> {


    final Environment env


//    public WebConfigurer(Environment env, FiberProperties fiberDefaults) {
//        this.env = env;
//        this.fiberDefaults = fiberDefaults;
//    }

    @Override
    void onStartup(ServletContext servletContext) throws ServletException {
//        if (env.getActiveProfiles().length != 0) {
//            log.info("Web application configuration, using profiles: {}", (Object[]) env.getActiveProfiles());
//        }
//        EnumSet<DispatcherType> disps = EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.ASYNC);
//        if (env.acceptsProfiles(Profiles.of(JHipsterConstants.SPRING_PROFILE_PRODUCTION))) {
//            initCachingHttpHeadersFilter(servletContext, disps);
//        }
//        if (env.acceptsProfiles(Profiles.of(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT))) {
//            initH2Console(servletContext);
//        }
        log.info("Web application fully configured")
    }


    @Override
    void customize(WebServerFactory server) {
//        setMimeMappings(server);
//         When running in an IDE or with ./gradlew bootRun, set location of the static web assets.
//        setLocationForStaticAssets(server);
    }

//    private void setMimeMappings(WebServerFactory server) {
//        if (server instanceof ConfigurableServletWebServerFactory) {
//            MimeMappings mappings = new MimeMappings(MimeMappings.DEFAULT);
//             IE issue, see https://github.com/jhipster/generator-jhipster/pull/711
//            mappings.add("html", MediaType.TEXT_HTML_VALUE + ";charset=" + StandardCharsets.UTF_8.name().toLowerCase());
//             CloudFoundry issue, see https://github.com/cloudfoundry/gorouter/issues/64
//            mappings.add("json", MediaType.TEXT_HTML_VALUE + ";charset=" + StandardCharsets.UTF_8.name().toLowerCase());
//            ConfigurableServletWebServerFactory servletWebServer = (ConfigurableServletWebServerFactory) server;
//            servletWebServer.setMimeMappings(mappings);
//        }
//    }

//    private void setLocationForStaticAssets(WebServerFactory server) {
//        if (server instanceof ConfigurableServletWebServerFactory) {
//            ConfigurableServletWebServerFactory servletWebServer = (ConfigurableServletWebServerFactory) server;
//            File root;
//            String prefixPath = resolvePathPrefix();
//            root = new File(prefixPath + "build/resources/main/static/");
//            if (root.exists() && root.isDirectory()) {
//                servletWebServer.setDocumentRoot(root);
//            }
//        }
//    }


//    private String resolvePathPrefix() {
//        String fullExecutablePath;
//        try {
//            fullExecutablePath = decode(this.getClass().getResource("").getPath(), StandardCharsets.UTF_8.name());
//        } catch (UnsupportedEncodingException e) {
//            /* try without decoding if this ever happens */
//            fullExecutablePath = this.getClass().getResource("").getPath();
//        }
//        String rootPath = Paths.get(".").toUri().normalize().getPath();
//        String extractedPath = fullExecutablePath.replace(rootPath, "");
//        int extractionEndIndex = extractedPath.indexOf("build/");
//        if (extractionEndIndex <= 0) {
//            return "";
//        }
//        return extractedPath.substring(0, extractionEndIndex);
//    }


//    private void initCachingHttpHeadersFilter(ServletContext servletContext,
//                                              EnumSet<DispatcherType> disps) {
//        log.debug("Registering Caching HTTP Headers Filter");
//        FilterRegistration.Dynamic cachingHttpHeadersFilter =
//            servletContext.addFilter("cachingHttpHeadersFilter",
//                new CachingHttpHeadersFilter(fiberDefaults));
//
//        cachingHttpHeadersFilter.addMappingForUrlPatterns(disps, true, "/i18n/*");
//        cachingHttpHeadersFilter.addMappingForUrlPatterns(disps, true, "/content/*");
//        cachingHttpHeadersFilter.addMappingForUrlPatterns(disps, true, "/app/*");
//        cachingHttpHeadersFilter.setAsyncSupported(true);
//    }

//    @Bean
//    CorsFilter corsFilter() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource()
//        CorsConfiguration config = fiberDefaults.cors
//        if (config.allowedOrigins != null && !config.allowedOrigins.empty) {
//            log.debug("Registering CORS filter")
//            source.registerCorsConfiguration("/api/**", config)
//            source.registerCorsConfiguration("/management/**", config)
//            source.registerCorsConfiguration("/v2/api-docs", config)
//        }
//        new CorsFilter(source)
//    }


}
