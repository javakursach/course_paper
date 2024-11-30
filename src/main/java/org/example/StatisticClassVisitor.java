package org.example;

import org.objectweb.asm.*;

// Класс StatisticClassVisitor наследует ClassVisitor и собирает статистику о классе
public class StatisticClassVisitor extends ClassVisitor {
    // Переменные для хранения статистики

    private int loopOpcodes = 0; // Счетчик опкодов циклов
    private int conditionalBranches = 0; // Счетчик условных переходов
    private int variableDeclarations = 0; // Счетчик объявлений переменных
    private int fieldCount = 0; // Счетчик полей класса

    // Конструктор класса, инициализирует родительский класс с версией ASM
    public StatisticClassVisitor() {
        super(Opcodes.ASM9);
    }


    /**
     * visitField - метод для обработки полей класса.
     *
     * @param access - доступ к полю (например, public, private, static и т.д.), определяет модификаторы доступа.
     * @param name - имя поля, которое обрабатывается.
     * @param desc - описание типа поля (например, "I" для int, "Ljava/lang/String;" для String и т.д.).
     * @param signature - (может быть null) дополнительная информация о типе, используемая для обобщений.
     * @param value - (может быть null) значение по умолчанию для поля, если оно статическое.
     */
    @Override
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        fieldCount++; // Увеличиваем счетчик полей
        return super.visitField(access, name, desc, signature, value); // Вызываем метод родителя
    }


    /**
     * visitMethod - метод для обработки методов класса.
     *
     * @param access - доступ к методу (например, public, private, static и т.д.), определяет модификаторы доступа.
     * @param name - имя метода, который обрабатывается.
     * @param desc - описание сигнатуры метода, включая типы параметров и возвращаемый тип (например, "(I)I" для метода, принимающего int и возвращающего int).
     * @param signature - (может быть null) дополнительная информация о типе, используемая для обобщений.
     * @param exceptions - массив строк, представляющий исключения, которые может выбросить метод (например, "java/lang/Exception").
     */
    @Override
    public MethodVisitor visitMethod(int access, String name, String desc,
                                     String signature, String[] exceptions) {
        return new StatisticMethodVisitor();
    }

    // Вложенный класс для сбора статистики о методах
    private class StatisticMethodVisitor extends MethodVisitor {

        // Конструктор класса, инициализирует родительский класс с версией ASM
        public StatisticMethodVisitor() {
            super(Opcodes.ASM9);
        }


        /**
         * visitVarInsn - метод для обработки инструкций работы с переменными.
         *
         * @param opcode - код операции, определяющий тип инструкции (например, загрузка или сохранение переменной).
         * @param var - индекс переменной в локальных переменных метода.
         */
        @Override
        public void visitVarInsn(int opcode, int var) {
            // Проверяем, является ли инструкция загрузкой переменной
            if (opcode >= Opcodes.ILOAD && opcode <= Opcodes.ALOAD) {
                variableDeclarations++; // Увеличиваем счетчик объявлений переменных
            }
            super.visitVarInsn(opcode, var); // Вызываем метод родителя
        }


        /**
         * visitJumpInsn - метод для обработки условных переходов.
         *
         * @param opcode - код операции, определяющий тип перехода (например, условный переход или безусловный переход).
         * @param label - метка, на которую происходит переход, используется для указания места в байт-коде.
         */
        @Override
        public void visitJumpInsn(int opcode, Label label) {
            // Проверяем, является ли инструкция условным переходом
            if (opcode == Opcodes.IFEQ || opcode == Opcodes.IFNE || opcode == Opcodes.IFLT || opcode == Opcodes.IFGE ||
                    opcode == Opcodes.IFGT || opcode == Opcodes.IFLE || opcode == Opcodes.IF_ICMPEQ ||
                    opcode == Opcodes.IF_ICMPNE || opcode == Opcodes.IF_ICMPLT || opcode == Opcodes.IF_ICMPGE ||
                    opcode == Opcodes.IF_ICMPGT || opcode == Opcodes.IF_ICMPLE || opcode == Opcodes.IF_ACMPEQ ||
                    opcode == Opcodes.IF_ACMPNE) {
                conditionalBranches++; // Увеличиваем счетчик условных переходов
            } else if (opcode == Opcodes.GOTO) {
                loopOpcodes++; // Увеличиваем счетчик опкодов циклов
            }
            super.visitJumpInsn(opcode, label); // Вызываем метод родителя
        }
    }

    // Метод, вызываемый в конце обработки класса
    @Override
    public void visitEnd() {
        // Выводим собранную статистику
        System.out.println("Статистика класса:");
        System.out.println("Количество опкодов циклов: " + loopOpcodes);
        System.out.println("Количество условных переходов: " + conditionalBranches);
        System.out.println("Количество объявлений переменных в методах: " + variableDeclarations);
        System.out.println("Количество полей в классе: " + fieldCount);
        super.visitEnd(); // Вызываем метод родителя
    }
}
