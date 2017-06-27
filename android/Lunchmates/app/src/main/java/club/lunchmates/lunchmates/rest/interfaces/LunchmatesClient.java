package club.lunchmates.lunchmates.rest.interfaces;


import java.net.Authenticator;
import java.util.Date;
import java.util.List;

import club.lunchmates.lunchmates.data.AuthenticationResult;
import club.lunchmates.lunchmates.data.Event;
import club.lunchmates.lunchmates.data.InsertResult;
import club.lunchmates.lunchmates.data.Position;
import club.lunchmates.lunchmates.data.UpdateResult;
import club.lunchmates.lunchmates.data.User;
import club.lunchmates.lunchmates.data.UsersEvents;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Interface for creating REST client via retrofit2
 */
public interface LunchmatesClient {
    @PUT("/event/add/{name}/{x}/{y}/{time}/{author}")
    Call<InsertResult> eventAdd(
            @Path("name") String name,
            @Path("x") String x,
            @Path("y") String y,
            @Path("time") Date time,
            @Path("author") int author);


    @GET("/event/getAll")
    Call<List<Event>> eventGetAl();

    @GET("/event/getById/{id}")
    Call<List<Event>> eventGetById(@Path("id") int id);

    @GET("/event/getByUser/{id}")
    Call<List<Event>> eventGetByUser(@Path("id") int id);

    @GET("/event/getParticipants/{event}")
    Call<List<User>> eventGetParticipants(@Path("event") int event);

    @PUT("/position/add/{x}/{y}/{author}")
    Call<InsertResult> positionAdd(
            @Path("x") String x,
            @Path("y") String y,
            @Path("author") int author);

    @GET("/position/getUsersLatest/{user}")
    Call<List<Position>> positionGetUsersLatest(@Path("user") int user);

    @GET("/position/getLatestPositionOfParticipants/{event}")
    Call<List<Position>> positionGetLatestPositionOfParticipants(@Path("event") int event);

    @GET("/position/getById/{id}")
    Call<List<Position>> positionGetById(@Path("id") int id);

    @PUT("/user/updateToken/{id}/{token}")
    Call<UpdateResult> userUpdateToken(
            @Path("id") int id,
            @Path("token") String token);

    @GET("/user/getEvents/{user}")
    Call<List<Event>> userGetEvents(@Path("user") int id);

    @GET("/user/getById/{id}")
    Call<List<User>> userGetById(@Path("id") int id);

    @GET("/user/delete/{id}")
    Call<UpdateResult> userDelete(@Path("id") int id);

    @PUT("/usersEvents/add/{user}/{event}")
    Call<InsertResult> usersEventsAdd(
            @Path("user") int user,
            @Path("event") int event);

    @GET("/usersEvents/remove/{user}/{event}")
    Call<UpdateResult> usersEventsRemove(
            @Path("user") int user,
            @Path("event") int event);

    @GET("/usersEvents/getById/{id}")
    Call<List<UsersEvents>> usersEventsGetById(@Path("id") int id);

    @PUT("/user/login/{token}")
    Call<AuthenticationResult> login(@Path("token")String token);

}
