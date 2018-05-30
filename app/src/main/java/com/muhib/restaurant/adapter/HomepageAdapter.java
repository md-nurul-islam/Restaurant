package com.muhib.restaurant.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.muhib.restaurant.R;
import com.muhib.restaurant.activity.DetailsActivity;
import com.muhib.restaurant.activity.MainActivity;
import com.muhib.restaurant.fragment.HomeFragment;
import com.muhib.restaurant.fragment.OrderDetailsFragment;
import com.muhib.restaurant.myinterface.OrderActionListener;
import com.muhib.restaurant.myinterface.OrderProcess;
import com.muhib.restaurant.utils.PaginationAdapterCallback;

import java.util.ArrayList;
import java.util.List;

import model.BillingAddressaModel;
import model.CategoryModel;
import model.Products;
import model.ShippingAddressaModel;

/**
 * Created by Suleiman on 19/10/16.
 */

public class HomepageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements OrderActionListener {


    // View Types
    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private static final int HERO = 2;
    private static final int ADD = 5;
//    private String[] titleStringArray = {"খবর", "বিজ্ঞানপ্রযুক্তি", "অ্যাপস ও গেইমস", "চ্যাম্পিয়ন", "জীবনযাত্রা", "রিসোর্স সেন্টার", "খেলাধুলা", "বিনোদন", "ভিডিও"};
//    private int[] titleArray = {R.string.news, R.string.scitech, R.string.apps_games, R.string.champion, R.string.life_style, R.string.resource_center, R.string.sports, R.string.entertainment, R.string.video};
//    private int[] linkArray = {AppConstant.NEWS, AppConstant.SCITECH, AppConstant.APPS_GAMES, AppConstant.CHAMPION, AppConstant.LIFE_STYLE, AppConstant.RESOURCE_CENTER, AppConstant.SPORTS, AppConstant.ENTERTAINMENT, AppConstant.VIDEO};

    private static final String BASE_URL_IMG = "https://image.tmdb.org/t/p/w150";

    private List<Products> orderList;
    private List<String> strList = new ArrayList<>();
    private Context context;

    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;

    private PaginationAdapterCallback mCallback;
    private OrderProcess mOrderProcessCallback;
    View myView;
    LayoutInflater layoutInflater;
    LinearLayout layout;
    HomeFragment homeFragment;
//
    private String errorMsg;
    //HomeFragment homeFragment;
//
    public HomepageAdapter(Context context, PaginationAdapterCallback mCallback, HomeFragment homeFragment) {
        this.context = context;
        this.mCallback = mCallback;
        orderList = new ArrayList<>();
        //this.mOrderProcessCallback = orderProcessCallback;
        this.homeFragment = homeFragment;
    }

    public HomepageAdapter(Context context) {
        this.context = context;
        orderList = new ArrayList<>();
    }

    public HomepageAdapter(Context context, HomeFragment homeFragment, ArrayList<String> strList) {
        this.context = context;
        this.mCallback = mCallback;
        this.strList = strList;

    }

    public List<Products> getMovies() {
        return orderList;
    }

