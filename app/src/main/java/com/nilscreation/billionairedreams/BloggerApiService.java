package com.nilscreation.billionairedreams;

import com.nilscreation.billionairedreams.model.BloggerModel;

import retrofit2.Call;
import retrofit2.http.GET;

import retrofit2.http.Path;

public interface BloggerApiService {
    @GET("blogs/5681934417179048726/posts/5864491363138699063?key=AIzaSyDEZjBt-NcdUe_JmYymxpFzjl5izIN3dAI")
    Call<BloggerModel> getBlogPosts();

    @GET("blogs/3174392454774953694/posts/{postId}/?key=AIzaSyB2NXeFloFkHIxuO3BlleBtUMLh97uW52I")
    Call<BloggerModel> getBlogPosts(@Path("postId") String postId);
}

