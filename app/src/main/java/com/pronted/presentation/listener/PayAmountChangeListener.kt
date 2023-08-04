/*
 *  Created by Vinay
 *  Copyright (c) 2022 . All rights reserved.
 *
 */

package com.pronted.presentation.listener

interface PayAmountChangeListener<T, S> {
    fun onAddPayment(amount: T, model: S)
    fun onRemovePayment(amount: T, model: S)
}