package com.gomz.festivallineuptracker.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "genre_relation", uniqueConstraints = {@UniqueConstraint(name = "uk_genre_relation_pair", columnNames = {"parent_genre_id", "child_genre_id"})})
public class GenreRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "parent_genre_id", nullable = false)
    private Genre parentGenre;

    @ManyToOne(optional = false)
    @JoinColumn(name = "child_genre_id", nullable = false)
    private Genre childGenre;


    public GenreRelation() {
    }



    public GenreRelation(Genre parentGenre, Genre childGenre) {
        this.parentGenre = parentGenre;
        this.childGenre = childGenre;
    }







    public int getId() {
        return id;
    }

    public Genre getParentGenre() {
        return parentGenre;
    }

    public Genre getChildGenre() {
        return childGenre;
    }

    public void setParentGenre(Genre parentGenre) {
        this.parentGenre = parentGenre;
    }

    public void setChildGenre(Genre childGenre) {
        this.childGenre = childGenre;
    }
}
