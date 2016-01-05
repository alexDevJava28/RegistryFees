import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by khodackovskiy on 10.12.2015.
 */
public interface SQLCommands {

    void insert ();

    void delete ();

}
