package com.actiknow.clearsale.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.actiknow.clearsale.R;


/**
 * Created by Admin on 08-05-2017.
 */
public class ExpandableTextView extends android.support.v7.widget.AppCompatTextView {
    private static final int DEFAULT_TRIM_LENGTH = 100;
    private static final String ELLIPSIS = "  View More..";
    
    private CharSequence originalText;
    private CharSequence trimmedText;
    private BufferType bufferType;
    private boolean trim = true;
    private int trimLength;
    
    public ExpandableTextView (Context context) {
        this (context, null);
    }
    
    public ExpandableTextView (Context context, AttributeSet attrs) {
        super (context, attrs);
        
        TypedArray typedArray = context.obtainStyledAttributes (attrs, R.styleable.ExpandableTextView);
        this.trimLength = typedArray.getInt (R.styleable.ExpandableTextView_trimLength, DEFAULT_TRIM_LENGTH);
        typedArray.recycle ();
    
        setOnTouchListener (new OnTouchListener () {
            @Override
            public boolean onTouch (View view, MotionEvent motionEvent) {
                trim = ! trim;
                setText ();
                requestFocusFromTouch ();
                return false;
            }
        });
    }
    
    private void setText () {
        super.setText (getDisplayableText (), bufferType);
    }
    
    private CharSequence getDisplayableText () {
        return trim ? originalText : trimmedText;
    }
    
    @Override
    public void setText (CharSequence text, BufferType type) {
        originalText = text;
        trimmedText = getTrimmedText (text);
        bufferType = type;
        setText ();
    }
    
    private CharSequence getTrimmedText (CharSequence text) {
        SpannableStringBuilder builder = new SpannableStringBuilder ();
        SpannableString str1 = new SpannableString (ELLIPSIS);
        str1.setSpan (new ForegroundColorSpan (getResources ().getColor (R.color.colorPrimary)), 0, str1.length (), 0);
        builder.append (str1);
        if (originalText != null && originalText.length () > trimLength) {
            return new SpannableStringBuilder (originalText, 0, trimLength + 100).append (str1);
        } else {
            return originalText;
        }
    }
    
    public CharSequence getOriginalText () {
        return originalText;
    }
    
    public int getTrimLength () {
        return trimLength;
    }
    
    public void setTrimLength (int trimLength) {
        this.trimLength = trimLength;
        trimmedText = getTrimmedText (originalText);
        setText ();
    }
}