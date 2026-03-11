package com.example.techvidyaaa.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.techvidyaaa.R;
import com.example.techvidyaaa.databinding.FragmentSlidesBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SlidesFragment extends Fragment {

    private FragmentSlidesBinding binding;
    private DatabaseReference mDatabase;
    private String subjectName;
    private final List<Slide> slideList = new ArrayList<>();
    private int currentSlideIndex = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSlidesBinding.inflate(inflater, container, false);
        if (getArguments() != null) {
            subjectName = getArguments().getString("subjectName");
        } else {
            subjectName = "C Programming";
        }
        mDatabase = FirebaseDatabase.getInstance().getReference("slides").child(subjectName);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.tvSlidesSubject.setText(subjectName + " Study Slides");

        loadSlides();

        binding.btnNextSlide.setOnClickListener(v -> {
            if (!slideList.isEmpty() && currentSlideIndex < slideList.size() - 1) {
                currentSlideIndex++;
                displaySlide();
            } else if (!slideList.isEmpty()) {
                if (isAdded()) requireActivity().onBackPressed();
            }
        });

        binding.btnPrevSlide.setOnClickListener(v -> {
            if (currentSlideIndex > 0) {
                currentSlideIndex--;
                displaySlide();
            }
        });
    }

    private void loadSlides() {
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (binding == null) return;
                slideList.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        Slide slide = data.getValue(Slide.class);
                        if (slide != null) slideList.add(slide);
                    }
                }
                
                if (slideList.isEmpty()) {
                    generateCdnContent();
                }
                displaySlide();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if (binding == null) return;
                if (slideList.isEmpty()) {
                    generateCdnContent();
                }
                displaySlide();
            }
        });
    }

    private void generateCdnContent() {
        // Comprehensive technical concepts with creative CDN imagery for ALL languages/topics
        String name = (subjectName != null) ? subjectName.trim().toLowerCase() : "";

        if (name.contains("c programming")) {
            slideList.add(new Slide("1. Introduction to C", "The 'Mother of Languages' developed in 1972 at Bell Labs.", "https://images.unsplash.com/photo-1461749280684-dccba630e2f6?w=700&q=80"));
            slideList.add(new Slide("2. Compilation Flowchart", "Source Code -> Compiler -> Linker -> Executable.", "https://images.unsplash.com/photo-1516116216624-53e697fedbea?w=700&q=80")); 
            slideList.add(new Slide("3. Memory Management", "Direct control over memory using pointers and malloc/free.", "https://images.unsplash.com/photo-1558494949-ef010cbdcc51?w=700&q=80")); 
            slideList.add(new Slide("4. Data Structures in C", "Arrays, Strings, and Structs define memory organization.", "https://images.unsplash.com/photo-1515879218367-8466d910aaa4?w=700&q=80"));
        } else if (name.contains("c++")) {
            slideList.add(new Slide("1. Intro to C++", "Superset of C adding classes and objects.", "https://images.unsplash.com/photo-1627398242454-45a1465c2479?w=700&q=80"));
            slideList.add(new Slide("2. OOP Principles", "Abstraction, Encapsulation, Inheritance, and Polymorphism.", "https://images.unsplash.com/photo-1504639725590-34d0984388bd?w=700&q=80")); 
            slideList.add(new Slide("3. STL Magic", "Vectors, Sets, and Maps provide highly efficient algorithms.", "https://images.unsplash.com/photo-1581091226825-a6a2a5aee158?w=700&q=80")); 
            slideList.add(new Slide("4. Memory & References", "Smart pointers and references for modern safe coding.", "https://images.unsplash.com/photo-1544197150-b99a580bb7a8?w=700&q=80"));
        } else if (name.contains("java")) {
            slideList.add(new Slide("1. Java Basics", "Write Once, Run Anywhere via the Java Virtual Machine.", "https://images.unsplash.com/photo-1517694712202-14dd9538aa97?w=700&q=80"));
            slideList.add(new Slide("2. JVM Architecture", "Bytecode execution through a secure virtual environment.", "https://images.unsplash.com/photo-1518770660439-4636190af475?w=700&q=80")); 
            slideList.add(new Slide("3. Multi-threading", "Concurrent execution for high-performance applications.", "https://images.unsplash.com/photo-1507238691740-187a5b1d37b8?w=700&q=80")); 
            slideList.add(new Slide("4. Java Frameworks", "Spring Boot and Hibernate powering enterprise web.", "https://images.unsplash.com/photo-1504639725590-34d0984388bd?w=700&q=80"));
        } else if (name.contains("python")) {
            slideList.add(new Slide("1. Python Simplicity", "Highly readable syntax designed for developer speed.", "https://images.unsplash.com/photo-1526374965328-7f61d4dc18c5?w=700&q=80"));
            slideList.add(new Slide("2. Data Science", "Powering AI with NumPy, Pandas, and TensorFlow.", "https://images.unsplash.com/photo-1551288049-bbbda536339a?w=700&q=80")); 
            slideList.add(new Slide("3. Automation", "Scripts for web scraping, devops, and rapid testing.", "https://images.unsplash.com/photo-1555949963-ff9fe0c870eb?w=700&q=80")); 
            slideList.add(new Slide("4. Machine Learning", "Training neural networks and predictive models.", "https://images.unsplash.com/photo-1527477396000-e27163b481c2?w=700&q=80"));
        } else if (name.contains("database") || name.contains("dbms")) {
            slideList.add(new Slide("1. ACID Properties", "Atomicity, Consistency, Isolation, and Durability.", "https://images.unsplash.com/photo-1544383835-bda2bc66a55d?w=700&q=80")); 
            slideList.add(new Slide("2. ER Flowchart", "Mapping relationships between data entities logically.", "https://images.unsplash.com/photo-1507925921958-8a62f3d1a50d?w=700&q=80")); 
            slideList.add(new Slide("3. Query Performance", "Indexing and optimization for lightning-fast retrieval.", "https://images.unsplash.com/photo-1555066931-4365d14bab8c?w=700&q=80"));
        } else if (name.contains("algorithm") || name.contains("ds") || name.contains("structure")) {
            slideList.add(new Slide("1. Logical Foundations", "Efficient ways to organize and process information.", "https://images.unsplash.com/photo-1504639725590-34d0984388bd?w=700&q=80")); 
            slideList.add(new Slide("2. Sorting & Complexity", "Big O notation and finding the optimal path.", "https://images.unsplash.com/photo-1509228468518-180dd4864904?w=700&q=80")); 
            slideList.add(new Slide("3. Tree & Graph Theory", "Modeling hierarchies and interconnected networks.", "https://images.unsplash.com/photo-1516116216624-53e697fedbea?w=700&q=80"));
        } else if (name.contains("web")) {
            slideList.add(new Slide("1. Modern Web Stack", "Building responsive UIs with HTML, CSS, and JS.", "https://images.unsplash.com/photo-1547658719-da2b51169166?w=700&q=80")); 
            slideList.add(new Slide("2. Backend Flow", "RESTful APIs and server-side logic in the cloud.", "https://images.unsplash.com/photo-1488590528505-98d2b5aba04b?w=700&q=80"));
            slideList.add(new Slide("3. UX Design", "Crafting intuitive digital experiences for users.", "https://images.unsplash.com/photo-1522542550221-31fd19575a2d?w=700&q=80"));
        } else if (name.contains("cloud") || name.contains("cyber")) {
            slideList.add(new Slide("1. Digital Security", "Protecting assets from digital threats and leaks.", "https://images.unsplash.com/photo-1550751827-4bd374c3f58b?w=700&q=80"));
            slideList.add(new Slide("2. Cloud Scaling", "Elastic computing for massive modern applications.", "https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=700&q=80"));
        } else {
            slideList.add(new Slide("1. Overview", "Core principles and industry standards of " + subjectName + ".", "https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=700&q=80"));
            slideList.add(new Slide("2. Deep Dive", "Practical implementation and real-world usage cases.", "https://images.unsplash.com/photo-1519389950473-47ba0277781c?w=700&q=80"));
        }
    }

    private void displaySlide() {
        if (binding == null || slideList.isEmpty()) return;
        
        if (currentSlideIndex >= slideList.size()) currentSlideIndex = slideList.size() - 1;
        Slide currentSlide = slideList.get(currentSlideIndex);

        binding.tvSlideTitle.setText(currentSlide.title);
        binding.tvSlideDescription.setText(currentSlide.description);
        binding.tvSlideCounter.setText((currentSlideIndex + 1) + " / " + slideList.size());

        // Load creative CDN image
        if (currentSlide.imageUrl != null && !currentSlide.imageUrl.isEmpty()) {
            Glide.with(this)
                .load(currentSlide.imageUrl)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_practice)
                .into(binding.ivSlideImage);
        } else {
            binding.ivSlideImage.setImageResource(R.drawable.ic_practice);
        }

        binding.btnPrevSlide.setVisibility(currentSlideIndex > 0 ? View.VISIBLE : View.INVISIBLE);
        binding.btnNextSlide.setText(currentSlideIndex == slideList.size() - 1 ? "Finish" : "Next");
    }

    public static class Slide {
        public String title, description, imageUrl;
        public Slide() {}
        public Slide(String t, String d, String i) { 
            this.title = t; 
            this.description = d; 
            this.imageUrl = i;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
