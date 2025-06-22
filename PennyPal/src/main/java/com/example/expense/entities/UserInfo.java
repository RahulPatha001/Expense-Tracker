 package com.example.expense.entities;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.*;

 @Entity
@ToString
@AllArgsConstructor
@Getter
 @Setter
@NoArgsConstructor
@Table(name = "users")
 @JsonIgnoreProperties(ignoreUnknown = true)
public class UserInfo {
	
	@Id
	@Column(name = "user_id")
	private String userId;
	private String username;
	private String password;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "user_roles",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id")
			)
	private Set<UserRole> roles = new HashSet<>();

//	public String getUserId() {
//		return userId;
//	}
//
//	public void setUserId(String userId) {
//		this.userId = userId;
//	}
//
//	public String getUsername() {
//		return username;
//	}
//
//	public void setUsername(String username) {
//		this.username = username;
//	}
//
//	public String getPassword() {
//		return password;
//	}
//
//	public void setPassword(String password) {
//		this.password = password;
//	}
//
//	public Set<UserRole> getRoles() {
//		return roles;
//	}
//
//	public void setRoles(Set<UserRole> roles) {
//		this.roles = roles;
//	}
//
//	public UserInfo(String userId, String username, String password, Set<UserRole> roles) {
//		super();
//		this.userId = userId;
//		this.username = username;
//		this.password = password;
//		this.roles = roles;
//	}
//	
//	public UserInfo() {
//		
//	}

}
