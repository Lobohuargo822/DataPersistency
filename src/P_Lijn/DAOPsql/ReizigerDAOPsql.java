package P_Lijn.DAOPsql;

import P_Lijn.DAO.AdresDAO;
import P_Lijn.DAO.OVChipkaartDAO;
import P_Lijn.DAO.ReizigerDAO;
import P_Lijn.klassen.OVChipkaart;
import P_Lijn.klassen.Reiziger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReizigerDAOPsql implements ReizigerDAO {
    private Connection conn;
    private AdresDAO adao;
    private OVChipkaartDAO ovdao;
//    private ArrayList<OVChipkaartDAO> ovdao;
    // ov dao toevoegen, for-ech loop toepassen


    public AdresDAO getAdao() {
        return adao;
    }

    public void setAdao(AdresDAO adao) {
        this.adao = adao;
    }

    public OVChipkaartDAO getOvdao() {
        return ovdao;
    }

    public void setOvdao(OVChipkaartDAO ovdao) {
        this.ovdao = ovdao;
    }

    public ReizigerDAOPsql(Connection conn) {
        this.conn = conn;
    }

    @Override
    public boolean save(Reiziger reiziger){
        try{
            String query = "INSERT INTO reiziger(reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum) values(?,?,?,?,?)";
            PreparedStatement st = conn.prepareStatement(query);
            st.setInt(1, reiziger.getReiziger_id());
            st.setString(2, reiziger.getVoorletters());
            st.setString(3, reiziger.getTussenvoegsel());
            st.setString(4, reiziger.getAchternaam());
            st.setDate(5, (Date) reiziger.getGeboortedatum());
            st.executeQuery();
            adao.save(reiziger.getAdres());
//            ovdao.save(reiziger.getOvchipkaarten());
            return true;
        } catch(Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(Reiziger reiziger){
        try{
            String query = "UPDATE reiziger SET reiziger_id = ?, voorletters = ?, tussenvoegsel = ?, achternaam = ?, geboortedatum = ? " +
                    "WHERE reiziger_id = ?";
            PreparedStatement st = conn.prepareStatement(query);
            st.setInt(1, reiziger.getReiziger_id());
            st.setString(2, reiziger.getVoorletters());
            st.setString(3, reiziger.getTussenvoegsel());
            st.setString(4, reiziger.getAchternaam());
            st.setDate(5, (Date) reiziger.getGeboortedatum());
            st.setInt(6, reiziger.getReiziger_id());
            st.executeUpdate();
            if(adao.findByReiziger(reiziger) != null) {
                adao.update(reiziger.getAdres());
            } else {
                adao.save(reiziger.getAdres());
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        } return true;
    }

    @Override
    public boolean delete(Reiziger reiziger){
        try{
            String query = "DELETE from reiziger WHERE reiziger_id = ? ";
            PreparedStatement st = conn.prepareStatement(query);
            st.setInt(1, reiziger.getReiziger_id());
            st.executeUpdate();
            adao.delete(reiziger.getAdres());
//            ovdao.delete(reiziger.getOvchipkaarten());
        } catch(Exception e){
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public Reiziger findById(int id){  // adao + ovdao toevoegen zodat alle info returned wordt
        try {
            String query = "SELECT * FROM reiziger WHERE reiziger_id = ? ";
            PreparedStatement st = conn.prepareStatement(query);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery(query);
            if(rs.next()){
                Reiziger reiziger = new Reiziger(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getDate(5));
                reiziger.setAdres(adao.findByReiziger(reiziger));
                reiziger.setOvchipkaarten((ArrayList<OVChipkaart>) ovdao.findByReiziger(reiziger));
                return reiziger;
            } else{
                return null;
            }
        } catch(Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public List<Reiziger> findByGbDatum(String datum){
        List<Reiziger> reizigers = new ArrayList<>();
        try {
            String tussenVoegsel = "";
            String query = "SELECT * FROM reiziger WHERE geboortedatum = ?";
            PreparedStatement st = conn.prepareStatement(query);
            ResultSet rs = st.executeQuery();
            while (rs.next()){
                if (rs.getString(3) != null) {
                    tussenVoegsel = rs.getString(3);
                }
                reizigers.add(new Reiziger(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getDate(5)));
                } return reizigers;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                return null;
        }
    }

    @Override
    public List<Reiziger> findAll()throws SQLException {
        List<Reiziger> reizigers = new ArrayList<>();
        try {
            String query = "SELECT * FROM reiziger";
            String tussenVoegsel = "";
            PreparedStatement st = conn.prepareStatement(query);
            ResultSet rs = st.executeQuery();
            while(rs.next()) {
                if (rs.getString(3) != null) {
                    tussenVoegsel = rs.getString(3);
                }
               Reiziger reiziger = new Reiziger(
                rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getDate(5));
                reiziger.setAdres(adao.findByReiziger(reiziger));
                reiziger.setOvchipkaarten((ArrayList<OVChipkaart>) ovdao.findByReiziger(reiziger));
                reizigers.add(reiziger);
                } return reizigers;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
        }
        return null;
    }
}
