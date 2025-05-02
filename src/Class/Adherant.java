package Class;
import Model.Model;

import java.sql.SQLException;

public class Adherant extends Utilisateur {

    public Adherant(String email, String password) {
        super(email, password);
    }

    public void reserver(int id_user,Seance s) throws SQLException, ClassNotFoundException {
        Model m = new Model();
        m.reservation(id_user, s.getId_seance());
    }
}
