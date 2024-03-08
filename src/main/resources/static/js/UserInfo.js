class UserInfo {
	
	constructor() {
		this.username = '';
		this.password = '';
	}
	
	setUsername(username) {
		this.username = username;
	}
	
	setPassword(password) {
		this.password = password;
	}
	
	toJSON() {
		return {
			username: this.username,
			password: this.password
		};
	}
}

export default UserInfo;