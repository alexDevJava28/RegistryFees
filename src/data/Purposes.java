package data;

import oracle.jdbc.OracleTypes;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by khodackovskiy on 10.12.2015.
 */
public class Purposes implements SQLCommands {

    private Connection conn;
    private Object whatPayFor;
    private ResultSet rs;

    public Purposes(Connection conn, Object whatPayFor){

        this.conn = conn;
        this.whatPayFor = whatPayFor;
    }

    @Override
    public void insert() {

        String sql = "{call INSERT_TEXT_INTO_PURPOSES(?)}";

        try (CallableStatement cs = conn.prepareCall(sql)){

            cs.setString("ITIP_TEXT", String.valueOf(whatPayFor));

            cs.executeUpdate();

        }catch (SQLException e){

            e.printStackTrace();

        }

    }

    @Override
    public void delete() {

        String sql = "{call DELETE_TEXT_FROM_PURPOSES(?)}";

        try (CallableStatement cs = conn.prepareCall(sql)){

            cs.setString("DTFP_TEXT", String.valueOf(whatPayFor));

            cs.executeUpdate();

        }catch (SQLException e){

            e.printStackTrace();
        }
    }

    public Long getWhatPayForId (){

        Long id = null;
        String sql = "{call SELECT_PURPOSE_FROM_PURPOSES(?, ?)}";

        try (CallableStatement cs = conn.prepareCall(sql)){

            cs.setString("SPFP_TEXT", String.valueOf(whatPayFor));
            cs.registerOutParameter("SPFP_PURPOSE", OracleTypes.NUMBER);

            cs.executeUpdate();

            id = cs.getLong("SPFP_PURPOSE");

        }catch (SQLException e){

            e.printStackTrace();
        }

        return id;
    }

    public void update (long purpose, Object newText){

        String sql = "{call UPDATE_TEXT_IN_PURPOSES(?, ?)}";

        try (CallableStatement cs = conn.prepareCall(sql)){

            cs.setLong("UTIP_PURPOSE", purpose);
            cs.setString("UTIP_TEXT", String.valueOf(newText));

            cs.executeUpdate();

        }catch (SQLException e){

            e.printStackTrace();

        }
    }
}
