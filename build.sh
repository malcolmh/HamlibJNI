swig -java -I../Hamlib/include/ -outcurrentdir -package hamlib -outdir src/hamlib -o jni/hamlib_wrap.c ../Hamlib/bindings/hamlib.swg
gcc -fPIC -c -I/Library/Java/JavaVirtualMachines/jdk1.8.0_241.jdk/Contents/Home/include -I/Library/Java/JavaVirtualMachines/jdk1.8.0_241.jdk/Contents/Home/include/darwin -I../Hamlib/src -o jni/libhamlib_wrap.dylib jni/hamlib_wrap.c
