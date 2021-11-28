package com.stevehechio.apps.magictheg.utils

/**
 * Created by stevehechio on 11/28/21
 */

enum class MyColors {
    Red, Blue, Green,  White, Black
}


object GetColorNumber {
    fun getColorNumber(color: String): Int{
        return when {
            color.equals(MyColors.Red.name,true) -> {
                0
            }
            color.equals(MyColors.Blue.name,true) -> {
                1
            }
            color.equals(MyColors.Green.name,true) -> {
                2
            }
            color.equals(MyColors.White.name,true) -> {
                3
            }
            color.equals(MyColors.Black.name,true) -> {
                4
            }
            else -> {
                4
            }
        }
    }
}