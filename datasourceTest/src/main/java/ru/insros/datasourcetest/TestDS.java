package ru.insros.datasourcetest;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.sql.DataSource;

/**
 *
 * @author prusakovan
 */
@Stateless
@WebService
public class TestDS {

    private static final Logger LOG = Logger.getLogger(TestDS.class.getName());

    @Resource(lookup = "java:/jboss/datasources/cabinet_ds")
    DataSource ds;

    public TestDS() {
    }

    /**
     * Web service operation
     *
     * @return
     * @throws java.sql.SQLException
     */
    @WebMethod(operationName = "calcTest")
    public String getParentOrder() throws SQLException {
        String sRes = "Ничего не произошло";

        try (Connection con = ds.getConnection(); CallableStatement prepareCall = con.prepareCall("call ADDTEST(?,?,?)")) {
            prepareCall.setInt(1, 15);
            prepareCall.setInt(2, 10);
            prepareCall.registerOutParameter(3, java.sql.Types.INTEGER);
            
            int exec = prepareCall.executeUpdate();
            int aInt = prepareCall.getInt(3);
            
            sRes = String.format("Результат операции: %d. Получено значение: %d", exec, aInt);
            LOG.info(sRes);
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "нет соединения с DataSource", ex);
        }

        return sRes;
    }

}
