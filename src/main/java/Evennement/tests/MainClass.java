package Evennement.tests;
import Evennement.entities.Evennement;
import Evennement.entities.Organisateur;
import Evennement.services.EvennementService;
import Evennement.services.OrganisateurService;
import Evennement.tools.MyConnection;

import java.security.Provider;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;


public class MainClass {
    public static void main(String[] args) throws ParseException {
        MyConnection myConn = new MyConnection();
        MyConnection myConn1 = new MyConnection();
        System.out.println(myConn);
        System.out.println(myConn1);



        ////////////////////////////////////AJOUT EVENNEMENT///////////////////////////////
      /* EvennementService service = new EvennementService();
        Date eventDate = null;
        SimpleDateFormat sdf = null;
        try {
            String dateStr = "5000-08-09 15:30:00";
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            eventDate = sdf.parse(dateStr);

            Evennement e = new Evennement("ElFuego", "Gammarth", "Aloo", eventDate);
            service.add(e);

        } catch (Exception e) {
            e.printStackTrace();
        }

        */

        ///////////////////////////////UPDATE EVENNEMENT////////////////////////////////////////
       /* EvennementService service = new EvennementService();

        // Define the event ID to update
        int IDE = 24; // Change this to an existing event ID in your database

        // Create a new event object with updated details
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date updatedDateE = sdf.parse("2025-02-10 12:00:00");

        Evennement updatedEvent = new Evennement("Updated Name", "Updated Location", "Updated Description", updatedDateE);

        // Call update method
        service.update(updatedEvent, IDE);


        System.out.println("After update:");
        service.getAllData().forEach(e -> {
            System.out.println("ID: " + e.getIDE());
            System.out.println("Nom: " + e.getNomE());
            System.out.println("Local: " + e.getLocal());
            System.out.println("Description: " + e.getDesE());
            System.out.println("Date: " + e.getDateE());
            System.out.println("------------------------------");
        });
    }
*/
        ///////////////////////////////////AFFICHAGE Evenement///////////////////////////////////////////////
   /* EvennementService service = new EvennementService();
        List<Evennement> events = service.getAllData();
        if (events.isEmpty()) {
            System.out.println("No events found.");
        } else {
            for (Evennement e : events) {
                System.out.println("ID: " + e.getIDE());
                System.out.println("Nom: " + e.getNomE());
                System.out.println("Local: " + e.getLocal());
                System.out.println("Description: " + e.getDesE());
                System.out.println("Date: " + e.getDateE());
                System.out.println("------------------------------");
            }
        }
*/
        ///////////////////////////DELETE EVENEMENT///////////////////////////////////////////

    /*    EvennementService service = new EvennementService();

        int idToDelete = 25;
        System.out.println("Before deletion:");
        List<Evennement> eventsBefore = service.getAllData();
        for (Evennement e : eventsBefore) {
            System.out.println(e.getIDE() + " - " + e.getNomE());
        }
        service.delete(idToDelete);

        System.out.println("\nAfter deletion:");
        List<Evennement> eventsAfter = service.getAllData();
        for (Evennement e : eventsAfter) {
            System.out.println("ID: " + e.getIDE());
            System.out.println("Nom: " + e.getNomE());
            System.out.println("Local: " + e.getLocal());
            System.out.println("Description: " + e.getDesE());
            System.out.println("Date: " + e.getDateE());
            System.out.println("------------------------------");
        }

            */
//////////////////////AJOUT ORGANISATEUR////////////////////////////////////////////
       /* OrganisateurService os = new OrganisateurService();
        Organisateur o = new Organisateur(1,"test",29401321);
        os.add(o);*/
///////////////////////UPDATE ORGANISATEUR/////////////////////////////////////////////

     /*    OrganisateurService service = new OrganisateurService();

        // Define the event ID to update
        // Change this to an existing event ID in your database


        Organisateur updatedOrganisateur = new Organisateur("Aloo",111111);


        // Call update method
        service.update(updatedOrganisateur, 2);

*/
//////////////////////DELETE ORGANISATEUR//////////////////////////////////////////////
/*
OrganisateurService orgService = new OrganisateurService();
orgService.delete(2);*/

//////////////////////////////AFFICHAGE ORGANISATEUR////////////////////////////////////

        OrganisateurService service = new OrganisateurService();
        List<Organisateur> organisateurs = service.getAllData();
        if (organisateurs.isEmpty()) {
            System.out.println("No events found.");
        } else {
            for (Organisateur o : organisateurs) {
                System.out.println("ID: " + o.getIDOr());
                System.out.println("Nom: " + o.getNomOr());
                System.out.println("Contact: " + o.getContact());
                System.out.println("------------------------------");
            }
        }

    }


}







