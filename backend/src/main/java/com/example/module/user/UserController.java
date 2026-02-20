package com.example.module.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/user")
	public @ResponseBody List<UiUser> search() {
		List<UiUser> users = new ArrayList<>();

		try {
			users = userService.search();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return users;

	}

	@PostMapping(value = "/user")
	public UiUser save(@RequestBody UiUser user) {
		UiUser bean = new UiUser();

		try {
			bean = userService.save(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bean;
	}
}
