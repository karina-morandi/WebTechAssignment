package com.tus.jpa.dto;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;

@Entity
@Table(name="Users")
public class Users {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id", unique = true)
    private Long id;
	
	@Column(nullable = false, unique = true, length = 45)
	private String login;
	
	@Column(nullable = false, unique = true, length = 45)
	private String email;

	@Column(nullable = false, length = 75)
	private String password;
	
	@OneToMany(mappedBy = "customer")
    @JsonIgnore // Add this annotation to ignore the orders field during serialization
	private List<Orders> orders;
	
	@Column(name="role")
	private String role;

	public Users () {
		
	}

	public Users(String login, String email, String password, String role) {
		super();
		this.login = login;
		this.email = email;
		this.password = password;
		this.role = role;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	public List<Orders> getOrders(){
		return orders;
	}

	public void setOrders(List<Orders> orders) {
		this.orders = orders;
	}
}