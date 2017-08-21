package com.thyrocare.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.thyrocare.utils.app.UiUtils;


public class RoundedImageView extends AppCompatImageView {
	Context context;
	private Paint paint;
	private Paint paintBorder;
	private BitmapShader shader;
	public RoundedImageView(Context context) {
		super(context);
		this.context = context;
		setup();
	}

	public RoundedImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		setup();
	}

	public RoundedImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		setup();
	}
	private void setup()
	{
		paint = new Paint();
		paint.setAntiAlias(true);
		paintBorder = new Paint();
		setBorderColor(Color.RED);
		paintBorder.setAntiAlias(true);
		this.setLayerType(LAYER_TYPE_SOFTWARE, paintBorder);
		paintBorder.setShadowLayer(24.0f, 0.0f, 12.0f, Color.GREEN);
	}

	public void setBorderColor(int borderColor)
	{
		if (paintBorder != null)
			paintBorder.setColor(borderColor);
		this.invalidate();
	}
	@Override
	protected void onDraw(Canvas canvas) {
		Drawable drawable = getDrawable();
		if (drawable == null){
			return;
		}

		if (getWidth() == 0 || getHeight() == 0){
			return;
		}
		Bitmap b = ((BitmapDrawable) drawable).getBitmap();
		Bitmap bitmap = b.copy(Config.ARGB_8888, true);

		int w = getWidth(), h = getHeight();


		Bitmap roundBitmap = getCroppedBitmap(bitmap, Math.min(w, h));
		canvas.drawBitmap(roundBitmap, 0, 0, null);



	}

	public Bitmap getCroppedBitmap(Bitmap bmp, int radius) {
		Bitmap sbmp;
		if (bmp.getWidth() != radius || bmp.getHeight() != radius)
			sbmp = Bitmap.createScaledBitmap(bmp, radius, radius, false);
		else
			sbmp = bmp;
		Bitmap output = Bitmap.createBitmap(sbmp.getWidth(),
		                                    sbmp.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		@SuppressWarnings("unused")
		final int color = 0xffa19774;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, sbmp.getWidth(), sbmp.getHeight());
		RectF rectF=new RectF(rect);

		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setDither(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(Color.parseColor("#BAB399"));

//        canvas.drawRoundRect(rectF,10.00f,10.0f,paint);
		canvas.drawCircle(sbmp.getWidth() / 2 + 0.7f, sbmp.getHeight() / 2 + 0.7f,
		                  sbmp.getWidth() / 2 - UiUtils.dpToPx(context, 2), paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(sbmp, rect, rect, paint);


		return output;
	}

}