    public void setMovies(List<Products> orderList) {
        this.orderList = orderList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                View viewItem = inflater.inflate(R.layout.single_item_res_list, parent, false);
                viewHolder = new OrderListItem(viewItem);
                break;
            case LOADING:
                View viewLoading = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(viewLoading);
                break;
//            case HERO:
//                View viewHero = inflater.inflate(R.layout.item_hero, parent, false);
//                viewHolder = new HeroVH(viewHero);
//                break;
//            case ADD:
//                View viewAdd = inflater.inflate(R.layout.layout, parent, false);
//                viewHolder = new HeroAdd(viewAdd);
//                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Products result = orderList.get(position); // Movie
        final Bundle bundle = new Bundle();
        switch (getItemViewType(position)) {
//            case HERO:
//                final HeroVH topItem = (HeroVH) holder;
//                topItem.cardView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        int next=0;
//                        if(position>4)
//                            next = 5;
//                        else
//                            next = 1;
//                        ArrayList<CategoryModel> childList = new ArrayList<CategoryModel>(orderList.subList(position, position+next));
//                        String str = new Gson().toJson(childList);
//                        bundle.putString("childList", str);
//                        gotoSingleNewsFragment(bundle);
//                    }
//                });
//
//                topItem.mMovieTitle.setText(result.getTitle().getRendered());
//                topItem.mMovieDesc.setText(android.text.Html.fromHtml(result.getExcerptModel().getRendered()).toString());
//                loadImage(result.getEmbedded().getFeatureMedia().get(0).get("source_url").getAsString()).into(topItem.mPosterImg);
//                break;
            case ITEM:
                final OrderListItem itemHolder = (OrderListItem) holder;
                int total = orderList.get(position).getItemList().size();
                layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//                itemHolder.orderTitle.setText(orderList.get(position).getId());
//                itemHolder.name.setText("Pizza");
//                itemHolder.quantity.setText("Total ietm 10");
                //itemHolder.itemNameLayout.removeAllViews();
                String customerName = "";
                int totalItem = 0;
                for(int i=0; i< total; i++){
//                    myView = layoutInflater.inflate(R.layout.item_row_view, itemHolder.itemNameLayout, false);
//                    LinearLayout ll = (LinearLayout) myView.findViewById(R.id.ll);
//                    TextView name = (TextView)myView.findViewById(R.id.itemName);
//                    if(!orderList.get(position).getItemList().get(i).getName().isEmpty())
//                        name.setText(orderList.get(position).getItemList().get(i).getName());
//                    TextView itemNo = (TextView)myView.findViewById(R.id.itemNo);
//                    if(orderList.get(position).getItemList().get(i).getQuantity()>0)
//                        itemNo.setText(""+orderList.get(position).getItemList().get(i).getQuantity());
//                    itemHolder.itemNameLayout.addView(ll);
                    totalItem = totalItem + orderList.get(position).getItemList().get(i).getQuantity();

                }

                ShippingAddressaModel shippingAddressaModel;
                JsonElement jsonElement = orderList.get(position).getShippingTo();
                if (orderList.get(position).getShippingTo() != null) {
                    Gson gson = new GsonBuilder().create();
                    shippingAddressaModel = gson.fromJson(orderList.get(position).getShippingTo(), ShippingAddressaModel.class);
                    customerName = shippingAddressaModel.getFirstName() + " " + shippingAddressaModel.getLastName();
                }
                BillingAddressaModel billingAddressaModel;
                JsonElement jsonElementBilling = orderList.get(position).getBilling();
                if (customerName.trim().isEmpty() && orderList.get(position).getShippingTo() != null) {
                    Gson gson = new GsonBuilder().create();
                    billingAddressaModel = gson.fromJson(orderList.get(position).getBilling(), BillingAddressaModel.class);
                    customerName = billingAddressaModel.getFirstName() + " " + billingAddressaModel.getLastName();
                }
                if(!customerName.isEmpty()) {
                    itemHolder.customerNameText.setText("Ordered By  ");
                    itemHolder.name.setText(": "+customerName);
                }
                if(totalItem>0) {
                    itemHolder.totalItemText.setText("Total Quantity  ");
                    itemHolder.quantity.setText(": "+ String.valueOf(totalItem));
                }

                if(!orderList.get(position).getDateCreated().isEmpty() && orderList.get(position).getDateCreated()!=null)
                    itemHolder.orderDate.setText(dateTimeParse(orderList.get(position).getDateCreated()));
                itemHolder.totalPay.setText(": "+orderList.get(position).getTotal());
                itemHolder.totalPayText.setText("Total pay in BDT");
                itemHolder.status.setText(orderList.get(position).getStatus());
                if(orderList.get(position).getStatus().equalsIgnoreCase("pending"))
                {
                    itemHolder.accepted.setVisibility(View.VISIBLE);
                    itemHolder.rejected.setVisibility(View.VISIBLE);

                }
                else {
                    itemHolder.accepted.setVisibility(View.GONE);
                    itemHolder.rejected.setVisibility(View.GONE);
                }
//                itemHolder.orderTitle.setText(orderList.get(position).getTitle().getRendered());
////                movieVH.mYear.setText(formatYearLabel(result));
//                itemHolder.mMovieDesc.setText(android.text.Html.fromHtml(result.getExcerptModel().getRendered()).toString());
//                itemHolder.mPosterImg.setImageResource(R.drawable.sample);
//                if (position > 6 && position % 6 == 4) {
//                    itemHolder.moreNewsLayout.setVisibility(View.VISIBLE);
//                    if (position == 58)
//                        itemHolder.moreText.setText("আরও ভিডিও >>");
//                    else
//                        itemHolder.moreText.setText("আরও খবর >>");
//                } else
//                    itemHolder.moreNewsLayout.setVisibility(View.GONE);
//                itemHolder.moreNewsLayout.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Bundle bundle = new Bundle();
//                        bundle.putInt(AppConstant.SELECTED_ITEM, linkArray[(position/6)-1]);
//                        bundle.putString(AppConstant.TITLE, titleStringArray[(position/6)-1]);
//                        if(position==58){
//                            gotoPaginationSingleFragment(bundle);
//                        }
//                        else {
//                            gotoPagerFragment(bundle);
//                        }
//                    }
//                });
//                itemHolder.itemLayout.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        int next=0;
//                        if(position>4)
//                            next = 5-(position%6);
//                        else
//                            next = 1;
//                        ArrayList<CategoryModel> childList = new ArrayList<CategoryModel>(orderList.subList(position, position+next));
//                        String str = new Gson().toJson(childList);
//                        bundle.putString("childList", str);
//                        gotoSingleNewsFragment(bundle);
//                    }
//                });
//                itemHolder.menuOption.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        //creating a popup menu
//                        PopupMenu popup = new PopupMenu(context, itemHolder.menuOption);
//                        //inflating menu from xml resource
//                        popup.inflate(R.menu.option_menu);
//                        //adding click listener
//                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                            @Override
//                            public boolean onMenuItemClick(MenuItem item) {
//                                switch (item.getItemId()) {
//                                    case R.id.share:
//                                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
//                                        shareIntent.setType("text/plain");
//                                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, orderList.get(position).getTitle().getRendered());
//                                        shareIntent.putExtra(Intent.EXTRA_TITLE, orderList.get(position).getTitle().getRendered());
//                                        shareIntent.putExtra(Intent.EXTRA_TEXT, orderList.get(position).getLink());
//                                        context.startActivity(Intent.createChooser(shareIntent, "Share link using"));
//                                        break;
////                                    case R.id.save:
////                                        //handle menu2 click
////                                        break;
//                                }
//                                return false;
//                            }
//                        });
//                        //displaying the popup
//                        popup.show();
//                    }
//                });
//
////                // load movie thumbnail
//                try {
//                    loadThumbImage(result.getEmbedded().getFeatureMedia().get(0).get("source_url").getAsString())
//                            .listener(new RequestListener<String, GlideDrawable>() {
//                                @Override
//                                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                                    // TODO: 08/11/16 handle failure
//                                    itemHolder.mProgress.setVisibility(View.GONE);
//                                    return false;
//                                }
//
//                                @Override
//                                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                                    // image ready, hide progress now
//                                    itemHolder.mProgress.setVisibility(View.GONE);
//                                    return false;   // return false if you want Glide to handle everything else.
//                                }
//                            })
//                            .into(itemHolder.itemImage);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                itemHolder.accepted.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ordeProcess(true, orderList.get(position).getId());
                    }
                });
                itemHolder.rejected.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ordeProcess(false, orderList.get(position).getId());
                    }
                });
                itemHolder.itemLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //gotoOrderDetailsFragment(orderList.get(position).getId());
                        String str = new Gson().toJson(orderList.get(position));
                        bundle.putString("products", str);
                        gotoOrderDetailsFragment(bundle);
