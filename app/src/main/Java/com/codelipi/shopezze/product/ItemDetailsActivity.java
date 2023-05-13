package com.codelipi.ecomm.product;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.codelipi.ecomm.R;
import com.codelipi.ecomm.fragments.ImageListFragment;
import com.codelipi.ecomm.fragments.ViewPagerActivity;
import com.codelipi.ecomm.notification.NotificationCountSetClass;
import com.codelipi.ecomm.options.CartListActivity;
import com.codelipi.ecomm.startup.MainActivity;
import com.codelipi.ecomm.utility.ImageUrlUtils;
import com.facebook.drawee.view.SimpleDraweeView;

public class ItemDetailsActivity extends AppCompatActivity {
    int imagePosition;
    TextView itemname,itemprice,firstDetail,secondDetail,thirdDetail;
    String stringImageUri,name,price,first,second,third;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        SimpleDraweeView mImageView = (SimpleDraweeView)findViewById(R.id.image1);
        TextView textViewAddToCart = (TextView)findViewById(R.id.text_action_bottom1);
          itemname = (TextView)findViewById(R.id.itemname);
          itemprice = (TextView)findViewById(R.id.itemprice);
          firstDetail=findViewById(R.id.first);
          secondDetail=findViewById(R.id.second);
          thirdDetail=findViewById(R.id.third);
        TextView textViewBuyNow = (TextView)findViewById(R.id.text_action_bottom2);

        //Getting image uri from previous screen
        if (getIntent() != null) {
            stringImageUri = getIntent().getStringExtra(ImageListFragment.STRING_IMAGE_URI);
            imagePosition = getIntent().getIntExtra(ImageListFragment.STRING_IMAGE_URI,0);
            name = getIntent().getStringExtra("Name");
            price= getIntent().getStringExtra("Price");
            first= getIntent().getStringExtra("FirstD");
            second= getIntent().getStringExtra("SecondD");
            third= getIntent().getStringExtra("ThirdD");
        }
        itemname.setText(name);
        itemprice.setText("Rs. "+price);
        firstDetail.setText("\u2022 "+first);
        secondDetail.setText("\u2022 "+second);
        thirdDetail.setText("\u2022 "+third);
        Uri uri = Uri.parse(stringImageUri);
        mImageView.setImageURI(uri);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(ItemDetailsActivity.this, ViewPagerActivity.class);
                    intent.putExtra("position", imagePosition);
                    startActivity(intent);

            }
        });

        textViewAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageUrlUtils imageUrlUtils = new ImageUrlUtils();
                imageUrlUtils.addCartListImageUri(stringImageUri,name,price);
                Toast.makeText(ItemDetailsActivity.this,"Item added to cart.", Toast.LENGTH_SHORT).show();
                MainActivity.notificationCountCart++;
                NotificationCountSetClass.setNotifyCount(MainActivity.notificationCountCart);
            }
        });

        textViewBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageUrlUtils imageUrlUtils = new ImageUrlUtils();
                imageUrlUtils.addCartListImageUri(stringImageUri,name,price);
                MainActivity.notificationCountCart++;
                NotificationCountSetClass.setNotifyCount(MainActivity.notificationCountCart);
                startActivity(new Intent(ItemDetailsActivity.this, CartListActivity.class));

            }
        });

    }
}
