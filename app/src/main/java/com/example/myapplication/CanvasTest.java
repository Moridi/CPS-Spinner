package com.example.myapplication;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class CanvasTest extends AppCompatActivity {

    private float xPos = 0;
    private float yPos = 0;

    public static int x;
    public static int y;

    Circle circle  = null;
    ShapeDrawable mDrawable = new ShapeDrawable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_canvas_test);
        circle = new Circle(this);
        setContentView(circle);
    }

    public class Circle extends View {
        static final int width = 50;
        static final int height = 50;

        public Circle(Context context) {
            super(context);

            mDrawable = new ShapeDrawable(new OvalShape());
            mDrawable.getPaint().setColor(0xff74AC23);
            mDrawable.setBounds(x, y, x + width, y + height);
        }

        protected void onDraw(Canvas canvas) {
            RectF oval = new RectF(CanvasTest.x, CanvasTest.y, CanvasTest.x + width, CanvasTest.y + height); // set bounds of rectangle
            Paint p = new Paint(); // set some paint options
            p.setColor(Color.BLUE);
            canvas.drawOval(oval, p);
            invalidate();
        }



    }


    public void drawSomething(View view) {
        xPos += 20;
        yPos += 50;
        ObjectAnimator animation = ObjectAnimator.ofFloat(view, "translationX", xPos);
        animation.setDuration(2000);
        animation.start();
    }
}
