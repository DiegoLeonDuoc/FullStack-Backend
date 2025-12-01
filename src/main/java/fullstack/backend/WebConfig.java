package fullstack.backend;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final DelayInterceptor delayInterceptor;

    public WebConfig(DelayInterceptor delayInterceptor) {
        this.delayInterceptor = delayInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Registra el interceptor para todas las solicitudes
        registry.addInterceptor(delayInterceptor)
                .addPathPatterns("/**");  // Aplica el interceptor a todas las rutas
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173") // Aquí localhost debe reemplazarse por la IP del servidor React+Vite
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }
}


