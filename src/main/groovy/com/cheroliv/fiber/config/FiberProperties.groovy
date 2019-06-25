package com.cheroliv.fiber.config

import groovy.transform.CompileStatic
import org.springframework.web.cors.CorsConfiguration

import javax.validation.constraints.NotNull

@CompileStatic
@Singleton
final class FiberProperties {

    final Async async = new Async()

    final Http http = new Http()

    final Cache cache = new Cache()

    final Mail mail = new Mail()

    final Security security = new Security()

    final Swagger swagger = new Swagger()

    final Metrics metrics = new Metrics()

    final Logging logging = new Logging()

    final CorsConfiguration cors = new CorsConfiguration()

    final Social social = new Social()

    final Gateway gateway = new Gateway()

    final Registry registry = new Registry()

    final ClientApp clientApp = new ClientApp()


    static class Async {

        int corePoolSize = FiberDefaults.Async.corePoolSize

        int maxPoolSize = FiberDefaults.Async.maxPoolSize

        int queueCapacity = FiberDefaults.Async.queueCapacity

    }

    static class Http {

        final Cache cache = new Cache()

        static class Cache {

            int timeToLiveInDays = FiberDefaults.Http.Cache.timeToLiveInDays
        }
    }

    static class Cache {

        final Hazelcast hazelcast = new Hazelcast()

        final Ehcache ehcache = new Ehcache()

        final Infinispan infinispan = new Infinispan()

        final Memcached memcached = new Memcached()

        static class Hazelcast {

            int timeToLiveSeconds = FiberDefaults.Cache.Hazelcast.timeToLiveSeconds

            int backupCount = FiberDefaults.Cache.Hazelcast.backupCount

            final ManagementCenter managementCenter = new ManagementCenter()

            static class ManagementCenter {

                boolean enabled = FiberDefaults.Cache.Hazelcast.ManagementCenter.enabled

                int updateInterval = FiberDefaults.Cache.Hazelcast.ManagementCenter.updateInterval

                String url = FiberDefaults.Cache.Hazelcast.ManagementCenter.url
            }
        }

        static class Ehcache {

            int timeToLiveSeconds = FiberDefaults.Cache.Ehcache.timeToLiveSeconds

            long maxEntries = FiberDefaults.Cache.Ehcache.maxEntries
        }

        static class Infinispan {

            String configFile = FiberDefaults.Cache.Infinispan.configFile

            boolean statsEnabled = FiberDefaults.Cache.Infinispan.statsEnabled

            final Local local = new Local()

            final Distributed distributed = new Distributed()

            final Replicated replicated = new Replicated()

            static class Local {

                long timeToLiveSeconds = FiberDefaults.Cache.Infinispan.Local.timeToLiveSeconds

                long maxEntries = FiberDefaults.Cache.Infinispan.Local.maxEntries
            }

            static class Distributed {

                long timeToLiveSeconds = FiberDefaults.Cache.Infinispan.Distributed.timeToLiveSeconds

                long maxEntries = FiberDefaults.Cache.Infinispan.Distributed.maxEntries

                int instanceCount = FiberDefaults.Cache.Infinispan.Distributed.instanceCount
            }

            static class Replicated {

                long timeToLiveSeconds = FiberDefaults.Cache.Infinispan.Replicated.timeToLiveSeconds

                long maxEntries = FiberDefaults.Cache.Infinispan.Replicated.maxEntries
            }
        }

        static class Memcached {

            boolean enabled = FiberDefaults.Cache.Memcached.enabled

            /**
             * Comma or whitespace separated list of servers' addresses.
             */
            String servers = FiberDefaults.Cache.Memcached.servers

            int expiration = FiberDefaults.Cache.Memcached.expiration

            boolean useBinaryProtocol = FiberDefaults.Cache.Memcached.useBinaryProtocol
        }
    }

    static class Mail {

        boolean enabled = FiberDefaults.Mail.enabled

        String from = FiberDefaults.Mail.from

        String baseUrl = FiberDefaults.Mail.baseUrl

    }

    static class Security {

        final ClientAuthorization clientAuthorization = new ClientAuthorization()

        final Authentication authentication = new Authentication()

        final RememberMe rememberMe = new RememberMe()

        static class ClientAuthorization {

            String accessTokenUri = FiberDefaults.Security.ClientAuthorization.accessTokenUri

            String tokenServiceId = FiberDefaults.Security.ClientAuthorization.tokenServiceId

            String clientId = FiberDefaults.Security.ClientAuthorization.clientId

            String clientSecret = FiberDefaults.Security.ClientAuthorization.clientSecret

        }

        static class Authentication {

            final Jwt jwt = new Jwt()

            static class Jwt {

                String secret = FiberDefaults.Security.Authentication.Jwt.secret

                String base64Secret = FiberDefaults.Security.Authentication.Jwt.base64Secret

                long tokenValidityInSeconds = FiberDefaults.Security.Authentication.Jwt
                        .tokenValidityInSeconds

                long tokenValidityInSecondsForRememberMe = FiberDefaults.Security.Authentication.Jwt
                        .tokenValidityInSecondsForRememberMe

            }
        }

        static class RememberMe {

            @NotNull
            String key = FiberDefaults.Security.RememberMe.key
        }
    }

    static class Swagger {

        String title = FiberDefaults.Swagger.title

        String description = FiberDefaults.Swagger.description

        String version = FiberDefaults.Swagger.version

        String termsOfServiceUrl = FiberDefaults.Swagger.termsOfServiceUrl

        String contactName = FiberDefaults.Swagger.contactName

        String contactUrl = FiberDefaults.Swagger.contactUrl

        String contactEmail = FiberDefaults.Swagger.contactEmail

        String license = FiberDefaults.Swagger.license

        String licenseUrl = FiberDefaults.Swagger.licenseUrl

        String defaultIncludePattern = FiberDefaults.Swagger.defaultIncludePattern

        String host = FiberDefaults.Swagger.host

        String[] protocols = FiberDefaults.Swagger.protocols

        boolean useDefaultResponseMessages = FiberDefaults.Swagger.useDefaultResponseMessages

    }

    static class Metrics {

        final Logs logs = new Logs()

        static class Logs {

            boolean enabled = FiberDefaults.Metrics.Logs.enabled

            long reportFrequency = FiberDefaults.Metrics.Logs.reportFrequency

        }
    }

    static class Logging {

        boolean useJsonFormat = FiberDefaults.Logging.useJsonFormat

        final Logstash logstash = new Logstash()


        static class Logstash {

            boolean enabled = FiberDefaults.Logging.Logstash.enabled

            String host = FiberDefaults.Logging.Logstash.host

            int port = FiberDefaults.Logging.Logstash.port

            int queueSize = FiberDefaults.Logging.Logstash.queueSize

        }
    }

    static class Social {

        String redirectAfterSignIn = FiberDefaults.Social.redirectAfterSignIn

    }

    static class Gateway {

        final RateLimiting rateLimiting = new RateLimiting()

        RateLimiting getRateLimiting() {
            return rateLimiting
        }

        Map<String, List<String>> authorizedMicroservicesEndpoints = FiberDefaults.Gateway
                .authorizedMicroservicesEndpoints


        static class RateLimiting {

            boolean enabled = FiberDefaults.Gateway.RateLimiting.enabled

            long limit = FiberDefaults.Gateway.RateLimiting.limit

            int durationInSeconds = FiberDefaults.Gateway.RateLimiting.durationInSeconds

        }
    }

    static class Registry {

        String password = FiberDefaults.Registry.password

    }

    static class ClientApp {

        String name = FiberDefaults.ClientApp.name

    }
}