//                        Products products = orderList.get(position);
//
//                        Intent intent = new Intent(context, DetailsActivity.class);
//                        intent.putExtra("products", str);
//                        context.startActivity(intent);
//                        ((AppCompatActivity)context).finish();
                    }
                });
                break;

            case LOADING:
                LoadingVH loadingVH = (LoadingVH) holder;

                if (retryPageLoad) {
                    loadingVH.mErrorLayout.setVisibility(View.VISIBLE);
                    loadingVH.mProgressBar.setVisibility(View.GONE);

                    loadingVH.mErrorTxt.setText(
                            errorMsg != null ?
                                    errorMsg :
                                    context.getString(R.string.error_msg_unknown));

                } else {
                    loadingVH.mErrorLayout.setVisibility(View.GONE);
                    loadingVH.mProgressBar.setVisibility(View.VISIBLE);
                }
                break;
//            case ADD:
//                final HeroAdd addHolder = (HeroAdd) holder;
//                //heroAdd.mMovieTitle.setText(result.getTitle().getRendered());
//                if (position / 6 <= 8) {
//                    addHolder.title.setText(titleArray[position / 6]);
//                    addHolder.title.setVisibility(View.VISIBLE);
//                } else
//                    addHolder.title.setVisibility(View.GONE);
//                break;
        }
    }

    private void ordeProcess(boolean b, String id) {
        String status="";
        if(b)
            status = "Accepted";
        else
            status = "cancelled";
        Toast.makeText(context, "Order " + status , Toast.LENGTH_SHORT).show();
        if(b)
            homeFragment.processOrder(id);
        else
            homeFragment.callUpdateApi(id, "");

    }


    private void gotoOrderDetailsFragment(Bundle bundle) {
//        Bundle bundle = new Bundle();
//        bundle.putString("id", id);

        OrderDetailsFragment detailsFragment = new OrderDetailsFragment();
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        detailsFragment.setArguments(bundle);
        transaction.add(R.id.container, detailsFragment, "detailsFragment").addToBackStack(null);
        transaction.commit();
    }

    @Override
    public int getItemCount() {
        //return strList == null ? 0 : strList.size();
        return orderList == null ? 0 : orderList.size();
    }

    @Override
    public int getItemViewType(int position) {
//        if (position % 6 == 0) {
//            return HERO;
//        } else if ((position % 6) == 5) {
//            if (position == 23 && isLoadingAdded)
//                return LOADING;
//            else
//                return ADD;
//        } else {
//            return ITEM;
//        }

//        if (position!= 24 && position%6 == 0 ) {
//            return HERO;
//        }
//        else if((position%6) == 5)
//        {
//            return ADD;
//        }
//        else {
        return (position == orderList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
//        }
    }

    /*
        Helpers - bind Views
   _________________________________________________________________________________________________
    */

    /**
//     * @param result
     * @return [releasedate] | [2letterlangcode]
     */
//    private String formatYearLabel(Result result) {
//        return result.getReleaseDate().substring(0, 4)  // we want the year only
//                + " | "
//                + result.getOriginalLanguage().toUpperCase();
//    }

//    /**
//     * Using Glide to handle image loading.
//     * Learn more about Glide here:
//     * <a href="http://blog.grafixartist.com/image-gallery-app-android-studio-1-4-glide/" />
//     *
//     * @param posterPath from {@link Result#getPosterPath()}
//     * @return Glide builder
//     */
    private DrawableRequestBuilder<String> loadImage(@NonNull String posterPath) {
        return Glide
                .with(context)
                .load(posterPath)
                .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
//                .centerCrop()
                .crossFade();
    }

    private DrawableRequestBuilder<String> loadThumbImage(@NonNull String posterPath) {
        return Glide
                .with(context)
                .load(posterPath)
                .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                .centerCrop()
                .crossFade();
    }


    /*
        Helpers - Pagination
   _________________________________________________________________________________________________
    */

    public void add(Products r) {
        orderList.add(r);
        notifyItemInserted(orderList.size() - 1);
    }

    public void addAllData(List<Products> moveResults) {
        for (Products result : moveResults) {
            add(result);
        }

    }

    public void addAllNewData(List<Products> moveResults) {
        orderList.clear();
        orderList.addAll(moveResults);
        notifyDataSetChanged();
    }

    public void clearList() {
        orderList.clear();
    }


    public void remove(Products r) {
        int position = orderList.indexOf(r);
        if (position > -1) {
            orderList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Products());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = orderList.size() - 1;
        Products result = getItem(position);

        if (result != null) {
            orderList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Products getItem(int position) {
        return orderList.get(position);
    }

    /**
     * Displays Pagination retry footer view along with appropriate errorMsg
     *
     * @param show
     * @param errorMsg to display if page load fails
     */
    public void showRetry(boolean show, @Nullable String errorMsg) {
        retryPageLoad = show;
        notifyItemChanged(orderList.size() - 1);

        if (errorMsg != null) this.errorMsg = errorMsg;
    }

    @Override
    public void orderSubmited(String id) {
        Toast.makeText(context, "submit", Toast.LENGTH_SHORT).show();
    }


   /*
   View Holders
   _________________________________________________________________________________________________
    */

    /**
     * Header ViewHolder
     */
//    protected class HeroVH extends RecyclerView.ViewHolder {
//        private TextView mMovieTitle;
//        private TextView mMovieDesc;
//        private TextView mYear; // displays "year | language"
//        private ImageView mPosterImg;
//        private CardView cardView;
//
//        public HeroVH(View itemView) {
//            super(itemView);
//
//            mMovieTitle = (TextView) itemView.findViewById(R.id.movie_title);
//            mMovieDesc = (TextView) itemView.findViewById(R.id.movie_desc);
////            mYear = (TextView) itemView.findViewById(R.id.movie_year);
//            mPosterImg = (ImageView) itemView.findViewById(R.id.movie_poster);
//            cardView = (CardView) itemView.findViewById(R.id.cardView);
//        }
//    }

    /**
     * Main list's content ViewHolder
     */
    protected class OrderListItem extends RecyclerView.ViewHolder {
        private TextView orderTitle;
        private TextView accepted;
        private TextView name, quantity, totalPay, totalPayText, orderDate; // displays "year | language"
        private ImageView itemImage;
        private ProgressBar mProgress;
        private TextView menuOption;
        private LinearLayout itemLayout;
        private LinearLayout itemNameLayout;
        private TextView rejected;
        private TextView status, customerNameText, totalItemText;

        public OrderListItem(View itemView) {
            super(itemView);

//            orderTitle = (TextView) itemView.findViewById(R.id.orderTitle);
            orderDate = (TextView) itemView.findViewById(R.id.orderDate);
            name = (TextView) itemView.findViewById(R.id.customerName);
            quantity = (TextView) itemView.findViewById(R.id.totalItem);
            customerNameText = (TextView) itemView.findViewById(R.id.customerNameText);
            totalItemText = (TextView) itemView.findViewById(R.id.totalItemText);
            totalPay = (TextView) itemView.findViewById(R.id.totalPay);
            totalPayText = (TextView) itemView.findViewById(R.id.totalPayText);
            accepted = (TextView) itemView.findViewById(R.id.accept);
            rejected = (TextView) itemView.findViewById(R.id.reject);
            status = (TextView) itemView.findViewById(R.id.status);
            // Layout inflater

//            itemImage = (ImageView) itemView.findViewById(R.id.itemImage);
//            mProgress = (ProgressBar) itemView.findViewById(R.id.movie_progress);
//            menuOption = (TextView) itemView.findViewById(R.id.menuOptions);
            itemLayout = (LinearLayout) itemView.findViewById(R.id.itemLayout);
            itemNameLayout = (LinearLayout) itemView.findViewById(R.id.itemNameLayout);
//            moreText = (TextView) itemView.findViewById(R.id.moreText);
        }
    }

//    protected class HeroAdd extends RecyclerView.ViewHolder {
//        private TextView title;
//
//        public HeroAdd(View itemView) {
//            super(itemView);
//
//            title = (TextView) itemView.findViewById(R.id.title);
//
//        }
//    }


    protected class LoadingVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ProgressBar mProgressBar;
        private ImageButton mRetryBtn;
        private TextView mErrorTxt;
        private LinearLayout mErrorLayout;

        public LoadingVH(View itemView) {
            super(itemView);

            mProgressBar = (ProgressBar) itemView.findViewById(R.id.loadmore_progress);
            mRetryBtn = (ImageButton) itemView.findViewById(R.id.loadmore_retry);
            mErrorTxt = (TextView) itemView.findViewById(R.id.loadmore_errortxt);
            mErrorLayout = (LinearLayout) itemView.findViewById(R.id.loadmore_errorlayout);

            mRetryBtn.setOnClickListener(this);
            mErrorLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.loadmore_retry:
                case R.id.loadmore_errorlayout:

                    showRetry(false, null);
                    mCallback.retryPageLoad();

                    break;
            }
        }
    }

    private String dateTimeParse(String dateTime){
        String parsedString = "";
        if(dateTime.contains("T")){
            String[] parts = dateTime.split("T");
            if(!parts[0].isEmpty() && parts[0]!=null)
                parsedString = parsedString + dateReverse(parts[0]);
            if(!parts[1].isEmpty() && parts[1]!=null)
                parsedString = parsedString + "  "+ parts[1];
        }

        return parsedString;
    }

    public static String dateReverse(String duedate){
        String result = "";
        String dateText = duedate;
        if(dateText!= null && dateText.contains("-")) {
            String[] parts = dateText.split("-");
            if(!parts[2].isEmpty())
                result = result + parts[2];
            if(!parts[1].isEmpty())
                result = result +"-"+ parts[1];
            if(!parts[0].isEmpty())
                result = result + "-"+ parts[0];
        }
        return result;
    }

}
