package org.example.s_users_collect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.Date;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LogWriter {

    final static Logger logger = LoggerFactory.getLogger(LogWriter.class);
    final public static String baseDriver = "org.postgresql.Driver";
    public static Connection connLog;

    public static String urlLog = "jdbc:postgresql://basis-supp.database-test-pg.cloud.vimpelcom.ru:5432/poweruser";

    public static void initConnection() throws SQLException, ClassNotFoundException {
        try {
            logger.info("TRY TO INIT AND CONNECT LOG DATABASE");
            Class.forName(baseDriver);
            connLog = DriverManager.getConnection(urlLog, "mivyamoiseev", "xid123MTPP_");
            connLog.setAutoCommit(false);
            logger.info("LOG DATABASE...OK");
        } catch (SQLException e) {
            logger.error("FAILED TO CONNECT LOG DATABASE: "+e.toString());
        }
    }

    public static void insertLog(String STATUS,
                                 //String APP_NAME,
                                 String PROCESS,
                                 String OPERATION,
                                 String OPTIONS,
                                 //String PARENT_ID,
                                 String LEVEL,
                                 //String USER,
                                  String TYPE,
                                 String ERROR_MESSAGE
                                 //String CLASS,
                                 //String LINE
    ) throws SQLException, ClassNotFoundException {
        if(connLog==null)
            initConnection();
        String sqlQuery = "INSERT INTO PUBLIC.\"LOGS\" \n" +
                "(STATUS,APP_NAME,PROCESS,OPERATION,\"OPTIONS\",PARENT_ID,\"LEVEL\",\"USER\",\"TYPE\",\"ERROR_MESSAGE\",\"CLASS\",LINE)\n" +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
        try{
            PreparedStatement preparedStatement = connLog.prepareStatement(sqlQuery);
            preparedStatement.setString(1, STATUS);
            preparedStatement.setString(2, "USERS_COLLECT");
            preparedStatement.setString(3, PROCESS);
            preparedStatement.setString(4, OPERATION);
            preparedStatement.setString(5, OPTIONS);
            preparedStatement.setString(6, "");
            preparedStatement.setString(7, LEVEL);
            preparedStatement.setString(8, TYPE);
            preparedStatement.setString(9, "");
            preparedStatement.setString(10, ERROR_MESSAGE);
            preparedStatement.setString(11, "");
            preparedStatement.setString(12, "");
            int addedRows = preparedStatement.executeUpdate();
            connLog.commit();
            //System.out.println(addedRows +" rows has been add");
        }catch (SQLException e){
            System.out.println("ERROR "+ e.toString());
        }
    }


    public static void writeNewUsersCount(String usersCount) throws SQLException, ClassNotFoundException {

        if(connLog==null)
            initConnection();

        String insertSql="INSERT\tINTO\n" +
                "\tusers_history (count)\n" +
                "VALUES('"+usersCount+"');";

        Statement stmt = connLog.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        try{
            stmt.executeUpdate(insertSql);
            logger.info("NEW RECORD OF USERS COUNT HAS BEEN ADDED");
        }catch (SQLException e){
            logger.error("FAILED TO ADD NEW RECORD: "+e.toString());
        }



    }


    public static void addNewDaily( String usersCount) throws SQLException, ClassNotFoundException {

        if(connLog==null)
            initConnection();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String curDate = LocalDateTime.now().format(dateTimeFormatter).toString();
        //logger.info(curDate);
        String insertSql="INSERT INTO daily_users_history(\"date\",count)\n" +
                "VALUES('"+curDate+"','"+usersCount+"')\n" +
                "ON CONFLICT (\"date\")\n" +
                "DO UPDATE SET \"date\" = '"+curDate+"', count  = '"+usersCount+"'";


        Statement stmt = connLog.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        try{
            stmt.executeUpdate(insertSql);
            logger.info("NEW DAY RECORD OF UNIQUE USERS COUNT HAS BEEN ADDED");
            connLog.commit();
        }catch (SQLException e){
            logger.error("FAILED DAY TO ADD NEW UNIQUE RECORD: "+e.toString());
        }
    }


    public static void addNewWeek( String usersCount) throws SQLException, ClassNotFoundException {

        if(connLog==null)
            initConnection();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        //String curDate = LocalDateTime.now().format(dateTimeFormatter).toString();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("w-yyyy");
        Date date = new Date();
        String curDate = simpleDateFormat.format(date).replace("-","H-");

        //logger.info(curDate);
        String insertSql="INSERT INTO week_users_history(\"date\",count)\n" +
                "VALUES('"+curDate+"','"+usersCount+"')\n" +
                "ON CONFLICT (\"date\")\n" +
                "DO UPDATE SET \"date\" = '"+curDate+"', count  = '"+usersCount+"'";


        Statement stmt = connLog.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        try{
            stmt.executeUpdate(insertSql);
            logger.info("NEW WEEK RECORD OF UNIQUE USERS  COUNT HAS BEEN ADDED");
            connLog.commit();
        }catch (SQLException e){
            logger.error("FAILED WEEK TO ADD NEW UNIQUE WEEK RECORD: "+e.toString());
        }
    }


    public static void addNewMonth(String monthName, String usersCount) throws SQLException, ClassNotFoundException {

        if(connLog==null)
            initConnection();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM");
        String curDate = LocalDateTime.now().format(dateTimeFormatter).toString();
        Month jan = Month.of(Integer.parseInt(curDate));
        Locale loc = Locale.forLanguageTag("ru");
        //logger.info(jan.getDisplayName(TextStyle.FULL_STANDALONE, loc)); // Вернёт Январь
        //logger.info(curDate);
        String insertSql="INSERT INTO month_users_history(\"month_name\",count)\n" +
                "VALUES('"+monthName+"','"+usersCount+"')\n" +
                "ON CONFLICT (\"month_name\")\n" +
                "DO UPDATE SET \"month_name\" = '"+monthName+"', count  = '"+usersCount+"'";


        Statement stmt = connLog.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        try{
            stmt.executeUpdate(insertSql);
            logger.info("NEW MONTH RECORD OF UNIQUE USERS  COUNT HAS BEEN ADDED");
            connLog.commit();
        }catch (SQLException e){
            logger.error("FAILED MONTH TO ADD NEW UNIQUE MONTH RECORD: "+e.toString());
        }
    }


    public static void addNewYear( String usersCount) throws SQLException, ClassNotFoundException {

        if(connLog==null)
            initConnection();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy");
        String curDate = LocalDateTime.now().format(dateTimeFormatter).toString();
        //logger.info(curDate);
        String insertSql="INSERT INTO year_users_history(\"date\",count)\n" +
                "VALUES('"+curDate+"','"+usersCount+"')\n" +
                "ON CONFLICT (\"date\")\n" +
                "DO UPDATE SET \"date\" = '"+curDate+"', count  = '"+usersCount+"'";


        Statement stmt = connLog.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        try{
            stmt.executeUpdate(insertSql);
            logger.info("NEW RECORD OF UNIQUE USERS YEAR COUNT HAS BEEN ADDED");
            connLog.commit();
        }catch (SQLException e){
            logger.error("FAILED TO ADD NEW YEAR UNIQUE RECORD: "+e.toString());
        }
    }



}
