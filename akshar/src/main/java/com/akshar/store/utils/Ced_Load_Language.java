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
package com.akshar.store.utils;
import android.content.Context;
import android.content.res.Configuration;
import java.util.Locale;
public class Ced_Load_Language
{
    public void setLanguagetoLoad(String Language,Context context)
    {
        setLocale(Language,context); //set langauge of the app
    }
    public static void setLocale(String local, Context con)
    {
        Locale locale = new Locale(local);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        con.getResources().updateConfiguration(config, con.getResources().getDisplayMetrics());
    }
}
