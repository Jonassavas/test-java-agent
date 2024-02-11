package com.jonassav.maven;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.matcher.ElementMatchers;

import javax.swing.text.Element;
import java.lang.instrument.Instrumentation;

import static net.bytebuddy.matcher.ElementMatchers.none;

public class PrInterceptAgent {

    public static void premain(String agentArgs, Instrumentation instrumentation) {
        new AgentBuilder.Default().ignore(none())
                .type(ElementMatchers.nameContains("Test"))
                .transform((builder, type, classLoader, module, protectionDomain) ->
                        builder.visit(Advice.to(PrintInterceptor.class).on(ElementMatchers.takesArgument(0, String.class)
                                .and(ElementMatchers.nameContainsIgnoreCase("print")))))
                .installOn(instrumentation);
    }

    public static class PrintInterceptor {
        @Advice.OnMethodEnter
        public static void intercept(@Advice.Argument(value = 0, readOnly = false) String message) {
            message = "AGENT MODIFIED: " + message.replace("the", "no longer the");
        }
    }
}