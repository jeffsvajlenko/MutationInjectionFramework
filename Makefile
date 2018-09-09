all:	
	javac -cp src/:dependencies/Apache-Commons/IO/commons-io-2.4.jar:dependencies/h2-1.3.167.jar src/**/*.java
	cd dependencies/astyle/build/gcc; make; cd ../../../../
	cd dependencies/NiCad/; make; cd ../../
clean:
	find src/ -type f -name "*.class" - delete
	cd dependencies/astyle/build/gcc/; make clean; cd ../../../../
	cd dependencies/NiCad/; make clean; cd ../../
