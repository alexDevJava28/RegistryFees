import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

/**
 * Created by khodackovskiy on 10.12.2015.
 */
public class Purposes implements SQLCommands{

    Connection conn;
    PreparedStatement psta;
    Statement sta;
    String whatPayFor;

    Purposes(Connection conn, Object whatPayFor){

        this.conn = conn;
        this.whatPayFor = whatPayFor.toString();
    }

    @Override
    public void insert() {

        try {

            String insert = "INSERT INTO Purposes (PURPOSE, TEXT) VALUES (PURPOSES_SEQ.nextval, ?)";

            psta = conn.prepareStatement(insert);
            psta.setString(1, whatPayFor);

            psta.executeUpdate();

        }catch (SQLException e){

            JOptionPane.showMessageDialog(null, "Insert Error in What pay for list");

        }

    }

    @Override
    public void delete() {

        try {

            String delete = "DELETE FROM Purposes WHERE TEXT = ?";

            psta = conn.prepareStatement(delete);
            psta.setString(1, whatPayFor);

            psta.executeUpdate();

        }catch (SQLException e){

            JOptionPane.showMessageDialog(null, "Delete Error in Purposes");
        }
    }

    public long getWhatPayForId (){

        int id = 0;

        try {

            String select = "SELECT PURPOSE FROM Purposes WHERE TEXT = '" + whatPayFor + "'";
            sta = conn.createStatement();

            try (ResultSet result = sta.executeQuery(select))
            {
                if (result.next()){

                    id = result.getInt(1);
                }
            }

        }catch (SQLException e){

            JOptionPane.showMessageDialog(null, "Get Id Error in Purposes");
        }

        return id;
    }

    public void update (long purpose, Object newText){

        try {

            String update = "UPDATE Purposes SET TEXT = '" + newText + "' WHERE PURPOSE = " + purpose;

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
