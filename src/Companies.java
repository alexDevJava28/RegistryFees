
import javax.swing.*;
import java.sql.*;

/**
 * Created by khodackovskiy on 10.12.2015.
 */
public class Companies implements SQLCommands {

    Connection conn;
    PreparedStatement psta;
    Statement sta;
    Object companyName;

    Companies(Connection conn, Object companyName){

        this.conn = conn;
        this.companyName = companyName;
    }

    @Override
    public void insert() {

        try {

            String insert = "INSERT INTO Companies (company, name) VALUES (COMPANIES_SEQ.nextval, ?)";

            psta = conn.prepareStatement(insert);
            psta.setString(1, String.valueOf(companyName));

            psta.executeUpdate();

        }catch (SQLException e){

            e.printStackTrace();

        }
    }

    @Override
    public void delete() {

        try {

            String delete = "DELETE FROM Companies WHERE NAME = ?";

            psta = conn.prepareStatement(delete);
            psta.setString(1, String.valueOf(companyName));

            psta.executeUpdate();

        }catch (SQLException e){

            JOptionPane.showMessageDialog(null, "Delete Error in Company name list");
            e.printStackTrace();
        }
    }

    public Long getCompanyNameId (){

        Long id = null;

        try {

            String select = "SELECT COMPANY FROM Companies WHERE NAME = '" + companyName + "'";
            sta = conn.createStatement();

            try (ResultSet result = sta.executeQuery(select))
            {
                if (result.next()){

                    id = result.getLong(1);
                }
            }

        }catch (SQLException e){

            JOptionPane.showMessageDialog(null, "Get Id Error in What pay for list");
            e.printStackTrace();
        }

        return id;
    }

    public void update(long company, Object newName){

        try {

            String update = "UPDATE Companies SET NAME = '" + newName + "' WHERE COMPANY = " + company;

            sta = conn.createStatement();
            sta.executeUpdate(update);

        }catch (SQLException e){

            e.printStackTrace();

        }finally {

            if (sta != null){

                try{

                    sta.close();

                }catch (SQLException e){

                    e.printStackTrace();
                }

            }

        }

    }

}
