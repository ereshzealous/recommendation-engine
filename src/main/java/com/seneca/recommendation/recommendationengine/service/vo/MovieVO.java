package com.seneca.recommendation.recommendationengine.service.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author Gorantla, Eresh
 * @created 03-01-2019
 */
@Getter
@Setter
public class MovieVO {
    private String name;
    private String productionCompany;
    private String description;
    private String country;
    private List<String> genres;
    private Long likesCount;
    private Integer releaseYear;
}
