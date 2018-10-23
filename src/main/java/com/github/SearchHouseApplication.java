package com.github;


import com.github.common.constant.ApiReasonPhrase;
import com.github.common.exception.SHException;
import com.github.common.utils.ApiResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
@Controller
public class SearchHouseApplication {

	public static void main(String[] args) {
		SpringApplication.run(SearchHouseApplication.class, args);
	}

	@GetMapping("/me")
	public void me(){
		throw new SHException(ApiReasonPhrase.INTERNAL_SERVER_ERROR_MSG);
	}
}
