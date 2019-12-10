package com.sustaincsej.sustain_cedricsebevanjean.models

/**
 * Used to transform json to object for UV Api call
 *
 * @author Jean Robatto
 */
data class UVI(val lat: Double, val lon: Double, val date_iso: String, val date: Int, val value: Double)