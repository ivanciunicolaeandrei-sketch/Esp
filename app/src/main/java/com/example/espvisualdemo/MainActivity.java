package com.example.espvisualdemo;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private DemoView demo;
    private TextView status;
    private LinearLayout floatingPanel;
    private boolean visuals=true, names=true, distances=true, demoAim=false, menuVisible=true;
    private int boxColor=Color.rgb(70,235,170);
    private float boxThickness=3f;
    private int targetIndex=0;

    @Override public void onCreate(Bundle b) {
        super.onCreate(b);
        showLogin();
    }

    private int dp(float v) { return (int)(v*getResources().getDisplayMetrics().density+0.5f); }
    private GradientDrawable bg(int color,int radius) {
        GradientDrawable d=new GradientDrawable(); d.setColor(color); d.setCornerRadius(dp(radius)); return d;
    }
    private TextView label(String s,int size,int color) {
        TextView t=new TextView(this); t.setText(s); t.setTextSize(size); t.setTextColor(color); return t;
    }
    private Button button(String s) {
        Button b=new Button(this); b.setText(s); b.setAllCaps(false); return b;
    }

    private void showLogin() {
        LinearLayout page=new LinearLayout(this); page.setOrientation(1); page.setGravity(Gravity.CENTER);
        page.setPadding(dp(28),dp(28),dp(28),dp(28)); page.setBackgroundColor(Color.rgb(9,14,24));
        TextView title=label("ESP  /  VISUAL LAB",25,Color.WHITE); title.setTypeface(null,Typeface.BOLD);
        page.addView(title);
        TextView sub=label("PRIVATE DEMO ACCESS",11,Color.rgb(85,225,175));
        sub.setPadding(0,dp(8),0,dp(24)); page.addView(sub);
        EditText pass=new EditText(this); pass.setSingleLine(true); pass.setHint("Enter demo password");
        pass.setHintTextColor(Color.GRAY); pass.setTextColor(Color.WHITE); pass.setInputType(129);
        pass.setPadding(dp(14),0,dp(14),0); pass.setBackground(bg(Color.rgb(26,36,52),12));
        page.addView(pass,new LinearLayout.LayoutParams(-1,dp(52)));
        Button unlock=button("UNLOCK DEMO"); unlock.setOnClickListener(v->{
            if("nikolas".contentEquals(pass.getText())) showDashboard();
            else Toast.makeText(this,"Incorrect demo password",Toast.LENGTH_SHORT).show();
        });
        LinearLayout.LayoutParams bp=new LinearLayout.LayoutParams(-1,dp(54)); bp.topMargin=dp(14); page.addView(unlock,bp);
        TextView note=label("Offline sandbox only • no game connection",11,Color.LTGRAY);
        note.setPadding(0,dp(20),0,0); page.addView(note);
        setContentView(page);
    }

    private void showDashboard() {
        LinearLayout root=new LinearLayout(this); root.setOrientation(1); root.setPadding(dp(16),dp(18),dp(16),dp(14));
        root.setBackgroundColor(Color.rgb(9,14,24));
        TextView title=label("ESP VISUAL LAB",23,Color.WHITE); title.setTypeface(null,Typeface.BOLD); root.addView(title);
        TextView sub=label("SANDBOX BUILD  •  SIMULATED TARGETS",10,Color.rgb(80,220,170));
        sub.setPadding(0,dp(4),0,dp(12)); root.addView(sub);
        status=label("● DEMO READY",12,Color.rgb(90,220,170)); status.setPadding(dp(12),dp(10),0,dp(10));
        status.setBackground(bg(Color.rgb(24,37,49),10)); root.addView(status);

        FrameLayout stage=new FrameLayout(this); stage.setBackground(bg(Color.rgb(13,22,34),14));
        LinearLayout.LayoutParams sp=new LinearLayout.LayoutParams(-1,0,1); sp.topMargin=dp(12); sp.bottomMargin=dp(10); root.addView(stage,sp);
        demo=new DemoView(); stage.addView(demo,new FrameLayout.LayoutParams(-1,-1));

        floatingPanel=new LinearLayout(this); floatingPanel.setOrientation(1); floatingPanel.setPadding(dp(10),dp(8),dp(10),dp(8));
        floatingPanel.setBackground(bg(0xE91A2939,12));
        TextView handle=label("⋮  DRAG MENU",11,Color.WHITE); handle.setTypeface(null,Typeface.BOLD);
        floatingPanel.addView(handle);
        Button hide=button("Hide / show menu"); hide.setOnClickListener(v->{
            menuVisible=!menuVisible; floatingPanel.setVisibility(menuVisible?View.VISIBLE:View.GONE);
            if(!menuVisible) Toast.makeText(this,"Tap SHOW MENU below",Toast.LENGTH_SHORT).show();
        }); floatingPanel.addView(hide);
        floatingPanel.setOnTouchListener(new View.OnTouchListener(){
            float dx,dy;
            public boolean onTouch(View v,MotionEvent e){
                FrameLayout.LayoutParams lp=(FrameLayout.LayoutParams)v.getLayoutParams();
                if(e.getAction()==MotionEvent.ACTION_DOWN){dx=e.getRawX()-lp.leftMargin;dy=e.getRawY()-lp.topMargin;return true;}
                if(e.getAction()==MotionEvent.ACTION_MOVE){lp.leftMargin=(int)(e.getRawX()-dx);lp.topMargin=(int)(e.getRawY()-dy);lp.gravity=Gravity.TOP|Gravity.LEFT;v.setLayoutParams(lp);return true;}
                return e.getAction()==MotionEvent.ACTION_UP;
            }
        });
        FrameLayout.LayoutParams fp=new FrameLayout.LayoutParams(dp(156),-2,Gravity.TOP|Gravity.RIGHT);
        fp.setMargins(0,dp(8),dp(8),0); stage.addView(floatingPanel,fp);

        Switch esp=new Switch(this); esp.setText("ESP BOXES"); esp.setTextColor(Color.WHITE); esp.setChecked(visuals);
        esp.setOnCheckedChangeListener((b,c)->{visuals=c;demo.invalidate();}); root.addView(esp);
        Switch aim=new Switch(this); aim.setText("DEMO DEMO AIMBOT"); aim.setTextColor(Color.rgb(255,205,100));
        aim.setOnCheckedChangeListener((b,c)->{demoAim=c;status.setText(c?"● DEMO AIM ASSIST: ON (VISUAL ONLY)":"● DEMO READY");demo.invalidate();}); root.addView(aim);
        TextView aimNote=label("Aimbot demo only: highlights a sample target; no aiming, taps, or game control.",10,Color.LTGRAY);
        root.addView(aimNote);

        LinearLayout actions=new LinearLayout(this); actions.setOrientation(0); actions.setGravity(Gravity.CENTER);
        Button settings=button("SETTINGS"); settings.setOnClickListener(v->showSettings()); actions.addView(settings,new LinearLayout.LayoutParams(0,dp(48),1));
        Button show=button("SHOW MENU"); show.setOnClickListener(v->{menuVisible=true;floatingPanel.setVisibility(View.VISIBLE);}); actions.addView(show,new LinearLayout.LayoutParams(0,dp(48),1));
        root.addView(actions);
        TextView foot=label("No connection to Standoff 2 • sample data only",10,Color.GRAY); foot.setGravity(Gravity.CENTER); foot.setPadding(0,dp(8),0,0); root.addView(foot);
        setContentView(root);
        AlphaAnimation fade=new AlphaAnimation(0f,1f); fade.setDuration(350); root.startAnimation(fade);
    }

    private void showSettings() {
        LinearLayout panel=new LinearLayout(this); panel.setOrientation(1); panel.setPadding(dp(20),dp(20),dp(20),dp(20)); panel.setBackgroundColor(Color.rgb(12,18,29));
        TextView title=label("VISUAL SETTINGS",21,Color.WHITE); title.setTypeface(null,Typeface.BOLD); panel.addView(title);
        Switch n=new Switch(this); n.setText("Show target names"); n.setTextColor(Color.WHITE); n.setChecked(names); n.setOnCheckedChangeListener((b,c)->{names=c;demo.invalidate();}); panel.addView(n);
        Switch d=new Switch(this); d.setText("Show distance labels"); d.setTextColor(Color.WHITE); d.setChecked(distances); d.setOnCheckedChangeListener((b,c)->{distances=c;demo.invalidate();}); panel.addView(d);
        TextView thick=label("BOX THICKNESS",13,Color.WHITE); thick.setPadding(0,dp(18),0,0); panel.addView(thick);
        SeekBar seek=new SeekBar(this); seek.setMax(8); seek.setProgress((int)boxThickness-1); seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            public void onProgressChanged(SeekBar s,int p,boolean u){boxThickness=p+1;demo.invalidate();}
            public void onStartTrackingTouch(SeekBar s){} public void onStopTrackingTouch(SeekBar s){}
        }); panel.addView(seek);
        TextView colors=label("BOX COLOUR",13,Color.WHITE); colors.setPadding(0,dp(14),0,dp(4)); panel.addView(colors);
        LinearLayout row=new LinearLayout(this);
        int[] palette={Color.rgb(70,235,170),Color.CYAN,Color.YELLOW,Color.MAGENTA,Color.RED};
        String[] names={"Mint","Cyan","Yellow","Pink","Red"};
        for(int i=0;i<palette.length;i++){final int c=palette[i];Button b=button(names[i]);b.setOnClickListener(v->{boxColor=c;demo.invalidate();});row.addView(b,new LinearLayout.LayoutParams(0,dp(46),1));}
        panel.addView(row);
        Button back=button("DONE"); back.setOnClickListener(v->showDashboard()); panel.addView(back);
        setContentView(panel);
    }

    private class DemoView extends View {
        private final Paint p=new Paint(3);
        DemoView(){super(MainActivity.this);}
        @Override protected void onDraw(Canvas c){
            super.onDraw(c); float w=getWidth(),h=getHeight();
            p.setColor(0x2434485D);p.setStrokeWidth(1);
            for(int x=0;x<w;x+=dp(42))c.drawLine(x,0,x,h,p);
            for(int y=0;y<h;y+=dp(42))c.drawLine(0,y,w,y,p);
            p.setColor(Color.LTGRAY);p.setTextSize(dp(11));c.drawText("OFFLINE TARGET SIMULATOR",dp(10),dp(22),p);
            float[][] pts={{w*.25f,h*.38f},{w*.69f,h*.57f},{w*.48f,h*.78f}};
            String[] ds={"12 m","24 m","08 m"};
            for(int i=0;i<pts.length;i++){
                float x=pts[i][0],y=pts[i][1];
                if(visuals){
                    p.setColor(boxColor);p.setStyle(Paint.Style.STROKE);p.setStrokeWidth(dp(boxThickness));
                    c.drawRect(x-dp(35),y-dp(53),x+dp(35),y+dp(53),p);
                    p.setStyle(Paint.Style.FILL);
                    if(names){p.setTextSize(dp(11));p.setTypeface(Typeface.DEFAULT_BOLD);c.drawText("BOT 0"+(i+1),x-dp(35),y-dp(61),p);}
                    if(distances){p.setTextSize(dp(10));p.setTypeface(Typeface.DEFAULT);c.drawText(ds[i],x-dp(35),y+dp(67),p);}
                }
            }
            if(demoAim){
                targetIndex=(targetIndex+1)%3; float x=pts[targetIndex][0],y=pts[targetIndex][1];
                p.setColor(Color.rgb(255,190,70));p.setStyle(Paint.Style.STROKE);p.setStrokeWidth(dp(2));
                c.drawCircle(x,y,dp(24),p);c.drawLine(w/2,h*.92f,x,y,p);
                p.setStyle(Paint.Style.FILL);p.setTextSize(dp(10));c.drawText("DEMO LOCK",x-dp(30),y-dp(72),p);
                postInvalidateDelayed(650);
            }
        }
    }
}
