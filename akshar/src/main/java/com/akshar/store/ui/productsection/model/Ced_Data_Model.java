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

package com.akshar.store.ui.productsection.model;

import org.json.JSONArray;

/*Created by developer on 2/23/2016.
 */
public class Ced_Data_Model {

    // Getter and Setter model for recycler view items
    private String title;
    private String image;

    public String getId()
    {
        return id;
    }

    private String id;
    private  String special_price;
    private String regular_price;
    private String currency_symbol;
    private String stock_status;

    public Ced_Data_Model(String title, String image, String id) {
        this.title = title;
        this.image = image;
        this.id = id;
    }
    public Ced_Data_Model(String title, String image, String id, String special_price, String regural_price,String currency_sym,String stock_status)
    {

        this.title = title;
        this.image = image;
        this.id = id;
        this.special_price = special_price;
        this.regular_price = regural_price;
        this.currency_symbol = currency_sym;
        this.stock_status = stock_status;
    }

    public Ced_Data_Model(String title, String image, String id,String normal_price,String special_price) {

        this.title = title;
        this.image = image;
        this.id = id;
        this.regular_price = normal_price;
        this.special_price = special_price;

    }

    public String getTitle() {
        return title;
    }



    public String getImage() {
        return image;
    }

    public String getSpecial_price(){

        return special_price;
    }
    public String getRegular_price(){
        return regular_price;
    }
    public String getCurrencySymbol(){
        return currency_symbol;
    }
    public String getStock_status() {
        return stock_status;
    }

    public void setStock_status(String stock_status) {
        this.stock_status = stock_status;
    }
}