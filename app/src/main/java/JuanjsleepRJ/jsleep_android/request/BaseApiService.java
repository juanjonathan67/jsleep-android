package JuanjsleepRJ.jsleep_android.request;

import java.util.ArrayList;
import java.util.List;

import JuanjsleepRJ.jsleep_android.model.*;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.Call;
import retrofit2.http.Query;

/**
 * Interface of all GET and POST requests
 */
public interface BaseApiService {

    /**
     * Calls a GET request.
     * Method to get an {@link JuanjsleepRJ.jsleep_android.model.Account} based on its id
     * @param id Id to be searched
     * @return Returns found account
     */
    @GET("account/{id}/getAccount")
    Call<Account> getAccount(@Path("id") int id);

    /**
     * Calls a GET request.
     * Method to get an {@link JuanjsleepRJ.jsleep_android.model.Renter} based on its id
     * @param id Id to be searched
     * @return Returns found renter
     */
    @GET("account/{id}/getRenter")
    Call<Renter> getRenter (@Path("id") int id);

    /**
     * Calls a GET request.
     * Method to get a List of booking {@link JuanjsleepRJ.jsleep_android.model.Payment} based on its id
     * @param id Id to be searched
     * @return Returns List of booking payment
     */
    @GET("payment/{id}/getBookingPayment")
    Call<List<Payment>> getBookingPayment(@Path("id") int id);

    /**
     * Calls a GET request.
     * Method to get a List of renting {@link JuanjsleepRJ.jsleep_android.model.Payment} based on its id
     * @param id Id to be searched
     * @return Returns List of renting payment
     */
    @GET("payment/{id}/getRentingPayment")
    Call<List<Payment>> getRentingPayment(@Path("id") int id);

    /**
     * Calls a GET request.
     * Method to get a List of completed {@link JuanjsleepRJ.jsleep_android.model.Payment} based on its id
     * @param id Id to be searched
     * @return Returns List of completed payment
     */
    @GET("payment/{id}/getCompletedPayment")
    Call<List<Payment>> getCompletedPayment(@Path("id") int id);

    /**
     * Calls a GET request.
     * Method to get an {@link JuanjsleepRJ.jsleep_android.model.Room} based on its id
     * @param id Id to be searched
     * @return Returns found room
     */
    @GET("room/{id}/getRoom")
    Call<Room> getRoom(@Path("id") int id);

    /**
     * Calls a POST request.
     * Method to get an {@link JuanjsleepRJ.jsleep_android.model.Account} based on its email and password
     * @param email Email to be searched
     * @param password Password to be searched
     * @return Returns found account
     */
    @POST("account/login")
    Call<Account> requestLogin(
            @Query("email") String email,
            @Query("password") String password
    );

    /**
     * Calls a POST request.
     * Method to register a new {@link JuanjsleepRJ.jsleep_android.model.Account} based on its name, email, and password
     * @param name Name to be registered
     * @param email Email to be registered
     * @param password Password to be registered
     * @return Returns registered account
     */
    @POST("account/register")
    Call<Account> requestRegister(
            @Query("name") String name,
            @Query("email") String email,
            @Query("password") String password
    );

    /**
     * Calls a POST request.
     * Method to register a new {@link JuanjsleepRJ.jsleep_android.model.Renter} based on its name, address, and phone number
     * @param id Id of the account
     * @param username Name to be registered
     * @param address Address to be registered
     * @param phoneNumber Phone Number to be registered
     * @return Returns registered renter
     */
    @POST("account/{id}/registerRenter")
    Call<Renter> requestRegisterRenter(@Path("id") int id,
                                        @Query("username") String username,
                                        @Query("address") String address,
                                        @Query("phoneNumber") String phoneNumber
    );

    /**
     * Calls a POST request.
     * Method to top up an account's {@link JuanjsleepRJ.jsleep_android.model.Account#balance} based on its amount
     * @param id Id of the account
     * @param balance Amount to be topped up
     * @return Returns true if top up was successful
     */
    @POST("account/{id}/topUp")
    Call<Boolean> requestTopUp(@Path("id") int id,
                               @Query("balance") double balance
    );

    /**
     * Calls GET request.
     * Method to paginate a List of {@link JuanjsleepRJ.jsleep_android.model.Room}
     * @param id Id of account
     * @param page Page number
     * @param pageSize Page size
     * @param rented Rented status
     * @return Returns List of paginated rooms
     */
    @GET("room/{id}/getPaginatedRoom")
    Call<List<Room>> getPaginatedRoom(
            @Path("id") int id,
            @Query("page") int page,
            @Query("pageSize") int pageSize,
            @Query("rented") boolean rented
    );

