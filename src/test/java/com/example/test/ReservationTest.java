package com.example.test;

import com.example.model.Equipement;
import com.example.model.Reservation;
import com.example.model.Salle;
import com.example.model.Utilisateur;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;

public class ReservationTest {

    private EntityManagerFactory emf;
    private EntityManager em;

    @Before
    public void setUp() {
        emf = Persistence.createEntityManagerFactory("gestion-reservations-pu");
        em = emf.createEntityManager();
    }

    @After
    public void tearDown() {
        if (em != null && em.isOpen()) em.close();
        if (emf != null && emf.isOpen()) emf.close();
    }

    @Test
    public void testCascadePersistEtLiaisonBidirectionnelle() {
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        Salle salle = new Salle("Salle B301", 30);
        Equipement eq = new Equipement("Projecteur Full HD");
        salle.addEquipement(eq);

        Utilisateur user = new Utilisateur("Berrada", "Siham", "siham.berrada@test.ma");
        Reservation res = new Reservation(LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(2).plusHours(2), "Réunion Département");

        salle.addReservation(res);
        user.addReservation(res);

        em.persist(salle);
        em.persist(user);
        tx.commit();

        em.clear();

        Utilisateur userPersiste = em.find(Utilisateur.class, user.getId());
        assertNotNull(userPersiste);
        assertEquals(1, userPersiste.getReservations().size());

        Salle sallePersistee = em.find(Salle.class, salle.getId());
        assertNotNull(sallePersistee);
        assertEquals(1, sallePersistee.getEquipements().size());
    }

    @Test
    public void testRequeteJPQLEquipementsParSalle() {
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        Salle salle = new Salle("Salle Conférence 1", 50);
        Equipement eq1 = new Equipement("Microphone sans fil");
        Equipement eq2 = new Equipement("Caméra 4K");
        salle.addEquipement(eq1);
        salle.addEquipement(eq2);

        em.persist(salle);
        tx.commit();

        em.clear();

        List<Equipement> equipements = em.createQuery(
            "SELECT e FROM Equipement e JOIN e.salles s WHERE s.nom = :nomSalle", Equipement.class)
            .setParameter("nomSalle", "Salle Conférence 1")
            .getResultList();

        assertEquals(2, equipements.size());
    }

    @Test
    public void testOrphanRemovalReservation() {
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        Salle salle = new Salle("Salle Polyvalente", 40);
        Utilisateur user = new Utilisateur("Mansour", "Yassine", "yassine.m@test.ma");
        Reservation res = new Reservation(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(1), "Atelier Git");

        salle.addReservation(res);
        user.addReservation(res);

        em.persist(salle);
        em.persist(user);
        tx.commit();

        Long resId = res.getId();
        em.clear();

        // Suppression de la collection
        tx.begin();
        Utilisateur userCharge = em.find(Utilisateur.class, user.getId());
        Reservation resChargee = userCharge.getReservations().iterator().next();
        userCharge.removeReservation(resChargee);
        tx.commit();

        em.clear();
        assertNull("La réservation orpheline doit être supprimée de la base", em.find(Reservation.class, resId));
    }
}
