swig -java -I../Hamlib/include/ -outcurrentdir -package hamlib -outdir src/hamlib -o jni/hamlib_wrap.c ../Hamlib/bindings/hamlib.swg
gcc -fPIC -I/usr/lib/jvm/java-8-openjdk-amd64/include/ -I/usr/lib/jvm/java-8-openjdk-amd64/include/linux/ -O0 -g3 -Wall -c -o jni/hamlib_wrap.o jni/hamlib_wrap.c
gcc -shared -o jni/libhamlib_wrap.so jni/hamlib_wrap.o -lhamlib
sudo cp jni/libhamlib_wrap.so /usr/local/lib/

