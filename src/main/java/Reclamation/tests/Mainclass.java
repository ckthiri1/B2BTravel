package Reclamation.tests;

import Reclamation.Services.ReclamationServices;
import Reclamation.Services.ReponseServices;
import Reclamation.entities.Reclamation;
//import Reclamation.entities.Reclamation;
import Reclamation.entities.Reponse;
import Reclamation.tools.MyConnection;

import java.time.LocalDate;
import java.util.List;

public class Mainclass {
    public static void main(String[] args) {
        MyConnection mc = new MyConnection(); // Initialisation de la connexion

        // Création d'une réclamation valide
        Reclamation rec1 = new Reclamation(
                "chedi",
                "L'utilisateur ne peut pas",
                LocalDate.of(2050, 11, 15),
                "en attente"
        );
        ReclamationServices rs = new ReclamationServices();

        rs.addEntity(rec1);


        System.out.println("Réclamation ajoutée avec succès !");


        Reclamation recToDelete = new Reclamation("aloo", "L'utilisateur ne peut pas", LocalDate.of(2050, 11, 15));
        recToDelete.setIDR(2); // Remplacer 1 par l'ID existant dans ta base de données
        rs.deleteEntity(recToDelete);
        int idInserted = 3;
        if (idInserted != -1) { // Vérifier si l'ajout a réussi
            System.out.println("Réclamation ajoutée avec succès ! ID: " + idInserted);

            // ✅ 1. Mise à jour de la réclamation ajoutée
            Reclamation updatedRec = new Reclamation(
                    "aaaaaaaalou",
                    "Description Modifiée",
                    LocalDate.of(2050, 12, 25)
            );
            updatedRec.setStatus("Traitée"); // Changer le statut
            rs.updateEntity(idInserted, updatedRec);

            // Afficher toutes les réclamations
            System.out.println("Liste des réclamations :");
            List<Reclamation> allReclamations = rs.getAllData();

            for (Reclamation rec : allReclamations) {
                System.out.println("ID : " + rec.getIDR() +
                        ", Titre : " + rec.getTitre() +
                        ", Description : " + rec.getDescription() +
                        ", Date : " + rec.getDateR() +
                        ", Statut : " + rec.getStatus());
            }
        }
    }
}
                // 2. Tester la gestion des réponses
/*
// Ajouter une réponse pour la réclamation 1
                int idReclamation1 = rec.getIDR();
                Reponse reponse1 = new Reponse("yap", LocalDate.of(2024, 2, 12), idReclamation1);
                ReponseServices reponseServices = new ReponseServices();

                int idReponse1 = reponseServices.addEntity(reponse1);
                System.out.println("\nRéponse ajoutée avec ID: " + idReponse1);

// Ajouter une réponse pour la réclamation 2
                int idReclamation2 = 3;
                Reponse reponse2 = new Reponse("okk", LocalDate.of(2024, 2, 13), idReclamation2);
                int idReponse2 = reponseServices.addEntity(reponse2);
                System.out.println("Réponse ajoutée avec ID: " + idReponse2);

// Afficher toutes les réponses
                System.out.println("\nToutes les réponses dans la base de données :");
                reponseServices.getAllData().forEach(System.out::println);

// Modifier une réponse
                Reponse updatedReponse = new Reponse("Réponse mise à jour", LocalDate.of(2024, 2, 15), idReclamation1);
                reponseServices.updateEntity(idReponse1, updatedReponse);
                System.out.println("\nRéponses après mise à jour :");
                reponseServices.getAllData().forEach(System.out::println);

// Supprimer une réponse
                reponseServices.deleteEntity(reponse2);
                System.out.println("\nRéponses restantes après suppression :");
                reponseServices.getAllData().forEach(System.out::println);

// Afficher les réponses pour une réclamation donnée (par exemple, IdRec = 1)
                System.out.println("\nRéponses pour la réclamation avec IdRec = 1 :");
                reponseServices.getReponsesByReclamation(idReclamation1).forEach(System.out::println);

            }

        }
    }
    }

}
}
*/