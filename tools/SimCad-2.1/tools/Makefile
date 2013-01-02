# Makefile for new NiCad tools
# J.R. Cordy March 2010

MACHINESIZE = -m32
# MACHINESIZE = -m64 -mcmodel=medium
TURINGSIZE = -mLINUX -DHUGE

all : streamprocess.x

streamprocess.x: TLI.c TLS.c TLglob.h LINUX/cinterface main.c streamprocess.c
	cc -w -O $(MACHINESIZE) -I . -o streamprocess.x main.c TLI.c TLS.c streamprocess.c

streamprocess.c: streamprocess.t 
	tpc $(TURINGSIZE) -O -A -q4 streamprocess.t

clean:
	/bin/rm -f streamprocess.x

reset:
	@echo ""
	@echo "*** Don't do this unless you are CERTAIN that you know what you are doing! ***"
	@echo ""
	@echo "Press return to proceed, CTRL-c to abort: "
	@read x
	/bin/rm -f streamprocess.x streamprocess.c *.o
