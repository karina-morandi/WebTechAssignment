//package com.tus.jpa.dto;
//
//import javax.persistence.*;
//import javax.validation.constraints.Size;
//
//@Entity
//@Table(name="Admin")
//public class Admin {
//
//	@Id
//	@GeneratedValue(strategy=GenerationType.IDENTITY)
//	@Column(name="id", unique = true)
//	private long id;
//	
//	@Column(nullable = false, unique = true, length = 45)
//	private String login;
//	
//	@Column(nullable = false, unique = true, length = 45)
//	private String email;
//	
//	@Column(nullable = false, length = 64)
//	private String password;
//	
//	@Column(name="role")
//	private String role = "admin";
//	
//    public Admin() {
//    	
//    }
//	
//	public Admin(String login, String email, String password, String role) {
//		super();
//		this.login = login;
//		this.email = email;
//		this.password = password;
//		this.role = "admin";
//	}
//
//	public String getLogin() {
//		return login;
//	}
//
//	public void setLogin(String login) {
//		this.login = login;
//	}
//
//	public String getEmail() {
//		return email;
//	}
//
//	public void setEmail(String email) {
//		this.email = email;
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
//	public long getId() {
//		return id;
//	}
//	
//	public String getRole() {
//        return role;
//	}
//	
//	public void setRole(String role) {
//		this.role = role;
//	}
//
////	@Override
////	public String toString() {
////		return "Admin [id=" + id + ", login=" + login + ", email=" + email + ", password=" + password + ", role=" + role + "]";
////	}
//}
