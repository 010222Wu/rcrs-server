#! /bin/sh
DIR=`dirname $0`
java -Xmx256m -cp $DIR/../oldsims/ traffic.Main localhost 7000 $*
