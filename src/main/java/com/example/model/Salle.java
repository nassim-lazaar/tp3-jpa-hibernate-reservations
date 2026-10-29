package com.example.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "salles")
public class Salle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private int capacite;

    @OneToMany(mappedBy = "salle", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Reservation> reservations = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "salle_equipement",
        joinColumns = @JoinColumn(name = "salle_id"),
        inverseJoinColumns = @JoinColumn(name = "equipement_id")
    )
    private Set<Equipement> equipements = new HashSet<>();

    public Salle() {}

    public Salle(String nomDeSalle, int capaciteMax) {
        this.nom = nomDeSalle;
        this.capacite = capaciteMax;
    }

    public void addEquipement(Equipement equipement) {
        equipements.add(equipement);
        equipement.getSalles().add(this);
    }

    public void removeEquipement(Equipement equipement) {
        equipements.remove(equipement);
        equipement.getSalles().remove(this);
    }

    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
        reservation.setSalle(this);
    }

    public void removeReservation(Reservation reservation) {
        reservations.remove(reservation);
        reservation.setSalle(null);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long idSalle) {
        this.id = idSalle;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nomDeSalle) {
        this.nom = nomDeSalle;
    }

    public int getCapacite() {
        return capacite;
    }

    public void setCapacite(int capaciteMax) {
        this.capacite = capaciteMax;
    }

    public Set<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(Set<Reservation> reservationsSalle) {
        this.reservations = reservationsSalle;
    }

    public Set<Equipement> getEquipements() {
        return equipements;
    }

    public void setEquipements(Set<Equipement> listeEquipements) {
        this.equipements = listeEquipements;
    }

    @Override
    public String toString() {
        return "Salle [id=" + id + ", nom=" + nom + ", capacite=" + capacite +
                ", equipementsCount=" + equipements.size() + "]";
    }
}
