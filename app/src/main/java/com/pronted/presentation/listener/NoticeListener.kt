/*
 *  Created by Vinay
 *  Copyright (c) 2022 . All rights reserved.
 *
 */

package com.pronted.presentation.listener

interface NoticeListener<T> {
    fun onEdit(item: T)
    fun onDelete(item: T)
}