package com.tus.jpa.dto;

import javax.persistence.*;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

@Entity
@Table(name="Wines")
public class Wines {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	private int year;
	private String color;
	
	@Size(min=3) //the name has to be a minimum size of 3(validation)
	private String name;
	private String winery;
	private String grapes;
	private String country;
	private String region;
	
    @Column(name = "picture_path") // Store the path to the image in the database
    private String picturePath;
	
//	@Lob
//	private String picture;
	
	public Wines() {
		
	}
	
	public Wines(Long id, String name, String winery, int year, String color, String grapes, String country, String region, String picturePath) {
	    this.id = id;
	    this.name = name;
	    this.winery = winery;
	    this.year = year;
	    this.color = color;
	    this.grapes = grapes;
	    this.country = country;
	    this.region = region;
	    this.picturePath = picturePath;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}
	
	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getWinery() {
		return winery;
	}

	public void setWinery(String winery) {
		this.winery = winery;
	}

	public String getGrapes() {
		return grapes;
	}

	public void setGrapes(String grapes) {
		this.grapes = grapes;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

//	public String getPicture() {
//		return picture;
//	}
//
//	public void setPicture(String picture) {
//		this.picture = picture;
//	}

    // Getters and setters for picturePath
    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }
	
	@Override
	public String toString() {
		return "Wine [id = " + id + " winery " + winery + " name " + name + " grapes " + grapes + "color" + color + " country " + country + " region " + region + "]";
	}
	
}
