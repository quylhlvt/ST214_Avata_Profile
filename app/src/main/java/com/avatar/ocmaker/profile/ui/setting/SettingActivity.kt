package com.avatar.ocmaker.profile.ui.setting

import android.view.View
import com.avatar.ocmaker.profile.base.AbsBaseActivity
import com.avatar.ocmaker.profile.ui.language.LanguageActivity
import com.avatar.ocmaker.profile.utils.RATE
import com.avatar.ocmaker.profile.utils.SharedPreferenceUtils
import com.avatar.ocmaker.profile.utils.newIntent
import com.avatar.ocmaker.profile.utils.onSingleClick
import com.avatar.ocmaker.profile.utils.policy
import com.avatar.ocmaker.profile.utils.rateUs
import com.avatar.ocmaker.profile.utils.shareApp
import com.avatar.ocmaker.profile.utils.unItem
import com.avatar.ocmaker.profile.R
import com.avatar.ocmaker.profile.databinding.ActivitySettingBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingActivity : AbsBaseActivity<ActivitySettingBinding>() {
    @Inject
    lateinit var sharedPreferences: SharedPreferenceUtils
    override fun getLayoutId(): Int = R.layout.activity_setting

    override fun initView() {
        binding.titleSetting.isSelected = true
        if (sharedPreferences.getBooleanValue(RATE)) {
            binding.llRateUs.visibility = View.GONE
        }
        unItem = {
            binding.llRateUs.visibility = View.GONE
        }
    }

    override fun onStop() {
        super.onStop()
    }
    override fun initAction() {
        binding.apply {
            llLanguage.onSingleClick {
                startActivity(
                    newIntent(
                        applicationContext,
                        LanguageActivity::class.java
                    )
                )
            }
            imvMusic.onSingleClick {
                initMusic(imvMusic)
            }
            llRateUs.onSingleClick {
                rateUs(0)
            }
            llShareApp.onSingleClick {
                shareApp()
            }
            llPrivacy.onSingleClick {
                policy()
            }
            imvBack.onSingleClick {
                finish()
            }
        }
    }
}