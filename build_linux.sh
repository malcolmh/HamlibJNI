swig -java -I../Hamlib/include/ -outcurrentdir -package hamlib -outdir src/hamlib -o jni/hamlib_wrap.c ../Hamlib/bindings/hamlib.swg
gcc -I/usr/lib/jvm/java-8-openjdk-amd64/include/ -I/usr/lib/jvm/java-8-openjdk-amd64/include/linux/ -O0 -g3 -Wall -c -o jni/hamlib_wrap.o jni/hamlib_wrap.c
gcc -c -o jni/main.o main.c
gcc -fPIC -o jni/libhamlib_wrap.so jni/hamlib_wrap.o jni/main.o -lhamlib
rm jni/*.o
sudo cp jni/libhamlib_wrap.so /usr/local/lib/

