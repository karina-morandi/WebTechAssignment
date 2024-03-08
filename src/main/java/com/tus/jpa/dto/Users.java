package com.tus.jpa.dto;

import javax.persistence.*;
import javax.validation.constraints.Size;

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
	
	@Column(nullable = false)
	private String role;

	public Users () {
		
	}

	public Users(String login, String password, String email, String role) {
        this.login = login;
        this.password = password;
        this.email = email;
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
	
	@Override
	public String toString() {
		return "Users [id=" + id + ", login=" + login + ", email=" + email + ", password=" + password + ", role=" + role
				+ "]";
	}
	
}