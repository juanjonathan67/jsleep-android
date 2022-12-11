package JuanjsleepRJ.jsleep_android;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import JuanjsleepRJ.jsleep_android.model.Renter;
import JuanjsleepRJ.jsleep_android.model.Room;
import JuanjsleepRJ.jsleep_android.request.BaseApiService;
import JuanjsleepRJ.jsleep_android.request.UtilsApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity which displays selected payment confirmation. Renter can accept and cancel, while buyer can only cancel.
 * @author juanjonathan67
 * @version 1.0.0
 */
public class PaymentConfirmationActivity extends AppCompatActivity {
    /**
     * {@link JuanjsleepRJ.jsleep_android.request.BaseApiService} attribute
     */
    BaseApiService mApiService;
    /**
     * Context of activity
     */
    Context mContext;
    /**
     * Renter of the selected payment
     */
    private Renter r;
    /**
     * Text view to display payment details
     */
    TextView title, room, renter, price, status;
    /**
     * Buttons to accept or cancel payment
     */
    Button cancelBooking, cancelRenting, acceptRenting;

    /**
     * Create activity
     * @param savedInstanceState Instance State
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_confirmation);
        mApiService = UtilsApi.getApiService();
        mContext = this;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title = findViewById(R.id.textViewConTitle);
        room = findViewById(R.id.textViewConRoom);
        renter = findViewById(R.id.textViewConRenter);
        price = findViewById(R.id.textViewConPrice);
        status = findViewById(R.id.textViewConStatus);
        cancelBooking = findViewById(R.id.cancelBookingPaymentButton);
        cancelRenting = findViewById(R.id.renterCancelButton);
        acceptRenting = findViewById(R.id.renterAcceptButton);

        if(PaymentActivity.viewPagerPosition == 0) {
            cancelRenting.setVisibility(View.INVISIBLE);
            acceptRenting.setVisibility(View.INVISIBLE);
            getRenter(PaymentActivity.bookingPaymentList.get(BookingFragment.bookingIndex).renterId);
            Room ro = PaymentActivity.bookingRoomList.get(BookingFragment.bookingIndex);
            title.setText("Booking Payment");
            room.setText(ro.name);
            price.setText(String.valueOf(ro.price.price * (((ro.booked.get(ro.booked.size() - 1)
                    .getTime() - ro.booked.get(0).getTime()) / (1000.0 * 60 * 60 * 24)) % 365)));
        } else if (PaymentActivity.viewPagerPosition == 1) {
            cancelBooking.setVisibility(View.INVISIBLE);
            getRenter(PaymentActivity.rentingPaymentList.get(RentingFragment.rentingIndex).renterId);
            Room ro = PaymentActivity.rentingRoomList.get(RentingFragment.rentingIndex);
            title.setText("Renting Payment");
            room.setText(ro.name);
            price.setText(String.valueOf(ro.price.price * (((ro.booked.get(ro.booked.size() - 1)
                    .getTime() - ro.booked.get(0).getTime()) / (1000.0 * 60 * 60 * 24)) % 365)));
        } else {
            cancelRenting.setVisibility(View.INVISIBLE);
            acceptRenting.setVisibility(View.INVISIBLE);
            cancelBooking.setVisibility(View.INVISIBLE);
            getRenter(PaymentActivity.completedPaymentList.get(HistoryFragment.completedIndex).renterId);
            Room ro = PaymentActivity.completedRoomList.get(HistoryFragment.completedIndex);
            title.setText("Completed Payment");
            room.setText(ro.name);
            price.setText(String.valueOf(ro.price.price * (((ro.booked.get(ro.booked.size() - 1)
                    .getTime() - ro.booked.get(0).getTime()) / (1000.0 * 60 * 60 * 24)) % 365)));
        }

        cancelBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel(PaymentActivity.bookingPaymentList.get(BookingFragment.bookingIndex).id);
                startActivity(new Intent(PaymentConfirmationActivity.this, PaymentActivity.class));
            }
        });

        cancelRenting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel(PaymentActivity.rentingPaymentList.get(BookingFragment.bookingIndex).id);
                startActivity(new Intent(PaymentConfirmationActivity.this, PaymentActivity.class));
            }
        });

        acceptRenting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accept(PaymentActivity.rentingPaymentList.get(BookingFragment.bookingIndex).id);
                startActivity(new Intent(PaymentConfirmationActivity.this, PaymentActivity.class));
            }
        });
    }

    /**
     * Method used to make GET request with implemented method {@link JuanjsleepRJ.jsleep_android.request.BaseApiService#getRenter(int)}.
     * @param id Id of account
     */
    protected void getRenter(int id){
        mApiService.getRenter(id).enqueue(new Callback<Renter>() {
            @Override
            public void onResponse(Call<Renter> call, Response<Renter> response) {
                r = response.body();
                renter.setText(r.username);
                if(PaymentActivity.viewPagerPosition != 2){
                    status.setText(String.format("Waiting for %s's response", r.username));
                }else{
                    status.setText(String.valueOf(PaymentActivity.completedPaymentList.get(HistoryFragment.completedIndex).status));
                }
            }

            @Override
            public void onFailure(Call<Renter> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    /**
     * Method used to make POST request with implemented method {@link JuanjsleepRJ.jsleep_android.request.BaseApiService#cancel(int)}
     * Cancels selected payment
     * @param id Id of payment
     */
    protected void cancel(int id){
        mApiService.cancel(id).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                Toast.makeText(mContext, "Payment Canceled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    /**
     * Method used to make POST request with implemented method {@link JuanjsleepRJ.jsleep_android.request.BaseApiService#accept(int)}
     * Accepts selected payment
     * @param id Id of payment
     */
    protected void accept(int id){
        mApiService.accept(id).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                Toast.makeText(mContext, "Payment Accepted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item){
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                finish();
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    public boolean onCreateOptionsMenu(Menu menu){
//        return true;
//    }

    /**
     * Method used to destroy activity when back is pressed and goes back to {@link JuanjsleepRJ.jsleep_android.PaymentActivity}.
     */
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(PaymentConfirmationActivity.this, PaymentActivity.class));
        finish();
    }
}