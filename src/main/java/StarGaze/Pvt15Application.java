package StarGaze;

import java.text.ParseException;
import java.util.ArrayList;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class Pvt15Application extends SpringBootServletInitializer {
	
	
	
	
	//------------------------------Contructors------------------------------------------
	
	
	
	
	
	//------------------------------Methods------------------------------------------
	
	public static void main(String[] args){
		SpringApplication.run(Pvt15Application.class, args);
		
		//ArrayList<String> test = DateHandler.getComingDates(); 	// Tar emot ArrayList<String> med dagens datum och kommande 5 dagar
	//	System.err.println("Today and the 5 upcoming dates:");
		//for(int i = 0; i < test.size(); i++){
			//System.err.println(test.get(i));
		
		//} tog bort för att testa SMHIdatareader klassen
		
		
		SMHIDataReader instance = new SMHIDataReader();
		instance.getPlaceDataForTonight("gardet");
		
		
		
		//ConnectionManager inst = ConnectionManager.getInstance();
		//inst.getConnection();
		
	}
	
	@Override
	 protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
	 return builder.sources(Pvt15Application.class);
	 }
}
