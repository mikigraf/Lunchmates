package club.lunchmates.lunchmates.rest;

import android.util.Log;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import club.lunchmates.lunchmates.data.AuthenticationResult;
import club.lunchmates.lunchmates.data.Event;
import club.lunchmates.lunchmates.data.InsertResult;
import club.lunchmates.lunchmates.data.Position;
import club.lunchmates.lunchmates.data.UpdateResult;
import club.lunchmates.lunchmates.data.User;
import club.lunchmates.lunchmates.data.UsersEvents;
import club.lunchmates.lunchmates.rest.interfaces.LunchmatesClient;
import club.lunchmates.lunchmates.rest.interfaces.RestHelper;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestHelperImpl implements RestHelper {
    private static final String API_BASE_URL = "http://lunchmates.club:9999/";
    private static final String TAG = "RestHelperImpl";

    private LunchmatesClient getClient() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(API_BASE_URL).addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.client(httpClient.build()).build();
        LunchmatesClient client =  retrofit.create(LunchmatesClient.class);

        return client;
    }

    @Override
    public void eventAdd(String name, String x, String y, Date time, int author, DataReceivedListener<InsertResult> listener) {
        LunchmatesClient client = getClient();

        Call<InsertResult> result = client.eventAdd(name, x, y, time, author);
        ResultCallback<InsertResult> callback = new ResultCallback<>();
        callback.addListener(listener);
        result.enqueue(callback);
    }

    @Override
    public void eventGetAll(DataReceivedListener<List<Event>> listener) {
        LunchmatesClient client = getClient();

        Call<List<Event>> result = client.eventGetAl();
        ResultCallback<List<Event>> callback = new ResultCallback<>();
        callback.addListener(listener);
        result.enqueue(callback);
    }

    @Override
    public void eventGetById(int id, DataReceivedListener<List<Event>> listener) {
        LunchmatesClient client = getClient();

        Call<List<Event>> result = client.eventGetById(id);
        ResultCallback<List<Event>> callback = new ResultCallback<>();
        callback.addListener(listener);
        result.enqueue(callback);
    }

    @Override
    public void eventGetByUser(int id, DataReceivedListener<List<Event>> listener) {
        LunchmatesClient client = getClient();

        Call<List<Event>> result = client.eventGetByUser(id);
        ResultCallback<List<Event>> callback = new ResultCallback<>();
        callback.addListener(listener);
        result.enqueue(callback);
    }

    @Override
    public void eventGetParticipants(int event, DataReceivedListener<List<User>> listener) {
        LunchmatesClient client = getClient();

        Call<List<User>> result = client.eventGetParticipants(event);
        ResultCallback<List<User>> callback = new ResultCallback<>();
        callback.addListener(listener);
        result.enqueue(callback);
    }

    @Override
    public void positionAdd(String x, String y, int author, DataReceivedListener<InsertResult> listener) {
        LunchmatesClient client = getClient();

        Call<InsertResult> result = client.positionAdd(x, y, author);
        ResultCallback<InsertResult> callback = new ResultCallback<>();
        callback.addListener(listener);
        result.enqueue(callback);
    }

    @Override
    public void positionGetUsersLatest(int user, DataReceivedListener<List<Position>> listener) {
        LunchmatesClient client = getClient();

        Call<List<Position>> result = client.positionGetUsersLatest(user);
        ResultCallback<List<Position>> callback = new ResultCallback<>();
        callback.addListener(listener);
        result.enqueue(callback);
    }

    @Override
    public void positionGetLatestPositionOfParticipants(int event, DataReceivedListener<List<Position>> listener) {
        LunchmatesClient client = getClient();

        Call<List<Position>> result = client.positionGetLatestPositionOfParticipants(event);
        ResultCallback<List<Position>> callback = new ResultCallback<>();
        callback.addListener(listener);
        result.enqueue(callback);
    }

    @Override
    public void positionGetById(int id, DataReceivedListener<List<Position>> listener) {
        LunchmatesClient client = getClient();

        Call<List<Position>> result = client.positionGetById(id);
        ResultCallback<List<Position>> callback = new ResultCallback<>();
        callback.addListener(listener);
        result.enqueue(callback);
    }

    @Override
    public void userUpdateToken(int id, String token, DataReceivedListener<UpdateResult> listener) {
        LunchmatesClient client = getClient();

        Call<UpdateResult> result = client.userUpdateToken(id, token);
        ResultCallback<UpdateResult> callback = new ResultCallback<>();
        callback.addListener(listener);
        result.enqueue(callback);
    }

    @Override
    public void userGetEvents(int id, DataReceivedListener<List<Event>> listener) {
        LunchmatesClient client = getClient();

        Call<List<Event>> result = client.userGetEvents(id);
        ResultCallback<List<Event>> callback = new ResultCallback<>();
        callback.addListener(listener);
        result.enqueue(callback);
    }

    @Override
    public void userGetById(int id, DataReceivedListener<List<User>> listener) {
        LunchmatesClient client = getClient();

        Call<List<User>> result = client.userGetById(id);
        ResultCallback<List<User>> callback = new ResultCallback<>();
        callback.addListener(listener);
        result.enqueue(callback);
    }

    @Override
    public void userDelete(int id, DataReceivedListener<UpdateResult> listener) {
        LunchmatesClient client = getClient();

        Call<UpdateResult> result = client.userDelete(id);
        ResultCallback<UpdateResult> callback = new ResultCallback<>();
        callback.addListener(listener);
        result.enqueue(callback);
    }

    @Override
    public void usersEventsAdd(int user, int event, DataReceivedListener<InsertResult> listener) {
        LunchmatesClient client = getClient();

        Call<InsertResult> result = client.usersEventsAdd(user, event);
        ResultCallback<InsertResult> callback = new ResultCallback<>();
        callback.addListener(listener);
        result.enqueue(callback);
    }

    @Override
    public void usersEventsRemove(int user, int event, DataReceivedListener<UpdateResult> listener) {
        LunchmatesClient client = getClient();

        Call<UpdateResult> result = client.usersEventsRemove(user, event);
        ResultCallback<UpdateResult> callback = new ResultCallback<>();
        callback.addListener(listener);
        result.enqueue(callback);
    }

    @Override
    public void usersEventsGetById(int id, DataReceivedListener<List<UsersEvents>> listener) {
        LunchmatesClient client = getClient();

        Call<List<UsersEvents>> result = client.usersEventsGetById(id);
        ResultCallback<List<UsersEvents>> callback = new ResultCallback<>();
        callback.addListener(listener);
        result.enqueue(callback);
    }

    @Override
    public void login(String token, DataReceivedListener<AuthenticationResult> listener) {
        LunchmatesClient client = getClient();
        Call<AuthenticationResult> result = client.login(token);
        ResultCallback<AuthenticationResult>  callback = new ResultCallback<>();
        callback.addListener(listener);
        result.enqueue(callback);
    }
    @Override
    public void getEventsCount(DataReceivedListener<Integer> listener) {
        LunchmatesClient client = getClient();

        Call<Integer> result = client.getEventsCount();
        ResultCallback<Integer> callback = new ResultCallback<>();
        callback.addListener(listener);
        result.enqueue(callback);
    }

    private class ResultCallback<T> implements Callback<T> {
        private List<DataReceivedListener<T>> listeners;

        public ResultCallback() {
            listeners = new LinkedList<>();
        }

        @Override
        public void onResponse(Call<T> call, Response<T> response) {
            if(response.isSuccessful()) {
                T data = response.body();
                for (DataReceivedListener<T> listener : listeners) {
                    listener.onDataReceived(data);
                }
            }
        }

        @Override
        public void onFailure(Call<T> call, Throwable t) {
            Log.e(TAG, "onFailure() "+t.getMessage());
            for (DataReceivedListener<T> listener : listeners) {
                listener.onDataReceived(null);
            }
        }

        public void addListener(DataReceivedListener<T> listener) {
            listeners.add(listener);
        }
    }
}
