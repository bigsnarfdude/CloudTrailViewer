/*    
CloudTrail Viewer, is a Java desktop application for reading AWS CloudTrail logs
files.

Copyright (C) 2015  Mark P. Haskins

This program is free software: you can redistribute it and/or modify it under the
terms of the GNU General Public License as published by the Free Software Foundation,
either version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,but WITHOUT ANY 
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.haskins.cloudtrailviewer.dao;

import com.haskins.cloudtrailviewer.model.AwsAccount;
import com.haskins.cloudtrailviewer.utils.ResultSetRow;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that acts as a DAO for the Credentails table
 * 
 * @author mark.haskins
 */
public class AccountDao {
    
    /**
     * Returns an AwsAccount object for the given account number
     * @param acctNum
     * @return Will return NULL if not found.
     */
    public static AwsAccount getAccountByAcctNum(String acctNum) {
        
        AwsAccount account = null;
        
        String query = "SELECT * FROM aws_credentials WHERE aws_acct LIKE '" + acctNum + "'";
        List<ResultSetRow> rows = DbManager.getInstance().executeCursorStatement(query);
        if (rows.size() == 1) {
            
            ResultSetRow row = rows.get(0);
            account = getAccountFromResultSetRow(row);
        }
        
        return account;
    }
    
    /**
     * Returns an AwsAccount object for the given account name
     * @param name
     * @return Will return NULL if not found.
     */
    public static AwsAccount getAccountByName(String name) {
        
        AwsAccount account = null;
        
        String query = "SELECT * FROM aws_credentials WHERE aws_name LIKE '" + name + "'";
        List<ResultSetRow> rows = DbManager.getInstance().executeCursorStatement(query);
        if (rows.size() == 1) {
            
            ResultSetRow row = rows.get(0);
            account = getAccountFromResultSetRow(row);
        }
        
        return account;
    }
    
    /**
     * Delete the account with the name
     * @param name name of account to delete
     */
    public static void deleteAccountByName(String name) {
        
        String query = "SDELETE FROM aws_credentials WHERE aws_name = '" + name + "'";
        DbManager.getInstance().doExecute(query); 
    }
    
    /**
     * returns all Account that have a bucket associated with them
     * @return 
     */
    public static List<AwsAccount> getAllAccountsWithBucket() {

        return executeQuery("SELECT * FROM aws_credentials WHERE LENGTH(aws_bucket) > 1");
    }
    
    /**
     * returns all accounts if they are active.
     * @param onlyActive
     * @return 
     */
    public static List<AwsAccount> getAllAccounts(boolean onlyActive) {
        
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM aws_credentials");
        
        if (onlyActive) {
            query.append(" WHERE active = 1");
        }
        
        return executeQuery(query.toString());
    }
    
    ////////////////////////////////////////////////////////////////////////////
    ///// private methods
    ////////////////////////////////////////////////////////////////////////////
    private static List<AwsAccount> executeQuery(String query) {
        
        List<AwsAccount> accounts = new ArrayList<>();
        
        List<ResultSetRow> rows = DbManager.getInstance().executeCursorStatement(query);
        for (ResultSetRow row : rows) {
            accounts.add(getAccountFromResultSetRow(row));
        }
        
        return accounts;
    }
    
    private static AwsAccount getAccountFromResultSetRow(ResultSetRow row) {

        return new AwsAccount(
                (Integer) row.get("id"),
                (String) row.get("aws_name"),
                (String) row.get("aws_acct"),
                (String) row.get("aws_bucket"),
                (String) row.get("aws_key"),
                (String) row.get("aws_secret"),
                (String) row.get("aws_prefix")
        );
    }
    
}
