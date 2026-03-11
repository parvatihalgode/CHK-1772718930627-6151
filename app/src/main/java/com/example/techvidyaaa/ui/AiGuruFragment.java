package com.example.techvidyaaa.ui;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.techvidyaaa.R;
import com.example.techvidyaaa.databinding.FragmentAiGuruBinding;

import java.util.ArrayList;
import java.util.List;

public class AiGuruFragment extends Fragment {

    private FragmentAiGuruBinding binding;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> messageList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAiGuruBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        messageList = new ArrayList<>();
        chatAdapter = new ChatAdapter(messageList);
        binding.rvChatMessages.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvChatMessages.setAdapter(chatAdapter);

        binding.btnSendMessage.setOnClickListener(v -> {
            String message = binding.etChatMessage.getText().toString().trim();
            if (!message.isEmpty()) {
                sendMessage(message);
                binding.etChatMessage.setText("");
            }
        });
    }

    private void sendMessage(String userMessage) {
        binding.cardWelcome.setVisibility(View.GONE);
        binding.rvChatMessages.setVisibility(View.VISIBLE);

        messageList.add(new ChatMessage(userMessage, true, null, null, null));
        chatAdapter.notifyItemInserted(messageList.size() - 1);
        binding.rvChatMessages.scrollToPosition(messageList.size() - 1);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            ChatMessage response = getTechnicalResponse(userMessage);
            messageList.add(response);
            chatAdapter.notifyItemInserted(messageList.size() - 1);
            binding.rvChatMessages.scrollToPosition(messageList.size() - 1);
        }, 800);
    }

    private ChatMessage getTechnicalResponse(String query) {
        String q = query.toLowerCase();
        String text;
        String imageUrl = null;
        String btnText = null;
        String resourceUrl = null;

        if (q.contains("hello") || q.contains("hi")) {
            text = "Hello! I am your Technical AI Guru. Ask me about C, Java, Python, DBMS, or Algorithms!";
        } else if (q.contains("c programming") || q.contains("language c") || q.contains("c notes")) {
            text = "C is a powerful procedural language. Mother of modern coding.\n\nLogic: Source -> Compiler -> Executable.";
            imageUrl = "https://images.unsplash.com/photo-1516116216624-53e697fedbea?w=600&q=80";
            btnText = "Download C Complete Notes (CDN)";
            // Updated to a working online PDF for CDN demonstration
            resourceUrl = "https://www.unf.edu/~wkloster/2220/ppts/c_notes.pdf";
        } else if (q.contains("java")) {
            text = "Java is Object-Oriented and Platform Independent.\n\nConcept: Write Once, Run Anywhere via JVM Architecture.";
            imageUrl = "https://images.unsplash.com/photo-1517694712202-14dd9538aa97?w=600&q=80";
            btnText = "Java Technical Docs (CDN)";
            resourceUrl = "https://docs.oracle.com/javase/tutorial/";
        } else if (q.contains("python")) {
            text = "Python is known for its simplicity and power in AI/ML.\n\nFeatures: Libraries like NumPy, Pandas, and TensorFlow.";
            imageUrl = "https://images.unsplash.com/photo-1526374965328-7f61d4dc18c5?w=600&q=80";
            btnText = "Python Cloud Resources";
            resourceUrl = "https://docs.python.org/3/";
        } else if (q.contains("database") || q.contains("dbms") || q.contains("sql")) {
            text = "DBMS manages structured data using ACID properties.\n\nLogic: Tables -> Relationships -> Queries.";
            imageUrl = "https://images.unsplash.com/photo-1544383835-bda2bc66a55d?w=600&q=80";
            btnText = "SQL Concept Guide (Cloud)";
            resourceUrl = "https://www.w3schools.com/sql/";
        } else if (q.contains("algorithm") || q.contains("data structure") || q.contains("ds")) {
            text = "Algorithms are step-by-step procedures for calculations.\n\nComplexity: Measured using Big O notation.";
            imageUrl = "https://images.unsplash.com/photo-1504639725590-34d0984388bd?w=600&q=80";
            btnText = "Visual Algo Guide";
            resourceUrl = "https://visualgo.net/";
        } else if (q.contains("web")) {
            text = "Web Dev covers Frontend (HTML/CSS/JS) and Backend (Node/Java/Python).\n\nFocus: Responsive Design and Scalable APIs.";
            imageUrl = "https://images.unsplash.com/photo-1547658719-da2b51169166?w=700&q=80";
            btnText = "Web Dev Roadmap";
            resourceUrl = "https://developer.mozilla.org/";
        } else {
            text = "That's an interesting technical topic! It involves core software engineering principles.";
            imageUrl = "https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=600&q=80";
        }

        return new ChatMessage(text, false, imageUrl, btnText, resourceUrl);
    }

    public static class ChatMessage {
        public String text;
        public boolean isUser;
        public String imageUrl;
        public String buttonText;
        public String resourceUrl;
        
        public ChatMessage(String text, boolean isUser, String imageUrl, String buttonText, String resourceUrl) { 
            this.text = text; 
            this.isUser = isUser; 
            this.imageUrl = imageUrl;
            this.buttonText = buttonText;
            this.resourceUrl = resourceUrl;
        }
    }

    private class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private static final int TYPE_USER = 1;
        private static final int TYPE_GURU = 2;
        private final List<ChatMessage> messages;

        public ChatAdapter(List<ChatMessage> messages) { this.messages = messages; }

        @Override
        public int getItemViewType(int position) {
            return messages.get(position).isUser ? TYPE_USER : TYPE_GURU;
        }

        @NonNull @Override 
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup p, int viewType) {
            if (viewType == TYPE_USER) {
                View view = LayoutInflater.from(p.getContext()).inflate(R.layout.item_chat_user, p, false);
                return new UserViewHolder(view);
            } else {
                View view = LayoutInflater.from(p.getContext()).inflate(R.layout.item_chat_guru, p, false);
                return new GuruViewHolder(view);
            }
        }

        @Override public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            ChatMessage m = messages.get(position);
            if (holder instanceof UserViewHolder) {
                ((UserViewHolder) holder).tvMessage.setText(m.text);
            } else if (holder instanceof GuruViewHolder) {
                GuruViewHolder gHolder = (GuruViewHolder) holder;
                gHolder.tvMessage.setText(m.text);
                
                if (m.imageUrl != null && !m.imageUrl.isEmpty()) {
                    gHolder.ivImage.setVisibility(View.VISIBLE);
                    Glide.with(AiGuruFragment.this).load(m.imageUrl).centerCrop().into(gHolder.ivImage);
                } else {
                    gHolder.ivImage.setVisibility(View.GONE);
                }

                if (m.buttonText != null && !m.buttonText.isEmpty()) {
                    gHolder.btnAction.setVisibility(View.VISIBLE);
                    gHolder.btnAction.setText(m.buttonText);
                    gHolder.btnAction.setOnClickListener(v -> {
                        try {
                            Uri uri = Uri.parse(m.resourceUrl);
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            // If it's a PDF, we suggest the type to help the system pick the right app
                            if (m.resourceUrl.toLowerCase().endsWith(".pdf")) {
                                intent.setDataAndType(uri, "application/pdf");
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            }
                            startActivity(intent);
                        } catch (Exception e) {
                            // If failed (e.g., no PDF viewer), try a simple web intent
                            try {
                                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(m.resourceUrl));
                                startActivity(webIntent);
                            } catch (Exception ex) {
                                Toast.makeText(getContext(), "Error opening resource", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    startBlinkAnimation(gHolder.btnAction);
                } else {
                    gHolder.btnAction.setVisibility(View.GONE);
                    gHolder.btnAction.clearAnimation();
                }
            }
        }

        private void startBlinkAnimation(View view) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", 1f, 0.4f, 1f);
            animator.setDuration(1000);
            animator.setRepeatCount(ValueAnimator.INFINITE);
            animator.start();
        }

        @Override public int getItemCount() { return messages.size(); }

        class UserViewHolder extends RecyclerView.ViewHolder {
            TextView tvMessage;
            public UserViewHolder(View v) { 
                super(v);
                tvMessage = v.findViewById(R.id.tvUserMessage);
            }
        }

        class GuruViewHolder extends RecyclerView.ViewHolder {
            TextView tvMessage;
            ImageView ivImage;
            Button btnAction;
            public GuruViewHolder(View v) { 
                super(v);
                tvMessage = v.findViewById(R.id.tvGuruMessage);
                ivImage = v.findViewById(R.id.ivGuruImage);
                btnAction = v.findViewById(R.id.btnPdfAction);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
