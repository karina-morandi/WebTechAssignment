package com.tus.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.tus.jpa.dto.Admin;
import com.tus.jpa.repositories.AdminRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {
	
	@Autowired
	private AdminRepository adminRepo;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testCreateUser() {
		Admin admin = new Admin();
		admin.setEmail("admin@root.ie");
		admin.setLogin("admin");
		admin.setPassword("admin");
		
		Admin savedAdmin = adminRepo.save(admin);
		
		Admin existUser = entityManager.find(Admin.class, savedAdmin.getId());
		
		assertThat(existUser.getEmail()).isEqualTo(admin.getEmail());
	}

}
