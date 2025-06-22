package com.example.expense.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.example.expense.entities.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.expense.entities.UserInfo;

public class CustomUserDetails extends UserInfo implements UserDetails {
	
	private String username;
	private String password;
	
	Collection<? extends GrantedAuthority> authorities; 
	
	public CustomUserDetails(UserInfo byUserName) {
		this.username = byUserName.getUsername();
		this.password = byUserName.getPassword();
		List<GrantedAuthority> auths = new ArrayList<>();
		for(UserRole role: byUserName.getRoles()){
			auths.add(new SimpleGrantedAuthority(role.getName().toUpperCase()));
		}
		
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
