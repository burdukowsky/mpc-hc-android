package tk.burdukowsky.mpc_hc_android

enum class Layout {
    SIMPLE, STRETCH;

    companion object {
        fun toLayout(layoutString: String): Layout {
            return try {
                valueOf(layoutString)
            } catch (ex: Exception) {
                SIMPLE
            }
        }

        val default = Layout.SIMPLE
    }
}
