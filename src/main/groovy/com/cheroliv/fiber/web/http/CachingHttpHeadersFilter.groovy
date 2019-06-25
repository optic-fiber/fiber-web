package com.cheroliv.fiber.web.http

import com.cheroliv.fiber.config.FiberProperties
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import javax.servlet.*
import javax.servlet.http.HttpServletResponse
import java.util.concurrent.TimeUnit

/**
 * This filter is used in production,
 * to put HTTP cache headers with a long (4 years) expiration time.
 */
@Slf4j
@CompileStatic
class CachingHttpHeadersFilter implements Filter {

    static final int DEFAULT_DAYS_TO_LIVE = 1461
    static final long DEFAULT_SECONDS_TO_LIVE = TimeUnit.DAYS.toMillis(DEFAULT_DAYS_TO_LIVE)

    long cacheTimeToLive = DEFAULT_SECONDS_TO_LIVE

    FiberProperties properties = FiberProperties.instance


    @Override
    void init(FilterConfig filterConfig) throws ServletException {
        cacheTimeToLive = TimeUnit.DAYS.toMillis(properties.http.cache.timeToLiveInDays)
    }

    @Override
    void destroy() {
    }

    @Override
    void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletResponse httpResponse = response as HttpServletResponse

        httpResponse.setHeader("Cache-Control", "max-age=" + cacheTimeToLive + ", public")
        httpResponse.setHeader("Pragma", "cache")

        // Setting Expires header, for proxy caching
        httpResponse.setDateHeader("Expires", cacheTimeToLive + System.currentTimeMillis())

        chain.doFilter(request, response)
    }
}
