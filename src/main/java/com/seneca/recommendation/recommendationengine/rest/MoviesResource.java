package com.seneca.recommendation.recommendationengine.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.seneca.recommendation.recommendationengine.exception.ApplicationException;
import com.seneca.recommendation.recommendationengine.service.MoviesService;
import com.seneca.recommendation.recommendationengine.service.vo.GenresVO;
import com.seneca.recommendation.recommendationengine.service.vo.MovieVO;
import com.seneca.recommendation.recommendationengine.service.vo.User;

/**
 * @author Gorantla, Eresh
 * @created 03-01-2019
 */
@RestController
@RequestMapping("/api")
public class MoviesResource {

    @Autowired
    private MoviesService moviesService;

    @GetMapping("/movies")
    public ResponseEntity<Object> getAllMovies() throws ApplicationException {
        List<MovieVO> movieVOS = moviesService.getAllMovies();
        return new ResponseEntity<Object>(movieVOS, HttpStatus.OK);
    }

    @GetMapping("/genres")
    public ResponseEntity<Object> getAllGenres() throws ApplicationException {
        List<GenresVO> movieVOS = moviesService.getAllGeneres();
        return new ResponseEntity<Object>(movieVOS, HttpStatus.OK);
    }
    
    @GetMapping("/getMovies")
    public ResponseEntity<Object> getHello(@RequestParam(value="userId", required=false) Long userId,
    		@RequestParam(value="countries", required=false) List<String> countries,
    		@RequestParam(value="genres", required=false) List<String> genres) throws ApplicationException {
    		
    	 List<MovieVO> movies = moviesService.getRecommendedMovies(userId, countries, genres);
    	 
        return new ResponseEntity<>(movies,HttpStatus.OK);
    }
}
