package com.barzi.util;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * <pre>
 *      A Utility class handling FTP related operations.
 * </pre>
 *
 * @author Barzi
 */
public class FTPUtil {

    private static final Logger logger = LoggerFactory.getLogger(FTPUtil.class); //Logger instance for logging.

    /**
     * <pre>
     *     This method will create all the folders/directories in the path if non-existent.
     * </pre>
     *
     * @param ftpClient   The ftpClient object.
     * @param directories The folder/directory hierarchy.
     * @throws IOException Throws IOException.
     */
    private static void createDirectories(FTPClient ftpClient, String directories) throws IOException {
        boolean dirExists = true;
        //tokenize the string and attempt to change into each directory level.  If you cannot, then start creating.
        String[] dirs = directories.split("/");
        for (String dir : dirs) {
            if (!dir.isEmpty()) {
                if (dirExists) {
                    dirExists = ftpClient.changeWorkingDirectory(dir);
                }
                if (!dirExists) {
                    if (!ftpClient.makeDirectory(dir)) {
                        throw new IOException("Unable to create remote directory '" + dir + "'.  error='" + ftpClient.getReplyString() + "'");
                    }
                    if (!ftpClient.changeWorkingDirectory(dir)) {
                        throw new IOException("Unable to change into newly created remote directory '" + dir + "'.  error='" + ftpClient.getReplyString() + "'");
                    }
                }
            }
        }
    }

    /**
     * <pre>
     *     This method will crate an FTP Client.
     * </pre>
     *
     * @param host     The IP/Address.
     * @param port     The port.
     * @param user     The user name/ID
     * @param password The Password.
     * @return FTPClient Returns the FTPClient object.
     */
    private static FTPClient openNewClient(String host, int port, String user, String password) {
        final FTPClient ftpClient;
        ftpClient = new FTPClient();
        try {
            if (port > 0) {
                ftpClient.connect(host, port);
            } else {
                ftpClient.connect(host);
            }
            ftpClient.login(user, password);


            ftpClient.type(FTP.BINARY_FILE_TYPE);

            ftpClient.printWorkingDirectory();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            ftpClient.enterLocalPassiveMode();

        } catch (IOException e) {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException ignore) {
                }
            }
            logger.info("Could not connect to server.", e);
        }
        return ftpClient;
    }
}
