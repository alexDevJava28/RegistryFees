package data;

import oracle.jdbc.OracleTypes;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by khodackovskiy on 10.12.2015.
 */
public class Companies implements SQLCommands {

    private Connection conn;
    private Object companyName;
    private ResultSet rs;

    public Companies(Connection conn, Object companyName){

        this.conn = conn;
        this.companyName = companyName;
    }

    @Override
    public void insert() {

        String sql = "{call INSERT_NAME_INTO_COMPANIES(?)}";

        try (CallableStatement cs = conn.prepareCall(sql)){

            cs.setString("INIC_NAME", String.valueOf(companyName));

            cs.executeUpdate();

        }catch (SQLException e){

            e.printStackTrace();

        }
    }

    @Override
    public void delete() {

        String sql = "{call DELETE_COMPANY_FROM_COMPANIES(?)}";

        try (CallableStatement cs = conn.prepareCall(sql)){

            cs.setString("DCFC_NAME", String.valueOf(companyName));

            cs.executeUpdate();

        }catch (SQLException e){

            e.printStackTrace();
        }
    }

    public Long getCompanyNameId (){

        Long id = null;
        String sql = "{call SELECT_COMPANY_FROM_COMPANIES(?, ?)}";

        try (CallableStatement cs = conn.prepareCall(sql)){

            cs.setString("SCFC_NAME", String.valueOf(companyName));
            cs.registerOutParameter("SCFC_COMPANY", OracleTypes.NUMBER);

            cs.executeUpdate();

            id = cs.getLong("SCFC_COMPANY");

        }catch (SQLException e){

            e.printStackTrace();
        }

        return id;
    }

    public void update(long company, Object newName){

        String sql = "{call UPDATE_NAME_IN_COMPANIES(?, ?)}";

        try (CallableStatement cs = conn.prepareCall(sql)){

            cs.setLong("UNIC_COMPANY", company);
            cs.setString("UNIC_NAME", String.valueOf(newName));

            cs.executeUpdate();

        }catch (SQLException e){

            e.printStackTrace();

        }

    }

}
