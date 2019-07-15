package com.barzi.ext;

import org.apache.commons.net.ftp.FTPSClient;

import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSessionContext;
import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Locale;

/**
 * <pre>
 *  The FTPS client of apache have some issues in socket opening and data transfer hence it
 *  can be extended and can be used for proper functioning.
 * </pre>
 *
 * @author Barzi
 */
public class ExtendedFTPSClient extends FTPSClient {

    /**
     * Default Constructor.
     */
    public ExtendedFTPSClient() {
        super("TLS", false);
    }

    /**
     * <pre>
     *     Parameterize constructor.
     * </pre>
     *
     * @param isImplicit
     */
    public ExtendedFTPSClient(boolean isImplicit) {
        super("TLS", isImplicit);
    }

    /**
     * <pre>
     *     This will prepare the data socket.
     * </pre>
     *
     * @param socket The socket.
     * @throws IOException Throws IOException.
     */
    protected void _prepareDataSocket_(final Socket socket) throws IOException {
        if (socket instanceof SSLSocket) {
            // Control socket is SSL
            final SSLSession session = ((SSLSocket) _socket_).getSession();
            if (session.isValid()) {
                final SSLSessionContext context = session.getSessionContext();
                try {
                    final Field sessionHostPortCache = context.getClass().getDeclaredField("sessionHostPortCache");
                    sessionHostPortCache.setAccessible(true);
                    final Object cache = sessionHostPortCache.get(context);
                    final Method method = cache.getClass().getDeclaredMethod("put", Object.class, Object.class);
                    method.setAccessible(true);
                    method.invoke(cache, String
                            .format("%s:%s", socket.getInetAddress().getHostName(), String.valueOf(socket.getPort()))
                            .toLowerCase(Locale.ROOT), session);
                    method.invoke(cache, String
                            .format("%s:%s", socket.getInetAddress().getHostAddress(), String.valueOf(socket.getPort()))
                            .toLowerCase(Locale.ROOT), session);
                } catch (NoSuchFieldException e) {
                    throw new IOException(e);
                } catch (Exception e) {
                    throw new IOException(e);
                }
            } else {
                throw new IOException("Invalid SSL Session");
            }
        }
    }
}
