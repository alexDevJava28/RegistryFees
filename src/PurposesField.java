
import javax.swing.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by khodackovskiy on 18.12.2015.
 */
public class PurposesField extends JComboBox {

    JLabel labelWhatPayFor;
    private Connection conn;
    Statement sta;
    AutoCompletion autoCompletion;

    public PurposesField(Connection conn) {

        this.conn = conn;

        fillComboBox();
        setEditable(true);
        setSelectedItem(null);

        autoCompletion = new AutoCompletion(this);

    }

    public JLabel getLabel (){

        labelWhatPayFor = new JLabel("Purpose");
        labelWhatPayFor.setBounds(460, 200, 420, 15);

        return labelWhatPayFor;

    }

    public void fillComboBox(){

        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>();

        try {

            String select = "SELECT TEXT FROM Purposes ORDER BY TEXT";

            sta = conn.createStatement();

            try (ResultSet result = sta.executeQuery(select))
            {
                while (result.next()){

                    model.addElement(result.getString(1));

                }
            }

            this.setModel(model);

        }catch (SQLException e){

            JOptionPane.showMessageDialog(null, "Get What pay for Error in What pay for list");
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
