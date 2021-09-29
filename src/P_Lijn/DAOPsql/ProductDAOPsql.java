package P_Lijn.DAOPsql;

import P_Lijn.DAO.ProductDAO;
import P_Lijn.klassen.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProductDAOPsql implements ProductDAO {
    private Connection conn;

    public ProductDAOPsql(Connection conn) {
        this.conn = conn;
    }

    @Override
    public boolean save(Product product) {
        try{
            String query = "INSERT INTO product(product_nummer, naam, beschrijving, prijs) " +
                    "values(?,?,?,?)";
            PreparedStatement st = conn.prepareStatement(query);
            st.setInt(1, product.getProduct_nummer());
            st.setString(2, product.getNaam());
            st.setString(3, product.getBeschrijving());
            st.setFloat(4, product.getPrijs());
            st.executeQuery();
            return true;
        } catch (SQLException e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(Product product) {
        try{
            String query = "UPDATE product SET product_nummer = ?, naam = ?, beschrijving = ?, prijs = ?" +
                    "WHERE product_nummer = ?";
            PreparedStatement st = conn.prepareStatement(query);
            st.setInt(1, product.getProduct_nummer());
            st.setString(2, product.getNaam());
            st.setString(3, product.getBeschrijving());
            st.setFloat(4, product.getPrijs());
            st.executeUpdate();
            return true;
        } catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(Product product) {
        try{
            String query = "DELETE from product WHERE product_nummer = ? ";
            PreparedStatement st = conn.prepareStatement(query);
            st.setInt(1, product.getProduct_nummer());
            st.executeUpdate();
        } catch(Exception e){
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }
}
