# cubeanalyse
kafka examples
����������Ϊ��дKafka producer��consumer��ģ�塣

�����mvn��Ŀpom������
<dependency>
  <groupId>org.apache.kafka</groupId>
  <artifactId>kafka-clients</artifactId>
  <version>0.8.2.1</version>
</dependency>

<dependency>
  <groupId>org.apache.kafka</groupId>
  <artifactId>kafka_2.10</artifactId>
  <version>0.8.2.1</version>
</dependency>

�����sbt��Ŀbuild.sbt���

libraryDependencies ++= Seq(
  "org.apache.kafka" %% "kafka" % "0.8.2.1")