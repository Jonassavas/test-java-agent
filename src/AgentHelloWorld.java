import java.lang.instrument.Instrumentation;

public class AgentHelloWorld {
    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("Hello World Agent works fine!");
    }
}

