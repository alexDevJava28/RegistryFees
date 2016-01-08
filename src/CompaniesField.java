
import javax.swing.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * Created by khodackovskiy on 17.12.2015.
 */
public class CompaniesField extends JComboBox{

    JLabel labelCompanyName;
    Connection conn;
    Statement sta;
    AutoCompletion autoCompletion;

    public CompaniesField(Connection conn) {

        this.conn = conn;

        fillComboBox();
        setEditable(true);
        setSelectedItem(null);

        autoCompletion = new AutoCompletion(this);

    }

    public JLabel getLabel (){

        labelCompanyName = new JLabel("Company");
        labelCompanyName.setBounds(20, 200, 250, 15);

        return labelCompanyName;

    }

    public void fillComboBox(){

            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>();

        try {

            String select = "SELECT name FROM Companies ORDER BY name";

            sta = conn.createStatement();

            try (ResultSet result = sta.executeQuery(select))
            {
                while (result.next()){

                    model.addElement(result.getString(1));
                }

            }

            this.setModel(model);

        }catch (SQLException e){

            JOptionPane.showMessageDialog(null, "Get Company name Error in Company name list");
            e.printStackTrace();
        }finally {

            try{

                if (sta != null){

                    sta.close();
                }
            }catch (SQLException e){

                e.printStackTrace();
            }
        }
    }

}
