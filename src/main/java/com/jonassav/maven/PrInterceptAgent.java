package com.jonassav.maven;

import org.objectweb.asm.util.TraceClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;
import net.bytebuddy.asm.Advice;

import java.io.PrintWriter;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

import static net.bytebuddy.matcher.ElementMatchers.none;

public class PrInterceptAgent {

    public static void premain(String agentArgs, Instrumentation instrumentation) {
        instrumentation.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
                if (className.equals("Test")) {
                    return transformString(className, classfileBuffer);
                }
                return classfileBuffer;
            }
        });
    }

    private static byte[] transformString(String className, byte[] classfileBuffer) {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(classfileBuffer);
        classReader.accept(classNode, 0);

        ClassWriter classWriterOld = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        classNode.accept(new TraceClassVisitor(classWriterOld, new PrintWriter(System.out)));

        for (MethodNode methodNode: classNode.methods) {
            if (methodNode.name.equals("main")) {
                System.out.println("Method main found");
                for (int i = 0; i < methodNode.instructions.size(); i++) {
                    if (methodNode.instructions.get(i).getOpcode() == 18) {
                        LdcInsnNode ldcInsnNode = (LdcInsnNode) methodNode.instructions.get(i);
                        ldcInsnNode.cst = "EVIL";
                        System.out.println("LDC found");
                    }
                }
            }
        }

        ClassWriter classWriterNew = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        classNode.accept(new TraceClassVisitor(classWriterNew, new PrintWriter(System.out)));
        return classWriterNew.toByteArray();

    }

    public static class PrintInterceptor {
        @Advice.OnMethodEnter
        public static void intercept(@Advice.Argument(value = 0, readOnly = false) String message) {
            message = "AGENT MODIFIED: " + message.replace("the", "no longer the");
        }
    }
}