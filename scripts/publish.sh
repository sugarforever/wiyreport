#!/bin/sh

cd ..
mvn package
scp target/wiyreport-0.0.2.jar root@www.wiysoft.xyz:~/wiyreport-0.0.2.jar.new
cd -
