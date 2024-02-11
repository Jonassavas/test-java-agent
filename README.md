# test-java-agent

I wanted the agent to only change the arguments to `Sytem.out.println` but was only able to remove them completely
as seen in [this](./test-files/PrInterceptAgentDesired.java) file. Replacing them with something else was unsuccessful.

This agent instead changes the argument to methods that contain "print" in their name.

#### Compile the test and the agent (from the root directory):
```
javac ./test-files/Test.java
```
#### Package the test class in a JAR-file:
```
jar cfm ./test-files/Test.jar ./test-files/Manifest.txt -C ./test-files/ Test.class
```

#### Package the agent:
```
mvn clean package
```

#### Running:
```
java -javaagent:./target/printercept-1.0-shaded.jar -jar ./test-files/Test.jar
```



