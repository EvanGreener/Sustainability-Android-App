package com.sustaincsej.sustain_cedricsebevanjean.models

import com.google.firebase.database.IgnoreExtraProperties

data class Fact (
    var image_url: String? = "",
    var source_url: String? = "",
    var text: String? = ""
)