package com.jonassav.maven;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.asm.MemberSubstitution;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.implementation.MethodCall;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.SuperMethodCall;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

import java.io.PrintStream;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;

import static net.bytebuddy.matcher.ElementMatchers.*;

public class PrInterceptAgent {

    public static void premain(String arguments,
                               Instrumentation instrumentation) throws NoSuchMethodException, NoSuchFieldException{
        Method m1 = PrintStream.class.getMethod("println", String.class);
        Field m2 = System.class.getField("out");

            new AgentBuilder.Default()
                    .type(ElementMatchers.any())
                    .transform((builder, type, classLoader, module, protectionDomain) ->
                            builder.method(any()).intercept(MethodCall.invoke(
                                            m1)
                                    .onField(m2)
                                    .with("Hello World")
                                    .andThen(SuperMethodCall.INSTANCE)))
                    .installOn(instrumentation);

    }
//@Advice.Argument(value = 0, readOnly = false) Object arg
//    public static class PrintInterceptor {
//        @Advice.OnMethodEnter
//        static void intercept() {
//            System.out.println("I'm an agent");
//
//        }
//    }
}
