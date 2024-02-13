package com.tus.jpa.dto;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name="Customers")
public class Users {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

}
