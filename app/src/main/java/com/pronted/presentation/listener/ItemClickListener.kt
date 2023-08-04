/*
 *  Created by Vinay
 *  Copyright (c) 2022 . All rights reserved.
 *
 */

package com.pronted.presentation.listener


interface ItemClickListener<T> {
    fun onClicked(item: T, position: Int)
}