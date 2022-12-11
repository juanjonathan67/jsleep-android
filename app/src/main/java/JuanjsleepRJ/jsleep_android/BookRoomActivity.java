package JuanjsleepRJ.jsleep_android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import JuanjsleepRJ.jsleep_android.model.Payment;
import JuanjsleepRJ.jsleep_android.request.BaseApiService;
import JuanjsleepRJ.jsleep_android.request.UtilsApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity which controls the room booking
 * @author juanjonathan67
 * @version 1.0.0
 */
public class BookRoomActivity extends AppCompatActivity {
    /**
     * {@link JuanjsleepRJ.jsleep_android.request.BaseApiService} attribute
     */
    BaseApiService mApiService;
    /**
     * Context of activity
     */
    Context mContext;
    /**
     * Start date picker
     */
    DatePickerDialog datePickerDialogFrom;
    /**
     * End date picker
     */
    DatePickerDialog datePickerDialogTo;
    /**
     * Start date picker button
     */
    Button datePickerButtonFrom;
    /**
     * End date picker button
     */
    Button datePickerButtonTo;
    /**
     * Book and pay the room button
     */
    Button payButton;
    /**
     * Calendar instance
     */
    Calendar cal;
    /**
     * Text view to display payment details
     */
    TextView price, voucher, discount, total, balance, left;
    /**
     * Parsed start date and end date
     */
    Date d1, d2;
    /**
     * Start date and end date type String with format "yyyy-MM-dd"
     */
    public static String fromDate = "0000-00-00", toDate = "0000-00-00";

    /**
     * Create activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_room);
        mApiService = UtilsApi.getApiService();
        mContext = this;
        cal = Calendar.getInstance();
        datePickerButtonFrom = findViewById(R.id.datePickerButtonFrom);
        datePickerButtonTo = findViewById(R.id.datePickerButtonTo);
        payButton = findViewById(R.id.payBookingButton);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String now =  cal.get(Calendar.YEAR) + "-" +
                (cal.get(Calendar.MONTH) + 1) + "-" +
                cal.get(Calendar.DAY_OF_MONTH);
        fromDate = now;
        toDate = now;
        now =  cal.get(Calendar.DAY_OF_MONTH) + " " +
                monthToStringConverter(cal.get(Calendar.MONTH)) + " " +
                cal.get(Calendar.YEAR);
        datePickerButtonFrom.setText(now);
        datePickerButtonTo.setText(now);

        price = findViewById(R.id.textViewRoomPrice);
        voucher = findViewById(R.id.textViewBuyerVoucher);
        discount = findViewById(R.id.textViewRoomDiscount);
        total = findViewById(R.id.textViewRoomTotal);
        balance = findViewById(R.id.textViewBuyerBalancce);
        left = findViewById(R.id.textViewBuyerLeft);

        voucher.setText("None");
        price.setText(String.valueOf(MainActivity.roomPaginatedList.get(MainActivity.roomIndex).price.price));
        discount.setText(String.valueOf(MainActivity.roomPaginatedList.get(MainActivity.roomIndex).price.discount));
        balance.setText(String.valueOf(LoginActivity.savedAccount.balance));
        total.setText(String.valueOf(MainActivity.roomPaginatedList.get(MainActivity.roomIndex).price.price));
        left.setText(String.valueOf(Double.parseDouble(balance.getText().toString()) -
                Double.parseDouble(total.getText().toString())));

        DatePickerDialog.OnDateSetListener dateSetListenerFrom = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                datePickerButtonFrom.setText(String.format("%d %s %d", i2, monthToStringConverter(i1), i));
                i1++;
                fromDate = i + "-" + i1 + "-" + i2;
            }
        };

        DatePickerDialog.OnDateSetListener dateSetListenerTo = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                datePickerButtonTo.setText(String.format("%d %s %d", i2, monthToStringConverter(i1), i));
                i1++;
                toDate = i + "-" + i1 + "-" + i2;
            }
        };

        datePickerDialogFrom = new DatePickerDialog(
                this,
                DatePickerDialog.THEME_DEVICE_DEFAULT_DARK,
                dateSetListenerFrom,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialogTo = new DatePickerDialog(
                this,
                DatePickerDialog.THEME_DEVICE_DEFAULT_DARK,
                dateSetListenerTo,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialogFrom.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    d1 = sdf.parse(fromDate);
                    d2 = sdf.parse(toDate);
                    if(d2.before(d1)) {
                        d2 = d1;
                        datePickerButtonTo.setText("");
                    }
                    datePickerDialogTo.getDatePicker().setMinDate(d1.getTime());
                    price.setText(String.valueOf(MainActivity.roomPaginatedList.get(MainActivity.roomIndex).price.price *
                            (((d2.getTime() - d1.getTime()) / (1000.0 * 60 * 60 * 24)) % 365)));
                    total.setText(String.valueOf(Double.parseDouble(price.getText().toString()) -
                            Double.parseDouble(discount.getText().toString())));
                    left.setText(String.valueOf(Double.parseDouble(balance.getText().toString()) -
                            Double.parseDouble(total.getText().toString())));
                }catch (ParseException p){
                    p.printStackTrace();
                }
            }
        });

        datePickerDialogTo.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    System.out.println(fromDate + " - " + toDate);
                    d1 = sdf.parse(fromDate);
                    d2 = sdf.parse(toDate);
                    price.setText(String.valueOf(MainActivity.roomPaginatedList.get(MainActivity.roomIndex).price.price *
                            (((d2.getTime() - d1.getTime()) / (1000.0 * 60 * 60 * 24)) % 365)));
                    total.setText(String.valueOf(Double.parseDouble(price.getText().toString()) -
                            Double.parseDouble(discount.getText().toString())));
                    left.setText(String.valueOf(Double.parseDouble(balance.getText().toString()) -
                            Double.parseDouble(total.getText().toString())));
                }catch (ParseException p){
                    p.printStackTrace();
                }
            }
        });

        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Payment payment = requestCreatePayment();
            }
        });
    }

    /**
     * Method to display start date picker
     * @param view Clicked view
     */
    public void openDatePickerFrom(View view){
        datePickerDialogFrom.getDatePicker().setMinDate(cal.getTimeInMillis());
        datePickerDialogFrom.show();
    }
    /**
     * Method to display end date picker
     * @param view Clicked view
     */
    public void openDatePickerTo(View view){
        datePickerDialogTo.show();
    }

