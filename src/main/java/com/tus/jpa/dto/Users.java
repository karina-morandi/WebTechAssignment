package com.tus.jpa.dto;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name="Customers")
public class Users {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	private String login;
//	private String pword;
	private String password;
	
	public Users(String login, String password) {
		this.login = login;
//		this.pword = pword;
		this.password = password;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

//	public String getPword() {
//		return pword;
//	}
//
//	public void setPword(String pword) {
//		this.pword = pword;
//	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "Users [login=" + login + ", password=" + password + "]";
	}

}
