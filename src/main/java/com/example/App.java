package com.example;

import com.example.model.Equipement;
import com.example.model.Reservation;
import com.example.model.Salle;
import com.example.model.Utilisateur;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDateTime;
import java.util.List;

public class App {

    public static void main(String[] args) {
        System.out.println("======================================================================");
        System.out.println(" TP 3 : Gestion Avancée des Réservations, Salles & Equipements (JPA) ");
        System.out.println("======================================================================");

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("gestion-reservations-pu");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            // [1] Création et liaison des Equipements et Salles
            System.out.println("\n[1] Création des Salles et Équipements...");
            tx.begin();

            Equipement videoProjecteur = new Equipement("Vidéoprojecteur 4K Laser");
            Equipement ecranInteractif = new Equipement("Écran Tactile Collaboratif");
            Equipement systemeVision = new Equipement("Système Visioconférence HD");

            Salle salleA = new Salle("Salle Ibn Battuta - Recherche", 35);
            Salle salleB = new Salle("Amphithéâtre Averroès", 160);

            // Liaison bidirectionnelle
            salleA.addEquipement(videoProjecteur);
            salleA.addEquipement(ecranInteractif);
            salleB.addEquipement(videoProjecteur);
            salleB.addEquipement(systemeVision);

            // [2] Création des Utilisateurs et Réservations
            System.out.println("\n[2] Création de l'Utilisateur et Réservation...");
            Utilisateur user = new Utilisateur("Lazaar", "Nassim", "nassim.lazaar@ump.ac.ma");

            LocalDateTime debut = LocalDateTime.now().plusDays(1).withHour(9).withMinute(0);
            LocalDateTime fin = debut.plusHours(2);
            Reservation reservation = new Reservation(debut, fin, "Soutenance Projet Fin d'Études");

            user.addReservation(reservation);
            salleA.addReservation(reservation);

            // Persistance en cascade (CascadeType.ALL / PERSIST)
            em.persist(salleA);
            em.persist(salleB);
            em.persist(user);

            tx.commit();
            System.out.println("-> Données persistées avec succès via CascadeType.ALL.");

            // [3] Vider le contexte de persistance (L1 Cache)
            em.clear();
            System.out.println("\n[3] Contexte de persistance vidé (em.clear()) pour garantir la relecture SQL.");

            // [4] Requête JPQL avec paramètre nommé
            System.out.println("\n[4] Requête JPQL : Équipements de la salle 'Salle Ibn Battuta - Recherche' :");
            List<Equipement> equipementsSalleA = em.createQuery(
                "SELECT e FROM Equipement e JOIN e.salles s WHERE s.nom = :nomSalle", Equipement.class)
                .setParameter("nomSalle", "Salle Ibn Battuta - Recherche")
                .getResultList();

            for (Equipement eq : equipementsSalleA) {
                System.out.println("   * " + eq.getNom() + " (ID: " + eq.getId() + ")");
            }

            // [5] Requête JPQL : Réservations de l'utilisateur
            System.out.println("\n[5] Requête JPQL : Réservations de l'utilisateur 'nassim.lazaar@ump.ac.ma' :");
            List<Reservation> reservationsUser = em.createQuery(
                "SELECT r FROM Reservation r WHERE r.utilisateur.email = :emailUser", Reservation.class)
                .setParameter("emailUser", "nassim.lazaar@ump.ac.ma")
                .getResultList();

            for (Reservation res : reservationsUser) {
                System.out.println("   * Motif: " + res.getMotif() +
                        " | Salle: " + res.getSalle().getNom() +
                        " | Du: " + res.getDateDebut() + " Au: " + res.getDateFin());
            }

            // [6] Démonstration de l'orphanRemoval = true
            System.out.println("\n[6] Démonstration d'orphanRemoval sur la collection de réservations...");
            tx.begin();
            Utilisateur uManaged = em.find(Utilisateur.class, user.getId());
            Reservation resASupprimer = uManaged.getReservations().iterator().next();
            Long resId = resASupprimer.getId();

            uManaged.removeReservation(resASupprimer);
            tx.commit();

            em.clear();
            Reservation verifSuppr = em.find(Reservation.class, resId);
            System.out.println("-> Réservation retirée de la collection : Réservation en base = " + verifSuppr +
                    " (Suppression automatique confirmée : orphanRemoval = true)");

            // [7] Démonstration du détachement ManyToMany sans suppression d'équipement
            System.out.println("\n[7] Dissociation d'un équipement d'une salle (sans suppression physique de l'équipement)...");
            tx.begin();
            Salle salleManaged = em.find(Salle.class, salleA.getId());
            Equipement eqRetire = salleManaged.getEquipements().iterator().next();
            Long eqId = eqRetire.getId();

            salleManaged.removeEquipement(eqRetire);
            tx.commit();

            em.clear();
            Equipement eqToujoursExistant = em.find(Equipement.class, eqId);
            System.out.println("-> Équipement retiré de la salle : Équipement toujours en base = " +
                    (eqToujoursExistant != null ? eqToujoursExistant.getNom() : "null") +
                    " (Intégrité référentielle ManyToMany préservée).");

            System.out.println("\n======================================================================");
            System.out.println(" Exécution terminée avec succès !");
            System.out.println("======================================================================");

        } finally {
            if (em.isOpen()) em.close();
            if (emf.isOpen()) emf.close();
        }
    }
}
