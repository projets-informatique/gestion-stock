import ma.projet.classes.Categorie;
import ma.projet.classes.Produit;
import ma.projet.service.ProduitService;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.*;

import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class test {

    private static ProduitService produitService;
    private static Categorie cat;

    @BeforeAll
    public static void setup() {
        produitService = new ProduitService();

        // Créer une catégorie test si elle n’existe pas
        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        cat = new Categorie();
        cat.setCode("TEST");
        cat.setLibelle("Catégorie Test");
        s.save(cat);
        tx.commit();
        s.close();
    }

    @Test
    @Order(1)
    public void testCreateProduit() {
        Produit p = new Produit();
        p.setReference("TST01");
        p.setPrix(150f);
        p.setCategorie(cat);

        boolean result = produitService.create(p);
        Assertions.assertTrue(result, "Le produit doit être créé avec succès");
    }

    @Test
    @Order(2)
    public void testFindAllProduits() {
        List<Produit> produits = produitService.findAll();
        Assertions.assertNotNull(produits, "La liste ne doit pas être nulle");
        Assertions.assertTrue(produits.size() > 0, "La base doit contenir au moins un produit");
    }

    @Test
    @Order(3)
    public void testFindById() {
        Produit p = produitService.findAll().get(0);
        Produit found = produitService.findById(p.getId());
        Assertions.assertNotNull(found, "Le produit doit être retrouvé");
        Assertions.assertEquals(p.getReference(), found.getReference(), "Les références doivent correspondre");
    }

    @Test
    @Order(4)
    public void testProduitsPrixSuperieur100() {
        List<Produit> produits = produitService.produitsPrixSuperieur100();
        Assertions.assertTrue(produits.size() > 0, "Il doit y avoir au moins un produit au-dessus de 100 DH");
    }
}