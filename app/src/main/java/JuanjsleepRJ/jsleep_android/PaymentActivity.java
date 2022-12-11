package JuanjsleepRJ.jsleep_android;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

import JuanjsleepRJ.jsleep_android.model.Payment;
import JuanjsleepRJ.jsleep_android.model.Room;
import JuanjsleepRJ.jsleep_android.request.BaseApiService;
import JuanjsleepRJ.jsleep_android.request.UtilsApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity which contains information of all payments made, separated with view pager to 3 fragments : booking, renting, and completed.
 * @author juanjonathan67
 * @version 1.0.0
 */
public class PaymentActivity extends FragmentActivity {
    /**
     * {@link JuanjsleepRJ.jsleep_android.request.BaseApiService} attribute
     */
    BaseApiService mApiService;
    /**
     * Card view for registering new renter
     */
    Context mContext;
    /**
     * Titles of Tab Layout
     */
    String[] titles = {"Booking", "Renting", "History"};
    /**
     * View pager to horizontally scroll between 3 fragments
     */
    ViewPager2 viewPager;
    /**
     * Tab layout for top menu of view pager
     */
    TabLayout tabLayout;
    /**
     * Swipe refresh layout. The page can be refreshed when scrolled up.
     */
    private SwipeRefreshLayout swipeRefreshLayout;
    /**
     * View pager last position
     */
    public static int viewPagerPosition = 0;
    /**
     * Booking payment list
     */
    public static List<Payment> bookingPaymentList = new ArrayList<>();
    /**
     * Renting payment list
     */
    public static List<Payment> rentingPaymentList = new ArrayList<>();
    /**
     * Completed payment list
     */
    public static List<Payment> completedPaymentList = new ArrayList<>();
    /**
     * Booking room list
     */
    public static List<Room> bookingRoomList = new ArrayList<>();
    /**
     * Renting room list
     */
    public static List<Room> rentingRoomList = new ArrayList<>();
    /**
     * Completed room list
     */
    public static List<Room> completedRoomList = new ArrayList<>();
    /**
     * Booking room names
     */
    public static List<String> bookingRoomNames = new ArrayList<>();
    /**
     * Renting room names
     */
    public static List<String> rentingRoomNames = new ArrayList<>();
    /**
     * Completed room names
     */
    public static List<String> completedRoomNames = new ArrayList<>();
    /**
     * Total view pager pages
     */
    private static final int pages = 3;

    /**
     * Create activity
     * @param savedInstanceState Instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        mApiService = UtilsApi.getApiService();
        mContext = this;

        initFragments();

        swipeRefreshLayout = findViewById(R.id.paymentRefresh);
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        FragmentStateAdapter fragmentStateAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(fragmentStateAdapter);
        viewPager.setPageTransformer(new ZoomOutPageTransformer());

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                viewPagerPosition = position;
            }
        });

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(titles[position])
        ).attach();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            }
        });
    }

    /**
     * View pager adapter which handles the position of view pager and which fragment to display.
     */
    private static class ViewPagerAdapter extends FragmentStateAdapter{
        /**
         * Constructor which accepts payment activity instance
         * @param paymentActivity Payment activity instance
         */
        public ViewPagerAdapter(PaymentActivity paymentActivity){
            super(paymentActivity);
        }

