package com.seneca.recommendation.recommendationengine.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seneca.recommendation.recommendationengine.exception.ApplicationException;
import com.seneca.recommendation.recommendationengine.service.vo.GenresVO;
import com.seneca.recommendation.recommendationengine.service.vo.MovieVO;
import com.seneca.recommendation.recommendationengine.service.vo.UsersVO;

/**
 * @author Gorantla, Eresh
 * @created 03-01-2019
 */
/**
 * @ModifiedBy Rishabh Kokra(rishabh.kokra@gmail.com)
 * @Date 11-01-2019
 */
@Service
public class MoviesService {

	@Autowired
	private ObjectMapper mapper;

	Predicate<String> checkOrder = (str) -> str.equalsIgnoreCase("genre");
	BiPredicate<MovieVO, String> listPredicateForFilter = null;

	BiPredicate<UsersVO, MovieVO> genrePrefPredicate = (vo, mo) -> mo.getGenres().stream()
			.anyMatch(gen -> !vo.getPreferences().isEmpty() && vo.getPreferences().get(0).getGenres().contains(gen));

	BiPredicate<UsersVO, MovieVO> countryPrefPredicate = (vo, mo) -> !vo.getPreferences().isEmpty()
			&& vo.getPreferences().get(0).getCountries().contains(mo.getCountry());

	Predicate<String> checkPref = (str) -> str.equalsIgnoreCase("genre");
	Predicate<String> checkInput = (str) -> str == null || str.isEmpty();

	BiPredicate<UsersVO, Integer> checkMovieWatch = (vo, id) -> vo.getWatchHistory().contains(id);

	private List<MovieVO> getUsersUnwatchedOrWatchedMovies(UsersVO usersVO, List<MovieVO> movies,
			BiPredicate<UsersVO, Integer> predicate) {
		// this method will return user's watched or unwatched movies
		return movies.stream().filter(mov -> predicate.test(usersVO, mov.getId())).collect(Collectors.toList());

	}

	private List<MovieVO> getMoviesByUserPreferenceFromStore(UsersVO usersVO, List<MovieVO> allMovies,
			List<MovieVO> alreadyOnList, BiPredicate<UsersVO, Integer> predicate,
			BiPredicate<MovieVO, Integer> checkAlrdyInList, BiPredicate<UsersVO, MovieVO> prefPredicate, int limit) {
		// this method will return movies that are not there in watched list or
		// already in the unwatched list and order by likesCount in desc

		return allMovies.stream().sorted((i, j) -> j.getLikesCount().compareTo(i.getLikesCount()))
				.filter(mov -> prefPredicate.test(usersVO, mov))
				.filter(mov -> predicate.test(usersVO, mov.getId())
						&& alreadyOnList.stream().noneMatch(saw -> checkAlrdyInList.test(saw, mov.getId())))
				.limit(limit).collect(Collectors.toList());

	}

