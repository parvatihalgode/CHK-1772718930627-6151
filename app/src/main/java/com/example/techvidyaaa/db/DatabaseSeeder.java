package com.example.techvidyaaa.db;

import android.content.Context;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class DatabaseSeeder {

    public static void seedIfNeeded(Context context) {
        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getDatabase(context);
            db.clearAllTables(); 
            
            List<QuestionEntity> questions = new ArrayList<>();
            
            addCQuestions(questions);
            addPythonQuestions(questions);
            addJavaQuestions(questions);
            addDataStructureQuestions(questions);
            addAlgorithmQuestions(questions);
            addMLQuestions(questions);
            addCloudQuestions(questions);

            db.questionDao().insertAll(questions);
        });
    }

    private static void addCQuestions(List<QuestionEntity> q) {
        String sub = "C Programming";
        // EASY (1-20)
        q.add(new QuestionEntity(sub, "Easy", "Who is the father of C?", "Dennis Ritchie", "Bjarne Stroustrup", "James Gosling", "Ken Thompson", 0));
        q.add(new QuestionEntity(sub, "Easy", "Which header file is used for printf()?", "stdio.h", "conio.h", "stdlib.h", "string.h", 0));
        q.add(new QuestionEntity(sub, "Easy", "What is the result of 5/2 in C?", "2.5", "2", "2.0", "Error", 1));
        q.add(new QuestionEntity(sub, "Easy", "Symbol used for logical AND?", "&", "&&", "|", "||", 1));
        q.add(new QuestionEntity(sub, "Easy", "Return type of main()?", "void", "int", "float", "char", 1));
        q.add(new QuestionEntity(sub, "Easy", "Which operator is used to get the address of a variable?", "*", "&", "->", "@", 1));
        q.add(new QuestionEntity(sub, "Easy", "Which keyword is used to declare a constant?", "const", "final", "static", "fixed", 0));
        q.add(new QuestionEntity(sub, "Easy", "What is the size of char in C?", "1 byte", "2 bytes", "4 bytes", "8 bytes", 0));
        q.add(new QuestionEntity(sub, "Easy", "Which escape sequence is used for a new line?", "\\t", "\\r", "\\n", "\\b", 2));
        q.add(new QuestionEntity(sub, "Easy", "C is what type of language?", "Object Oriented", "Procedural", "Scripting", "Functional", 1));
        q.add(new QuestionEntity(sub, "Easy", "Which loop is exit-controlled?", "for", "while", "do-while", "None", 2));
        q.add(new QuestionEntity(sub, "Easy", "What is the range of signed char?", "-128 to 127", "0 to 255", "-255 to 255", "0 to 127", 0));
        q.add(new QuestionEntity(sub, "Easy", "Which function is used to read a single character?", "scanf()", "getch()", "getchar()", "read()", 2));
        q.add(new QuestionEntity(sub, "Easy", "Which operator is used for modulo division?", "/", "%", "*", "||", 1));
        q.add(new QuestionEntity(sub, "Easy", "Is C case sensitive?", "Yes", "No", "Depends on compiler", "Only for variables", 0));
        q.add(new QuestionEntity(sub, "Easy", "What is the index of the first element in an array?", "1", "0", "-1", "Depends", 1));
        q.add(new QuestionEntity(sub, "Easy", "Which keyword is used to exit a loop?", "quit", "stop", "break", "return", 2));
        q.add(new QuestionEntity(sub, "Easy", "Which operator is used for Bitwise OR?", "|", "||", "&", "&&", 0));
        q.add(new QuestionEntity(sub, "Easy", "What is a string in C?", "An object", "An array of characters", "A primitive type", "None", 1));
        q.add(new QuestionEntity(sub, "Easy", "Which function returns the length of a string?", "length()", "sizeof()", "strlen()", "count()", 2));

        // MEDIUM (21-40)
        q.add(new QuestionEntity(sub, "Medium", "C is a _____ level language.", "Low", "High", "Middle", "Machine", 2));
        q.add(new QuestionEntity(sub, "Medium", "Which operator is used for structure member access?", ".", "->", "Both", "None", 2));
        q.add(new QuestionEntity(sub, "Medium", "Size of 'int' in C is?", "2 bytes", "4 bytes", "Compiler dependent", "8 bytes", 2));
        q.add(new QuestionEntity(sub, "Medium", "Which loop is guaranteed to execute at least once?", "for", "while", "do-while", "None", 2));
        q.add(new QuestionEntity(sub, "Medium", "Function to read string with spaces?", "scanf()", "gets()", "read()", "input()", 1));
        q.add(new QuestionEntity(sub, "Medium", "Which storage class has global visibility?", "auto", "static", "extern", "register", 2));
        q.add(new QuestionEntity(sub, "Medium", "What is a pointer?", "A variable that stores a value", "A variable that stores an address", "A function", "A data type", 1));
        q.add(new QuestionEntity(sub, "Medium", "Which operator is used for indirect access through a pointer?", "&", "*", "->", "::", 1));
        q.add(new QuestionEntity(sub, "Medium", "What is the purpose of realloc()?", "Allocate memory", "Deallocate memory", "Resize memory", "Clear memory", 2));
        q.add(new QuestionEntity(sub, "Medium", "Which keyword is used to define a custom data type name?", "struct", "union", "typedef", "enum", 2));
        q.add(new QuestionEntity(sub, "Medium", "How are arguments passed in C by default?", "Call by value", "Call by reference", "Both", "None", 0));
        q.add(new QuestionEntity(sub, "Medium", "What is the size of a pointer on a 32-bit system?", "2 bytes", "4 bytes", "8 bytes", "Depends on type", 1));
        q.add(new QuestionEntity(sub, "Medium", "Which header file is used for mathematical functions?", "stdio.h", "stdlib.h", "math.h", "string.h", 2));
        q.add(new QuestionEntity(sub, "Medium", "What is an enum in C?", "A class", "A set of named integer constants", "A floating point type", "None", 1));
        q.add(new QuestionEntity(sub, "Medium", "Which function is used to search for a character in a string?", "strstr()", "strpbrk()", "strchr()", "strcmp()", 2));
        q.add(new QuestionEntity(sub, "Medium", "What is a static variable?", "Variable that changes value", "Variable that retains value between calls", "Variable that is deleted after call", "None", 1));
        q.add(new QuestionEntity(sub, "Medium", "Which bitwise operator is used for shifting bits to the left?", ">>", "<<", "&", "~", 1));
        q.add(new QuestionEntity(sub, "Medium", "What is the use of 'volatile' keyword?", "Prevents optimization", "Ensures thread safety", "Makes variable constant", "None", 0));
        q.add(new QuestionEntity(sub, "Medium", "What does 'void' pointer mean?", "Null pointer", "Generic pointer", "Dangling pointer", "Invalid pointer", 1));
        q.add(new QuestionEntity(sub, "Medium", "Which operator is used to access structure members using a pointer?", ".", "->", "&", "*", 1));

        // HARD (41-60)
        q.add(new QuestionEntity(sub, "Hard", "Which keyword prevents any changes to a variable?", "static", "volatile", "const", "immutable", 2));
        q.add(new QuestionEntity(sub, "Hard", "Function for dynamic memory allocation in C?", "malloc()", "calloc()", "realloc()", "All of above", 3));
        q.add(new QuestionEntity(sub, "Hard", "Default value of local variable?", "0", "1", "Garbage", "Null", 2));
        q.add(new QuestionEntity(sub, "Hard", "Pointer declaration syntax?", "int p;", "int &p;", "int *p;", "pointer p;", 2));
        q.add(new QuestionEntity(sub, "Hard", "Which is ternary operator?", "?:", "::", "->", ".*", 0));
        q.add(new QuestionEntity(sub, "Hard", "What is a dangling pointer?", "Pointer to null", "Pointer to a freed memory location", "Pointer not initialized", "None", 1));
        q.add(new QuestionEntity(sub, "Hard", "Which function is used to free dynamic memory?", "malloc()", "free()", "delete()", "clear()", 1));
        q.add(new QuestionEntity(sub, "Hard", "What is the purpose of a bit field?", "To store large integers", "To use memory at bit level", "To define array", "None", 1));
        q.add(new QuestionEntity(sub, "Hard", "Which preprocessor directive is used for conditional compilation?", "#define", "#include", "#ifdef", "#pragma", 2));
        q.add(new QuestionEntity(sub, "Hard", "What is a function pointer?", "A pointer to a variable", "A pointer that stores the address of a function", "A function that returns a pointer", "None", 1));
        q.add(new QuestionEntity(sub, "Hard", "What is the output of 'sizeof(char *)' on a 64-bit system?", "1", "4", "8", "16", 2));
        q.add(new QuestionEntity(sub, "Hard", "Which function is used to compare two strings?", "strcpy()", "strcmp()", "strcat()", "strlen()", 1));
        q.add(new QuestionEntity(sub, "Hard", "What is a recursive function?", "A function that calls another", "A function that calls itself", "A loop", "None", 1));
        q.add(new QuestionEntity(sub, "Hard", "What is the difference between malloc() and calloc()?", "malloc initializes to 0", "calloc initializes to 0", "No difference", "malloc is for objects", 1));
        q.add(new QuestionEntity(sub, "Hard", "Which operator is used to get the size of a data type?", "len()", "size()", "sizeof()", "count()", 2));
        q.add(new QuestionEntity(sub, "Hard", "What is the use of #pragma directive?", "Define constants", "Include files", "Compiler specific instructions", "None", 2));
        q.add(new QuestionEntity(sub, "Hard", "What is a memory leak?", "Memory is corrupted", "Allocated memory is not freed", "Pointer is null", "None", 1));
        q.add(new QuestionEntity(sub, "Hard", "Which function is used to copy a string?", "strcpy()", "strcat()", "strcmp()", "strlen()", 0));
        q.add(new QuestionEntity(sub, "Hard", "What is an abstract data type in C?", "int", "struct", "union", "None", 1));
        q.add(new QuestionEntity(sub, "Hard", "Which function is used to open a file?", "open()", "fopen()", "read()", "write()", 1));
    }

    private static void addPythonQuestions(List<QuestionEntity> q) {
        String sub = "Python";
        // EASY (1-20)
        q.add(new QuestionEntity(sub, "Easy", "Who developed Python?", "Guido van Rossum", "Dennis Ritchie", "James Gosling", "Bjarne", 0));
        q.add(new QuestionEntity(sub, "Easy", "Extension of Python file?", ".py", ".pyt", ".python", ".txt", 0));
        q.add(new QuestionEntity(sub, "Easy", "How to start a comment in Python?", "//", "/*", "#", "--", 2));
        q.add(new QuestionEntity(sub, "Easy", "Correct way to create a list?", "[]", "{}", "()", "<>", 0));
        q.add(new QuestionEntity(sub, "Easy", "Output of print(2**3)?", "6", "8", "9", "5", 1));
        q.add(new QuestionEntity(sub, "Easy", "Which keyword is used for functions?", "func", "def", "define", "function", 1));
        q.add(new QuestionEntity(sub, "Easy", "Python is what type of language?", "Compiled", "Interpreted", "Assembly", "Low-level", 1));
        q.add(new QuestionEntity(sub, "Easy", "Which function is used to get input?", "input()", "get()", "read()", "scan()", 0));
        q.add(new QuestionEntity(sub, "Easy", "What is the output of print('Hello'[0])?", "H", "e", "l", "o", 0));
        q.add(new QuestionEntity(sub, "Easy", "Which data type is used to store multiple items in a single variable?", "int", "float", "list", "bool", 2));
        q.add(new QuestionEntity(sub, "Easy", "How do you start a for loop in Python?", "for i in range(5):", "for (i=0; i<5; i++)", "foreach i in 5", "for i to 5", 0));
        q.add(new QuestionEntity(sub, "Easy", "Which operator is used for addition?", "+", "-", "*", "/", 0));
        q.add(new QuestionEntity(sub, "Easy", "What is the output of bool(0)?", "True", "False", "None", "Error", 1));
        q.add(new QuestionEntity(sub, "Easy", "Which method is used to remove whitespace from both ends of a string?", "strip()", "trim()", "cut()", "remove()", 0));
        q.add(new QuestionEntity(sub, "Easy", "How do you create a variable in Python?", "int x = 5", "x = 5", "var x = 5", "declare x = 5", 1));
        q.add(new QuestionEntity(sub, "Easy", "Which keyword is used for a class?", "Class", "class", "Object", "def", 1));
        q.add(new QuestionEntity(sub, "Easy", "What is the output of type(5)?", "<class 'int'>", "<class 'float'>", "<class 'str'>", "<class 'bool'>", 0));
        q.add(new QuestionEntity(sub, "Easy", "Which operator is used for floor division?", "/", "//", "%", "**", 1));
        q.add(new QuestionEntity(sub, "Easy", "How do you check the length of a list?", "list.size()", "len(list)", "list.length", "count(list)", 1));
        q.add(new QuestionEntity(sub, "Easy", "Which collection is ordered and unchangeable?", "List", "Set", "Tuple", "Dictionary", 2));

        // MEDIUM (21-40)
        q.add(new QuestionEntity(sub, "Medium", "Is Python case sensitive?", "Yes", "No", "Depends", "None", 0));
        q.add(new QuestionEntity(sub, "Medium", "Keyword used for function in Python?", "func", "define", "def", "function", 2));
        q.add(new QuestionEntity(sub, "Medium", "Which is mutable data type?", "Tuple", "String", "List", "Int", 2));
        q.add(new QuestionEntity(sub, "Medium", "Function to get length of list?", "size()", "length()", "len()", "count()", 2));
        q.add(new QuestionEntity(sub, "Medium", "Operator for power (x^y)?", "^", "**", "*", "//", 1));
        q.add(new QuestionEntity(sub, "Medium", "Which statement is used to handle exceptions?", "try...except", "if...else", "for...in", "while", 0));
        q.add(new QuestionEntity(sub, "Medium", "What is a lambda function?", "A function with multiple names", "An anonymous function", "A class method", "None", 1));
        q.add(new QuestionEntity(sub, "Medium", "Which method is used to add an item to the end of a list?", "add()", "insert()", "append()", "extend()", 2));
        q.add(new QuestionEntity(sub, "Medium", "What does the 'range(5)' function return?", "[0, 1, 2, 3, 4, 5]", "[0, 1, 2, 3, 4]", "0, 5", "None", 1));
        q.add(new QuestionEntity(sub, "Medium", "Which library is used for scientific computing?", "Flask", "Pandas", "NumPy", "Django", 2));
        q.add(new QuestionEntity(sub, "Medium", "What is the purpose of '__init__' method?", "To initialize a class instance", "To destroy an instance", "To print values", "None", 0));
        q.add(new QuestionEntity(sub, "Medium", "How do you import a module in Python?", "include module", "using module", "import module", "require module", 2));
        q.add(new QuestionEntity(sub, "Medium", "Which method is used to return a list of all keys in a dictionary?", "keys()", "get_keys()", "list_keys()", "values()", 0));
        q.add(new QuestionEntity(sub, "Medium", "What is list comprehension?", "A way to copy a list", "A concise way to create lists", "A built-in function", "None", 1));
        q.add(new QuestionEntity(sub, "Medium", "Which operator is used for membership testing?", "is", "in", "==", "and", 1));
        q.add(new QuestionEntity(sub, "Medium", "What is a set in Python?", "An ordered collection", "An unordered collection of unique items", "A sequence of characters", "None", 1));
        q.add(new QuestionEntity(sub, "Medium", "Which function is used to open a file in Python?", "open()", "file()", "read()", "write()", 0));
        q.add(new QuestionEntity(sub, "Medium", "What is the output of 'not True'?", "True", "False", "None", "Error", 1));
        q.add(new QuestionEntity(sub, "Medium", "How do you define a dictionary?", "[]", "{}", "()", "<>", 1));
        q.add(new QuestionEntity(sub, "Medium", "Which keyword is used to skip the current iteration of a loop?", "break", "pass", "continue", "skip", 2));

        // HARD (41-60)
        q.add(new QuestionEntity(sub, "Hard", "Keyword for 'not equal'?", "!=", "<>", "not", "is not", 0));
        q.add(new QuestionEntity(sub, "Hard", "Library for Data Analysis?", "Django", "Flask", "Pandas", "PyGame", 2));
        q.add(new QuestionEntity(sub, "Hard", "Correct way to define class?", "class MyClass:", "def MyClass:", "object MyClass:", "new MyClass", 0));
        q.add(new QuestionEntity(sub, "Hard", "Create an empty dictionary?", "[]", "{}", "()", "set()", 1));
        q.add(new QuestionEntity(sub, "Hard", "Tuples are _____.", "Mutable", "Immutable", "Both", "None", 1));
        q.add(new QuestionEntity(sub, "Hard", "What is a decorator in Python?", "A class", "A function that modifies another function", "A variable", "None", 1));
        q.add(new QuestionEntity(sub, "Hard", "What is the purpose of 'yield' keyword?", "To return a value", "To create a generator", "To stop a loop", "None", 1));
        q.add(new QuestionEntity(sub, "Hard", "Which method is used to find the memory address of an object?", "address()", "loc()", "id()", "mem()", 2));
        q.add(new QuestionEntity(sub, "Hard", "What is the difference between '==' and 'is'?", "No difference", "'==' checks value, 'is' checks identity", "'is' checks value, '==' checks identity", "None", 1));
        q.add(new QuestionEntity(sub, "Hard", "What are *args and **kwargs?", "Variable arguments", "Static arguments", "Default arguments", "None", 0));
        q.add(new QuestionEntity(sub, "Hard", "What is the Global Interpreter Lock (GIL)?", "A lock for files", "A mechanism to allow only one thread to execute", "A database lock", "None", 1));
        q.add(new QuestionEntity(sub, "Hard", "Which library is used for creating web scrapers?", "Requests", "BeautifulSoup", "Scrapy", "All of above", 3));
        q.add(new QuestionEntity(sub, "Hard", "What is a virtual environment?", "A simulated OS", "An isolated environment for Python projects", "A cloud platform", "None", 1));
        q.add(new QuestionEntity(sub, "Hard", "How do you copy an object in Python?", "copy()", "deepcopy()", "Both", "None", 2));
        q.add(new QuestionEntity(sub, "Hard", "What is the purpose of 'with' statement?", "Looping", "Error handling", "Resource management", "None", 2));
        q.add(new QuestionEntity(sub, "Hard", "What is introspection in Python?", "Ability to examine objects at runtime", "A debugging tool", "A performance boost", "None", 0));
        q.add(new QuestionEntity(sub, "Hard", "Which method is used for operator overloading?", "__add__", "plus()", "sum()", "None", 0));
        q.add(new QuestionEntity(sub, "Hard", "What is monkey patching?", "Updating software", "Dynamic replacement of attributes at runtime", "A security flaw", "None", 1));
        q.add(new QuestionEntity(sub, "Hard", "What are metaclasses?", "Classes that create classes", "Subclasses", "Abstract classes", "None", 0));
        q.add(new QuestionEntity(sub, "Hard", "Which module is used for regular expressions?", "re", "regex", "search", "match", 0));
    }

    private static void addJavaQuestions(List<QuestionEntity> q) {
        String sub = "Java";
        // EASY (1-20)
        q.add(new QuestionEntity(sub, "Easy", "Keyword to create object in Java?", "class", "new", "object", "create", 1));
        q.add(new QuestionEntity(sub, "Easy", "Java is a _____ language.", "Object-Oriented", "Procedural", "Scripting", "Machine", 0));
        q.add(new QuestionEntity(sub, "Easy", "Method for string length?", "size()", "length()", "len()", "count()", 1));
        q.add(new QuestionEntity(sub, "Easy", "Java is platform _____.", "Dependent", "Independent", "Both", "None", 1));
        q.add(new QuestionEntity(sub, "Easy", "Int wrapper class?", "Int", "integer", "Integer", "Number", 2));
        q.add(new QuestionEntity(sub, "Easy", "Which keyword is used for a class?", "Class", "class", "Object", "def", 1));
        q.add(new QuestionEntity(sub, "Easy", "How do you start the main method?", "public static void main(String[] args)", "void main()", "static void main()", "None", 0));
        q.add(new QuestionEntity(sub, "Easy", "Which operator is used for multiplication?", "x", "*", "multi", "None", 1));
        q.add(new QuestionEntity(sub, "Easy", "What is the size of int in Java?", "2 bytes", "4 bytes", "8 bytes", "Depends", 1));
        q.add(new QuestionEntity(sub, "Easy", "Which keyword is used to inherit a class?", "inherits", "extends", "implements", "using", 1));
        q.add(new QuestionEntity(sub, "Easy", "What is the default value of a boolean?", "true", "false", "null", "0", 1));
        q.add(new QuestionEntity(sub, "Easy", "Which package is imported by default?", "java.util", "java.io", "java.lang", "java.net", 2));
        q.add(new QuestionEntity(sub, "Easy", "How do you print something in Java?", "print()", "System.out.println()", "printf()", "None", 1));
        q.add(new QuestionEntity(sub, "Easy", "Which keyword is used for a constant?", "const", "final", "static", "fixed", 1));
        q.add(new QuestionEntity(sub, "Easy", "Is Java case sensitive?", "Yes", "No", "Depends", "None", 0));
        q.add(new QuestionEntity(sub, "Easy", "What is the result of 10 / 3 in integer division?", "3.33", "3", "4", "Error", 1));
        q.add(new QuestionEntity(sub, "Easy", "Which keyword is used to handle exceptions?", "try", "catch", "finally", "All of above", 3));
        q.add(new QuestionEntity(sub, "Easy", "How do you declare an array?", "int arr[]", "int[] arr", "Both", "None", 2));
        q.add(new QuestionEntity(sub, "Easy", "Which operator is used for comparison?", "=", "==", "===", "!=", 1));
        q.add(new QuestionEntity(sub, "Easy", "What is the entry point of a Java program?", "main method", "init method", "start method", "None", 0));

        // MEDIUM (21-40)
        q.add(new QuestionEntity(sub, "Medium", "Which component runs bytecode?", "JDK", "JRE", "JVM", "JIT", 2));
        q.add(new QuestionEntity(sub, "Medium", "Size of boolean in Java?", "1 bit", "8 bits", "16 bits", "Depends", 0));
        q.add(new QuestionEntity(sub, "Medium", "Package containing Random class?", "java.lang", "java.io", "java.util", "java.net", 2));
        q.add(new QuestionEntity(sub, "Medium", "Keyword for inheritance?", "implements", "extends", "inherits", "import", 1));
        q.add(new QuestionEntity(sub, "Medium", "Size of char in Java?", "1 byte", "2 bytes", "4 bytes", "8 bytes", 1));
        q.add(new QuestionEntity(sub, "Medium", "Which keyword is used to refer to current object?", "super", "this", "self", "me", 1));
        q.add(new QuestionEntity(sub, "Medium", "What is method overloading?", "Methods with same name but different parameters", "Methods with same name and same parameters", "A method in subclass", "None", 0));
        q.add(new QuestionEntity(sub, "Medium", "What is method overriding?", "Method with same signature in subclass", "Method with same name but different parameters", "A constructor", "None", 0));
        q.add(new QuestionEntity(sub, "Medium", "Which keyword is used for interface?", "class", "interface", "extends", "implements", 1));
        q.add(new QuestionEntity(sub, "Medium", "What is the purpose of static keyword?", "Makes variable constant", "Belongs to class rather than instance", "Allows changes", "None", 1));
        q.add(new QuestionEntity(sub, "Medium", "Which class is the root of all classes?", "System", "String", "Object", "Main", 2));
        q.add(new QuestionEntity(sub, "Medium", "What is garbage collection in Java?", "Deleting files", "Automatic memory management", "Manual memory management", "None", 1));
        q.add(new QuestionEntity(sub, "Medium", "Which keyword is used to call a superclass constructor?", "super", "this", "parent", "base", 0));
        q.add(new QuestionEntity(sub, "Medium", "What is an abstract class?", "A class with no methods", "A class that cannot be instantiated", "A final class", "None", 1));
        q.add(new QuestionEntity(sub, "Medium", "Which exception is thrown when array index is out of bounds?", "IOException", "ArrayIndexOutOfBoundsException", "NullPointerException", "ArithmeticException", 1));
        q.add(new QuestionEntity(sub, "Medium", "What is the difference between StringBuffer and StringBuilder?", "StringBuffer is thread-safe", "StringBuilder is thread-safe", "No difference", "None", 0));
        q.add(new QuestionEntity(sub, "Medium", "Which keyword is used to restrict access?", "public", "private", "protected", "All of above", 3));
        q.add(new QuestionEntity(sub, "Medium", "What is an interface?", "A concrete class", "A contract with abstract methods", "A static class", "None", 1));
        q.add(new QuestionEntity(sub, "Medium", "Which keyword is used for thread synchronization?", "async", "synchronized", "volatile", "static", 1));
        q.add(new QuestionEntity(sub, "Medium", "What is the use of 'finally' block?", "To catch exceptions", "To execute code after try-catch", "To stop execution", "None", 1));

        // HARD (41-60)
        q.add(new QuestionEntity(sub, "Hard", "Default value of String?", "Empty", "null", "0", "undefined", 1));
        q.add(new QuestionEntity(sub, "Hard", "Not a Java access modifier?", "public", "private", "protected", "internal", 3));
        q.add(new QuestionEntity(sub, "Hard", "Superclass of all classes?", "String", "System", "Object", "Main", 2));
        q.add(new QuestionEntity(sub, "Hard", "Prevents method overriding?", "final", "static", "const", "private", 0));
        q.add(new QuestionEntity(sub, "Hard", "Exception for divide by zero?", "IOException", "NullPointerException", "ArithmeticException", "RuntimeException", 2));
        q.add(new QuestionEntity(sub, "Hard", "What is Reflection in Java?", "Ability to inspect classes at runtime", "A UI library", "A database connector", "None", 0));
        q.add(new QuestionEntity(sub, "Hard", "Which method is used to force garbage collection?", "gc()", "System.gc()", "Runtime.gc()", "All of above", 3));
        q.add(new QuestionEntity(sub, "Hard", "What is a deadlock in Java threads?", "A crash", "Threads waiting for each other indefinitely", "Threads running out of memory", "None", 1));
        q.add(new QuestionEntity(sub, "Hard", "What is the purpose of 'volatile' keyword in Java?", "Memory visibility", "Constant values", "Thread safety", "None", 0));
        q.add(new QuestionEntity(sub, "Hard", "Which library is used for Unit Testing?", "JUnit", "TestNG", "Both", "None", 2));
        q.add(new QuestionEntity(sub, "Hard", "What is Serialization?", "Converting object to bytes", "Converting bytes to object", "Encryption", "None", 0));
        q.add(new QuestionEntity(sub, "Hard", "What are Generics in Java?", "Universal classes", "Type-safe programming", "Subclasses", "None", 1));
        q.add(new QuestionEntity(sub, "Hard", "Which class is used for file I/O?", "File", "FileInputStream", "FileWriter", "All of above", 3));
        q.add(new QuestionEntity(sub, "Hard", "What is the difference between '==' and 'equals()'?", "No difference", "'==' checks reference, 'equals()' checks content", "'equals()' checks reference, '==' checks content", "None", 1));
        q.add(new QuestionEntity(sub, "Hard", "What is the memory area where objects are stored?", "Stack", "Heap", "Method Area", "None", 1));
        q.add(new QuestionEntity(sub, "Hard", "Which interface is used to compare objects?", "Comparable", "Comparator", "Both", "None", 2));
        q.add(new QuestionEntity(sub, "Hard", "What is the use of Optional class?", "Handling null values", "Optional parameters", "Optional methods", "None", 0));
        q.add(new QuestionEntity(sub, "Hard", "Which keyword is used for native methods?", "native", "extern", "using", "static", 0));
        q.add(new QuestionEntity(sub, "Hard", "What is Lambda expression in Java 8?", "Anonymous function", "A class", "A thread", "None", 0));
        q.add(new QuestionEntity(sub, "Hard", "What is Stream API?", "A network library", "A tool for functional-style operations on collections", "A UI component", "None", 1));
    }

    private static void addDataStructureQuestions(List<QuestionEntity> q) {
        String sub = "Data Structures";
        // EASY (1-20)
        q.add(new QuestionEntity(sub, "Easy", "Which DS follows LIFO?", "Queue", "Stack", "List", "Tree", 1));
        q.add(new QuestionEntity(sub, "Easy", "Which DS follows FIFO?", "Queue", "Stack", "Graph", "Array", 0));
        q.add(new QuestionEntity(sub, "Easy", "Tree with at most 2 children?", "B-Tree", "Binary Tree", "Full Tree", "AVL Tree", 1));
        q.add(new QuestionEntity(sub, "Easy", "Top element of tree is?", "Leaf", "Root", "Branch", "Node", 1));
        q.add(new QuestionEntity(sub, "Easy", "Node with no children?", "Root", "Parent", "Leaf", "Child", 2));
        q.add(new QuestionEntity(sub, "Easy", "What is an array?", "A collection of similar data elements", "A linked collection", "A hierarchy", "None", 0));
        q.add(new QuestionEntity(sub, "Easy", "Which operation is used to add to a stack?", "Push", "Pop", "Peek", "Add", 0));
        q.add(new QuestionEntity(sub, "Easy", "Which operation is used to remove from a stack?", "Push", "Pop", "Peek", "Remove", 1));
        q.add(new QuestionEntity(sub, "Easy", "What is a queue?", "LIFO structure", "FIFO structure", "Random structure", "None", 1));
        q.add(new QuestionEntity(sub, "Easy", "What is a linked list?", "A static array", "A dynamic collection of nodes linked by pointers", "A tree", "None", 1));
        q.add(new QuestionEntity(sub, "Easy", "What is the first node in a linked list called?", "Tail", "Head", "Root", "Branch", 1));
        q.add(new QuestionEntity(sub, "Easy", "What is the last node in a linked list called?", "Tail", "Head", "Root", "End", 0));
        q.add(new QuestionEntity(sub, "Easy", "Which DS is used for recursion?", "Queue", "Stack", "Array", "Graph", 1));
        q.add(new QuestionEntity(sub, "Easy", "What is the height of a tree?", "Number of nodes", "Longest path from root to leaf", "Number of edges", "None", 1));
        q.add(new QuestionEntity(sub, "Easy", "What is a balanced tree?", "Tree with equal nodes", "Tree where height difference is minimal", "A full tree", "None", 1));
        q.add(new QuestionEntity(sub, "Easy", "Which DS is used to represent relationships?", "Stack", "Queue", "Graph", "Array", 2));
        q.add(new QuestionEntity(sub, "Easy", "What is a node?", "A pointer", "A basic unit of data structure", "A function", "None", 1));
        q.add(new QuestionEntity(sub, "Easy", "Which DS uses a key-value pair?", "Stack", "Queue", "Hash Table", "List", 2));
        q.add(new QuestionEntity(sub, "Easy", "What is an edge in a graph?", "A node", "A connection between two nodes", "A weight", "None", 1));
        q.add(new QuestionEntity(sub, "Easy", "What is a leaf node?", "Node with two children", "Node with no children", "Root node", "None", 1));

        // MEDIUM (21-40)
        q.add(new QuestionEntity(sub, "Medium", "Worst case search in array?", "O(1)", "O(log n)", "O(n)", "O(n^2)", 2));
        q.add(new QuestionEntity(sub, "Medium", "Which DS used for BFS?", "Stack", "Queue", "Priority Queue", "Heap", 1));
        q.add(new QuestionEntity(sub, "Medium", "Linear search complexity?", "O(1)", "O(n)", "O(log n)", "O(n log n)", 1));
        q.add(new QuestionEntity(sub, "Medium", "DS using pointers to link nodes?", "Array", "Linked List", "Hash Table", "Stack", 1));
        q.add(new QuestionEntity(sub, "Medium", "Complexity of Binary Search?", "O(n)", "O(log n)", "O(1)", "O(n^2)", 1));
        q.add(new QuestionEntity(sub, "Medium", "What is a circular queue?", "Queue with no end", "Queue where last position connects back to first", "A stack", "None", 1));
        q.add(new QuestionEntity(sub, "Medium", "What is a doubly linked list?", "Nodes point only forward", "Nodes point both forward and backward", "A tree", "None", 1));
        q.add(new QuestionEntity(sub, "Medium", "Which DS is used for DFS?", "Stack", "Queue", "Heap", "Array", 0));
        q.add(new QuestionEntity(sub, "Medium", "What is the purpose of hashing?", "To sort data", "To map data of arbitrary size to fixed size", "To search linearly", "None", 1));
        q.add(new QuestionEntity(sub, "Medium", "What is a binary search tree?", "A tree with all nodes equal", "A binary tree where left < root < right", "A graph", "None", 1));
        q.add(new QuestionEntity(sub, "Medium", "What is the time complexity to insert into a heap?", "O(1)", "O(n)", "O(log n)", "O(n log n)", 2));
        q.add(new QuestionEntity(sub, "Medium", "What is an adjacency list?", "A way to represent a graph", "A sorting algorithm", "A tree traversal", "None", 0));
        q.add(new QuestionEntity(sub, "Medium", "What is a priority queue?", "A queue with FIFO", "A queue where each element has a priority", "A stack", "None", 1));
        q.add(new QuestionEntity(sub, "Medium", "What is tree traversal?", "Adding nodes", "Visiting all nodes in a tree", "Deleting nodes", "None", 1));
        q.add(new QuestionEntity(sub, "Medium", "Which traversal gives sorted order in a BST?", "Preorder", "Inorder", "Postorder", "None", 1));
        q.add(new QuestionEntity(sub, "Medium", "What is a complete binary tree?", "All levels are full", "All levels except possibly last are full", "A full tree", "None", 1));
        q.add(new QuestionEntity(sub, "Medium", "What is an AVL tree?", "A self-balancing BST", "A graph", "A simple binary tree", "None", 0));
        q.add(new QuestionEntity(sub, "Medium", "What is the purpose of a B-tree?", "To search memory", "To organize data on disk", "To sort arrays", "None", 1));
        q.add(new QuestionEntity(sub, "Medium", "What is the space complexity of an adjacency matrix?", "O(V)", "O(E)", "O(V^2)", "O(V+E)", 2));
        q.add(new QuestionEntity(sub, "Medium", "What is a spanning tree?", "A subgraph that includes all vertices and is a tree", "A complete graph", "A cyclic graph", "None", 0));

        // HARD (41-60)
        q.add(new QuestionEntity(sub, "Hard", "Best DS for undo operations?", "Queue", "Stack", "Array", "Graph", 1));
        q.add(new QuestionEntity(sub, "Hard", "Last node points to first in?", "Singly LL", "Circular LL", "Doubly LL", "Binary Tree", 1));
        q.add(new QuestionEntity(sub, "Hard", "Priority Queue implementation?", "Stack", "Linked List", "Heap", "Array", 2));
        q.add(new QuestionEntity(sub, "Hard", "Self-balancing binary tree?", "Binary Tree", "AVL Tree", "B-Tree", "Huffman Tree", 1));
        q.add(new QuestionEntity(sub, "Hard", "Hash collision resolution?", "Chaining", "Searching", "Sorting", "Dividing", 0));
        q.add(new QuestionEntity(sub, "Hard", "What is a Red-Black tree?", "A balanced tree", "A binary search tree with extra bit for color", "A graph", "None", 1));
        q.add(new QuestionEntity(sub, "Hard", "What is a Trie?", "A sorting algorithm", "A retrieval tree for strings", "A hash table", "None", 1));
        q.add(new QuestionEntity(sub, "Hard", "What is a segment tree?", "A tree used for range queries", "A binary tree", "A linked list", "None", 0));
        q.add(new QuestionEntity(sub, "Hard", "What is a Disjoint Set Union (DSU)?", "A sorting method", "A structure to keep track of set elements", "A graph", "None", 1));
        q.add(new QuestionEntity(sub, "Hard", "What is the complexity of union by rank in DSU?", "O(log n)", "O(alpha(n))", "O(1)", "O(n)", 1));
        q.add(new QuestionEntity(sub, "Hard", "What is a Fenwick tree?", "A binary indexed tree", "A red-black tree", "A trie", "None", 0));
        q.add(new QuestionEntity(sub, "Hard", "What is a Suffix Tree?", "Tree representing all suffixes of a string", "A BST", "A heap", "None", 0));
        q.add(new QuestionEntity(sub, "Hard", "What is the complexity of Dijkstra's with a priority queue?", "O(E log V)", "O(V^2)", "O(E + V)", "None", 0));
        q.add(new QuestionEntity(sub, "Hard", "What is a flow network in graphs?", "A directed graph with capacity", "An undirected graph", "A tree", "None", 0));
        q.add(new QuestionEntity(sub, "Hard", "What is the max-flow min-cut theorem?", "Max flow = min cut", "Max flow = total edges", "Min cut = 0", "None", 0));
        q.add(new QuestionEntity(sub, "Hard", "What is a skip list?", "A randomized DS", "A linked list with multiple layers", "Both", "None", 2));
        q.add(new QuestionEntity(sub, "Hard", "What is a persistent data structure?", "Data that never changes", "Data that preserves its previous versions", "A database", "None", 1));
        q.add(new QuestionEntity(sub, "Hard", "What is the time complexity of building a heap?", "O(n)", "O(n log n)", "O(log n)", "O(1)", 0));
        q.add(new QuestionEntity(sub, "Hard", "What is an amortized complexity?", "Average case", "Average time per operation over a sequence", "Worst case", "None", 1));
        q.add(new QuestionEntity(sub, "Hard", "What is a topological sort?", "Sorting by value", "Ordering of nodes in a DAG", "BFS traversal", "None", 1));
    }

    private static void addAlgorithmQuestions(List<QuestionEntity> q) {
        String sub = "Algorithms";
        // EASY (1-20)
        q.add(new QuestionEntity(sub, "Easy", "Complexity of Bubble Sort worst case?", "O(n)", "O(n log n)", "O(n^2)", "O(1)", 2));
        q.add(new QuestionEntity(sub, "Easy", "Binary search requires _____ array.", "Unsorted", "Sorted", "Large", "Empty", 1));
        q.add(new QuestionEntity(sub, "Easy", "Algorithm using Divide and Conquer?", "Bubble Sort", "Merge Sort", "Insertion Sort", "Selection Sort", 1));
        q.add(new QuestionEntity(sub, "Easy", "What is an algorithm?", "A programming language", "A step-by-step procedure to solve a problem", "A hardware component", "None", 1));
        q.add(new QuestionEntity(sub, "Easy", "What is linear search?", "Searching in sorted array", "Checking each element sequentially", "Binary search", "None", 1));
        q.add(new QuestionEntity(sub, "Easy", "Complexity of Linear Search?", "O(1)", "O(n)", "O(log n)", "O(n^2)", 1));
        q.add(new QuestionEntity(sub, "Easy", "Which sorting algorithm is simplest?", "Quick Sort", "Bubble Sort", "Merge Sort", "Heap Sort", 1));
        q.add(new QuestionEntity(sub, "Easy", "What is the purpose of sorting?", "To hide data", "To arrange data in a particular order", "To delete data", "None", 1));
        q.add(new QuestionEntity(sub, "Easy", "What is Big O notation used for?", "Naming variables", "Measuring algorithm efficiency", "Writing comments", "None", 1));
        q.add(new QuestionEntity(sub, "Easy", "Which sorting algorithm uses a pivot?", "Bubble Sort", "Quick Sort", "Insertion Sort", "Merge Sort", 1));
        q.add(new QuestionEntity(sub, "Easy", "What is the base case in recursion?", "The end of the loop", "The condition that stops recursion", "The start of the function", "None", 1));
        q.add(new QuestionEntity(sub, "Easy", "Which algorithm is used to find the shortest path?", "Binary Search", "Dijkstra's", "Bubble Sort", "None", 1));
        q.add(new QuestionEntity(sub, "Easy", "What is a greedy algorithm?", "An algorithm that takes the best local choice", "An algorithm that takes the worst choice", "A random algorithm", "None", 0));
        q.add(new QuestionEntity(sub, "Easy", "What is the complexity of an empty loop?", "O(1)", "O(n)", "O(0)", "None", 0));
        q.add(new QuestionEntity(sub, "Easy", "Which algorithm is used for pattern matching?", "KMP", "Bubble Sort", "Dijkstra's", "None", 0));
        q.add(new QuestionEntity(sub, "Easy", "What is a stable sort?", "Sort that keeps relative order of equal elements", "Sort that is fast", "Sort that uses less memory", "None", 0));
        q.add(new QuestionEntity(sub, "Easy", "What is the complexity of accessing an array element?", "O(1)", "O(n)", "O(log n)", "None", 0));
        q.add(new QuestionEntity(sub, "Easy", "What is a brute force approach?", "The most optimized way", "Trying all possible solutions", "A random guess", "None", 1));
        q.add(new QuestionEntity(sub, "Easy", "Which algorithm is used for graph traversal?", "BFS", "DFS", "Both", "None", 2));
        q.add(new QuestionEntity(sub, "Easy", "What is the complexity of Binary Search in the best case?", "O(1)", "O(log n)", "O(n)", "None", 0));

        // MEDIUM (21-40)
        q.add(new QuestionEntity(sub, "Medium", "Dijkstra's algorithm is for?", "Sorting", "Shortest Path", "Searching", "Encryption", 1));
        q.add(new QuestionEntity(sub, "Medium", "Complexity of Quick Sort (avg)?", "O(n)", "O(n log n)", "O(n^2)", "O(log n)", 1));
        q.add(new QuestionEntity(sub, "Medium", "Which is a greedy algorithm?", "Merge Sort", "Huffman Coding", "Quick Sort", "DFS", 1));
        q.add(new QuestionEntity(sub, "Medium", "What is dynamic programming?", "A way to write code fast", "Breaking problem into subproblems and storing results", "A hardware feature", "None", 1));
        q.add(new QuestionEntity(sub, "Medium", "Complexity of Merge Sort?", "O(n)", "O(n log n)", "O(n^2)", "O(1)", 1));
        q.add(new QuestionEntity(sub, "Medium", "What is the Knapsack problem?", "An optimization problem", "A sorting problem", "A searching problem", "None", 0));
        q.add(new QuestionEntity(sub, "Medium", "Which algorithm is used for finding MST?", "Kruskal's", "Prim's", "Both", "None", 2));
        q.add(new QuestionEntity(sub, "Medium", "Complexity of Insertion Sort in worst case?", "O(n)", "O(n log n)", "O(n^2)", "O(1)", 2));
        q.add(new QuestionEntity(sub, "Medium", "What is backtracking?", "Going back in history", "Trying solutions and undoing if they fail", "A loop", "None", 1));
        q.add(new QuestionEntity(sub, "Medium", "Complexity of Selection Sort?", "O(n^2)", "O(n log n)", "O(n)", "O(1)", 0));
        q.add(new QuestionEntity(sub, "Medium", "What is a hash collision?", "Two keys mapping to same hash", "A database error", "A crash", "None", 0));
        q.add(new QuestionEntity(sub, "Medium", "Which algorithm is used for matrix multiplication?", "Strassen's", "Bubble Sort", "Binary Search", "None", 0));
        q.add(new QuestionEntity(sub, "Medium", "What is the complexity of DFS?", "O(V+E)", "O(V^2)", "O(log V)", "None", 0));
        q.add(new QuestionEntity(sub, "Medium", "What is the complexity of BFS?", "O(V+E)", "O(V^2)", "O(log V)", "None", 0));
        q.add(new QuestionEntity(sub, "Medium", "What is the Traveling Salesperson Problem?", "Finding the shortest route", "Sorting cities", "Searching for a path", "None", 0));
        q.add(new QuestionEntity(sub, "Medium", "What is the complexity of Huffman Coding?", "O(n log n)", "O(n)", "O(1)", "None", 0));
        q.add(new QuestionEntity(sub, "Medium", "What is the purpose of the Bellman-Ford algorithm?", "Shortest path with negative weights", "Fast sorting", "Searching", "None", 0));
        q.add(new QuestionEntity(sub, "Medium", "What is a DAG?", "Directed Acyclic Graph", "Database Access Group", "Digital Array Graph", "None", 0));
        q.add(new QuestionEntity(sub, "Medium", "Which algorithm is used for finding strongly connected components?", "Tarjan's", "Kosaraju's", "Both", "None", 2));
        q.add(new QuestionEntity(sub, "Medium", "What is the complexity of the Floyd-Warshall algorithm?", "O(V^3)", "O(V^2)", "O(E log V)", "None", 0));

        // HARD (41-60)
        q.add(new QuestionEntity(sub, "Hard", "Which sort is stable?", "Quick Sort", "Merge Sort", "Heap Sort", "Selection Sort", 1));
        q.add(new QuestionEntity(sub, "Hard", "DFS uses which data structure?", "Queue", "Stack", "Array", "Linked List", 1));
        q.add(new QuestionEntity(sub, "Hard", "Find Minimum Spanning Tree using?", "Dijkstra", "Kruskal", "Binary Search", "DFS", 1));
        q.add(new QuestionEntity(sub, "Hard", "Dynamic Programming example?", "Linear Search", "Fibonacci Series", "Bubble Sort", "Quick Sort", 1));
        q.add(new QuestionEntity(sub, "Hard", "What is NP-completeness?", "Class of problems solvable in polynomial time", "Class of hardest problems in NP", "Hardware design", "None", 1));
        q.add(new QuestionEntity(sub, "Hard", "What is the Boyer-Moore algorithm used for?", "String searching", "Sorting", "Graph traversal", "None", 0));
        q.add(new QuestionEntity(sub, "Hard", "What is the A* search algorithm?", "A pathfinding algorithm", "A sorting algorithm", "A searching algorithm", "None", 0));
        q.add(new QuestionEntity(sub, "Hard", "What is a Randomized Algorithm?", "An algorithm that uses random numbers", "A guess", "A slow algorithm", "None", 0));
        q.add(new QuestionEntity(sub, "Hard", "What is the Ford-Fulkerson algorithm for?", "Maximum flow", "Shortest path", "MST", "None", 0));
        q.add(new QuestionEntity(sub, "Hard", "What is the complexity of the KMP algorithm?", "O(n+m)", "O(n*m)", "O(log n)", "None", 0));
        q.add(new QuestionEntity(sub, "Hard", "What is the purpose of the Sieve of Eratosthenes?", "Finding prime numbers", "Sorting", "Searching", "None", 0));
        q.add(new QuestionEntity(sub, "Hard", "What is the complexity of Heapsort?", "O(n log n)", "O(n^2)", "O(n)", "None", 0));
        q.add(new QuestionEntity(sub, "Hard", "What is bit manipulation?", "Using bits for optimization", "Software testing", "Hardware design", "None", 0));
        q.add(new QuestionEntity(sub, "Hard", "What is the complexity of the Rabin-Karp algorithm?", "O(n+m) average", "O(n*m) worst", "Both", "None", 2));
        q.add(new QuestionEntity(sub, "Hard", "What is the Master Theorem used for?", "Solving recurrences", "Sorting", "Searching", "None", 0));
        q.add(new QuestionEntity(sub, "Hard", "What is a Red-Black Tree search complexity?", "O(log n)", "O(n)", "O(1)", "None", 0));
        q.add(new QuestionEntity(sub, "Hard", "What is the complexity of Radix Sort?", "O(nk)", "O(n log n)", "O(n^2)", "None", 0));
        q.add(new QuestionEntity(sub, "Hard", "What is the Longest Common Subsequence problem?", "Finding the longest string in a list", "Finding the longest shared subsequence", "A sorting problem", "None", 1));
        q.add(new QuestionEntity(sub, "Hard", "What is the complexity of a balanced BST search?", "O(log n)", "O(n)", "O(1)", "None", 0));
        q.add(new QuestionEntity(sub, "Hard", "What is the difference between Kruskal's and Prim's?", "Kruskal's uses edges, Prim's uses vertices", "No difference", "Kruskal's is for shortest path", "None", 0));
    }

    private static void addMLQuestions(List<QuestionEntity> q) {
        String sub = "Machine Learning";
        // EASY (1-20)
        q.add(new QuestionEntity(sub, "Easy", "Learning from labeled data is?", "Unsupervised", "Supervised", "Reinforcement", "Clustering", 1));
        q.add(new QuestionEntity(sub, "Easy", "What does 'K' in K-Means stand for?", "Kernels", "Clusters", "Constant", "Key", 1));
        q.add(new QuestionEntity(sub, "Easy", "Basic unit of Neural Network?", "Neuron", "Kernel", "Cluster", "Layer", 0));
        q.add(new QuestionEntity(sub, "Easy", "What is Machine Learning?", "AI that learns from data", "A type of hardware", "A database", "None", 0));
        q.add(new QuestionEntity(sub, "Easy", "What is a dataset?", "A collection of data", "A software", "A computer", "None", 0));
        q.add(new QuestionEntity(sub, "Easy", "What is a feature in ML?", "An individual property or characteristic", "A bug", "A result", "None", 0));
        q.add(new QuestionEntity(sub, "Easy", "What is a label?", "The output we want to predict", "A name", "A feature", "None", 0));
        q.add(new QuestionEntity(sub, "Easy", "What is training data?", "Data used to teach the model", "Data used to test the model", "Fake data", "None", 0));
        q.add(new QuestionEntity(sub, "Easy", "What is test data?", "Data used to evaluate the model", "Data used to teach the model", "None", "None", 0));
        q.add(new QuestionEntity(sub, "Easy", "What is an algorithm in ML?", "A procedure for learning", "A computer", "A file", "None", 0));
        q.add(new QuestionEntity(sub, "Easy", "What is a model in ML?", "A mathematical representation of a real-world process", "A physical object", "A person", "None", 0));
        q.add(new QuestionEntity(sub, "Easy", "What is classification?", "Predicting a category", "Predicting a continuous value", "Grouping data", "None", 0));
        q.add(new QuestionEntity(sub, "Easy", "What is regression?", "Predicting a continuous value", "Predicting a category", "Grouping data", "None", 0));
        q.add(new QuestionEntity(sub, "Easy", "What is clustering?", "Grouping similar items", "Predicting values", "Sorting data", "None", 0));
        q.add(new QuestionEntity(sub, "Easy", "What is over-fitting?", "Model learning training data too well", "Model not learning enough", "A good model", "None", 0));
        q.add(new QuestionEntity(sub, "Easy", "What is under-fitting?", "Model not learning enough", "Model learning too well", "A good model", "None", 0));
        q.add(new QuestionEntity(sub, "Easy", "What is an epoch?", "One pass through the entire dataset", "A year", "A mistake", "None", 0));
        q.add(new QuestionEntity(sub, "Easy", "What is bias?", "Systematic error", "Random error", "A feature", "None", 0));
        q.add(new QuestionEntity(sub, "Easy", "What is variance?", "Error from sensitivity to fluctuations", "Bias", "A result", "None", 0));
        q.add(new QuestionEntity(sub, "Easy", "What is a hyper-parameter?", "A parameter set before training", "A result", "A feature", "None", 0));

        // MEDIUM (21-40)
        q.add(new QuestionEntity(sub, "Medium", "Algorithm for classification?", "Linear Regression", "K-Means", "Logistic Regression", "Apriori", 2));
        q.add(new QuestionEntity(sub, "Medium", "Used for Dimensionality Reduction?", "PCA", "CNN", "RNN", "SVM", 0));
        q.add(new QuestionEntity(sub, "Medium", "Overfitting means high _____?", "Bias", "Variance", "Precision", "Recall", 1));
        q.add(new QuestionEntity(sub, "Medium", "What is a Random Forest?", "An ensemble of decision trees", "A single tree", "A neural network", "None", 0));
        q.add(new QuestionEntity(sub, "Medium", "What is Gradient Descent?", "An optimization algorithm", "A sorting algorithm", "A searching algorithm", "None", 0));
        q.add(new QuestionEntity(sub, "Medium", "What is a Support Vector Machine?", "A classification algorithm", "A hardware device", "A database", "None", 0));
        q.add(new QuestionEntity(sub, "Medium", "What is cross-validation?", "A way to evaluate model performance", "A type of training", "A mistake", "None", 0));
        q.add(new QuestionEntity(sub, "Medium", "What is a confusion matrix?", "A table to describe performance", "A mistake", "A data structure", "None", 0));
        q.add(new QuestionEntity(sub, "Medium", "What is precision?", "Ratio of true positives to total positives", "Accuracy", "Recall", "None", 0));
        q.add(new QuestionEntity(sub, "Medium", "What is recall?", "Ratio of true positives to actual positives", "Accuracy", "Precision", "None", 0));
        q.add(new QuestionEntity(sub, "Medium", "What is an F1 score?", "Harmonic mean of precision and recall", "Average", "Accuracy", "None", 0));
        q.add(new QuestionEntity(sub, "Medium", "What is regularization?", "Preventing overfitting", "Normalizing data", "A loop", "None", 0));
        q.add(new QuestionEntity(sub, "Medium", "What is PCA?", "Principal Component Analysis", "Personal Computer Agent", "Process Control Action", "None", 0));
        q.add(new QuestionEntity(sub, "Medium", "What is a decision tree?", "A tree-like model for decisions", "A database", "A hardware", "None", 0));
        q.add(new QuestionEntity(sub, "Medium", "What is k-Nearest Neighbors?", "A classification algorithm based on distance", "A sorting algorithm", "A search", "None", 0));
        q.add(new QuestionEntity(sub, "Medium", "What is Naive Bayes?", "A probabilistic classifier", "A search", "A sorting", "None", 0));
        q.add(new QuestionEntity(sub, "Medium", "What is a neural network?", "A system modeled after the brain", "A computer network", "A file system", "None", 0));
        q.add(new QuestionEntity(sub, "Medium", "What is deep learning?", "ML using multi-layered neural networks", "Deep search", "Hard coding", "None", 0));
        q.add(new QuestionEntity(sub, "Medium", "What is a kernel in SVM?", "A function to transform data", "A hardware part", "A bug", "None", 0));
        q.add(new QuestionEntity(sub, "Medium", "What is a learning rate?", "Size of steps in gradient descent", "Speed of training", "A feature", "None", 0));

        // HARD (41-60)
        q.add(new QuestionEntity(sub, "Hard", "Underfitting means high _____?", "Bias", "Variance", "Accuracy", "Error", 0));
        q.add(new QuestionEntity(sub, "Hard", "Common metric for Regression?", "Accuracy", "F1-Score", "MSE", "Precision", 2));
        q.add(new QuestionEntity(sub, "Hard", "Used for Image Processing?", "RNN", "CNN", "SVM", "Linear Reg", 1));
        q.add(new QuestionEntity(sub, "Hard", "NLP model example?", "LSTM", "K-Means", "PCA", "Dijkstra", 0));
        q.add(new QuestionEntity(sub, "Hard", "What is backpropagation?", "Updating weights in a neural network", "A reverse loop", "A data structure", "None", 0));
        q.add(new QuestionEntity(sub, "Hard", "What is a convolutional layer?", "A layer that extracts features from images", "A sorting layer", "A memory layer", "None", 0));
        q.add(new QuestionEntity(sub, "Hard", "What is an RNN?", "Recurrent Neural Network", "Random Network", "Recursive Network", "None", 0));
        q.add(new QuestionEntity(sub, "Hard", "What is reinforcement learning?", "Learning by trial and error with rewards", "Supervised learning", "Clustering", "None", 0));
        q.add(new QuestionEntity(sub, "Hard", "What is a GAN?", "Generative Adversarial Network", "Graph Array Network", "Group Action Network", "None", 0));
        q.add(new QuestionEntity(sub, "Hard", "What is transfer learning?", "Using a pre-trained model", "Moving data", "Updating software", "None", 0));
        q.add(new QuestionEntity(sub, "Hard", "What is an autoencoder?", "A neural network for data compression", "A file encoder", "A hardware part", "None", 0));
        q.add(new QuestionEntity(sub, "Hard", "What is a transformer model?", "A model for sequence-to-sequence tasks", "A car", "A power part", "None", 0));
        q.add(new QuestionEntity(sub, "Hard", "What is the vanishing gradient problem?", "Gradients becoming very small", "A bug", "A fast training", "None", 0));
        q.add(new QuestionEntity(sub, "Hard", "What is batch normalization?", "Normalizing layer inputs", "Cleaning data", "A loop", "None", 0));
        q.add(new QuestionEntity(sub, "Hard", "What is dropout?", "Ignoring neurons during training to prevent overfitting", "Stopping school", "A bug", "None", 0));
        q.add(new QuestionEntity(sub, "Hard", "What is an activation function?", "A function that determines output of a neuron", "A start button", "A file", "None", 0));
        q.add(new QuestionEntity(sub, "Hard", "What is the curse of dimensionality?", "Problems with high-dimensional data", "A myth", "A hardware limit", "None", 0));
        q.add(new QuestionEntity(sub, "Hard", "What is stochastic gradient descent?", "GD using random subsets", "Fast GD", "Slow GD", "None", 0));
        q.add(new QuestionEntity(sub, "Hard", "What is a support vector?", "Data points closest to the hyperplane", "A helper", "A bug", "None", 0));
        q.add(new QuestionEntity(sub, "Hard", "What is ensemble learning?", "Combining multiple models", "A single model", "Learning together", "None", 0));
    }

    private static void addCloudQuestions(List<QuestionEntity> q) {
        String sub = "Cloud Computing";
        // EASY (1-20)
        q.add(new QuestionEntity(sub, "Easy", "AWS stands for?", "Amazon Web Services", "Alpha Web Site", "Advanced Web System", "None", 0));
        q.add(new QuestionEntity(sub, "Easy", "Example of SaaS?", "AWS EC2", "Azure SQL", "Google Docs", "Heroku", 2));
        q.add(new QuestionEntity(sub, "Easy", "Azure is a product of?", "Google", "Amazon", "Microsoft", "IBM", 2));
        q.add(new QuestionEntity(sub, "Easy", "What is cloud computing?", "On-demand delivery of IT resources over internet", "A physical cloud", "Local storage", "None", 0));
        q.add(new QuestionEntity(sub, "Easy", "What is IaaS?", "Infrastructure as a Service", "Internet as a Service", "Instance as a Service", "None", 0));
        q.add(new QuestionEntity(sub, "Easy", "What is PaaS?", "Platform as a Service", "Public as a Service", "Private as a Service", "None", 0));
        q.add(new QuestionEntity(sub, "Easy", "What is a public cloud?", "Cloud services over public internet", "A cloud for the government", "A physical cloud", "None", 0));
        q.add(new QuestionEntity(sub, "Easy", "What is a private cloud?", "Cloud used by a single organization", "A cloud with a password", "Local storage", "None", 0));
        q.add(new QuestionEntity(sub, "Easy", "What is a hybrid cloud?", "Combination of public and private clouds", "A fast cloud", "A mix of hardware", "None", 0));
        q.add(new QuestionEntity(sub, "Easy", "What is scalability?", "Ability to handle growth", "Weight", "Speed", "None", 0));
        q.add(new QuestionEntity(sub, "Easy", "What is elasticity?", "Ability to scale up or down automatically", "Flexibility", "Stretching", "None", 0));
        q.add(new QuestionEntity(sub, "Easy", "What is a cloud region?", "A geographical area with data centers", "A country", "A city", "None", 0));
        q.add(new QuestionEntity(sub, "Easy", "What is an availability zone?", "Isolated location within a region", "A time zone", "A safe place", "None", 0));
        q.add(new QuestionEntity(sub, "Easy", "What is cloud storage?", "Storing data on remote servers", "Local hard drive", "RAM", "None", 0));
        q.add(new QuestionEntity(sub, "Easy", "What is a virtual machine?", "Software computer", "Physical hardware", "A robot", "None", 0));
        q.add(new QuestionEntity(sub, "Easy", "What is an instance?", "A virtual server", "A second", "A file", "None", 0));
        q.add(new QuestionEntity(sub, "Easy", "What is cloud migration?", "Moving data to cloud", "Moving birds", "Updating OS", "None", 0));
        q.add(new QuestionEntity(sub, "Easy", "What is a cloud provider?", "Company that offers cloud services", "An ISP", "A hardware maker", "None", 0));
        q.add(new QuestionEntity(sub, "Easy", "What is AWS S3?", "Storage service", "Computing service", "Database service", "None", 0));
        q.add(new QuestionEntity(sub, "Easy", "What is Google Cloud Platform?", "Cloud services by Google", "A search engine", "An Android OS", "None", 0));

        // MEDIUM (21-40)
        q.add(new QuestionEntity(sub, "Medium", "Model where user manages OS?", "IaaS", "PaaS", "SaaS", "BaaS", 0));
        q.add(new QuestionEntity(sub, "Medium", "Google Cloud Storage is which service?", "Storage", "Compute", "Networking", "Database", 0));
        q.add(new QuestionEntity(sub, "Medium", "Pay only for what you use is _____?", "On-premise", "Pay-as-you-go", "Fixed-cost", "Subscription", 1));
        q.add(new QuestionEntity(sub, "Medium", "What is a Content Delivery Network?", "Network of servers to deliver content faster", "A TV station", "A post office", "None", 0));
        q.add(new QuestionEntity(sub, "Medium", "What is serverless computing?", "Running code without managing servers", "A computer with no CPU", "Cloud with no power", "None", 0));
        q.add(new QuestionEntity(sub, "Medium", "What is a container?", "Lightweight, portable software unit", "A physical box", "A database", "None", 0));
        q.add(new QuestionEntity(sub, "Medium", "What is Kubernetes?", "An orchestration system for containers", "A cloud provider", "A hardware device", "None", 0));
        q.add(new QuestionEntity(sub, "Medium", "What is a VPC?", "Virtual Private Cloud", "Very Private Computer", "Virtual Public Cloud", "None", 0));
        q.add(new QuestionEntity(sub, "Medium", "What is auto-scaling?", "Automatically adjusting resources", "A weight", "A zoom feature", "None", 0));
        q.add(new QuestionEntity(sub, "Medium", "What is fault tolerance?", "System continues to operate during failure", "Patience", "Error handling", "None", 0));
        q.add(new QuestionEntity(sub, "Medium", "What is disaster recovery?", "Plan to restore data after failure", "A backup", "A crash", "None", 0));
        q.add(new QuestionEntity(sub, "Medium", "What is cloud security?", "Protecting data and infrastructure", "A firewall", "A password", "None", 0));
        q.add(new QuestionEntity(sub, "Medium", "What is identity and access management?", "Controlling who can access resources", "A login", "A badge", "None", 0));
        q.add(new QuestionEntity(sub, "Medium", "What is multi-tenancy?", "Sharing resources among multiple users", "Having many tenants", "A building", "None", 0));
        q.add(new QuestionEntity(sub, "Medium", "What is a load balancer?", "Distributing traffic across servers", "A scale", "A battery", "None", 0));
        q.add(new QuestionEntity(sub, "Medium", "What is edge computing?", "Processing data closer to the source", "Computing at the limit", "A sharp computer", "None", 0));
        q.add(new QuestionEntity(sub, "Medium", "What is a snapshot?", "A point-in-time copy of data", "A photo", "A quick look", "None", 0));
        q.add(new QuestionEntity(sub, "Medium", "What is the difference between S3 and EBS?", "S3 is object storage, EBS is block storage", "No difference", "S3 is faster", "None", 0));
        q.add(new QuestionEntity(sub, "Medium", "What is the purpose of AWS Lambda?", "Run code without servers", "Store data", "Networking", "None", 0));
        q.add(new QuestionEntity(sub, "Medium", "What is Azure DevOps?", "Services for software development lifecycle", "A database", "A hardware part", "None", 0));

        // HARD (41-60)
        q.add(new QuestionEntity(sub, "Hard", "Large network of proxy servers?", "VPN", "CDN", "VPC", "DNS", 1));
        q.add(new QuestionEntity(sub, "Hard", "Serverless computing example?", "AWS Lambda", "EC2", "S3", "RDS", 0));
        q.add(new QuestionEntity(sub, "Hard", "Sharing physical resources is _____?", "Virtualization", "Containerization", "Scaling", "Caching", 0));
        q.add(new QuestionEntity(sub, "Hard", "Private network in cloud?", "VPN", "VPC", "HTTP", "LAN", 1));
        q.add(new QuestionEntity(sub, "Hard", "What is high availability?", "System is operational for long periods", "Very expensive", "Fast", "None", 0));
        q.add(new QuestionEntity(sub, "Hard", "What is a cloud-native application?", "App built specifically for cloud", "Local app", "Fake app", "None", 0));
        q.add(new QuestionEntity(sub, "Hard", "What is micro-services architecture?", "App composed of small, independent services", "A single app", "Hardware part", "None", 0));
        q.add(new QuestionEntity(sub, "Hard", "What is the shared responsibility model?", "Security shared between provider and customer", "Sharing files", "A community", "None", 0));
        q.add(new QuestionEntity(sub, "Hard", "What is a data lake?", "Large repository of raw data", "A pond", "A database", "None", 0));
        q.add(new QuestionEntity(sub, "Hard", "What is cold storage?", "Storage for rarely accessed data", "Frozen hardware", "A fridge", "None", 0));
        q.add(new QuestionEntity(sub, "Hard", "What is an API gateway?", "Management tool for APIs", "A door", "A network", "None", 0));
        q.add(new QuestionEntity(sub, "Hard", "What is cloud governance?", "Rules for using cloud services", "Government cloud", "A king", "None", 0));
        q.add(new QuestionEntity(sub, "Hard", "What is a managed service?", "Service managed by the provider", "A trained person", "A hardware", "None", 0));
        q.add(new QuestionEntity(sub, "Hard", "What is AWS Redshift?", "Data warehousing service", "Storage", "Compute", "None", 0));
        q.add(new QuestionEntity(sub, "Hard", "What is Google BigQuery?", "Serverless data warehouse", "Search engine", "Android app", "None", 0));
        q.add(new QuestionEntity(sub, "Hard", "What is infrastructure as code?", "Managing infrastructure through code", "Writing programs", "Hardware design", "None", 0));
        q.add(new QuestionEntity(sub, "Hard", "What is a service level agreement (SLA)?", "Contract for service performance", "A handshake", "A law", "None", 0));
        q.add(new QuestionEntity(sub, "Hard", "What is compliance in cloud?", "Adhering to laws and regulations", "Patience", "Agreeing", "None", 0));
        q.add(new QuestionEntity(sub, "Hard", "What is the purpose of an IAM role?", "Assign permissions to entities", "A password", "A user", "None", 0));
        q.add(new QuestionEntity(sub, "Hard", "What is a blue-green deployment?", "Deploying using two identical environments", "Colorful app", "A bug", "None", 0));
    }
}
