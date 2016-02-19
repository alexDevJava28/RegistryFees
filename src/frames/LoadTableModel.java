package frames;

import javax.swing.table.DefaultTableModel;
import java.util.ResourceBundle;

/**
 * Created by Пользователь on 27.12.2015.
 */
public class LoadTableModel extends DefaultTableModel {


    public LoadTableModel (ResourceBundle resString){

        super(new String[]{resString.getString("tableID"),
                            resString.getString("tableDate"),
                            resString.getString("tableCompanyName"),
                            resString.getString("tableSum"),
                            resString.getString("tablePaymentDetails"),
                            resString.getString("tableStatus")}, 0);

    }

    @Override
    public boolean isCellEditable(int row, int column) {

        return false;

    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {

        Class clazz = String.class;

        switch (columnIndex){

            case 0:
                clazz = Integer.class;
                break;
            case 3:
                clazz = Double.class;
                break;
            case 5:
                clazz = Boolean.class;
                break;

        }
        return clazz;
    }
}
