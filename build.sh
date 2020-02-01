swig -java -I../Hamlib/include/ -outcurrentdir -package hamlib -outdir src/hamlib -o jni/hamlib_wrap.c ../Hamlib/bindings/hamlib.swg
gcc -I/Library/Java/JavaVirtualMachines/jdk1.8.0_241.jdk/Contents/Home/include -I/Library/Java/JavaVirtualMachines/jdk1.8.0_241.jdk/Contents/Home/include/darwin -O0 -g3 -Wall -c -o "jni/hamlib_wrap.o" "jni/hamlib_wrap.c"
gcc -c -o jni/main.o main.c
gcc -fPIC -o jni/libhamlib_wrap.dylib jni/hamlib_wrap.o jni/main.o -lhamlib
rm jni/*.o
sudo cp jni/libhamlib_wrap.dylib /usr/local/lib/

