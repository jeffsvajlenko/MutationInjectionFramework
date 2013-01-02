
















 


#include <config.h>

#ifdef HAVE_STDIO_H
#include <stdio.h>
#endif

#ifdef HAVE_STDLIB_H
#include <stdlib.h>
#endif

#ifdef HAVE_ERRNO_H
#include <errno.h>
#endif

#ifdef HAVE_SIGNAL_H
#include <signal.h>
#endif

#ifdef HAVE_UNISTD_H
#include <unistd.h>
#endif

#ifdef HAVE_SYS_TYPES_H
#include <sys/types.h>
#endif

#ifdef HAVE_SYS_STAT_H
#include <sys/stat.h>
#endif

#ifdef HAVE_FCNTL_H
#include <fcntl.h>
#endif

#ifdef HAVE_STRING_H
#include <string.h>
#endif

#include "monitor.h"











 




 

 
 
 

 




 
void  daemonize()
{

    pid_t pid;

    

 
    umask(0);

    

 
    if((pid= fork ()) < 0)
    {

        log("Cannot fork of a new process\n") ;
        exit (1);

    }
    else if(pid != 0)
    {

        _exit(0);

    }

    setsid(   );

    if((pid= fork ()) < 0)
    {

        log("Cannot fork of a new process\n");
        exit (1);

    }
    else if(pid != 0  )
    {

        _exit(0);

    }


    


 
    if(chdir("/") < 0)
    {

        log("Cannot chdir to / -- %s\n", STRERROR);
        exit(1);

    }

	 
 
 
 

    


 
    redirect_stdfd();

}







 





 
int kill_daemon(int sig)
{

    pid_t pid;

    if ( (pid= exist_daemon()) > 0 )
    {

        if ( kill(pid, sig) < 0 )
        {

            log("%s: Cannot send signal to daemon process -- %s\n",prog, STRERROR);
            return FALSE;

        }

    }
    else
    {

        log("%s: No daemon process found\n", prog);
        return TRUE;

    }

    if(sig == SIGTERM)
    {

        fprintf(stdout, "%s daemon with pid [%d] killed\n", prog, (int)pid) ;
        fflush(stdout);

    }

    return TRUE;

}





 
int exist_daemon()
{

    pid_t pid;

    errno= 0;

    if( (pid= get_pid(Run.pidfile)) )
        if( (getpgid(pid)) > -1 || (errno == EPERM) )
            return( (int)pid );

    return (FALSE);

}

