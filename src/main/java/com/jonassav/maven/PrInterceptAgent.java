package com.jonassav.maven;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.matcher.ElementMatchers;
import java.lang.instrument.Instrumentation;

public class PrInterceptAgent {

    public static void premain(String agentArgs, Instrumentation instrumentation) {
        new AgentBuilder.Default()
                .type(ElementMatchers.any()) // Match all classes
                .transform(new AgentBuilder.Transformer.ForAdvice()
                        .include(PrintInterceptor.class.getClassLoader())
                        .advice(ElementMatchers.named("println"), PrintInterceptor.class.getName()))
                .installOn(instrumentation);
    }

    public static class PrintInterceptor {
        @Advice.OnMethodEnter
        public static void intercept() {
            // You can replace the print statement with your desired logic
            System.out.println("Intercepted: Printing suppressed by agent");
        }
    }
}
