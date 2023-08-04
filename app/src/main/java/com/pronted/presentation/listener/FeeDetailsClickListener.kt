/*
 *  Created by Vinay
 *  Copyright (c) 2022 . All rights reserved.
 *
 */

package com.pronted.presentation.listener

interface FeeDetailsClickListener<T> {
    fun onPayClick(item: T)
    fun onShowSummary(item: T)
}