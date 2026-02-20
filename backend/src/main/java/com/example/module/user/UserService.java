package com.example.module.user;

import java.util.List;

public interface UserService {
	public List<UiUser> search();
	public UiUser save(UiUser bean);
}