    /**
     * Calls POST request.
     * Method to create a new {@link JuanjsleepRJ.jsleep_android.model.Room}
     * @param accountId Id of renter
     * @param name Room's name
     * @param size Room's size
     * @param price Room's {@link JuanjsleepRJ.jsleep_android.model.Price}
     * @param facility Room's {@link JuanjsleepRJ.jsleep_android.model.Facility}
     * @param city Room's {@link JuanjsleepRJ.jsleep_android.model.City}
     * @param bedType Room's {@link JuanjsleepRJ.jsleep_android.model.BedType}
     * @param address Room's address
     * @return Returns newly created room
     */
    @POST("room/create")
    Call<Room> requestCreateRoom(
            @Query("accountId") int accountId,
            @Query("name") String name,
            @Query("size") int size,
            @Query("price") int price,
            @Query("facility") ArrayList<Facility> facility,
            @Query("city") City city,
            @Query("bedType") BedType bedType,
            @Query("address") String address
    );

    /**
     * Calls POST request.
     * Method to create a new {@link JuanjsleepRJ.jsleep_android.model.Payment}
     * @param buyerId Id of the buyer
     * @param renterId Id of the renter
     * @param roomId Id of the room
     * @param from Start date of booking
     * @param to End date of booking
     * @return Returns newly created payment
     */
    @POST("payment/create")
    Call<Payment> requestCreatePayment(
            @Query("buyerId") int buyerId,
            @Query("renterId") int renterId,
            @Query("roomId") int roomId,
            @Query("from") String from,
            @Query("to") String to
    );

    /**
     * Calls POST request.
     * Method to cancel a {@link JuanjsleepRJ.jsleep_android.model.Payment}
     * @param id Id of payment
     * @return Returns true if cancelation was successful
     */
    @POST("payment/{id}/cancel")
    Call<Boolean> cancel(@Path("id") int id);

    /**
     * Calls POST request.
     * Method to accept a {@link JuanjsleepRJ.jsleep_android.model.Payment}
     * @param id Id of payment
     * @return Returns true if accept was successful
     */
    @POST("payment/{id}/accept")
    Call<Boolean> accept(@Path("id") int id);

    /**
     * Calls GET request.
     * Method to get all available {@link JuanjsleepRJ.jsleep_android.model.Payment}
     * @return Returns List of all available payments
     */
    @GET("payment/getAllPayment")
    Call<List<Payment>> getAllPayment();

    /**
     * Calls GET request.
     * Method to get all available {@link JuanjsleepRJ.jsleep_android.model.Room}
     * @return Returns List of all available rooms
     */
    @GET("room/getAllRoom")
    Call<List<Room>> getAllRoom();

    /**
     * Calls GET request.
     * Method to filter List of {@link JuanjsleepRJ.jsleep_android.model.Room} by its {@link JuanjsleepRJ.jsleep_android.model.City}
     * @param id Id of room
     * @param city City to be filtered
     * @param rented Rented status
     * @return Returns List of filtered rooms
     */
    @GET("room/{id}/filteredRoomByCity")
    Call<List<Room>> getFilteredRoomByCity(
            @Path("id") int id,
            @Query("city") City city,
            @Query("rented") boolean rented
    );

    /**
     * Calls GET request.
     * Method to filter List of {@link JuanjsleepRJ.jsleep_android.model.Room} by its {@link JuanjsleepRJ.jsleep_android.model.Facility}
     * @param id Id of room
     * @param facility Facility to be filtered
     * @param rented Rented status
     * @return Returns List of filtered rooms
     */
    @GET("room/{id}/filteredRoomByFacilities")
    Call<List<Room>> getFilteredRoomByFacilities(
            @Path("id") int id,
            @Query("facility") Facility facility,
            @Query("rented") boolean rented
    );

    /**
     * Calls GET request.
     * Method to filter List of {@link JuanjsleepRJ.jsleep_android.model.Room} by its {@link JuanjsleepRJ.jsleep_android.model.BedType}
     * @param id Id of room
     * @param bedType Bed type to be filtered
     * @param rented Rented status
     * @return Returns List of filtered rooms
     */
    @GET("room/{id}/filteredRoomByBedType")
    Call<List<Room>> getFilteredRoomByBedType(
            @Path("id") int id,
            @Query("bedType") BedType bedType,
            @Query("rented") boolean rented
    );

    /**
     * Calls GET request.
     * Method to filter List of {@link JuanjsleepRJ.jsleep_android.model.Room} by its {@link JuanjsleepRJ.jsleep_android.model.Price#price}
     * @param id Id of room
     * @param priceLow Lower bound of price
     * @param priceHigh Upper bound of price
     * @param rented Rented status
     * @return Returns List of filtered rooms
     */
    @GET("room/{id}/filteredRoomByPrice")
    Call<List<Room>> getFilteredRoomByPrice(
            @Path("id") int id,
            @Query("priceLow") double priceLow,
            @Query("priceHigh") double priceHigh,
            @Query("rented") boolean rented
    );
}
