package com.example.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "utilisateurs")
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    @Column(nullable = false, unique = true)
    private String email;

    @OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Reservation> reservations = new HashSet<>();

    public Utilisateur() {}

    public Utilisateur(String nomFamille, String prenomUser, String adresseMail) {
        this.nom = nomFamille;
        this.prenom = prenomUser;
        this.email = adresseMail;
    }

    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
        reservation.setUtilisateur(this);
    }

    public void removeReservation(Reservation reservation) {
        reservations.remove(reservation);
        reservation.setUtilisateur(null);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long idUtilisateur) {
        this.id = idUtilisateur;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nomFamille) {
        this.nom = nomFamille;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenomUser) {
        this.prenom = prenomUser;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String adresseMail) {
        this.email = adresseMail;
    }

    public Set<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(Set<Reservation> listeReservations) {
        this.reservations = listeReservations;
    }

    @Override
    public String toString() {
        return "Utilisateur [id=" + id + ", nom=" + nom + ", prenom=" + prenom +
                ", email=" + email + ", reservationsCount=" + reservations.size() + "]";
    }
}
