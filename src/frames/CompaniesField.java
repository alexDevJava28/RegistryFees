package frames;

import javax.swing.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;


/**
 * Created by khodackovskiy on 17.12.2015.
 */
public class CompaniesField extends JComboBox{

    private JLabel labelCompanyName;
    private Connection conn;
    private Statement sta;
    AutoCompletion autoCompletion;
    private ResourceBundle resString;

    public CompaniesField(Connection conn, ResourceBundle resString) {

        this.conn = conn;
        this.resString = resString;

        fillComboBox();
        setEditable(true);
        setSelectedItem(null);

        autoCompletion = new AutoCompletion(this);

    }

    public JLabel getLabel (){

        labelCompanyName = new JLabel(resString.getString("lblCompanyName"));
        labelCompanyName.setBounds(20, 200, 250, 15);

        return labelCompanyName;

    }

    public void fillComboBox(){

            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>();

        try {

            String select = "SELECT NAME FROM AFP.COMPANIES ORDER BY NAME";

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
