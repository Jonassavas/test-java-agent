package com.jonassav.maven;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.asm.MemberSubstitution;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.matcher.ElementMatchers;

import javax.swing.text.Element;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;

import static net.bytebuddy.matcher.ElementMatchers.named;
import static net.bytebuddy.matcher.ElementMatchers.takesArgument;

public class PrInterceptAgent {

    public static void premain(String agentArgs, Instrumentation instrumentation) {
        new AgentBuilder.Default()
                .type(ElementMatchers.nameContains("Test")) // Match all classes
                .transform((builder, type, classLoader, module, protectionDomain) ->
                        builder.visit(Advice.to(PrintInterceptor.class).on(ElementMatchers.takesArgument(0, String.class))))
                .installOn(instrumentation);
    }

    public static class PrintInterceptor {
        @Advice.OnMethodEnter
        public static void intercept(@Advice.Argument(value = 0, readOnly = false) String message) {
            String modifiedMessage = "Modified: " + message;
            System.out.println(modifiedMessage);
            message = modifiedMessage;
        }
    }
}