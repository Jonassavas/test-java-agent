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

    public static void premain(String agentArgs, Instrumentation instrumentation) throws NoSuchMethodException {

        Method m1 = PrintInterceptor.class.getMethod("intercept", String.class);

        new AgentBuilder.Default()
                .type(ElementMatchers.any()) // Match all classes
                .transform((builder, type, classLoader, module, protectionDomain) -> builder
                        .visit(MemberSubstitution.strict()
                                .method(named("println"))
                                .stub()
                                .on(ElementMatchers.any()))).installOn(instrumentation);
    }



    public static class PrintInterceptor {
        public static void intercept(String message) {
            String modifiedMessage = "Modified: " + message;
            System.out.println(modifiedMessage);
        }
    }
}