package com.spingboot.demo.spingbootdemo.body;

import javax.persistence.Column;

public class BaseIdBody {
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    @Column(nullable = true)
    Integer id;
}
