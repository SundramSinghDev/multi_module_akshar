/*
 * Copyright/**
 *          * CedCommerce
 *           *
 *           * NOTICE OF LICENSE
 *           *
 *           * This source file is subject to the End User License Agreement (EULA)
 *           * that is bundled with this package in the file LICENSE.txt.
 *           * It is also available through the world-wide-web at this URL:
 *           * http://cedcommerce.com/license-agreement.txt
 *           *
 *           * @category  Ced
 *           * @package   MageNative
 *           * @author    CedCommerce Core Team <connect@cedcommerce.com >
 *           * @copyright Copyright CEDCOMMERCE (http://cedcommerce.com/)
 *           * @license      http://cedcommerce.com/license-agreement.txt
 *
 */
package com.akshar.store.Ced_MageNative_FontSetting;
import android.content.Context;
import android.graphics.Typeface;
import androidx.appcompat.widget.AppCompatButton;

import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
public class Ced_FontSetting
{
    public  void setFontforTextviews(TextView view,String font,Context context)
    {
        Typeface avnbook = Typeface.createFromAsset(context.getAssets(), "fonts/"+font);
        view.setTypeface(avnbook);
    }
    public void setfontforButtons(AppCompatButton button,String font,Context context)
    {
        Typeface avnbook = Typeface.createFromAsset(context.getAssets(), "fonts/"+font);
        button.setTypeface(avnbook);
    }
    public void setfontforRadio(RadioButton button, String font, Context context)
    {
        Typeface avnbook = Typeface.createFromAsset(context.getAssets(), "fonts/"+font);
        button.setTypeface(avnbook);
    }
    public void set_regular_font_forCheckBox(CheckBox button, String font, Context context)
    {
        Typeface avnbook = Typeface.createFromAsset(context.getAssets(), "fonts/"+font);
        button.setTypeface(avnbook);
    }
    public void setfontforEditText(EditText editText,String font,Context context)
    {
        Typeface avnbook = Typeface.createFromAsset(context.getAssets(), "fonts/"+font);
        editText.setTypeface(avnbook);
    }
}
