#/bin/ksh

/java/bin/java -Dcamera.login=xanC9D309:7cPoU95o1ip6\
 -Djavax.net.ssl.trustStore=/java/lib/security/mykeystore \
 -Djavax.net.ssl.trustStorePassword=changeit -cp .:guava-17.0.jar:log4j-1.2.17.jar:commons-cli-1.1.jar:httpclient-4.3.4.jar:urlstreamer.jar:commons-codec-1.6.jar:httpcore-4.3.2.jar:commons-logging-1.1.3.jar com.att.wifi.camera.URLStreamer /tmp/junkimage $*
 
guava-17.0.jar:log4j-1.2.17.jar:commons-cli-1.1.jar:httpclient-4.3.4.jar:urlstreamer.jar:commons-codec-1.6.jar:httpcore-4.3.2.jar:commons-logging-1.1.3.jar
