import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Пользователь on 26.12.2015.
 */
public class MyTableModel extends DefaultTableModel {

    public MyTableModel (){

        super(new String[]{"ID", "COMPANY NAME", "SUM", "PAYMENT DETAILS", "STATUS"}, 0);

    }

    @Override
    public boolean isCellEditable(int row, int column) {

        if (column <=3)
            return false;
        else
            return true;

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
