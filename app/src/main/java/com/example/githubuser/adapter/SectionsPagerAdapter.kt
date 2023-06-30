package com.example.githubuser.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.githubuser.fragment.FollowingFragment

class SectionsPagerAdapter(activity: AppCompatActivity, private val username: String) :
    FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = FollowingFragment()
        fragment.arguments = Bundle().apply {
            putInt(FollowingFragment.ARG_POSITION, position + 1)
            putString(FollowingFragment.ARG_USERNAME, username)
        }
        return fragment
    }

}