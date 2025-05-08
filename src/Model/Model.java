package Model;

import java.sql.*;
import java.time.LocalDate;

public class Model {

    public Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection c;
        return DriverManager.getConnection("jdbc:mysql://localhost:3307/gym_bdd","root","2003");
    }

    public int adduser(String nom, String prenom, String telephone, String email, String password) throws SQLException, ClassNotFoundException {
        Connection c=getConnection();

        String checkQuery = "SELECT id_user FROM utilisateur WHERE email = ?";
        PreparedStatement checkStmt = c.prepareStatement(checkQuery);
        checkStmt.setString(1, email);
        ResultSet rs1 = checkStmt.executeQuery();

        if (rs1.next())
        {
            return rs1.getInt("id_user"); // retourne l'ID existant
        }

        String query="insert into utilisateur(nom,prenom,telephone,email,password) values(?,?,?,?,?)";
        PreparedStatement ps = c.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, nom);
        ps.setString(2, prenom);
        ps.setString(3, telephone);
        ps.setString(4, email);
        ps.setString(5, password);
        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next())
        {
            return rs.getInt(1);
        }
        else
        {
            throw new SQLException("Échec de la récupération de l'ID utilisateur.");
        }
    }

    public void addadherant(String login, String password,int id_user) throws SQLException, ClassNotFoundException {
        Connection c=getConnection();

        String checkQuery = "SELECT * FROM adherent WHERE id_user = ?";
        PreparedStatement checkStmt = c.prepareStatement(checkQuery);
        checkStmt.setInt(1, id_user);
        ResultSet rs = checkStmt.executeQuery();

        if (rs.next()) {
            System.out.println("⚠️ Adhérant existe déjà pour cet utilisateur (id_user = " + id_user + ")");
            return;
        }

        String query="insert into adherent(login,password,id_user) values(?,?,?)";
        PreparedStatement ps=c.prepareStatement(query);
        ps.setString(1, login);
        ps.setString(2, password);
        ps.setInt(3, id_user);
        ps.executeUpdate();
    }

    public void deleteadherant(int id_adherant) throws SQLException, ClassNotFoundException {
        Connection c=getConnection();
        String query="delete from adherent where id_adherent = ?";
        PreparedStatement ps=c.prepareStatement(query);
        ps.setInt(1, id_adherant);
        ps.executeUpdate();
    }

    public void deleteuser(int id_user) throws SQLException, ClassNotFoundException {
        Connection c=getConnection();
        String query="delete from utilisateur where id_user = ?" ;
        PreparedStatement ps=c.prepareStatement(query);
        ps.setInt(1, id_user);
        ps.executeUpdate();
    }

    public void update_userad(int id_usera,String nom2,String prenom2,String tel,String email2,String mdp) throws SQLException, ClassNotFoundException {
        Connection c=getConnection();
        String query="update utilisateur set nom=?,prenom=?,telephone=?,email=?,password=? where id_user = ?";
        PreparedStatement ps=c.prepareStatement(query);
        ps.setString(1, nom2);
        ps.setString(2, prenom2);
        ps.setString(3, tel);
        ps.setString(4, email2);
        ps.setString(5, mdp);
        ps.setInt(6, id_usera);
        ps.executeUpdate();

        String query2="update adherent set login=?,password=? where id_user = ?";
        PreparedStatement ps1=c.prepareStatement(query2);
        ps1.setString(1, email2);
        ps1.setString(2, mdp);
        ps1.setInt(3, id_usera);
        ps1.executeUpdate();
    }

    public LocalDate date_exp_abonnement(int id_user) throws SQLException, ClassNotFoundException
    {
        Connection c=getConnection();
        String query="select date_inscription from abonnement where id_user=?";
        PreparedStatement ps=c.prepareStatement(query);
        ps.setInt(1, id_user);
        ResultSet rs = ps.executeQuery();

        if (rs.next())
        {
            return rs.getDate("date_inscription").toLocalDate();
        }
        else
        {
            return null;
        }
    }

    public int duree(int user_id) throws SQLException, ClassNotFoundException {
        Connection c=getConnection();
        String query="select duree from abonnement where id_user=?";
        PreparedStatement ps=c.prepareStatement(query);
        ps.setInt(1, user_id);
        ResultSet rs = ps.executeQuery();
        if(rs.next())
        {
            return rs.getInt("duree");
        }
        else
        {
            return 0;
        }
    }

    public ResultSet searchAdherant(String keyword) throws SQLException, ClassNotFoundException {
        Connection c = getConnection();
        String query = "SELECT * FROM utilisateur u JOIN adherent a ON u.id_user = a.id_user " +
                "WHERE u.nom LIKE ? OR u.prenom LIKE ? OR u.telephone LIKE ? OR u.email LIKE ?";
        PreparedStatement ps = c.prepareStatement(query);
        String wildcardKeyword = "%" + keyword + "%";
        ps.setString(1, wildcardKeyword);
        ps.setString(2, wildcardKeyword);
        ps.setString(3, wildcardKeyword);
        ps.setString(4, wildcardKeyword);
        return ps.executeQuery();
    }

    public void reservation(int id_user,int id_seance) throws SQLException, ClassNotFoundException {
        Connection c = getConnection();

        String query0 ="select * from reservation where id_user=?";
        PreparedStatement ps0=c.prepareStatement(query0);
        ps0.setInt(1, id_user);
        ResultSet rs = ps0.executeQuery();

        if(!rs.next())
        {
        String query="insert into reservation(id_user,id_seance,date_reservation) values(?,?,?)";
        PreparedStatement ps=c.prepareStatement(query);
        ps.setInt(1, id_user);
        ps.setInt(2, id_seance);
        ps.setDate(3, new java.sql.Date(System.currentTimeMillis()));
        ps.executeUpdate();

        String query2="insert into group_seance(id_seance) values(?)";
        PreparedStatement ps2 = c.prepareStatement(query2, Statement.RETURN_GENERATED_KEYS);
        ps2.setInt(1, id_seance);
        ps2.executeUpdate();
        ResultSet rs3 = ps2.getGeneratedKeys();
            int id_group = -1;
            if (rs3.next())
            {
                id_group = rs3.getInt(1);
            }
            else
            {
                throw new SQLException("Échec de la récupération de l'id_group généré.");
            }

        String query3="insert into membre_group(id_group,id_user) values(?,?)";
        PreparedStatement ps3=c.prepareStatement(query3);
        ps3.setInt(1, id_group);
        ps3.setInt(2, id_user);
        ps3.executeUpdate();

        String query4="UPDATE seance SET max = max-1 WHERE id_seance = ?;";
        PreparedStatement ps4=c.prepareStatement(query4);
        ps4.setInt(1, id_seance);
        ps4.executeUpdate();
        }
    }

    public int nbr_adherant() throws SQLException, ClassNotFoundException {
        Connection c = getConnection();
        String query="select count(*) from adherent";
        Statement s=c.createStatement();
        ResultSet rs=s.executeQuery(query);
        if(rs.next())
        {
            return rs.getInt(1);
        }
        else
        {
            return 0;
        }
    }

    public double Chiffre_daffaires() throws SQLException, ClassNotFoundException {
        Connection c = getConnection();
        String query="select sum(tarif) from abonnement";
        Statement s=c.createStatement();
        ResultSet rs=s.executeQuery(query);
        if(rs.next())
        {
            return rs.getDouble(1);
        }
        else
        {
            return 0;
        }
    }

    public int Abonnements_par_promotion() throws SQLException, ClassNotFoundException {
        Connection c = getConnection();
        String query="select count(*) from abonnement where promotion=1";
        Statement s=c.createStatement();
        ResultSet rs=s.executeQuery(query);
        if(rs.next())
        {
            return rs.getInt(1);
        }
        else
        {
            return 0;
        }
    }

    public double zoneA() throws SQLException, ClassNotFoundException {
        Connection c = getConnection();
        Statement s=c.createStatement();
        String query="select sum(tarif) from abonnement where zone='Zone A'";
        ResultSet rs=s.executeQuery(query);
        if(rs.next())
        {
            return rs.getDouble(1);
        }
        else
        {
            return 0;
        }
    }

    public double zoneB() throws SQLException, ClassNotFoundException {
        Connection c = getConnection();
        Statement s=c.createStatement();
        String query="select sum(tarif) from abonnement where zone='Zone B'";
        ResultSet rs=s.executeQuery(query);
        if(rs.next())
        {
            return rs.getDouble(1);
        }
        else
        {
            return 0;
        }
    }

    public double zoneC() throws SQLException, ClassNotFoundException {
        Connection c = getConnection();
        Statement s=c.createStatement();
        String query="select sum(tarif) from abonnement where zone='Zone C'";
        ResultSet rs=s.executeQuery(query);
        if(rs.next())
        {
            return rs.getDouble(1);
        }
        else
        {
            return 0;
        }
    }

    public double zoneVIP() throws SQLException, ClassNotFoundException {
        Connection c = getConnection();
        Statement s=c.createStatement();
        String query="select sum(tarif) from abonnement where zone='Zone VIP'";
        ResultSet rs=s.executeQuery(query);
        if(rs.next())
        {
            return rs.getDouble(1);
        }
        else
        {
            return 0;
        }
    }

}
