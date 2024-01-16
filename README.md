# test-java-agent


#### Compile the test and the agent (while in the src directory):
```
javac AgentHelloWorld.java Test.java
```

#### Create Manifest files to instruct the JVM of the entrypoints in the jar's:
In MANIFEST.MF (for the agent)
```
Premain-Class: AgentHelloWorld
```
In Manifest.txt for the test class:
```
Main-Class: Test
```

#### Package them into jar files:
```
jar cfm Test0.jar Manifest.txt Test.class
```
```
jar cvf Agent0.jar AgentHelloWorld.class
jar cvmf MANIFEST.MF Agent0.jar AgentHelloWorld.class
```

#### Running:
```
java -javaagent:Agent0.jar -jar Test0.jar
```



