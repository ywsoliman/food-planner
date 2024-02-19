package com.example.foodplanner.onboarding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.foodplanner.R;

import java.util.Arrays;
import java.util.List;

public class ViewPagerFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_pager, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<Fragment> fragmentList = Arrays.asList(
                new OnboardingFirstFragment(),
                new OnboardingSecondFragment(),
                new OnboardingThirdFragment()
        );

        ViewPager2 viewPager = view.findViewById(R.id.viewPager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(fragmentList, requireActivity().getSupportFragmentManager(), getLifecycle());
        viewPager.setAdapter(adapter);

    }
}