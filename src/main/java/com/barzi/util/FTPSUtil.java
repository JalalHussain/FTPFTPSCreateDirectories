package com.barzi.util;

import com.barzi.ext.ExtendedFTPSClient;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FTPSUtil {

    private static final Logger logger = LoggerFactory.getLogger(FTPSUtil.class); //Logger instance for logging.

    private static ExtendedFTPSClient openNewClient(String host, int port, String user, String password ) {

        final ExtendedFTPSClient ftp;
        ftp = new ExtendedFTPSClient(true);
        try {

            int reply;
            if ( port > 0 ) {
                ftp.connect( host, port );
            } else {
                ftp.connect( host);
            }
            ftp.login( user, password );

            ftp.execPBSZ(0);
            ftp.execPROT("P");
            ftp.type(FTP.BINARY_FILE_TYPE);

            ftp.printWorkingDirectory();
            ftp.setFileType( FTP.BINARY_FILE_TYPE );

            ftp.enterLocalPassiveMode();

        } catch ( IOException e ) {
            if ( ftp.isConnected() ) {
                try {
                    ftp.disconnect();
                } catch ( IOException ignore ) {}
            }
            // Log.error( "Could not connect to server." );
            e.printStackTrace();
        }
        return ftp;
    }

    /**
     *
     * @param client
     * @param directories
     * @throws IOException
     */
    private static void createDirectories(ExtendedFTPSClient client, String directories ) throws IOException {
        boolean dirExists = true;
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
