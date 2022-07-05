cd c:/kafka
bin/windows/kafka-server-start.bat config/server.properties

rem https://stackoverflow.com/questions/59187659/windows-kafka-java-nio-file-filesystemexception

rem If you have to use kafka in windows environment. You have to disable log retention.
rem In Kafka server.properties

rem          log.retention.hours=-1
rem          log.cleaner.enable=false

rem Remove any other rows start from log.retention.*


rem start call bin/windows/zookeeper-server-start.bat config/zookeeper.properties
rem start call bin/windows/kafka-server-start.bat config/server.properties