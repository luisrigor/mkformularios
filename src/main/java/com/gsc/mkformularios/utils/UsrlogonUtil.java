package com.gsc.mkformularios.utils;

import com.gsc.mkformularios.config.ApplicationConfiguration;
import com.sc.commons.dbconnection.ServerJDBCConnection;
import com.sc.commons.exceptions.SCErrorException;
import com.sc.commons.utils.DataBaseTasks;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@Log4j
public class UsrlogonUtil {

    public String getMailsForProfile(int idProfile, String oidDealerParent, String oidNet) throws SCErrorException {

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        StringBuilder SQL = new StringBuilder("");
        StringBuilder strTo = new StringBuilder();
        try {
            conn = ServerJDBCConnection.getInstance().getConnection(ApplicationConfiguration.DATASOURCE_USRLOGON);
            String viewName = ApplicationConfiguration.getUsrlogonViewName(oidNet);
            SQL.append("SELECT DISTINCT EMAIL_UTILIZADOR FROM " + viewName + " WHERE ID_PERFIL = ? AND OID_DEALER_PARENT = ? AND IS_TO_SEND_ALERT = 1");

            pstmt = conn.prepareStatement(SQL.toString());
            int pos=1;
            pstmt.setInt(pos++, idProfile);
            pstmt.setString(pos++, oidDealerParent);
            log.debug("SQL:" + SQL.toString() + " idProfile:" + idProfile + " oidDealerParent:" + oidDealerParent);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                strTo.append(rs.getString("EMAIL_UTILIZADOR") + ",");
            }
            if (strTo.length()>0)
                strTo.deleteCharAt(strTo.length()-1);

        } catch (SQLException sqlEx) {
            throw new SCErrorException("USRLOGONUTIL.GETMAILSFORPROFILE", "SQL:" + SQL.toString() + " idProfile:" + idProfile + " oidDealerParent:" + oidDealerParent, sqlEx);
        } finally {
            DataBaseTasks.close(rs, pstmt, conn);
        }
        return strTo.toString();
    }
}
