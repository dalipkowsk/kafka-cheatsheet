cd ..\..\..\simple-kafka-cluster-data\kafka1\var\lib\kafka\data\simplest-topic-2

kafka-run-class kafka.tools.DumpLogSegments --deep-iteration --print-data-log --files 00000000000000000000.log