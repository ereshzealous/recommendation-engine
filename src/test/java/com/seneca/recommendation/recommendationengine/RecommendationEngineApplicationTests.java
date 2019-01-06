package com.seneca.recommendation.recommendationengine;

import static org.junit.Assert.*;


import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.seneca.recommendation.recommendationengine.exception.ApplicationException;
import com.seneca.recommendation.recommendationengine.rest.MoviesResource;
import com.seneca.recommendation.recommendationengine.service.vo.MovieVO;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RecommendationEngineApplicationTests {

	@Autowired
	MoviesResource movieResource;

	@Test
	public void contextLoads() {

	}

	@Test
	public void testGetMovies() {
		try {
			
			ResponseEntity movies= movieResource.getMovies(1001L, null, null);
			
			List<MovieVO> movieVOs = (List<MovieVO>) movies.getBody();
			assertEquals(List.class, movieVOs);
			assertEquals(20,movieVOs.size() );
		} catch (ApplicationException e) {
			
		}
	}

}
