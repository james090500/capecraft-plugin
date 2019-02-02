package net.capecraft.protect.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.plugin.Plugin;

import net.capecraft.protect.ArmorStandProtect;

public class SQLUtils {
	   
	Plugin instance;
	
	public SQLUtils(Plugin instance) {
		this.instance = instance;
	}

	public static void sqlInit(String dbName) {   
	   Connection connection = sqlOpen(dbName);
	   PreparedStatement statement = null;
	   try {
		   statement = connection.prepareStatement("create table if not exists armorstand (stand varchar(255), player varchar(255));  ");
		   sqlUpdate(connection, statement);       
	   } catch (Exception oops) {
		   ArmorStandProtect.logger.severe("[" + ArmorStandProtect.pdfFile.getName() + "] Exception: '" + oops.getMessage() + "'. ");       
		   oops.printStackTrace(System.err);           
	   } finally {
		   sqlClose(connection);
	   }
   }
	   
   public static Connection sqlOpen(String dbNname) {
	   Connection connection = null;
       try {
    	   Class.forName("org.sqlite.JDBC");
    	   connection = DriverManager.getConnection("jdbc:sqlite:" + ArmorStandProtect.dbName);
       } catch (ClassNotFoundException oops) {
    	   ArmorStandProtect.logger.severe("[" + ArmorStandProtect.pdfFile.getName() + "] You need the SQLite JBDC library.  DuckDuckGo it. ");
           oops.printStackTrace(System.err);
       } catch(SQLException oops) {
    	   ArmorStandProtect.logger.severe("[" + ArmorStandProtect.pdfFile.getName() + "] SQL Exception: '" + oops.getErrorCode() + "'. ");
    	   oops.printStackTrace();  
       } catch (Exception oops) {
    	   ArmorStandProtect.logger.severe("[" + ArmorStandProtect.pdfFile.getName() + "] Exception: '" + oops.getMessage() + "'. ");       
           oops.printStackTrace(System.err);           
       }
       return (connection);
   }
	         
   public static ResultSet sqlQuery(Connection connection, PreparedStatement statement) {
	   ResultSet data = null;
       try {
    	   data = statement.executeQuery();
       } catch(SQLException oops) {
    	   ArmorStandProtect.logger.severe("[" + ArmorStandProtect.pdfFile.getName() + "] SQL Exception: '" + oops.getErrorCode() + "'. ");
    	   oops.printStackTrace();  
       } catch (Exception oops) {
    	   ArmorStandProtect.logger.severe("[" + ArmorStandProtect.pdfFile.getName() + "] Exception: '" + oops.getMessage() + "'. ");       
           oops.printStackTrace(System.err);
       }   
       return (data);
   }
	   
   public static void sqlUpdate(Connection connection, PreparedStatement statement) {
	   try {
           statement.executeUpdate();
       } catch(SQLException oops) {
    	   ArmorStandProtect.logger.severe("[" + ArmorStandProtect.pdfFile.getName() + "] SQL Exception: '" + oops.getErrorCode() + "'. ");
    	   oops.printStackTrace();  
       } catch (Exception oops) {
    	   ArmorStandProtect.logger.severe("[" + ArmorStandProtect.pdfFile.getName() + "] Exception: '" + oops.getMessage() + "'. ");       
           oops.printStackTrace(System.err);
       }   
    }
      
   public static void sqlClose(Connection connection) {
	   if (connection != null) {
	       try {
	           connection.close();
           } catch(SQLException oops) {
        	   ArmorStandProtect.logger.severe("[" + ArmorStandProtect.pdfFile.getName() + "] SQL Exception: '" + oops.getErrorCode() + "'. ");
	           oops.printStackTrace();  
           } catch (Exception oops) {
        	   ArmorStandProtect.logger.severe("[" + ArmorStandProtect.pdfFile.getName() + "] Exception: '" + oops.getMessage() + "'. ");       
        	   oops.printStackTrace(System.err);
           }  
       }
   }
}
