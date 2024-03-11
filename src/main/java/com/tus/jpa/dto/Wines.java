package com.tus.jpa.dto;

import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.Size;

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
	private double price;
	private String description;
	
    @Column(name = "picture") // Store the path to the image in the database
    private String picture;
    
    @ElementCollection
//    @CollectionTable(name="wine_ratings", joinColumns=@JoinColumn(name="wine_id"))
    @Column(name="rating")
    private List<Double> ratings;
	
//	@Lob
//	private String picture;
	
	public Wines() {
		
	}
	
	public Wines(Long id, String name, String winery, int year, String color, String grapes, String country, String region, double price, String description, String picture, List<Double> ratings) {
	    this.id = id;
	    this.name = name;
	    this.winery = winery;
	    this.year = year;
	    this.color = color;
	    this.grapes = grapes;
	    this.country = country;
	    this.region = region;
	    this.price = price;
	    this.description = description;
	    this.picture = picture;
        this.ratings = ratings;
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
	
	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

    // Getters and setters for picturePath
    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
    
    public List<Double> getRatings() {
        return ratings;
    }

    public void setRatings(List<Double> ratings) {
        this.ratings = ratings;
    }

	@Override
	public String toString() {
		return "Wines [id=" + id + ", year=" + year + ", color=" + color + ", name=" + name + ", winery=" + winery
				+ ", grapes=" + grapes + ", country=" + country + ", region=" + region + ", price=" + price
				+ ", description=" + description + ", picture=" + picture + ", ratings=" + ratings + "]";
	}	
}