    /**
     * Method used to make POST request with implemented method {@link JuanjsleepRJ.jsleep_android.request.BaseApiService#requestCreatePayment(int, int, int, String, String)}.
     * Checks if room is eligible for booking, then creates it.
     * @return null
     */
    protected Payment requestCreatePayment(){
        mApiService.requestCreatePayment(
                LoginActivity.savedAccount.id,
                MainActivity.roomPaginatedList.get(MainActivity.roomIndex).accountId,
                MainActivity.roomPaginatedList.get(MainActivity.roomIndex).id,
                fromDate,
                toDate
        ).enqueue(new Callback<Payment>() {
            @Override
            public void onResponse(Call<Payment> call, Response<Payment> response) {
                System.out.println(response.body());
                startActivity(new Intent(BookRoomActivity.this, DetailRoomActivity.class));
            }

            @Override
            public void onFailure(Call<Payment> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(mContext, "Kamar sudah terpesan atau uang tidak cukup!", Toast.LENGTH_SHORT).show();
            }
        });
        return null;
    }

    /**
     * Convert Calendar.MONTH to String
     * @param m Calendar.MONTH to be converted
     * @return Return Calendar.MONTH type String
     */
    private String monthToStringConverter(int m){
        if(m == 0)
            return "Jan";
        else if(m == 1)
            return "Feb";
        else if(m == 2)
            return "Mar";
        else if(m == 3)
            return "Apr";
        else if(m == 4)
            return "May";
        else if(m == 5)
            return "June";
        else if(m == 6)
            return "July";
        else if(m == 7)
            return "Aug";
        else if(m == 8)
            return "Sep";
        else if(m == 9)
            return "Oct";
        else if(m == 10)
            return "Nov";
        else if(m == 11)
            return "Dec";
        return "Jan";
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu){
        return true;
    }

    /**
     * Method to destroy activity when back button is pressed and return to {@link JuanjsleepRJ.jsleep_android.MainActivity}.
     */
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(BookRoomActivity.this, MainActivity.class));
        finish();
    }
}