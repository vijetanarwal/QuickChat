package com.vijet.quickchat.ui.chat

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.vijet.quickchat.R
import com.vijet.quickchat.adapter.ChatAdapter
import com.vijet.quickchat.databinding.ChatFragmentBinding
import com.vijet.quickchat.model.ChatMessage
import com.vijet.quickchat.model.User
import com.vijet.quickchat.utils.Constant
import com.vijet.quickchat.utils.decodeToBitmap
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChatFragment : Fragment(R.layout.chat_fragment) {

    private lateinit var binding: ChatFragmentBinding
    private val viewModel: ChatViewModel by viewModels()
    private lateinit var user: User
    private lateinit var chatAdapter: ChatAdapter

    @Inject
    lateinit var pref: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ChatFragmentBinding.bind(view)

        getArgument()
        setClickListener()
        setRecyclerview()

        binding.tvName.text = user.name
        user.image?.decodeToBitmap()?.let {
            binding.ivUserImage.setImageBitmap(it)
        }

        observeChat()
    }

    private fun getArgument() {
        arguments?.let {
            user = ChatFragmentArgs.fromBundle(it).user
        }
    }

    private fun observeChat() {
        viewModel.eventListener(user.id, object : ChatObserver {
            override fun observeChat(newChat: List<ChatMessage>) {
                if (newChat.isNotEmpty()) {
                    chatAdapter.addMessage(newChat, binding.rvChat)
                }
                binding.pb.visibility = View.GONE
                viewModel.conversionId.isEmpty().let {
                    if (chatAdapter.getMessageSize() != 0) {
                        viewModel.checkForConversation(user.id)
                    }
                }
            }
        })
    }

    private fun setClickListener() {
        binding.ivBack.setOnClickListener { findNavController().popBackStack() }
        binding.ivSend.setOnClickListener {
            if (binding.etMessage.text.isNullOrBlank() && binding.etMessage.text.toString()
                    .trim().length < 0
            )
                return@setOnClickListener
            viewModel.sendMessage(binding.etMessage.text.trim().toString(), user)
            binding.etMessage.text.clear()
        }
    }

    private fun setRecyclerview() {
        chatAdapter = ChatAdapter(
            pref.getString(Constant.KEY_USER_ID, null).toString(),
            emptyList()
        )
        user.image?.decodeToBitmap()?.let {
            chatAdapter.setProfileImage(it)
        }
        binding.rvChat.apply {
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    interface ChatObserver {
        fun observeChat(newChat: List<ChatMessage>)
    }

    override fun onResume() {
        super.onResume()
        viewModel.listenerAvailabilityOfReceiver(user.id) { availability, fcm, profileImage, lastSeen ->
            if (availability) {
                binding.tvAvailability.text = "Online"
                binding.tvAvailability.visibility = View.VISIBLE
            } else if (lastSeen > 0L) {
                val sdf = java.text.SimpleDateFormat("hh:mm a, dd MMM", java.util.Locale.getDefault())
                binding.tvAvailability.text = "Last seen: ${sdf.format(java.util.Date(lastSeen))}"
                binding.tvAvailability.visibility = View.VISIBLE
            } else {
                binding.tvAvailability.visibility = View.GONE
            }
            user.token = fcm
            if (user.image.isNullOrEmpty()) {
                user.image = profileImage
                user.image?.decodeToBitmap()?.let {
                    binding.ivUserImage.setImageBitmap(it)
                    chatAdapter.setProfileImage(it)
                }
            }
        }
    }
}