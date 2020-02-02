swig -java -I../Hamlib/include/ -outcurrentdir -package hamlib -outdir src/hamlib -o jni/hamlib_wrap.c ../Hamlib/bindings/hamlib.swg
gcc -fPIC -I/Library/Java/JavaVirtualMachines/jdk1.8.0_241.jdk/Contents/Home/include -I/Library/Java/JavaVirtualMachines/jdk1.8.0_241.jdk/Contents/Home/include/darwin -O0 -g3 -Wall -c -o "jni/hamlib_wrap.o" "jni/hamlib_wrap.c"
gcc -shared -o jni/libhamlib_wrap.dylib jni/hamlib_wrap.o -lhamlib
sudo cp jni/libhamlib_wrap.dylib /usr/local/lib/

