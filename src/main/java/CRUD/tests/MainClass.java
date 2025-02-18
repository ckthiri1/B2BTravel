package CRUD.tests;

import CRUD.entities.Hebergement;
import CRUD.entities.Reservation;
import CRUD.services.HebergementService;
import CRUD.services.ReservationService;

import java.time.LocalDateTime;

public class MainClass {
    public static void main(String[] args) {
        LocalDateTime dateReservation = LocalDateTime.of(2025, 2, 12, 0, 0, 0);

        Reservation p = new Reservation(dateReservation,500,"Resolue",1);
        Reservation p1 = new Reservation(dateReservation,101,"En attente",2);

        Hebergement h = new Hebergement("coucou","tunis","Hotel","lsgbilhs");
        Hebergement h1 = new Hebergement("6 etoile","sfax","Hostel","hellloooooooooo");
        Hebergement h2 = new Hebergement("coucou","tunis","Hotel","this is a description");

        ReservationService ps = new ReservationService();
        HebergementService hs = new HebergementService();
       // ps.addEntity(p1);

        //hs.addEntity2(h1);
        //hs.addEntity2(h);0
        //hs.updateEntity(2,h2);
        //hs.deleteEntity(h1);



        //System.out.println(ps.getAllData());
        System.out.println(hs.getAllData());
    }
}
