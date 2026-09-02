package com.gomz.festivallineuptracker.model;

import jakarta.persistence.*;

@Entity
@Table(name = "stage", uniqueConstraints = {@UniqueConstraint(columnNames = {"festival_id", "name"})})
public class Stage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "festival_id", nullable = false)
    private Festival festival;

    @Column(nullable = false, length = 150)
    private String name;




    public Stage() {
    }




    public Stage(Festival festival, String name) {
        this.festival = festival;
        this.name = name;
    }





    public int getId() {
        return id;
    }

    public Festival getFestival() {
        return festival;
    }

    public String getName() {return name;}

    public void setFestival(Festival festival) {
        this.festival = festival;
    }

    public void setName(String name) {
        this.name = name;
    }
}
