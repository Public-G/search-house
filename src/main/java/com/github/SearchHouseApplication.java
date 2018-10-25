package com.github;


import com.github.common.constant.ApiReasonConstant;
import com.github.common.exception.SHException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@SpringBootApplication
@Controller
public class SearchHouseApplication {

	public static void main(String[] args) {
		SpringApplication.run(SearchHouseApplication.class, args);
	}

	@GetMapping("/me")
	public void me(){
		throw new SHException(ApiReasonConstant.INTERNAL_SERVER_ERROR_MSG);
	}

	@Secured("ROLE_ADMIN")
	@GetMapping("/admin/hello")
	@ResponseBody
	public String hello(){
		return "hello";
	}

	@GetMapping("/hello/?")
	@ResponseBody
	public String hello2(){
		return "hello" + "?";
	}
}
