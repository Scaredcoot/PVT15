package StarGaze;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {
	
	
	
	//------------------------------Contructors------------------------------------------
	
	
	
	
	
	
	
	//------------------------------Methods------------------------------------------
	
	
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Pvt15Application.class);
	}

}
