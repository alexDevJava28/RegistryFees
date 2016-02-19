package frames;

import javax.swing.table.DefaultTableModel;
import java.util.ResourceBundle;

/**
 * Created by Пользователь on 26.12.2015.
 */
public class MyTableModel extends DefaultTableModel {

    public MyTableModel (ResourceBundle resString){

        super(new String[]{resString.getString("tableID"),
                            resString.getString("tableCompanyName"),
                            resString.getString("tableSum"),
                            resString.getString("tablePaymentDetails"),
                            resString.getString("tableStatus")}, 0);

    }

    @Override
    public boolean isCellEditable(int row, int column) {

        return column > 3;

    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {

        Class clazz = String.class;

        switch (columnIndex){

            case 0:
                clazz = Integer.class;
                break;
            case 2:
                clazz = Double.class;
                break;
            case 4:
                clazz = Boolean.class;
                break;

        }
        return clazz;
    }
}
