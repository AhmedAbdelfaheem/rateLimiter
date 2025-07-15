package com.example.ratelimiter.components;

import com.example.ratelimiter.annotations.RateLimited;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import com.example.ratelimiter.services.RateLimitingService;

import java.lang.reflect.Method;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    private final RateLimitingService rateLimitingService;

    @Autowired
    public RateLimitInterceptor(RateLimitingService rateLimitingService) {
        this.rateLimitingService = rateLimitingService;
    }

    /**
     * This method is called before the actual handler (controller method) is executed.
     * It checks for the @RateLimited annotation on the handler method.
     * If present, it attempts to acquire a permit from the RateLimitingService.
     * If the permit cannot be acquired (rate limit exceeded), it sends a 429 Too Many Requests
     * status code and prevents the request from proceeding to the controller.
     *
     * @param request The current HttpServletRequest.
     * @param response The current HttpServletResponse.
     * @param handler The handler (controller method) that will be executed.
     * @return true if the request should proceed to the controller, false otherwise.
     * @throws Exception if an error occurs during processing.
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();

            // Check if the method is annotated with @RateLimited
            if (method.isAnnotationPresent(RateLimited.class)) {
                RateLimited rateLimited = method.getAnnotation(RateLimited.class);
                int capacity = rateLimited.capacity();
                long refillSeconds = rateLimited.refillSeconds();
                RateLimited.Algorithm algorithm = rateLimited.algorithm();

                // For simplicity, using the request's remote address as the key.
                String key = request.getRemoteAddr() + ":" + request.getRequestURI(); // Example key: IP + URI

                // Attempt to acquire a permit using the specified algorithm
                if (!rateLimitingService.tryAcquire(key, capacity, refillSeconds, algorithm)) {
                    // If rate limit exceeded, set HTTP status to 429 Too Many Requests
                    response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                    response.getWriter().write("Too Many Requests. Please try again later.");
                    return false; // Prevent the request from proceeding to the controller
                }
            }
        }
        return true; // Allow the request to proceed
    }


}
