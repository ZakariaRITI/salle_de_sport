import Class.Utilisateur;
import Class.Admin;
import Class.Adherant;
import Class.Seance;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException, ParseException {

    Utilisateur u=new Utilisateur("alami","mostapha","0699875412","alamimosta@gmail.com","3002");
    Utilisateur u1=new Utilisateur("hassani","said","0699875412","rhtrh@gmail.com","3002");
    Utilisateur u2=new Utilisateur("madani","karim","0699875412","uiluyjtji@gmail.com","3002");
    Utilisateur u3=new Utilisateur("alouch","sabir","0699875412","uoluillity@gmail.com","3002");
    Utilisateur u4=new Utilisateur("hfyan","kawtar","0699875412","rjheeebj@gmail.com","3002");
    Utilisateur u5=new Utilisateur("abid","nordine","0699875412","trihgtruihtgr@gmail.com","3002");
    Admin a=new Admin("ceo","148596");
    a.insereradherant(u);
    a.insereradherant(u1);
    a.insereradherant(u2);
    a.insereradherant(u3);
    a.insereradherant(u4);
    a.insereradherant(u5);
    a.supprimeradherant(11);
    a.supprimeradherant(15);
    a.supprimerutilisateur(20);
    a.supprimerutilisateur(19);
    a.supprimerutilisateur(25);

    Utilisateur u6=new Utilisateur("moslah","hamid","0688745213","aqwxszr@gmail.com","2003");
    a.modifierutilisateur(21,u6);
    Utilisateur u7=new Utilisateur("amrani","ines","07158963254","ines@gmail.com","1234");
    a.modifierutilisateur(24,u7);

    System.out.println("########################################################################################");
    System.out.println("l abonnement expire dans:   "+a.date_limite_abonnement(3)+"   jours");

        System.out.println("########################################################################################");
        String motCle = "mi";
        List<Adherant> adherantsTrouves = a.chercherAdherants(motCle);

        if (adherantsTrouves.isEmpty())
        {
            System.out.println("❌ Aucun adhérant trouvé pour le mot clé : " + motCle);
        } else
        {
            System.out.println("✅ Adhérents trouvés pour le mot clé : " + motCle);
            for (Adherant aa : adherantsTrouves)
            {
                System.out.println("---------------------------------");
                System.out.println("Nom: " + aa.getNom());
                System.out.println("Prénom: " + aa.getPrenom());
                System.out.println("Téléphone: " + aa.getTelephone());
                System.out.println("Email: " + aa.getEmail());
            }
         }

        JFrame j=new JFrame();
        j.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        j.setSize(200,200);

        JPanel p=new JPanel();
        p.setLayout(null);
        JButton b1=new JButton("reserver");
        b1.setBounds(50,100,100,30);
        p.add(b1);

        Adherant ad=new Adherant("zakrt@gmail.com","1234");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date utilDate = sdf.parse("2025-05-01");
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

        Seance s = new Seance(3, sqlDate, Time.valueOf("08:00:00"), 'H', "fitness", 13);
        b1.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e)
            {
                try {
                    ad.reserver(3,s);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        j.add(p);
        j.setVisible(true);
}
}