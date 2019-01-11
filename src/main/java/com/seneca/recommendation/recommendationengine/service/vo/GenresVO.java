package com.seneca.recommendation.recommendationengine.service.vo;

/**
 * @author Gorantla, Eresh
 * @created 03-01-2019
 */
public class GenresVO {
    private Integer id;
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

	public GenresVO(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public GenresVO() {
		super();
		// TODO Auto-generated constructor stub
	}
    
    
}
