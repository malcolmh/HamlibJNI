gcc -I/Library/Java/JavaVirtualMachines/jdk1.8.0_241.jdk/Contents/Home/include -I/Library/Java/JavaVirtualMachines/jdk1.8.0_241.jdk/Contents/Home/include/darwin -O0 -g3 -Wall -c -o "hamrig.o" "hamrig.c"
gcc -fPIC -o "libhamrig.dylib"  ./hamrig.o   -lhamlib
sudo cp libhamrig.dylib /usr/local/lib/
