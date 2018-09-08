package kr.ac.smu.kuni.testtest;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
/**
 * Created by Taekyun on 15. 9. 29..
 */
public class PaintTest extends AppCompatActivity {

    class Point {
        float x;
        float y;
        boolean isDraw;
        public Point(float x, float y, boolean isDraw) {
            this.x = x;
            this.y = y;
            this.isDraw = isDraw;
        }
    }
    class MyPaint extends View {
        public MyPaint(Context context) {
            super(context);
            setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            this.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch( event.getAction() ) {
                        case MotionEvent.ACTION_MOVE:
                            points.add(new Point(event.getX(), event.getY(), true));
                            invalidate();
                            break;
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_DOWN:
                            points.add(new Point(event.getX(), event.getY(), false));
                    }
                    return true;
                }
            });
        }
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            Paint p = new Paint();
            p.setColor(Color.MAGENTA);
            p.setStrokeWidth(10);
            for(int i=1; i<points.size(); i++) {
                if(!points.get(i).isDraw) continue;
                p.isAntiAlias();
                canvas.drawLine(points.get(i-1).x, points.get(i-1).y, points.get(i).x, points.get(i).y, p);
                //canvas.drawCircle(points.get(i-1).x, points.get(i-1).y, 10, p);
            }
        }
    }
    ArrayList<Point> points = new ArrayList<Point>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyPaint mp = new MyPaint(this);
        setContentView(mp);
    }
}