package tk.burdukowsky.mpc_hc_android

enum class Layout(private val resourceId: Int) {
    SIMPLE(R.string.simple_layout), STRETCH(R.string.stretch_layout);

    fun getResource() = App.instance.applicationContext.getString(resourceId)

    companion object {
        fun toLayout(layoutString: String): Layout {
            return try {
                valueOf(layoutString)
            } catch (ex: Exception) {
                SIMPLE
            }
        }

        val default = SIMPLE
    }
}
