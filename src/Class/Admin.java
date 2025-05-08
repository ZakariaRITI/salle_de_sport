package Class;
import Model.Model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Admin extends Utilisateur{


    public Admin(String login, String password) {
        super(login, password);
    }

    public void insereradherant(Utilisateur u) throws SQLException, ClassNotFoundException {
        Model m=new Model();
        int id=m.adduser(u.getNom(),u.getPrenom(),u.getTelephone(),u.getEmail(),u.getPassword());
        m.addadherant(u.getEmail(),u.getPassword(),id);
    }

    public void supprimeradherant(int id) throws SQLException, ClassNotFoundException {
        Model m=new Model();
        m.deleteadherant(id);
    }

    public void supprimerutilisateur(int id) throws SQLException, ClassNotFoundException {
        Model m=new Model();
        m.deleteuser(id);
    }

    public void modifierutilisateur(int id,Utilisateur u) throws SQLException, ClassNotFoundException {
        Model m=new Model();
        m.update_userad(id,u.getNom(), u.getPrenom(), u.getTelephone(), u.getEmail(), u.getPassword());
    }

    public int date_limite_abonnement(int id_user) throws SQLException, ClassNotFoundException {
        Model m=new Model();
        LocalDate d = m.date_exp_abonnement(id_user);
        if (d == null)
        {
            System.out.println("Aucune date d'abonnement trouvée pour l'utilisateur " + id_user);
            return 0;
        }
        LocalDate today = LocalDate.now();


        long joursRestants = java.time.temporal.ChronoUnit.DAYS.between(today, d);
        int x= (int) joursRestants;
        int y=m.duree(id_user);
        int f=y+x;
        return f;
    }

    public static List<Adherant> chercherAdherants(String keyword) throws SQLException, ClassNotFoundException {
        Model m = new Model();
        ResultSet rs = m.searchAdherant(keyword);
        List<Adherant> adherantsTrouves = new ArrayList<>();

        while (rs.next())
        {
            Adherant a = new Adherant(rs.getString("email"), rs.getString("password"));
            a.setNom(rs.getString("nom"));
            a.setPrenom(rs.getString("prenom"));
            a.setTelephone(rs.getString("telephone"));
            adherantsTrouves.add(a);
        }
        return adherantsTrouves;
    }

    public int stats1() throws SQLException, ClassNotFoundException {
        Model m = new Model();
        return m.nbr_adherant();
    }

    public double stats2() throws SQLException, ClassNotFoundException {
        Model m = new Model();
        return m.Chiffre_daffaires();
    }

    public int stats3() throws SQLException, ClassNotFoundException {
        Model m = new Model();
        return m.Abonnements_par_promotion();
    }

    public double stats4() throws SQLException, ClassNotFoundException {
        Model m = new Model();
        return m.zoneA();
    }

    public double stats5() throws SQLException, ClassNotFoundException {
        Model m = new Model();
        return m.zoneB();
    }
    public double stats6() throws SQLException, ClassNotFoundException {
        Model m = new Model();
        return m.zoneC();
    }

    public double stats7() throws SQLException, ClassNotFoundException {
        Model m = new Model();
        return m.zoneVIP();
    }
}
