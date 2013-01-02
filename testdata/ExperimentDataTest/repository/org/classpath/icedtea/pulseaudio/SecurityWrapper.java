package org.classpath.icedtea.pulseaudio;

import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * A wrapper around the security sensitive functions
 *
 */
final class SecurityWrapper
{

    static void loadNativeLibrary()
    {

        if (System.getSecurityManager() != null)
        {
            PrivilegedAction<Boolean> action = new PrivilegedAction<Boolean>()
            {
                @Override
                public Boolean run()
                {
                    System.loadLibrary("pulse-java");
                    return true;
                }

            };

            AccessController.doPrivileged(action);

        }
        else
        {
            System.loadLibrary("pulse-java");
        }

    }
}
