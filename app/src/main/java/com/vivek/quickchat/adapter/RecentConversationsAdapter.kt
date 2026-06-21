package com.vijet.quickchat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vijet.quickchat.databinding.ItemUserListBinding
import com.vijet.quickchat.databinding.ItemUserListRecentConversionBinding
import com.vijet.quickchat.model.ChatMessage
import com.vijet.quickchat.model.User
import com.vijet.quickchat.utils.decodeToBitmap

class RecentConversationsAdapter :
    RecyclerView.Adapter<RecentConversationsAdapter.ConversationViewHolder>() {

    private var recentConversationList = mutableListOf<ChatMessage>()
    var onClickConversation: ((User) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ConversationViewHolder(ItemUserListRecentConversionBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: ConversationViewHolder, position: Int) {
        holder.setData(recentConversationList[position])
    }

    override fun getItemCount() = recentConversationList.size

    fun getRecentList() = recentConversationList

    fun updateRecentConversion(conversation: List<ChatMessage>) {
        recentConversationList.clear()
        recentConversationList.addAll(conversation)
        notifyDataSetChanged()
    }

    inner class ConversationViewHolder(private val binding: ItemUserListRecentConversionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(chatMessage: ChatMessage) {
            with(binding) {
                tvName.text = chatMessage.conversionName
                tvRecentMessage.text = chatMessage.message
                try {
                    val img = chatMessage.conversionImage
                    if (!img.isNullOrEmpty() && img != "null") {
                        ivProfile.setImageBitmap(img.decodeToBitmap())
                    } else {
                        ivProfile.setImageResource(com.vijet.quickchat.R.drawable.background_icon)
                    }
                } catch (e: Exception) {
                    ivProfile.setImageResource(com.vijet.quickchat.R.drawable.background_icon)
                }
                root.setOnClickListener {
                    onClickConversation?.let { it1 ->
                        it1(User(
                            id = chatMessage.conversionId.toString(),
                            name = chatMessage.conversionName.toString(),
                            image = chatMessage.conversionImage.toString()
                        ))
                    }
                }
            }

        }

    }

}