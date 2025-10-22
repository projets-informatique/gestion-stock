package ma.projet.service;
import ma.projet.classes.Produit;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;
import java.util.Date;

public class ProduitService implements IDao<Produit> {
    public boolean create(Produit o) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        s.save(o); tx.commit(); s.close(); return true;
    }
    public boolean update(Produit o) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        s.update(o); tx.commit(); s.close(); return true;
    }
    public boolean delete(Produit o) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        s.delete(o); tx.commit(); s.close(); return true;
    }
    public Produit findById(int id) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        Produit p = s.get(Produit.class, id); s.close(); return p;
    }
    public List<Produit> findAll() {
        Session s = HibernateUtil.getSessionFactory().openSession();
        List<Produit> list = s.createQuery("from Produit", Produit.class).list();
        s.close(); return list;
    }
    public List<Produit> produitsParCategorie(int idCat) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        List<Produit> produits = s.createQuery("from Produit where categorie.id = :id", Produit.class)
                .setParameter("id", idCat).list();
        s.close(); return produits;
    }
    public List<Object[]> produitsCommandesEntreDates(Date d1, Date d2) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        List<Object[]> res = s.createQuery(
                        "select p.reference, p.prix, sum(l.quantite) from LigneCommandeProduit l join l.produit p join l.commande c where c.date between :d1 and :d2 group by p.id", Object[].class)
                .setParameter("d1", d1)
                .setParameter("d2", d2)
                .list();
        s.close(); return res;
    }
    public List<Object[]> produitsDansCommande(int idCmd) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        List<Object[]> res = s.createQuery(
                        "select p.reference, p.prix, l.quantite from LigneCommandeProduit l join l.produit p where l.commande.id = :id", Object[].class)
                .setParameter("id", idCmd).list();
        s.close(); return res;
    }
    public List<Produit> produitsPrixSuperieur100() {
        Session s = HibernateUtil.getSessionFactory().openSession();
        List<Produit> produits = s.createQuery("from Produit where prix > 100", Produit.class).list();
        s.close(); return produits;
    }
}
