/*
 *
 *  * Copyright/**
 *  *          * CedCommerce
 *  *           *
 *  *           * NOTICE OF LICENSE
 *  *           *
 *  *           * This source file is subject to the End User License Agreement (EULA)
 *  *           * that is bundled with this package in the file LICENSE.txt.
 *  *           * It is also available through the world-wide-web at this URL:
 *  *           * http://cedcommerce.com/license-agreement.txt
 *  *           *
 *  *           * @category  Ced
 *  *           * @package   MageNative
 *  *           * @author    CedCommerce Core Team <connect@cedcommerce.com >
 *  *           * @copyright Copyright CEDCOMMERCE (http://cedcommerce.com/)
 *  *           * @license      http://cedcommerce.com/license-agreement.txt
 *  *
 *
 */

package com.akshar.store.ui.notificationactivity;

import android.app.Application;


/**
 * Created by developer on 2/26/2016.
 */
public class MyApplication extends Application {
   /* public static final String TAG = MyApplication.class
            .getSimpleName();*/

    private static MyApplication mInstance;

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

    }
}
