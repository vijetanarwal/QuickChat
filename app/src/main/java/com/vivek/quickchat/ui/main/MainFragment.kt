package com.vijet.quickchat.ui.main

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.vijet.quickchat.R
import com.vijet.quickchat.adapter.RecentConversationsAdapter
import com.vijet.quickchat.databinding.MainFragmentBinding
import com.vijet.quickchat.ui.registration.RegistrationActivity
import com.vijet.quickchat.utils.Constant
import com.vijet.quickchat.utils.decodeToBitmap
import com.vijet.quickchat.utils.encodeToBase64
import com.vijet.quickchat.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.main_fragment) {

    private lateinit var binding: MainFragmentBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var adapter: RecentConversationsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = MainFragmentBinding.bind(view)

        clickListener()
        updateDetails()
        setRecyclerview()

        viewModel.recentMessageEventListener(adapter.getRecentList()) {
            adapter.updateRecentConversion(it)
            binding.rvRecentConversation.visibility = View.VISIBLE
            binding.pb.visibility = View.GONE
            binding.rvRecentConversation.smoothScrollToPosition(0)
        }

    }

    private fun updateDetails() {
        try {
            val image = viewModel.loadUserDetails()
            if (!image.isNullOrEmpty() && image != "null") {
                binding.ivProfile.setImageBitmap(image.decodeToBitmap())
            } else {
                binding.ivProfile.setImageResource(R.drawable.background_icon)
            }
        } catch (e: Exception) {
            binding.ivProfile.setImageResource(R.drawable.background_icon)
        }
        binding.tvName.text = viewModel.getName()
    }

    private fun clickListener() {
        binding.ivSearch.setOnClickListener { findNavController().navigate(R.id.action_mainFragment_to_usersFragment) }
        binding.ivMore.setOnClickListener { showMoreMenu() }
    }

    private fun setRecyclerview() {
        adapter = RecentConversationsAdapter()
        binding.rvRecentConversation.apply {
            setHasFixedSize(true)
            adapter = this@MainFragment.adapter
        }
        adapter.onClickConversation = { user ->
            val bundle = Bundle()
            bundle.putSerializable(Constant.KEY_USER, user)
            findNavController().navigate(R.id.action_mainFragment_to_chatFragment, bundle)
        }
    }

    private fun signOut() {

        viewModel.signOut().observe(viewLifecycleOwner) {
            if (it) {
                requireContext().toast("SignOut")
                val intent = Intent(requireActivity(), RegistrationActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)

            } else {
                requireContext().toast("Unable to signOut")
            }
        }


    }

    fun showMoreMenu() {
        val moreMenu = PopupMenu(requireContext(), binding.ivMore)
        moreMenu.inflate(R.menu.menu_more)
        moreMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_sign_out -> {
                    signOut()
                    return@setOnMenuItemClickListener true
                }
                R.id.action_edit_profile -> {
                    val options = arrayOf("Change Photo", "Remove Photo")
                    android.app.AlertDialog.Builder(requireContext())
                        .setTitle("Edit Profile")
                        .setItems(options) { _, which ->
                            when (which) {
                                0 -> {
                                    val pickImageIntent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
                                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                    }
                                    pickImage.launch(pickImageIntent)
                                }
                                1 -> {
                                    binding.ivProfile.setImageResource(R.drawable.background_icon)
                                    viewModel.updateProfileImage("")
                                }
                            }
                        }
                        .show()
                    return@setOnMenuItemClickListener true
                }
            }
            false
        }
        moreMenu.show()
    }

    private val pickImage = registerForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                try {
                    val inputStream = requireContext().contentResolver.openInputStream(uri)
                    val bitmap = android.graphics.BitmapFactory.decodeStream(inputStream)
                    binding.ivProfile.setImageBitmap(bitmap)
                    val encodedImage = bitmap.encodeToBase64()
                    viewModel.updateProfileImage(encodedImage)
                } catch (e: Exception) {
                    requireContext().toast("Failed to update profile picture")
                }
            }
        }
    }

}