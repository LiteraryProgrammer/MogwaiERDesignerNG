/**
 * Mogwai ERDesigner. Copyright (C) 2002 The Mogwai Project.
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 */
package de.erdesignerng.dialect.mysql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import de.erdesignerng.dialect.JDBCReverseEngineeringStrategy;
import de.erdesignerng.dialect.ReverseEngineeringNotifier;
import de.erdesignerng.dialect.ReverseEngineeringOptions;
import de.erdesignerng.dialect.TableEntry;
import de.erdesignerng.model.Attribute;
import de.erdesignerng.model.Model;
import de.erdesignerng.model.Table;

/**
 * @author $Author: mirkosertic $
 * @version $Date: 2008-03-02 12:03:45 $
 */
public class MySQLReverseEngineeringStrategy extends JDBCReverseEngineeringStrategy<MySQLDialect> {

    public MySQLReverseEngineeringStrategy(MySQLDialect aDialect) {
        super(aDialect);
    }

    @Override
    protected String convertIndexNameFor(Table aTable, String aIndexName) {
        if ("PRIMARY".equals(aIndexName)) {
            return "PK_" + aTable.getName();
        }
        return super.convertIndexNameFor(aTable, aIndexName);
    }

    @Override
    protected void reverseEngineerAttribute(Model aModel, Attribute aAttribute, ReverseEngineeringOptions aOptions,
            ReverseEngineeringNotifier aNotifier, TableEntry aEntry, Connection aConnection)
            throws SQLException {
        Statement theStatement = aConnection.createStatement();
        ResultSet theResult = theStatement.executeQuery("DESCRIBE " + aEntry.getTableName());
        while (theResult.next()) {
            String theColumnName = theResult.getString("Field");
            if (aAttribute.getName().equals(theColumnName)) {
                String theExtra = theResult.getString("Extra");
                if ("AUTO_INCREMENT".equalsIgnoreCase(theExtra)) {
                    aAttribute.setExtra("AUTO_INCREMENT PRIMARY KEY");
                }
            }
        }
        theResult.close();
        theStatement.close();
    }

}