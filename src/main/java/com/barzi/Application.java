package com.barzi;

import com.barzi.config.ConfigTag;
import com.barzi.util.FTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Consumer;

public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class); //Logger instance for logging.
    public static void main(String args[]){
        logger.info("Running application FTPFTPSCreateDirectories");
        String host;
        int port;
        String user;
        String password;
        String dirs;
        try {
            logger.info("Setting configured credentials");
            host= ConfigTag.getProperty("server.host");
            port=Integer.parseInt(ConfigTag.getProperty("server.port"));
            user=ConfigTag.getProperty("server.user");
            password=ConfigTag.getProperty("server.passwor");
            dirs=ConfigTag.getProperty("server.directories");
            logger.info("Credentials are " +
                    "[ Host : "+host+" Port :"+port+" User : "+user+" Password : "+password+" Directories : "+dirs+"]");
        }catch (Exception e){
        logger.error("Exception occured while setting credentials ",e);
        }

        logger.info("Directory creation task started...");
        logger.info("Directory creation task finished.");
        logger.info("FTPFTPSCreateDirectories application finished");
    }
}
