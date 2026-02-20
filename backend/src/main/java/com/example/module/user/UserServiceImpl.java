package com.example.module.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	@Transactional
	public List<UiUser> search() {
		
		List<UiUser> beans = new ArrayList<>();

		Iterable<User> entities = userRepository.findAll();

		for (User entity : entities) {

			UiUser bean = new UiUser();

			bean.setId(entity.getId());
			bean.setFirstName(entity.getFirstName());
			bean.setLastName(entity.getLastName());

			beans.add(bean);
			
		}

		return beans;
	}
	
	@Override
	@Transactional
	public UiUser save(UiUser bean) {
		User entity = new User();

		entity.setFirstName(bean.getFirstName());
		entity.setLastName(bean.getLastName());

		userRepository.save(entity);

		bean.setId(entity.getId());
		return bean;
	}
}
