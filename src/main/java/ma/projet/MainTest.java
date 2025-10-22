package ma.projet;

import ma.projet.classes.*;
import ma.projet.service.ProduitService;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainTest {
    public static void main(String[] args) {
        // --- INJECTION DES DONNÉES ---
        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();

        // Cherche la commande 4
        Commande c = s.get(Commande.class, 4);
        if (c == null) {
            // Crée une catégorie si besoin
            Categorie cat2 = (Categorie) s.createQuery("from Categorie where id = 2").uniqueResult();
            if (cat2 == null) {
                cat2 = new Categorie();
                cat2.setCode("INFO");
                cat2.setLibelle("Informatique");
                s.save(cat2);
            }

            // Crée les produits
            Produit p1 = new Produit(); p1.setReference("ES12"); p1.setPrix(120); p1.setCategorie(cat2); s.save(p1);
            Produit p2 = new Produit(); p2.setReference("ZR85"); p2.setPrix(100); p2.setCategorie(cat2); s.save(p2);
            Produit p3 = new Produit(); p3.setReference("EE85"); p3.setPrix(200); p3.setCategorie(cat2); s.save(p3);

            // Crée la commande numéro 4
            c = new Commande();
            c.setId(4);
            c.setDate(new GregorianCalendar(2013, Calendar.MARCH, 14).getTime());
            s.save(c);

            // Lignes de commande
            LigneCommandeProduit l1 = new LigneCommandeProduit(); l1.setProduit(p1); l1.setCommande(c); l1.setQuantite(7); s.save(l1);
            LigneCommandeProduit l2 = new LigneCommandeProduit(); l2.setProduit(p2); l2.setCommande(c); l2.setQuantite(14); s.save(l2);
            LigneCommandeProduit l3 = new LigneCommandeProduit(); l3.setProduit(p3); l3.setCommande(c); l3.setQuantite(5); s.save(l3);
        }
        tx.commit();
        s.close();

        // --- AFFICHAGE DE LA COMMANDE 4 ---
        ProduitService ps = new ProduitService();
        Session session = HibernateUtil.getSessionFactory().openSession();

        int idCommande = 4;

        // Récupère la commande depuis la BDD
        Commande commande = session.get(Commande.class, idCommande);
        if (commande == null) {
            System.out.println("Aucune commande trouvée avec l’ID : " + idCommande);
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.FRENCH);
        String dateFormatee = sdf.format(commande.getDate());

        System.out.println("Commande : " + idCommande + "     Date : " + dateFormatee);
        System.out.println("Liste des produits :");
        System.out.printf("%-12s %-10s %-10s%n", "Référence", "Prix", "Quantité");
        System.out.println("----------------------------------");

        List<Object[]> produits = ps.produitsDansCommande(idCommande);
        if (produits.isEmpty()) {
            System.out.println("Aucun produit trouvé pour cette commande.");
        } else {
            for (Object[] row : produits) {
                String reference = (String) row[0];
                float prix = (float) row[1];
                int quantite = (int) row[2];
                System.out.printf("%-12s %-8.0fDH %-10d%n", reference, prix, quantite);
            }
        }
        System.out.println("----------------------------------");
        session.close();
    }
}
