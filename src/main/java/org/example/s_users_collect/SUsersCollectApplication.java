package org.example.s_users_collect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.sql.SQLException;

@SpringBootApplication
public class SUsersCollectApplication {

    final static Logger logger = LoggerFactory.getLogger(SUsersCollectApplication.class);
    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        SpringApplication.run(SUsersCollectApplication.class, args);

        logger.info("COLLECTOR STARTED");
        LogWriter.initConnection();

        try {
            ZabbixAPI.updateAuthToken();
        }catch (IOException e){
            logger.error("AUTH TOKEN NOT UPDATED: "+e.toString());
            return;
        }

        try{
            String usersCount  = ZabbixAPI.getUsersCount();
            String usersUniqueCount  = ZabbixAPI.getUniqueUsersFromDay();
            logger.info("CURRENT COUNT OF USERS: "+ usersCount);
            logger.info("CURRENT UNIQUE DAY COUNT OF USERS: "+ usersUniqueCount);
            LogWriter.writeNewUsersCount(usersCount);
            LogWriter.addNewDaily(usersUniqueCount);
            String weekUsers = ZabbixAPI.getUniqueUsersFromWeek();
            logger.info("CURRENT UNIQUE WEEK COUNT OF USERS " + weekUsers);
            LogWriter.addNewWeek(weekUsers);
            String[] month = ZabbixAPI.getUniqueUsersFromMonth().split("!");
            logger.info("CURRENT MONTH:"+month[1]+" USERS COUNT: "+month[0]);
            LogWriter.addNewMonth(month[1],month[0]);
            String yearUsers = ZabbixAPI.getUniqueUsersFromYear();
            logger.info("CURRENT YEAR COUNT USERS: "+yearUsers);
            LogWriter.addNewYear(yearUsers);
        }catch (IOException e){
            logger.error("ERROR: "+e.toString());
        }

    }

}
