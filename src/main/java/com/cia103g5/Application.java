package com.cia103g5;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**##################################
 #							   		#
 #							  		#
 #  		  啟動 class				#
 #							     	#
 #	  						     	#
 ###################################*/

@SpringBootApplication
@EnableScheduling
public class Application {
	

	public static void main(String[] args) {

		
		SpringApplication.run(Application.class, args);
	}

}
