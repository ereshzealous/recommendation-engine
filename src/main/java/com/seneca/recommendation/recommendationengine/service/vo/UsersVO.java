package com.seneca.recommendation.recommendationengine.service.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class UsersVO {

	private int id;
	private String firstName;
	private String lastName;
	
	private List<Preferences> preferences;
	
	private List<Integer> watchHistory;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	

	public List<Preferences> getPreferences() {
		return preferences;
	}

	public void setPreferences(List<Preferences> preferences) {
		this.preferences = preferences;
	}

	public List<Integer> getWatchHistory() {
		return watchHistory;
	}

	public void setWatchHistory(List<Integer> watchHistory) {
		this.watchHistory = watchHistory;
	}

	@Override
	public String toString() {
		return "UsersVO [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", preferences="
				+ preferences + ", watchHistory=" + watchHistory + "]";
	}

}
