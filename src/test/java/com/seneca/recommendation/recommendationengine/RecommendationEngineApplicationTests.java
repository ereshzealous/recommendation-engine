package com.seneca.recommendation.recommendationengine;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RecommendationEngineApplicationTests {

	
	@LocalServerPort
	private int port;

	String host = "http://localhost:";

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void testForUnregisteredUser() {
		assertThat(restTemplate.getForObject(host + port + "/api/getMoviesByUser?userId=0", List.class).size() ==20);

	}

	@Test
	public void testForRegisteredUser() {
		assertThat(restTemplate.getForObject(host + port + "/api/getMoviesByUser?userId=1000", List.class).size() ==20);

	}

	@Test
	public void testForRegisteredUserByPreferences() {
		assertThat(restTemplate
				.getForObject(host + port + "/api/getMoviesByUserPref?userId=1000&prefType=genre&pref=Action",
						List.class)
				.size() ==20);
	}
	
	@Test
	public void testMoviesGroupedByGenre() {
		assertThat(
				restTemplate.getForObject(host + port + "/api/getMoviesGroupedByGenre", Map.class).size() >0);

	}

	// negative test cases starts from here
	
	@Test
	@Ignore
	public void testForRegisteredUserByPreferencesNegative() {
		assertThat(restTemplate
				.getForObject(host + port + "/api/getMoviesByUserPref?userId=100011&prefType=genre",
						List.class)
				.size() ==20);
	}

	@Test
	@Ignore
	public void testForRegisteredUserNegative() {
		assertThat(
				restTemplate.getForObject(host + port + "/api/getMoviesByUser?userId=null", List.class).size()==20);

	}
	
	


}
