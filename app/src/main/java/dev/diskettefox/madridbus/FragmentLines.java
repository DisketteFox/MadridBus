package dev.diskettefox.madridbus;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import dev.diskettefox.madridbus.api.ApiCall;
import dev.diskettefox.madridbus.api.ApiInterface;
import dev.diskettefox.madridbus.api.StopModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentLines extends Fragment {
    public FragmentLines(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.view_lines, container, false);

        ApiInterface apiInterface = ApiCall.getStop().create(ApiInterface.class);
        Call<StopModel> call = apiInterface.getLineDetail(55,4, ApiCall.token);

        call.enqueue(new Callback<StopModel>() {
            @Override
            public void onResponse(Call<StopModel> call, Response<StopModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    StopModel stopModel = response.body();

                }
            }

            @Override
            public void onFailure(Call<StopModel> call, Throwable t) {
                Log.e("StopActivity", "Error fetching stop details", t);
            }
        });

        return view;
    }
}