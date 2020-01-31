cd src
javac -d ../bin/ test/Test.java rig/Enums.java rig/Rig.java
javah -d ../jni rig.Rig
cd -

