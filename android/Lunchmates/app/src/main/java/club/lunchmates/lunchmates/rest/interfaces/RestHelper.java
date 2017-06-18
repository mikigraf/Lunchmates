package club.lunchmates.lunchmates.rest.interfaces;

import java.util.Date;
import java.util.List;

import club.lunchmates.lunchmates.data.Event;
import club.lunchmates.lunchmates.data.InsertResult;
import club.lunchmates.lunchmates.data.Position;
import club.lunchmates.lunchmates.data.UpdateResult;
import club.lunchmates.lunchmates.data.User;
import club.lunchmates.lunchmates.data.UsersEvents;

/**
 * Helper class for handling REST calls. A listener has to be passed through every method and this listener will be called when received a result. <code>null</code> will be passed if an exception occurred.
 */
public interface RestHelper {
    void eventAdd(String name, String x, String y, Date time, int author, DataReceivedListener<InsertResult> listener);

    void eventGetAll(DataReceivedListener<List<Event>> listener);

    void eventGetById(int id, DataReceivedListener<List<Event>> listener);

    void eventGetByUser(int id, DataReceivedListener<List<Event>> listener);

    void eventGetParticipants(int event, DataReceivedListener<List<User>> listener);

    void positionAdd(String x, String y, int author, DataReceivedListener<InsertResult> listener);

    void positionGetUsersLatest(int user, DataReceivedListener<List<Position>> listener);

    void positionGetLatestPositionOfParticipants(int event, DataReceivedListener<List<Position>> listener);

    void positionGetById(int id, DataReceivedListener<List<Position>> listener);

    void userUpdateToken(int id, String token, DataReceivedListener<UpdateResult> listener);

    void userGetEvents(int id, DataReceivedListener<List<Event>> listener);

    void userGetById(int id, DataReceivedListener<List<User>> listener);

    void userDelete(int id, DataReceivedListener<UpdateResult> listener);

    void usersEventsAdd(int user, int event, DataReceivedListener<InsertResult> listener);

    void usersEventsRemove(int user, int event, DataReceivedListener<UpdateResult> listener);

    void usersEventsGetById(int id, DataReceivedListener<List<UsersEvents>> listener);

    interface DataReceivedListener<T> {
        void onDataReceived(T data);
    }
}