	public List<MovieVO> getAllMovies() throws ApplicationException {
		try {
			File propertiesFile = ResourceUtils.getFile("classpath:jsons/movies.json");
			List<MovieVO> movieVOS = mapper.readValue(propertiesFile,
					mapper.getTypeFactory().constructCollectionType(List.class, MovieVO.class));
			return movieVOS;
		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());
		}
	}

	public List<GenresVO> getAllGeneres() throws ApplicationException {
		try {
			File propertiesFile = ResourceUtils.getFile("classpath:jsons/genres.json");

			List<GenresVO> genresVOS = mapper.readValue(propertiesFile,
					mapper.getTypeFactory().constructCollectionType(List.class, GenresVO.class));
			return genresVOS;
		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());
		}
	}

	public Optional<UsersVO> getUser(Long userId) throws Exception {
		try {
			File propertiesFile = ResourceUtils.getFile("classpath:jsons/users.json");
			List<UsersVO> usersVOS = mapper.readValue(propertiesFile,
					mapper.getTypeFactory().constructCollectionType(List.class, UsersVO.class));
			Optional<UsersVO> user = usersVOS.stream().filter(i -> i.getId() == userId).findFirst();
			if (user.isPresent())
				return user;

		} catch (Exception e) {
			System.err.println("ERROR WHILE GETTING USER DETAILS!");
			throw e;
		}
		return Optional.empty();
	}

	public ResponseEntity<?> getRecommendedMovies(Long userId) {
		// TODO ---- For registered and un registered
		List<MovieVO> finalMovieList = null;
		try {
			Optional<UsersVO> userObj = getUser(userId);
			if (!userObj.isPresent()) {
				// Unregistered user.
				finalMovieList = getAllMovies().stream()
						.sorted((i, y) -> y.getLikesCount().compareTo(i.getLikesCount())).limit(20)
						.collect(Collectors.toList());

			} else {
				// Registered user

				finalMovieList = getRecommendedMovies(userObj.get(), "genre", 20,
						false);/*
								 * this method excepts user details and order in
								 * which movies should get populate first. if
								 * you pass country then movies list will
								 * contain country order first
								 */

			}
		} catch (ApplicationException ex) {
			System.err.println("FAILED TO GET MOVIES RECOMMENDATIONS DUE TO: " + ex.getErrorMessage());
			return new ResponseEntity<String>("FAILED TO GET MOVIES RECOMMENDATIONS \n CAUSE: " + ex.getErrorMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			System.err.println("FAILED TO GET MOVIES RECOMMENDATIONS DUE TO: " + e);
			return new ResponseEntity<String>("FAILED TO GET MOVIES RECOMMENDATIONS!",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<List<MovieVO>>(finalMovieList, HttpStatus.OK);
	}

	public ResponseEntity<?> getRecommendedMoviesByPreference(Long userId, String prefType, String preference) {
		// TODO ---- For registered and un registered
		List<MovieVO> finalMovieList = null;
		try {

			if (checkInput.test(preference))
				throw new ApplicationException("PREFERENCE CAN'T BE EMPTY OR NULL");
			if (checkInput.test(prefType))
				throw new ApplicationException("PREFERENCE TYPE CAN'T BE EMPTY OR NULL");

			Optional<UsersVO> userObj = getUser(userId);

			if (!userObj.isPresent()) {
				// Unregistered user.considering 0 as an unregistered userId and
				// user set his/her preferences

				if (checkPref.test(prefType))
					listPredicateForFilter = (mo, str) -> mo.getGenres().contains(str);
				else
					listPredicateForFilter = (mo, str) -> mo.getCountry().equalsIgnoreCase(str);

				finalMovieList = getAllMovies().stream().filter(mo -> listPredicateForFilter.test(mo, preference))
						.sorted((i, y) -> y.getLikesCount().compareTo(i.getLikesCount())).limit(20)
						.collect(Collectors.toList());
			} else {
				// Registered user
				List<String> prefL = new ArrayList<>();
				prefL.add(preference);
				// user preference I am assuming it would be two types: genre or
				// country
				if (checkPref.test(prefType))
					userObj.get().getPreferences().get(0).setGenres(prefL);
				else
					userObj.get().getPreferences().get(0).setCountries(prefL);

				finalMovieList = getRecommendedMovies(userObj.get(), prefType, 20,
						true);/*
								 * this method excepts user details and order in
								 * which movies should get populate first. if
								 * you pass country then movies list will
								 * contain country order first
								 */

			}
		} catch (ApplicationException ex) {
			System.err.println("FAILED TO GET MOVIES RECOMMENDATIONS DUE TO: " + ex.getErrorMessage());
			return new ResponseEntity<String>("FAILED TO GET MOVIES RECOMMENDATIONS \n CAUSE: " + ex.getErrorMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			System.err.println("FAILED TO GET MOVIES RECOMMENDATIONS DUE TO: " + e);
			return new ResponseEntity<String>("FAILED TO GET MOVIES RECOMMENDATIONS!",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<List<MovieVO>>(finalMovieList, HttpStatus.OK);
	}

	public Map<String, List<MovieVO>> getMoviesGroupedByCountries() throws Exception {

		return getAllMovies().stream().collect(Collectors.groupingBy(MovieVO::getCountry, Collectors.toList()));

	}

	public ResponseEntity<?> getMoviesGroupedByGenres() throws ApplicationException {

		Map<String, List<MovieVO>> map = new HashMap<>();
		List<Integer> moviesId = new ArrayList<>();
		try {
			getAllGeneres().stream().forEach(genre -> {
				try {
					getAllMovies().forEach(mov -> {
						if (map.get(genre.getName()) == null && !moviesId.contains(mov.getId())) {
							List<MovieVO> moviesList = new ArrayList<>();
							moviesId.add(mov.getId());
							moviesList.add(mov);
							map.put(genre.getName(), moviesList);

						} else {
							if (!moviesId.contains(mov.getId())) {
								map.get(genre.getName()).add(mov);
								moviesId.add(mov.getId());
							}
						}

					});
				} catch (ApplicationException e) {
					System.err.println("ERROR WHILE RETREIVING MOVIES GROUPED BY GENRE");
					e.printStackTrace();
				}
			});
		} catch (Exception e) {
			System.err.println("ERROR WHILE RETREIVING MOVIES GROUPED BY GENRE");
			return new ResponseEntity<String>("ERROR WHILE RETREIVING MOVIES GROUPED BY GENRE",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Map<String, List<MovieVO>>>(map, HttpStatus.OK);
	}

	private List<MovieVO> getRecommendedMovies(UsersVO userObj, String orderBy, int listSize,
			boolean moviesByUserSelectedPref) throws ApplicationException {
		List<MovieVO> orderedMoviesList = null;
		List<MovieVO> unwatchedMovieList = new ArrayList<>();
		List<MovieVO> watchedMovieList = new ArrayList<>();
		try {

			List<MovieVO> orderFirstList = new ArrayList<>();
			List<MovieVO> orderSecondList = new ArrayList<>();
			List<MovieVO> moviesList = getAllMovies();

			orderFirstList = getMoviesByPrefOrder(moviesList,
					checkOrder.test(orderBy) ? genrePrefPredicate : countryPrefPredicate, orderBy, userObj, listSize,
					(i, j) -> j.getLikesCount().compareTo(i.getLikesCount()), new ArrayList<>());

			if (orderFirstList.size() < listSize && !moviesByUserSelectedPref)
				orderSecondList = getMoviesByPrefOrder(moviesList,
						checkOrder.negate().test(orderBy) ? genrePrefPredicate : countryPrefPredicate, orderBy, userObj,
						(listSize - orderFirstList.size()), (i, j) -> j.getLikesCount().compareTo(i.getLikesCount()),
						orderFirstList);

			orderedMoviesList = Stream.concat(orderFirstList.stream(), orderSecondList.stream())
					.collect(Collectors.toList());

			unwatchedMovieList = getUsersUnwatchedOrWatchedMovies(userObj, orderedMoviesList, checkMovieWatch.negate());// populating
																														// unwatched
																														// movies

			watchedMovieList = getUsersUnwatchedOrWatchedMovies(userObj, orderedMoviesList, checkMovieWatch);// populating
																												// watched
																												// movies

			if (moviesByUserSelectedPref) {
				// user scenario 1 and point 6 implementation
				List<MovieVO> pullMoviesFromStore = null;
				List<MovieVO> usersMovies = Stream.concat(unwatchedMovieList.stream(), watchedMovieList.stream())
						.collect(Collectors.toList());

				if (unwatchedMovieList.size() < 20)
					pullMoviesFromStore = getMoviesByUserPreferenceFromStore(userObj, moviesList, usersMovies,
							checkMovieWatch.negate(), (saw, id) -> saw.getId() == id,
							checkOrder.test(orderBy) ? genrePrefPredicate.negate() : countryPrefPredicate.negate(),
							(20 - usersMovies.size()));

				return Stream.concat(unwatchedMovieList.stream(), pullMoviesFromStore.stream())
						.sorted((i, j) -> j.getLikesCount().compareTo(i.getLikesCount())).collect(Collectors.toList());

			}

		} catch (Exception e) {
			System.err.println("FAILED TO CREATE MOVIE OBJECT: " + e);
			throw new ApplicationException("FAILED TO CREATE MOVIE OBJECT");
		}

		// returning list with first unwatched movies followed by watched movies
		// and ordered by preference
		return Stream.concat(unwatchedMovieList.stream(), watchedMovieList.stream()).collect(Collectors.toList());

	}

	private List<MovieVO> getMoviesByPrefOrder(List<MovieVO> movies, BiPredicate<UsersVO, MovieVO> pred, String orderBy,
			UsersVO userObj, int limit, Comparator<MovieVO> comparator, List<MovieVO> moviesOnList) {
		// this method will return movies with order of genre or country and
		// sorted by likesCount in desc
		Set<Integer> moviesId = new HashSet<>();

		return movies.stream()
				.filter(mov -> pred.test(userObj, mov)
						&& moviesOnList.stream().noneMatch(listMovie -> listMovie.getId() == mov.getId())
						&& moviesId.add(mov.getId()))
				.peek(mov -> moviesId.add(mov.getId())).limit(limit).sorted(comparator).collect(Collectors.toList());

	}

}
