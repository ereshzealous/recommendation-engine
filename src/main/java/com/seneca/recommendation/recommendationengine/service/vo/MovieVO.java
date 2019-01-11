package com.seneca.recommendation.recommendationengine.service.vo;

import java.util.List;

/**
 * @author Gorantla, Eresh
 * @created 03-01-2019
 */
public class MovieVO {
    private Integer id;
    private String name;
    private String productionCompany;
    private String description;
    private String country;
    private List<String> genres;
    private Long likesCount;
    private Integer releaseYear;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProductionCompany() {
        return productionCompany;
    }

    public void setProductionCompany(String productionCompany) {
        this.productionCompany = productionCompany;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public Long getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(Long likesCount) {
        this.likesCount = likesCount;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

	public MovieVO(Integer id, String name, String productionCompany, String description, String country,
			List<String> genres, Long likesCount, Integer releaseYear) {
		super();
		this.id = id;
		this.name = name;
		this.productionCompany = productionCompany;
		this.description = description;
		this.country = country;
		this.genres = genres;
		this.likesCount = likesCount;
		this.releaseYear = releaseYear;
	}

	public MovieVO() {
		super();
		// TODO Auto-generated constructor stub
	}
    
    
}
