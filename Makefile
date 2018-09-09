all:	
	javac -cp src/:dependencies/Apache-Commons/IO/commons-io-2.4.jar:dependencies/h2-1.3.167.jar src/**/*.java
	cd dependencies/NiCad/; make clean; make; cd ../../
