package com.barzi.util;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class FTPUtil {

    private static final Logger logger = LoggerFactory.getLogger(FTPUtil.class); //Logger instance for logging.
    private static FTPClient openNewClient(String host, int port, String user, String password ) {
        final FTPClient ftpClient;
        ftpClient = new FTPClient();
        try {
            if ( port > 0 ) {
                ftpClient.connect( host, port );
            } else {
                ftpClient.connect( host);
            }
            ftpClient.login( user, password );


            ftpClient.type(FTP.BINARY_FILE_TYPE);

            ftpClient.printWorkingDirectory();
            ftpClient.setFileType( FTP.BINARY_FILE_TYPE );

            ftpClient.enterLocalPassiveMode();

        } catch ( IOException e ) {
            if ( ftpClient.isConnected() ) {
                try {
                    ftpClient.disconnect();
                } catch ( IOException ignore ) {}
            }
            logger.info("Could not connect to server.",e);
        }
        return ftpClient;
    }

    /**
     *
     * @param client
     * @param directories
     * @throws IOException
     */
    private static void createDirectories( FTPClient client, String directories ) throws IOException {
        boolean dirExists = true;
        //tokenize the string and attempt to change into each directory level.  If you cannot, then start creating.
        String[] dirs = directories.split("/");
        for (String dir : dirs ) {
            if (!dir.isEmpty() ) {
                if (dirExists) {
                    dirExists = client.changeWorkingDirectory(dir);
                }
                if (!dirExists) {
                    if (!client.makeDirectory(dir)) {
                        throw new IOException("Unable to create remote directory '" + dir + "'.  error='" + client.getReplyString()+"'");
                    }
                    if (!client.changeWorkingDirectory(dir)) {
                        throw new IOException("Unable to change into newly created remote directory '" + dir + "'.  error='" + client.getReplyString()+"'");
                    }
                }
            }
        }
    }

}
