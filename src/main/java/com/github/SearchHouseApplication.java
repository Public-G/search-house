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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@SpringBootApplication
@Controller
public class SearchHouseApplication {

	public static void main(String[] args) {
		SpringApplication.run(SearchHouseApplication.class, args);
	}

	@GetMapping("/me")
	public void me(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		throw new SHException(ApiReasonConstant.INTERNAL_SERVER_ERROR_MSG);
		request.getRequestDispatcher("classpath:/templates/success.html").forward(request, response);
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
