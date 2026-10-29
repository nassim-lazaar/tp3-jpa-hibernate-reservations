package com.example.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dateDebut;

    @Column(nullable = false)
    private LocalDateTime dateFin;

    @Column
    private String motif;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = {javax.persistence.CascadeType.PERSIST, javax.persistence.CascadeType.MERGE})
    @JoinColumn(name = "salle_id", nullable = false)
    private Salle salle;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = {javax.persistence.CascadeType.PERSIST, javax.persistence.CascadeType.MERGE})
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private Utilisateur utilisateur;

    public Reservation() {}

    public Reservation(LocalDateTime debutReservation, LocalDateTime finReservation, String motifSeance) {
        this.dateDebut = debutReservation;
        this.dateFin = finReservation;
        this.motif = motifSeance;
    }

    public Reservation(LocalDateTime debutReservation, LocalDateTime finReservation, String motifSeance,
                       Salle salleChoisie, Utilisateur utilisateurDemandeur) {
        this.dateDebut = debutReservation;
        this.dateFin = finReservation;
        this.motif = motifSeance;
        this.salle = salleChoisie;
        this.utilisateur = utilisateurDemandeur;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long idReservation) {
        this.id = idReservation;
    }

    public LocalDateTime getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDateTime debutReservation) {
        this.dateDebut = debutReservation;
    }

    public LocalDateTime getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDateTime finReservation) {
        this.dateFin = finReservation;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motifSeance) {
        this.motif = motifSeance;
    }

    public Salle getSalle() {
        return salle;
    }

    public void setSalle(Salle salleChoisie) {
        this.salle = salleChoisie;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateurDemandeur) {
        this.utilisateur = utilisateurDemandeur;
    }

    @Override
    public String toString() {
        return "Reservation [id=" + id + ", dateDebut=" + dateDebut + ", dateFin=" + dateFin +
                ", motif=" + motif +
                ", salle=" + (salle != null ? salle.getNom() : "null") +
                ", utilisateur=" + (utilisateur != null ? utilisateur.getNom() : "null") + "]";
    }
}
