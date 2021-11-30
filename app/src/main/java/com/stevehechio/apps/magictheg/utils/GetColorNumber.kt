package com.stevehechio.apps.magictheg.utils

/**
 * Created by stevehechio on 11/28/21
 */

object GetColorNumber {
    fun getColorNumber(color: String): Int{
        return when {
            color.equals("Red",true) -> {
                0
            }
            color.equals("Blue",true) -> {
                1
            }
            color.equals("Green",true) -> {
                2
            }
            color.equals("White",true) -> {
                3
            }
            color.equals("Black",true) -> {
                4
            }
            else -> {
                4
            }
        }
    }
}