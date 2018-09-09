MutationInjectionFramework
==========================

A framework for evaluating clone detection tools based upon source code mutation and injection.

For any questions or problems, please contact me at jeff.svajlenko@gmail.com.

Dependencies
============
The following commands must be installed and available on the $PATH variable: txl, astyle, dos2unix, and sed.

TXL is available here: https://www.txl.ca/.

dos2unix is included in most linux distribution repositories.  On mac, it can be installed using homebrew (https://brew.sh/), 'brew install unix2dos'.

Astyle is available in most linux distribution repositories.  However, the Mutation Framework may not be compatible with new version.  For this reason we include astyle in dependencies/astyle.  We include the binary, which should be compatible with Linux.  To compile for your system, run 'make clean' followed by 'make' in dependencies/astyle/build/gcc.  The binary file will now be in ependencies/astyle/build/gcc/bin and can be added to $PATH or copied to /user/local/bin (for example).

We depend on the gnu version of sed.  The default version of sed included with mac is not compatible.  Gnu-sed can be installed on mac using homebrew (https://brew.sh/) with 'brew install gnu-sed'.  You then need to remap sed to gsed in the console window where you execute the mutation framework.  Alternatively, you can make gsed the default with 'brew install gnu-sed --with-default-names', but this could hurt compatability with other mac programs.

Installation
============
Clone the git repository to your system and run 'make' within the root directory.  The Mutation Framework is then executed with './run' from the root directory.

Usage
=====
To understand the procedure, please refer to the Mutation Framework publications.

The application is executed by a menu-based system that is self-documenting.

Tool Runners
============
Tool runners implement a communication protocol between the framework and the clone detection tool.  The framework executes the tool runner with the following arguments:
1: Full path to the directory containing the subject system to run clone detection on.
2: The programming language of the subject system (java,c,cs).
3: Full path to the mutation framework installation directory.
4: Full path to the directory containing the clone detection tool (as specified to the framework).
5: The granularity of the clone (function,block).
6: Full path to the directory containing this tool runner.
7: Minimum clone size in lines.
8: Min clone size in tokens.
9: Max clone size in lines.
10: Max clone size in tokens
11: Max clone dissaimilarity.

After executing the tool for the subject software system, the tool should output to stdout the full path to the clone detection report summarizing the detected clone pairs.  The clone detection report file should be formatted as a newline delimited list of clone pairs, where each line has a clone pair in format: 'srcfile1,startline1,endline1,srcfile2,startline2,endline2'.  For example:

/Path/to/File1.java,5,10,/Path/to/File2,20,25\
/Path/to/File2,20,25,/Path/to/File3,50,55

An example tool runner is included in exampleToolRunner/.  Note that this is included as an example, and won't necissary work on your system.

Import Manual Clones
====================
The framework supports importing manually constructed clones.  It requires a import file which lists the clone pairs in the following format:

/Some/Path/CodeFragment1Clone1\
/Some/Path/CodeFragment2Clone1

/Some/Path/CodeFragment1Clone2\
/Some/Path/CodeFragment2Clone12

/Some/Path/CodeFragment1Clone3\
/Some/Path/CodeFragment2Clone3

An example of such an import is included in exampleImport/.

Having Problems?
================
If you are having difficulty using the framework, please do not hestitate to contact us at jeff.svajlenko@gmail.com and croy@cs.usask.ca.  If you are expirencing crashes or bugs, please let us know your environment configuration so we can improve compatability of the tool.
