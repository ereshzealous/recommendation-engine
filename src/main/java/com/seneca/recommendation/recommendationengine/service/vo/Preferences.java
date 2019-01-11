package com.seneca.recommendation.recommendationengine.service.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
public class Preferences {
	@JsonFormat(shape=JsonFormat.Shape.ARRAY)
	private List<String> genres;
	@JsonFormat(shape=JsonFormat.Shape.ARRAY)
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
	@Override
	public String toString() {
		return "Preferences [genres=" + genres + ", countries=" + countries + "]";
	}
	
	
	
}
