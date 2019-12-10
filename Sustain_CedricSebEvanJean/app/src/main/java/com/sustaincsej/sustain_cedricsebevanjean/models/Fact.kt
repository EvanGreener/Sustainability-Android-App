package com.sustaincsej.sustain_cedricsebevanjean.models


/**
 * Data class that contains all information for a Sustainability fact from the Firebase database.
 */
data class Fact (
    var image_url: String? = "",
    var source_url: String? = "",
    var text: String? = ""
)