        /**
         * Create fragment according to position
         * @param position Position of view pager
         * @return Returns the created fragment
         */
        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position){
                case 0:
                    return new BookingFragment();
                case 1:
                    return new RentingFragment();
                case 2:
                    return new HistoryFragment();
                default:
                    return null;
            }
        }

        /**
         * Get total amount of pages of the view pager
         * @return Return amount of pages
         */
        @Override
        public int getItemCount() {
            return pages;
        }
    }

    /**
     * Class which handles view pager animations upon scrolling
     */
    private static class ZoomOutPageTransformer implements ViewPager2.PageTransformer{
        /**
         * Minimum scale which the page zooms out
         */
        private static final float MIN_SCALE = 0.85f;
        /**
         * Minimum opacity when the page zooms out
         */
        private static final float MIN_ALPHA = 0.5f;

        /**
         * Plays animation upon scrolling
         * @param page Page to be zoomed out
         * @param position Position of view pager
         */
        @Override
        public void transformPage(@NonNull View page, float position) {
            int pageWidth = page.getWidth();
            int pageHeight = page.getHeight();

            if(position < -1){
                page.setAlpha(0f);
            }else if(position <= 1){
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horMargin = pageWidth * (1 - scaleFactor) / 2;

                if(position < 0){
                    page.setTranslationX(horMargin - vertMargin / 2);
                }else{
                    page.setTranslationX(-horMargin + vertMargin / 2);
                }
                page.setScaleX(scaleFactor);
                page.setScaleY(scaleFactor);
                page.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE)/(1 - MIN_SCALE)*(1 - MIN_SCALE));
            }else{
                page.setAlpha(0f);
            }
        }
    }

    /**
     * Get booking payment list
     */
    protected void getBookingPayment(){
        mApiService.getBookingPayment(
                LoginActivity.savedAccount.id
        ).enqueue(new Callback<List<Payment>>() {
            @Override
            public void onResponse(Call<List<Payment>> call, Response<List<Payment>> response) {
                bookingPaymentList.clear();
                bookingRoomList.clear();
                bookingRoomNames.clear();
                bookingPaymentList = response.body();
                if(!bookingPaymentList.isEmpty()) {
                    for (Payment p : bookingPaymentList) {
                        Room r = MainActivity.allRoomList.get(p.getRoomId());
                        bookingRoomList.add(r);
                        bookingRoomNames.add(r.name);
                    }
                }
                System.out.println("Book " + bookingRoomNames);
            }

            @Override
            public void onFailure(Call<List<Payment>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    /**
     * Get renting payment list
     */
    protected void getRentingPayment(){
        mApiService.getRentingPayment(
                LoginActivity.savedAccount.id
        ).enqueue(new Callback<List<Payment>>() {
            @Override
            public void onResponse(Call<List<Payment>> call, Response<List<Payment>> response) {
                rentingPaymentList.clear();
                rentingRoomList.clear();
                rentingRoomNames.clear();
                rentingPaymentList = response.body();
                if(!rentingPaymentList.isEmpty()) {
                    for (Payment p : rentingPaymentList) {
                        Room r = MainActivity.allRoomList.get(p.getRoomId());
                        rentingRoomList.add(r);
                        rentingRoomNames.add(r.name);
                    }
                }
                System.out.println("Rent " + rentingRoomNames);
            }

            @Override
            public void onFailure(Call<List<Payment>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    /**
     * Get completed payment list
     */
    protected void getCompletedPayment(){
        mApiService.getCompletedPayment(
                LoginActivity.savedAccount.id
        ).enqueue(new Callback<List<Payment>>() {
            @Override
            public void onResponse(Call<List<Payment>> call, Response<List<Payment>> response) {
                completedPaymentList.clear();
                completedRoomList.clear();
                completedRoomNames.clear();
                completedPaymentList = response.body();
                if(!completedPaymentList.isEmpty()) {
                    for (Payment p : completedPaymentList) {
                        Room r = MainActivity.allRoomList.get(p.getRoomId());
                        completedRoomList.add(r);
                        completedRoomNames.add(r.name);
                    }
                }
                System.out.println("Comp " + completedRoomNames);
            }

            @Override
            public void onFailure(Call<List<Payment>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

//    protected Room getRoom(int id, List<Room> lst){
//        mApiService.getRoom(id).enqueue(new Callback<Room>() {
//            @Override
//            public void onResponse(Call<Room> call, Response<Room> response) {
//                lst.add(response.body());
//                System.out.println(lst);
//            }
//
//            @Override
//            public void onFailure(Call<Room> call, Throwable t) {
//                t.printStackTrace();
//            }
//        });
//        return null;
//    }

    /**
     * Initialize all fragments
     */
    private void initFragments(){
        getBookingPayment();
        getRentingPayment();
        getCompletedPayment();
    }

    /**
     * Method used to destroy activity when back is pressed and goes back to {@link JuanjsleepRJ.jsleep_android.MainActivity}.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(PaymentActivity.this, MainActivity.class));
        finish();
    }

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
}