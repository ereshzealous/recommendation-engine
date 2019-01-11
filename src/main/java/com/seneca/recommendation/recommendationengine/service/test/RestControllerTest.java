package com.seneca.recommendation.recommendationengine.service.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.seneca.recommendation.recommendationengine.rest.MoviesResource;
import com.seneca.recommendation.recommendationengine.service.MoviesService;

@RunWith(SpringRunner.class)
@WebMvcTest(value = MoviesResource.class, secure = false)
public class RestControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private MoviesService moviesService;

	@Test
	public void testControllerMoviesForUser() throws Exception {
		mockMvc.perform(get("/api/getMoviesByUser?userId=8888")).andExpect(status().isOk());

	}

	@Test
	public void testControllerMoviesForUserByPreference() throws Exception {
		mockMvc.perform(get("/api/getMoviesByUserPref?userId=8888&prefType=genre&pref=Action"))
				.andExpect(status().isOk());

	}
	
	@Test
	public void testControllerMoviesGroupByGenre() throws Exception {
		mockMvc.perform(get("/api/getMoviesGroupedByGenre"))
				.andExpect(status().isOk());

	}

}
