package com.sample.swipetodismissview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SwipeToDismissView.SwipeCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // To use swipe to dismiss functionality first create object of SwipeToDismissView class
        // using this object call init method with parent and child id that's it
        // Implement callback if view is dismiss or cancel
        SwipeToDismissView view1 = new SwipeToDismissView(this);
        view1.init(findViewById(R.id.swipe_container1), findViewById(R.id.btn_swipe1));

        SwipeToDismissView view2 = new SwipeToDismissView(this);
        view2.init(findViewById(R.id.swipe_container2), findViewById(R.id.btn_swipe2));
    }

    @Override
    public void onDismiss(View view) {
        Toast.makeText(this, String.valueOf("OnDismiss: " + view.getId()), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCancel(View view) {
        Toast.makeText(this, String.valueOf("OnCancel: " + view.getId()), Toast.LENGTH_SHORT).show();
    }
}
