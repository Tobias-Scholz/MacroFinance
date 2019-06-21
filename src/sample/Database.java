package sample;

import java.sql.*;

public class Database
{
    private static Connection con;
    private static boolean hasData = false;

    public static ResultSet query(String query)
    {
        try
        {
            Statement statement = con.createStatement();
            return statement.executeQuery(query);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static void getConnection()
    {
        try
        {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:Database.db");
        }
        catch (ClassNotFoundException | SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static Connection getCon()
    {
        return con;
    }
}
