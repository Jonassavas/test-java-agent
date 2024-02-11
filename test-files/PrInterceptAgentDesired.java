public class PrInterceptAgentDesired {

    public static void intercept(String message) {
        System.out.println("AGENT MODIFIED: " + message);
    }

    public static void premain(String agentArgs, Instrumentation instrumentation) throws NoSuchMethodException {

        Method m1 = PrintInterceptor.class.getMethod("intercept", String.class);

        new AgentBuilder.Default()
                .type(ElementMatchers.nameContains("Test"))
                .transform((builder, type, classLoader, module, protectionDomain) -> builder
                        .visit(MemberSubstitution.strict()
                                .method(named("println"))
                                .stub() // replaceWith(m1) and similar 'advice' tests does not work.
                                .on(ElementMatchers.any()))).installOn(instrumentation);
    }
}