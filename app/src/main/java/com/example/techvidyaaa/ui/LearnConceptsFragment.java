package com.example.techvidyaaa.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.techvidyaaa.databinding.FragmentLearnConceptsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LearnConceptsFragment extends Fragment {

    private FragmentLearnConceptsBinding binding;
    private DatabaseReference mDatabase;
    private String subjectName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLearnConceptsBinding.inflate(inflater, container, false);
        if (getArguments() != null) {
            subjectName = getArguments().getString("subjectName");
        }
        mDatabase = FirebaseDatabase.getInstance().getReference("concepts").child(subjectName);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.tvConceptSubject.setText(subjectName + " Fundamentals");
        
        loadConceptContent();
    }

    private void loadConceptContent() {
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String content = snapshot.getValue(String.class);
                    binding.tvConceptContent.setText(content);
                } else {
                    generateCdnConcept();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                generateCdnConcept();
            }
        });
    }

    private void generateCdnConcept() {
        StringBuilder sb = new StringBuilder();
        if ("C Programming".equalsIgnoreCase(subjectName)) {
            sb.append("C is a powerful general-purpose programming language. It is fast, portable and available in all platforms.\n\n");
            sb.append("Key Concepts:\n");
            sb.append("1. Tokens: Keywords, Identifiers, Constants, Strings, Operators.\n");
            sb.append("2. Data Types: Basic (int, char, float), Derived (Array, Pointer, Structure), Enumeration (enum), Void.\n");
            sb.append("3. Memory Management: Static and Dynamic memory allocation using malloc(), calloc(), free().\n");
            sb.append("4. Preprocessor: #include, #define for macro and file inclusion.");
        } else if ("Python".equalsIgnoreCase(subjectName)) {
            sb.append("Python is a high-level, interpreted programming language known for its readability and versatility.\n\n");
            sb.append("Key Concepts:\n");
            sb.append("1. Dynamic Typing: Variables don't need explicit declaration.\n");
            sb.append("2. List Comprehensions: Elegant way to create lists.\n");
            sb.append("3. Generators: Efficient memory usage for large data.\n");
            sb.append("4. Decorators: Modify behavior of functions or classes.");
        } else if ("Java".equalsIgnoreCase(subjectName)) {
            sb.append("Java is a popular programming language, created in 1995. It is owned by Oracle, and more than 3 billion devices run Java.\n\n");
            sb.append("Key Concepts:\n");
            sb.append("1. Platform Independence: Bytecode runs on any JVM.\n");
            sb.append("2. Garbage Collection: Automatic memory management.\n");
            sb.append("3. Multithreading: Perform multiple tasks simultaneously.\n");
            sb.append("4. Robustness: Strong memory management and exception handling.");
        } else if ("Data Structures".equalsIgnoreCase(subjectName)) {
            sb.append("A data structure is a specialized format for organizing, processing, retrieving and storing data.\n\n");
            sb.append("Key Structures:\n");
            sb.append("1. Linear: Arrays, Linked Lists, Stacks, Queues.\n");
            sb.append("2. Non-Linear: Trees, Graphs.\n");
            sb.append("3. Hash Tables: Key-value pair mapping for fast lookup.\n");
            sb.append("4. Complexity: Time and Space complexity (Big O notation).");
        } else {
            sb.append("Comprehensive concepts for " + subjectName + " are being curated from the CDN.\n\n");
            sb.append("What to expect:\n");
            sb.append("- Core Principles and Architectural Overview.\n");
            sb.append("- Practical use-cases and industry implementations.\n");
            sb.append("- Advanced optimization techniques.\n");
            sb.append("- Interview preparation points.");
        }
        binding.tvConceptContent.setText(sb.toString());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
