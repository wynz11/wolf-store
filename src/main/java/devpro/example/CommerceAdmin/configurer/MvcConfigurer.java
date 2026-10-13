package devpro.example.CommerceAdmin.configurer;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfigurer implements WebMvcConfigurer {
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		
		registry.addResourceHandler("/UploadFiles/**").addResourceLocations(
				"file:C:/Users/Admin/Downloads/CommerceAdmin/CommerceAdmin/UploadFiles/");
	}

}
