package com.linkedu.backend.Test;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Test {
    @Id
    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
