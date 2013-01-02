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
#ifdef HAVE_STRING_H
#include <string.h>
#endif
#ifdef HAVE_STRINGS_H
#include <strings.h>
#endif
#ifdef HAVE_SYS_TYPES_H
#include <sys/types.h>
#endif
#ifdef HAVE_SYS_STAT_H
#include <sys/stat.h>
#endif
#ifdef HAVE_UNISTD_H
#include <unistd.h>
#endif
#ifdef HAVE_FCNTL_H
#include <fcntl.h>
#endif
#include "monitor.h"

void init_files () {
    char pidfile [STRLEN];
    char statefile [STRLEN];
    if (Run.pidfile == NULL) {
        if (!getuid ()) {
            snprintf (pidfile, STRLEN, "%s/%s", MYPIDDIR, MYPIDFILE);
        }
        else {
            snprintf (pidfile, STRLEN, "%s/.%s", Run.Env.home, MYPIDFILE);
        }
        Run.pidfile = xstrdup (pidfile);
    }
    if (Run.statefile == NULL) {
        snprintf (statefile, STRLEN, "%s/.%s", Run.Env.home, MYSTATEFILE);
        Run.statefile = xstrdup (statefile);
    }
}

void finalize_files () {
    unlink (Run.pidfile);
    unlink (Run.statefile);
}

time_t get_timestamp (char *object, mode_t type) {
    struct  stat buf;
    ASSERT (object);
    if (!stat (object, &buf)) {
        if (((type == S_IFREG) && S_ISREG (buf.st_mode)) || ((type == S_IFDIR) && S_ISDIR (buf.st_mode)) || ((type == (S_IFREG | S_IFDIR)) && (S_ISREG (buf.st_mode) || S_ISDIR (buf.st_mode)))) {
            return MAX (buf.st_mtime, buf.st_ctime);
        }
        else {
            log ("%s: Invalid object type - %s\n", prog, object);
        }
    }
    return FALSE;
}

char *find_rcfile () {
    char *rcfile = xmalloc (STRLEN);
    snprintf (rcfile, STRLEN, "%s/.%s", Run.Env.home, MONITRC);
    if (exist_file (rcfile)) {
        return (rcfile);
    }
    memset (rcfile, 0, STRLEN);
    snprintf (rcfile, STRLEN, "/etc/%s", MONITRC);
    if (exist_file (rcfile)) {
        return (rcfile);
    }
    if (exist_file (MONITRC)) {
        memset (rcfile, 0, STRLEN);
        snprintf (rcfile, STRLEN, "%s/%s", Run.Env.cwd, MONITRC);
        return (rcfile);
    }
    log ("%s: Cannot find the control file at ~/.%s, /etc/%s or at ./%s \n", prog, MONITRC, MONITRC, MONITRC);
    exit (1);
}

int create_pidfile (char *pidfile) {
    FILE *F = NULL;
    ASSERT (pidfile);
    umask (MYPIDMASK);
    unlink (pidfile);
    if ((F = fopen (pidfile, "w")) == ( FILE  *) NULL) {
        log ("%s: Error opening pidfile '%s' for writing -- %s\n", prog, pidfile, STRERROR);
        return (FALSE);
    }
    fprintf (F, "%d\n", (int) getpid ());
    fclose (F);
    return TRUE;
}

int check_rcfile (char *rcfile) {
    ASSERT (rcfile);
    return check_file_stat (rcfile, "control file", S_IRUSR | S_IWUSR | S_IXUSR);
}

int isreg_file (char *file) {
    struct  stat buf;
    ASSERT (file);
    return (stat (file, &buf) == 0 && S_ISREG (buf.st_mode));
}

int exist_file (char *file) {
    struct  stat buf;
    ASSERT (file);
    return (stat (file, &buf) == 0);
}

int check_file_stat (char *filename, char *description, int permmask) {
    struct  stat buf;
    errno = 0;
    ASSERT (filename);
    ASSERT (description);
    if (lstat (filename, &buf) < 0) {
        log ("%s: Cannot stat the %s '%s' -- %s\n", prog, description, filename, STRERROR);
        return FALSE;
    }
    if (S_ISLNK (buf.st_mode)) {
        log ("%s: The %s '%s' must not be a symbolic link.\n", prog, description, filename);
        return (FALSE);
    }
    if (!S_ISREG (buf.st_mode)) {
        log ("%s: The %s '%s' is not a regular file.\n", prog, description, filename);
        return FALSE;
    }
    if (buf.st_uid != geteuid ()) {
        log ("%s: The %s '%s' must be owned by you.\n", prog, description, filename);
        return FALSE;
    }
    if ((buf.st_mode & 0777) & ~permmask) {
        log ("%s: The %s '%s' must have permissions no more " "than -%c%c%c%c%c%c%c%c%c (0%o); " "right now permissions are -%c%c%c%c%c%c%c%c%c (0%o).\n", prog, description, filename, permmask & S_IRUSR ? 'r' : '-', permmask & S_IWUSR ? 'w' : '-', permmask & S_IXUSR ? 'x' : '-', permmask & S_IRGRP ? 'r' : '-', permmask & S_IWGRP ? 'w' : '-', permmask & S_IXGRP ? 'x' : '-', permmask & S_IROTH ? 'r' : '-', permmask & S_IWOTH ? 'w' : '-', permmask & S_IXOTH ? 'x' : '-', permmask & 0777, buf.st_mode & S_IRUSR ? 'r' : '-', buf.st_mode & S_IWUSR ? 'w' : '-', buf.st_mode & S_IXUSR ? 'x' : '-', buf.st_mode & S_IRGRP ? 'r' : '-', buf.st_mode & S_IWGRP ? 'w' : '-', buf.st_mode & S_IXGRP ? 'x' : '-', buf.st_mode & S_IROTH ? 'r' : '-', buf.st_mode & S_IWOTH ? 'w' : '-', buf.st_mode & S_IXOTH ? 'x' : '-', buf.st_mode & 0777);
        return FALSE;
    }
    return TRUE;
}

