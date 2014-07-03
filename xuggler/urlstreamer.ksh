#/bin/ksh

/java/bin/java -Dcamera.login=xanC9D309:7cPoU95o1ip6\
 -Djavax.net.ssl.trustStore=/java/lib/security/mykeystore \
 -Djavax.net.ssl.trustStorePassword=changeit -cp .:guava-17.0.jar:log4j-1.2.17.jar:urlstreamer.jar com.att.wifi.camera.URLConnectionReader /tmp/junkimage $*