package com.codelipi.ecomm.options;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codelipi.ecomm.LoginActivity;
import com.codelipi.ecomm.ViewAddress;
import com.codelipi.ecomm.product.ItemDetailsActivity;
import com.codelipi.ecomm.startup.MainActivity;
import com.codelipi.ecomm.utility.ImageUrlUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import com.codelipi.ecomm.R;
import static com.codelipi.ecomm.fragments.ImageListFragment.STRING_IMAGE_POSITION;
import static com.codelipi.ecomm.fragments.ImageListFragment.STRING_IMAGE_URI;

public class CartListActivity extends AppCompatActivity {
    private static Context mContext;
    String loginemail;
    TextView amount;
    int totalamount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_list);
        mContext = CartListActivity.this;
        SharedPreferences pref = getSharedPreferences("MyPref1", 0);
        String logintype = pref.getString("logintype",null);
        loginemail = pref.getString("loginemail",null);
        ImageUrlUtils imageUrlUtils = new ImageUrlUtils();
        ArrayList<String> cartlistImageUri =imageUrlUtils.getCartListImageUri();
        ArrayList<String> cartlistNameUri =imageUrlUtils.getCartListNameUri();
        ArrayList<String> cartlistPriceUri =imageUrlUtils.getCartListPriceUri();
        amount=findViewById(R.id.text_action_bottom1);
        //Show cart layout based on items
        setCartLayout();
         TextView text_action_bottom2= (TextView)findViewById(R.id.text_action_bottom2);
         text_action_bottom2.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 if(loginemail!=null)
                 {
                     Intent i=new Intent(CartListActivity.this,ViewAddress.class);
                     i.putExtra("from","Payment");
                     i.putExtra("Total",amount.getText().toString());
                     startActivity(i);
                 }else
                 {
                     Intent i=new Intent(CartListActivity.this,LoginActivity.class);
                     startActivity(i);
                 }
             }
         });
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager recylerViewLayoutManager = new LinearLayoutManager(mContext);

        recyclerView.setLayoutManager(recylerViewLayoutManager);
        recyclerView.setAdapter(new CartListActivity.SimpleStringRecyclerViewAdapter(recyclerView, cartlistImageUri,cartlistNameUri,cartlistPriceUri,CartListActivity.this));
        updateAmount();

    }

    private void updateAmount() {
        totalamount=0;
        ImageUrlUtils imageUrlUtils = new ImageUrlUtils();
        List<String> cartlistPriceUri1 =imageUrlUtils.getCartListPriceUri();
        for(int i=0;i<cartlistPriceUri1.size();i++)
        {
            totalamount=totalamount+Integer.parseInt(cartlistPriceUri1.get(i));
        }
        amount.setText("Rs. "+totalamount+"");
    }

    public static class SimpleStringRecyclerViewAdapter
            extends RecyclerView.Adapter<CartListActivity.SimpleStringRecyclerViewAdapter.ViewHolder> {

        private ArrayList<String> mCartlistImageUri,mCartlistNameUri,mCartlistPriceUri;
        private RecyclerView mRecyclerView;
        public CartListActivity cartListActivity;

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView itemname,itemprice;
            public final SimpleDraweeView mImageView;
            public final LinearLayout mLayoutItem;

            public final RelativeLayout mLayoutRemove;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mImageView = (SimpleDraweeView) view.findViewById(R.id.image_cartlist);
                mLayoutItem = (LinearLayout) view.findViewById(R.id.layout_item_desc);
                itemname = (TextView) view.findViewById(R.id.itemname);
                itemprice = (TextView) view.findViewById(R.id.itemprice);

                mLayoutRemove = (RelativeLayout) view.findViewById(R.id.layout_action1);
            }
        }

        public SimpleStringRecyclerViewAdapter(RecyclerView recyclerView, ArrayList<String> wishlistImageUri, ArrayList<String> wishlistNameUri, ArrayList<String> wishlistPriceUri, CartListActivity cartListActivity) {
            mCartlistImageUri = wishlistImageUri;
            mCartlistNameUri = wishlistNameUri;
            mCartlistPriceUri = wishlistPriceUri;
            mRecyclerView = recyclerView;
            this.cartListActivity=cartListActivity;
        }

        @Override
        public CartListActivity.SimpleStringRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_cartlist_item, parent, false);
            return new CartListActivity.SimpleStringRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onViewRecycled(CartListActivity.SimpleStringRecyclerViewAdapter.ViewHolder holder) {
            if (holder.mImageView.getController() != null) {
                holder.mImageView.getController().onDetach();
            }
            if (holder.mImageView.getTopLevelDrawable() != null) {
                holder.mImageView.getTopLevelDrawable().setCallback(null);
//                ((BitmapDrawable) holder.mImageView.getTopLevelDrawable()).getBitmap().recycle();
            }
        }

        @Override
        public void onBindViewHolder(final CartListActivity.SimpleStringRecyclerViewAdapter.ViewHolder holder, final int position) {
            final Uri uri = Uri.parse(mCartlistImageUri.get(position));
            holder.mImageView.setImageURI(uri);
            holder.itemname.setText(mCartlistNameUri.get(position));
            holder.itemprice.setText("Rs. "+mCartlistPriceUri.get(position));
            holder.mLayoutItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ItemDetailsActivity.class);
                    intent.putExtra(STRING_IMAGE_URI,mCartlistImageUri.get(position));
                    intent.putExtra("Name",mCartlistNameUri.get(position));
                    intent.putExtra("Price",mCartlistPriceUri.get(position));
                    intent.putExtra(STRING_IMAGE_POSITION, position);
                    mContext.startActivity(intent);
                }
            });

           //Set click action
            holder.mLayoutRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImageUrlUtils imageUrlUtils = new ImageUrlUtils();
                    imageUrlUtils.removeCartListImageUri(position);
                    notifyDataSetChanged();
                    cartListActivity.updateAmount();
                    //Decrease notification count
                    MainActivity.notificationCountCart--;

                }
            });

            //Set click action
           /* holder.mLayoutEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });*/
        }

        @Override
        public int getItemCount() {
            return mCartlistImageUri.size();
        }
    }

    protected void setCartLayout(){
        LinearLayout layoutCartItems = (LinearLayout) findViewById(R.id.layout_items);
        LinearLayout layoutCartPayments = (LinearLayout) findViewById(R.id.layout_payment);
        LinearLayout layoutCartNoItems = (LinearLayout) findViewById(R.id.layout_cart_empty);

        if(MainActivity.notificationCountCart >0){
            layoutCartNoItems.setVisibility(View.GONE);
            layoutCartItems.setVisibility(View.VISIBLE);
            layoutCartPayments.setVisibility(View.VISIBLE);
        }else {
            layoutCartNoItems.setVisibility(View.VISIBLE);
            layoutCartItems.setVisibility(View.GONE);
            layoutCartPayments.setVisibility(View.GONE);


           /* try {
                PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
                for (Signature signature : info.signatures) {
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                    String hashKey = new String(Base64.encode(md.digest(), 0));
                    Log.i("Code", "printHashKey() Hash Key: " + hashKey);
                }
            } catch (NoSuchAlgorithmException e) {
                Log.e("Error", "printHashKey()", e);
            } catch (Exception e) {
                Log.e("Error", "printHashKey()", e);
            }
            */

            Button bStartShopping = (Button) findViewById(R.id.bAddNew);
            bStartShopping.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }
    }
}
