package com.example.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "equipements")
public class Equipement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nom;

    @ManyToMany(mappedBy = "equipements")
    private Set<Salle> salles = new HashSet<>();

    public Equipement() {}

    public Equipement(String intituleEquipement) {
        this.nom = intituleEquipement;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long idEquipement) {
        this.id = idEquipement;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String intituleEquipement) {
        this.nom = intituleEquipement;
    }

    public Set<Salle> getSalles() {
        return salles;
    }

    public void setSalles(Set<Salle> sallesLiees) {
        this.salles = sallesLiees;
    }

    @Override
    public String toString() {
        return "Equipement [id=" + id + ", nom=" + nom + "]";
    }
}
