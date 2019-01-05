package com.seneca.recommendation.recommendationengine.service.vo;

import java.util.List;

public class Preferences {
	
	private List<String> genres;
	private List<String> countries;
	public List<String> getGenres() {
		return genres;
	}
	public void setGenres(List<String> genres) {
		this.genres = genres;
	}
	public List<String> getCountries() {
		return countries;
	}
	public void setCountries(List<String> countries) {
		this.countries = countries;
	}
	 
	
}
