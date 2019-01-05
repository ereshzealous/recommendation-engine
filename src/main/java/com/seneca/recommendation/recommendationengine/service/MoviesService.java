package com.seneca.recommendation.recommendationengine.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seneca.recommendation.recommendationengine.exception.ApplicationException;
import com.seneca.recommendation.recommendationengine.service.vo.GenresVO;
import com.seneca.recommendation.recommendationengine.service.vo.MovieVO;
import com.seneca.recommendation.recommendationengine.service.vo.Preferences;
import com.seneca.recommendation.recommendationengine.service.vo.User;

import ch.qos.logback.core.net.SyslogOutputStream;

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
    public List<User> getAllUsers() throws ApplicationException {
        try {
            File propertiesFile = ResourceUtils.getFile("classpath:jsons/users.json");
            List<User> users = mapper.readValue(propertiesFile, mapper.getTypeFactory().constructCollectionType(List.class, User.class));
           
            
            return users;
        } catch (Exception e) {
            throw new ApplicationException(e.getMessage());
        }
    }
    
    

	/**
	 * @param userId
	 * @return List of top 20 movies 
	 */
	public List<MovieVO> getRecommendedMovies(Long userId) {
		// TODO ---- For registered and unregistered
		try {
			List<User> users = getAllUsers();

			List<MovieVO> returning = new ArrayList<>(20);

			Optional<User> matchingObject = users.stream().filter(n -> n.getId().equals(userId)).findFirst();

			User requiredUser = matchingObject.orElse(null);

			/*
			 * if no user exist with passing userId 
			 * will return User object as null
			 */
			if (requiredUser == null) {
				// Unregistered user.
				List<MovieVO> movieVOs = getAllMovies();

				// Sorting top movies based on LikesCount and returning first 20

				movieVOs.sort((MovieVO s1, MovieVO s2) ->  Long.compare(s2.getLikesCount() , s1.getLikesCount()));
				//movieVOs.forEach((s)->System.out.println("Likes count "+s.getLikesCount()));

			} else {
				// Registered user

				if (requiredUser != null) {
					List<MovieVO> movieVOs = getAllMovies();
					List<Preferences> preferences = requiredUser.getPreferences();
					for (Preferences preferences2 : preferences) {
						List<String> coutries = preferences2.getCountries();
						List<String> generes = preferences2.getGenres();
						
						for (Iterator iterator = generes.iterator(); iterator.hasNext();) {
							String genre = (String) iterator.next();
							returning.addAll(movieVOs.stream().filter(n -> n.getGenres().contains(genre)).limit(20)
									.collect(Collectors.toList()));
						}
						
						if (returning.size()<20) {
							for (Iterator iterator = coutries.iterator(); iterator.hasNext();) {
								String country = (String) iterator.next();
								returning.addAll(movieVOs.stream().filter(n -> n.getCountry().equals(country)).limit(20)
										.collect(Collectors.toList()));
							}
						}
					}
					returning.sort((MovieVO m1, MovieVO m2)-> m1.getCountry().compareTo(m2.getCountry()));
					returning.sort((MovieVO s1, MovieVO s2) ->  Long.compare(s2.getLikesCount() , s1.getLikesCount()));
					//returning.sort((MovieVO m1, MovieVO m2)-> (int)(m1.getLikesCount()-m2.getLikesCount()));

				}
			}
			returning = returning.subList(0, 20);
			return returning;
		} catch (ApplicationException e) {
			e.printStackTrace();
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
