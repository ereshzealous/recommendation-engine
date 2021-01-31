package com.seneca.recommendation.recommendationengine.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seneca.recommendation.recommendationengine.exception.ApplicationException;
import com.seneca.recommendation.recommendationengine.service.vo.GenresVO;
import com.seneca.recommendation.recommendationengine.service.vo.MovieVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * @author Gorantla, Eresh
 * @created 03-01-2019
 */
@Service
public class MoviesService {

    @Autowired
    private ObjectMapper mapper;

    public List<MovieVO> getAllMovies() throws ApplicationException {
        try {
        	//testing
            File propertiesFile = ResourceUtils.getFile("classpath:jsons/movies.json");
            List<MovieVO> movieVOS = mapper.readValue(propertiesFile, mapper.getTypeFactory().constructCollectionType(List.class, MovieVO.class));
            return movieVOS;
        } catch (Exception e) {
            throw new ApplicationException(e.getMessage());
        }
    }

    public List<GenresVO> getAllGeneres() throws ApplicationException {
        try {
            File propertiesFile = ResourceUtils.getFile("classpath:jsons/genres.json");
            List<GenresVO> genresVOS = mapper.readValue(propertiesFile, mapper.getTypeFactory().constructCollectionType(List.class, GenresVO.class));
            return genresVOS;
        } catch (Exception e) {
            throw new ApplicationException(e.getMessage());
        }
    }

    public List<MovieVO> getRecommendedMovies(Long userId) {
        // TODO ---- For registered and un registered
        if (userId == null) {
            // Unregistered user.
        } else {
            // Registered user
        }
        return Collections.emptyList();
    }

    public List<?> getMoviesGroupedByCountries() {
        return Collections.emptyList();
    }

    public List<?> getMoviesGroupedByGenres() {
        return Collections.emptyList();
    }
}
