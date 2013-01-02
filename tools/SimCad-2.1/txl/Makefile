# Makefile to compile all TXL tools used in NiCad

.SUFFIXES:
.SUFFIXES:.txl .x

all: c-extract-blocks.x c-extract-functions.x \
	c-rename-blind-blocks.x c-rename-blind-functions.x \
	c-rename-consistent-blocks.x c-rename-consistent-functions.x \
	cs-extract-blocks.x cs-extract-functions.x \
	cs-rename-blind-blocks.x cs-rename-blind-functions.x cs-rename-consistent-blocks.x cs-rename-consistent-functions.x \
	java-extract-blocks.x java-extract-functions.x \
	java-rename-blind-blocks.x \
	java-rename-blind-functions.x java-rename-consistent-blocks.x java-rename-consistent-functions.x \
	py-extract-blocks.x py-extract-functions.x \
	py-rename-blind-blocks.x py-rename-blind-functions.x py-rename-consistent-blocks.x \
	py-rename-consistent-functions.x getoriginal.x ifdef.x pyindent.x

clean:
	/bin/rm -f *.x

.txl.x: ; 
	txlc -s 400 $*.txl
